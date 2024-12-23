package org.tvd.projects.google.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class GooglePage {

	// Locator for the Google search input
	private final SelenideElement searchInput = $("[name='q']");

	// Locator for the first search result link
	private final SelenideElement firstSearchResult = $("h3");

	// Open the Google homepage
	@Step
	public GooglePage openPage() {
		open("https://www.google.com");
		return this;
	}

	// Perform a search
	@Step
	public void search(String query) {
		searchInput.setValue(query).pressEnter();
	}

	// Verify that the first search result contains the expected text
	@Step
	public boolean isFirstResultContains(String expectedText) {
		return firstSearchResult.getText().contains(expectedText);
	}
}
