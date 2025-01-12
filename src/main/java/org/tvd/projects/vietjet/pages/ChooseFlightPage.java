package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.tvd.utilities.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ChooseFlightPage {

	public ChooseFlightPage(){
		closeInformationPopup();
	}
	private static final SelenideElement informationPopup = $x("//div[img[@alt='popup information']]");
	private static final SelenideElement closePopupButton = $x("//div[img[@alt='popup " +
			"information']]/preceding-sibling::button");
	private static final ElementsCollection currentLowestPrice = $$("div.slick-current p.MuiTypography-subtitle1 span");
	private static final ElementsCollection ticketPrices = $$("p.MuiTypography-h4");
	private static final SelenideElement continueBtn = $("button.MuiButton-contained");
	private static final String ticketPricesSelector = "p.MuiTypography-h4";

	public void chooseTicketsForMultipleDays() {

			chooseTheLowestPriceTicket();
			selectContinue();

	}

	@Step
	public void selectContinue() {
		LogUtils.info("Selecting Continue button");
		continueBtn.shouldBe(visible).click();
	}

	@Step
	public void chooseTheLowestPriceTicket() {
		String cheapestPrice = getSuggestedPrice();
		LogUtils.info("The cheapest price: " + cheapestPrice);

		List<String> allPrices = getAllTicketPrices();
		LogUtils.info("All ticket prices: " + allPrices);
		boolean found = false;
		for (int i = 0; i < allPrices.size(); i++) {
			String price = allPrices.get(i);
			if (price.equals(cheapestPrice)) {
				LogUtils.info("Select the ticket has prices: " + price);
				SelenideElement priceE = $x(String.format("(//p[contains(@class, 'MuiTypography-h4')])[%d]", i + 1));
				priceE.shouldBe(visible).click();
				found = true;
				break;
			}
		}
		if (!found) {
			LogUtils.info("No ticket found with the cheapest price: ", cheapestPrice);
		}
	}

	private String getSuggestedPrice() {
		// Wait for price elements to be present, adjust timeout if needed
		currentLowestPrice.shouldBe(sizeGreaterThan(0));

		StringBuilder flightPrice = new StringBuilder();
		for (String text : currentLowestPrice.texts()) {
			flightPrice.append(text);
		}
		String formattedPrice = flightPrice.toString().replace(",", "").replace(" ", "");
		return formattedPrice.replaceAll("\\D", "");
	}

//	private List<String> getAllTicketPrices() {
//		ticketPrices.shouldBe(sizeGreaterThan(0));
//		List<String> formattedPrices = new ArrayList<>();
//		for (String price : ticketPrices.texts()) {
//			String formattedPrice = price.replace(",", "").replace(" ", "") + "000";
//			formattedPrices.add(formattedPrice);
//		}
//
//		return formattedPrices;
//	}

	private List<String> getAllTicketPrices() {
		List<String> formattedPrices = new ArrayList<>();

		while (true) {
			ElementsCollection visiblePrices = $$(ticketPricesSelector);

			for (SelenideElement priceElement : visiblePrices) {
				String price = priceElement.getText().replace(",", "").replace(" ", "") + "000";
				formattedPrices.add(price);
			}

			SelenideElement lastVisiblePrice = visiblePrices.last();
			if (!lastVisiblePrice.isDisplayed()) {
				lastVisiblePrice.scrollIntoView("{behavior: 'smooth', block: 'end'}");
			}

			if (isLastPriceVisible(lastVisiblePrice)) {
				break;
			}
			LogUtils.info("Scroll to " + lastVisiblePrice.getText());
		}

		return formattedPrices;
	}

	private boolean isLastPriceVisible(SelenideElement lastPrice) {
		Object jsResult = executeJavaScript("return window.innerHeight;");
		if (jsResult != null) {
			int windowHeight = ((Number) jsResult).intValue();
			return lastPrice.getLocation().getY() + lastPrice.getSize().getHeight() <= windowHeight;
		}
		return false;
	}

	private static void closeInformationPopup() {
		if (informationPopup.isDisplayed()) closePopupButton.shouldBe(visible).click();
	}

	@Step
	public void shouldDisplayTicketFlight() {
		closeInformationPopup();
		$x("//p[contains(@class, 'MuiTypography-h5 MuiTypography-colorTextPrimary')]//span").shouldBe(visible);
	}
}
