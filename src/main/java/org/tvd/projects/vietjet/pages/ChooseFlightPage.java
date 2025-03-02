package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.tvd.utilities.LogUtils;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ChooseFlightPage {

	private static final SelenideElement informationPopup = $x("//div[img[@alt='popup information']]");
	private static final SelenideElement closePopupButton = $x("//div[img[@alt='popup " +
			"information']]/preceding-sibling::button");
	private static final ElementsCollection currentLowestPrice = $$("div.slick-current p.MuiTypography-subtitle1 span");
	private static final ElementsCollection ticketPrices = $$("p.MuiTypography-h4");
	private static final SelenideElement continueBtn = $("button.MuiButton-contained");
	private static final SelenideElement vjFlightIcon = $x("//img[@alt='vietjet flight']");

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
		LogUtils.info("Selecting 'Continue' button");
		continueBtn.shouldBe(visible).click();
	}

	private void scrollToBottom() {
		LogUtils.info("Scrolling to the bottom of the page...");
		vjFlightIcon.shouldBe(exist).scrollTo();
	}

	private void waitForPricesToLoad() {
		ticketPrices.shouldBe(sizeGreaterThan(0), Duration.ofSeconds(10));
		LogUtils.info("Ticket prices loaded successfully.");
	}

	@Step("Choose the tickets for arrival flight and departure flight")
	public void selectTicketRoundTrip() {
		LogUtils.info("Selecting the tickets for arrival and departure flights");
		chooseTheLowestPriceTicket();
		sleep(1000);
		selectContinue();
//		sleep(3000);
		chooseTheLowestPriceTicket();
		sleep(1000);
		selectContinue();
	}

	@Step
	private void chooseTheLowestPriceTicket() {
		waitForPricesToLoad();
		LogUtils.info("Choosing the lowest price");
		String cheapestPrice = getSuggestedPrice();
		LogUtils.info("The cheapest price: " + cheapestPrice);
		scrollStepByStepToPriceAndSelect(cheapestPrice);
//		selectContinue();
	}

	private String getSuggestedPrice() {
		currentLowestPrice.shouldBe(sizeGreaterThan(0));
		return removeTrailingZeros(formatPrice(String.join("", currentLowestPrice.texts())));
	}

	private String removeTrailingZeros(String price) {
		if (price.endsWith("000")) {
			return price.substring(0, price.length() - 3);
		}
		return price;
	}

	private void scrollStepByStepToPriceAndSelect(String targetPrice) {
		boolean priceFound = false;
		int maxScrollAttempts = 20;
		int attempts = 0;

		ticketPrices.shouldBe(sizeGreaterThan(0));

		while (!priceFound && attempts < maxScrollAttempts) {
			ElementsCollection priceElements = $$x("//p[contains(@class, 'MuiTypography-h4')]");
			List<String> allPrices = priceElements.texts().stream()
					.map(this::formatPrice)
					.toList();

			LogUtils.info("Attempt " + (attempts + 1) + " Found " + allPrices.size() + " new prices.");
			LogUtils.info("All ticket prices: " + allPrices);

			for (int i = 0; i < allPrices.size(); i++) {
				String price = allPrices.get(i);
				if (price.equals(targetPrice)) {
					LogUtils.info("Selecting the ticket with price: " + price);
					SelenideElement priceElement = $x(String.format("(//p[contains(@class, 'MuiTypography-h4')])[%d]"
							, i + 1));
					priceElement.scrollIntoView("{behavior: \"instant\", block: \"center\", inline: \"center\"}").click();
					LogUtils.info("Clicked on the ticket with price: " + priceElement.getText());

					priceFound = true;
					break;
				}
			}

			if (!priceFound) {
				LogUtils.info("Scrolling down step by step...");
				scrollToBottom();
			}
			attempts++;
		}

		if (!priceFound) {
			LogUtils.warn("Target price not found after " + maxScrollAttempts + " attempts.");
		}
	}

	private String formatPrice(String price) {
		return price.replace(",", "").replace(" ", "").replaceAll("\\D", "");
	}


}
