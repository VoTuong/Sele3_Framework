package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class PassengerInfoPage {
	private static final SelenideElement passengerInfoForm = $x("//i[@class = 'fa fa-male']/ancestor::div[contains(@style,'padding-bottom')]");


	@Step("Check if Passenger Info form is displayed")
	public void shouldPassengerInfoFormDisplay() {
		passengerInfoForm.shouldBe(visible);
	}
}
