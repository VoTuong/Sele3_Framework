package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class SelectFlightPage {
	private static final SelenideElement informationPopup = $x("//div[img[@alt='popup information']]");
	private static final SelenideElement closePopupButton= $x("//div[img[@alt='popup " +
			"information']]/preceding-sibling::button");


	private void closeInformationPopup() {
		if(informationPopup.isDisplayed()) closePopupButton.shouldBe(visible).click();
	}

	@Step
	public void shouldDisplayTicketFlight(){
		closeInformationPopup();
		$x("//p[contains(@class, 'MuiTypography-h5 MuiTypography-colorTextPrimary')]//span").shouldBe(visible);
	}
}
