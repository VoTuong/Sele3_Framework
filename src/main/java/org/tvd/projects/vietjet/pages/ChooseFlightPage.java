package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.tvd.utilities.LogUtils;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ChooseFlightPage {

	private static final SelenideElement informationPopup = $x("//div[img[@alt='popup information']]");
	private static final SelenideElement closePopupButton = $x("//div[img[@alt='popup " +
			"information']]/preceding-sibling::button");
	private static final ElementsCollection currentLowestPrice = $$("div.slick-current p.MuiTypography-subtitle1 span");
	private static final ElementsCollection ticketPrices = $$("p.MuiTypography-h4");
	private static final SelenideElement continueBtn = $("button.MuiButton-contained");

	public ChooseFlightPage() {
		LogUtils.info("Initializing Select Ticket Page...");
		monitorAndClosePopup();
	}

	private void monitorAndClosePopup() {
		LogUtils.info("Starting to monitor for the popup...");
		try {
			informationPopup.shouldBe(visible, Duration.ofSeconds(20));
			LogUtils.info("Popup detected. Closing...");

			closePopupButton.shouldBe(visible).click();
			LogUtils.info("Popup closed successfully.");
		} catch (Exception e) {
			LogUtils.info("Popup did not appear within the timeout.");
		}
	}

	@Step
	public void selectContinue() {
		LogUtils.info("Selecting Continue button");
		continueBtn.shouldBe(visible).click();
	}

	private void scrollToBottom() {
		LogUtils.info("Scrolling to the bottom of the page...");
		executeJavaScript("window.scrollTo(0, document.body.scrollHeight);");
//		sleep(2000); // Chờ một chút để các phần tử tải nếu cần
	}

	@Step
	public void chooseTheLowestPriceTicket() {
		LogUtils.info("Choosing the lowest price");
		String cheapestPrice = getSuggestedPrice();
		LogUtils.info("The cheapest price: " + cheapestPrice);

		List<String> allPrices = getAllTicketPrices();
		LogUtils.info("All ticket prices: " + allPrices);

		for (int i = 0; i < allPrices.size(); i++) {
			String price = allPrices.get(i);
			if (price.equals(cheapestPrice)) {
				LogUtils.info("Selecting the ticket with price: " + price);
				SelenideElement priceElement = $x(String.format("(//p[contains(@class, 'MuiTypography-h4')])[%d]", i + 1));
				priceElement.shouldBe(visible).click();
				return;
			}
		}
		LogUtils.warn("No ticket found with the cheapest price: " + cheapestPrice);
	}

	private String getSuggestedPrice() {
		currentLowestPrice.shouldBe(sizeGreaterThan(0));
		return formatPrice(String.join("", currentLowestPrice.texts()));
	}

	private List<String> getAllTicketPrices() {
		scrollToBottom();
		ticketPrices.shouldBe(sizeGreaterThan(0));
		return ticketPrices.texts().stream()
				.map(this::formatPrice)
				.collect(Collectors.toList());
	}

	private String formatPrice(String price) {
		return price.replace(",", "").replace(" ", "").replaceAll("\\D", "");
	}

//	private String getSuggestedPrice() {
//		currentLowestPrice.shouldBe(sizeGreaterThan(0));
//		List<String> prices = currentLowestPrice.texts();
//
//		// Chuyển đổi và định dạng tất cả giá trị
//		List<Integer> formattedPrices = prices.stream()
//				.map(this::convertToInteger)
//				.toList();
//
//		// Lấy giá nhỏ nhất
//		int minPrice = formattedPrices.stream().min(Integer::compareTo).orElseThrow();
//		return String.valueOf(minPrice);
//	}
//
//	private List<String> getAllTicketPrices() {
//		ticketPrices.shouldBe(sizeGreaterThan(0));
//		scrollToBottom();
//		List<Integer> formattedPrices = new ArrayList<>();
//		for (String price : ticketPrices.texts()) {
//			try {
//				String formattedPrice = price.replace(",", "").replace(" ", "").trim();
//				if (!formattedPrice.matches("\\d+")) {
//					LogUtils.warn("Invalid price encountered: " + price);
//					continue;
//				}
//
//				int priceValue = Integer.parseInt(formattedPrice) * 1000;
//				formattedPrices.add(priceValue);
//			} catch (NumberFormatException e) {
//				LogUtils.error("Failed to convert price to integer: " + price, e);
//			}
//		}
//
//		return formattedPrices.stream().map(String::valueOf).collect(Collectors.toList());
//	}
//
//	private int convertToInteger(String price) {
//		String cleanPrice = price.replace(",", "").replace(" ", "").replaceAll("\\D", "");
//		return Integer.parseInt(cleanPrice);
//	}


	private boolean isLastPriceVisible(SelenideElement lastPrice) {
		Object jsResult = executeJavaScript("return window.innerHeight;");
		if (jsResult != null) {
			int windowHeight = ((Number) jsResult).intValue();
			return lastPrice.getLocation().getY() + lastPrice.getSize().getHeight() <= windowHeight;
		}
		return false;
	}

}
