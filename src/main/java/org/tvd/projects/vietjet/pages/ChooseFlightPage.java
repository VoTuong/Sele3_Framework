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
	private static final SelenideElement informationPopup = $x("//div[img[@alt='popup information']]");
	private static final SelenideElement closePopupButton = $x("//div[img[@alt='popup " +
			"information']]/preceding-sibling::button");
	private static final ElementsCollection currentLowestPrice = $$("div.slick-current p.MuiTypography-subtitle1 span");
	private static final ElementsCollection ticketPrices = $$("p.MuiTypography-h4");
	private static final SelenideElement continueBtn = $("button.MuiButton-contained");


	public void selectContinue() {
		continueBtn.shouldBe(visible).click();
	}

	public void chooseTheLowestPriceTicket() {
		String cheapestPrice = getSuggestedPrice();
		LogUtils.info("The cheapest price: " + cheapestPrice);

		boolean found = false;
		for (String price : getAllTicketPrices()) {
			if (price.equals(cheapestPrice)) {
				LogUtils.info("Select the ticket has prices: " + price);
				$x(String.format("(//p[contains(@class, 'MuiTypography-h4')])[%s]",
						getAllTicketPrices().indexOf(price))).click();
				found = true;
				break;
			}
		}

		if (!found) {
			LogUtils.info("Không tìm thấy giá vé nào khớp với giá rẻ nhất.");
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

	private List<String> getAllTicketPrices() {
		ticketPrices.shouldBe(sizeGreaterThan(0));
		List<String> formattedPrices = new ArrayList<>();
		for (String price : ticketPrices.texts()) {
			String formattedPrice = price.replace(",", "").replace(" ", "") + "000";
			formattedPrices.add(formattedPrice);
		}

		return formattedPrices;
	}

	private void closeInformationPopup() {
		if (informationPopup.isDisplayed()) closePopupButton.shouldBe(visible).click();
	}

	@Step
	public void shouldDisplayTicketFlight() {
		closeInformationPopup();
		$x("//p[contains(@class, 'MuiTypography-h5 MuiTypography-colorTextPrimary')]//span").shouldBe(visible);
	}
}
