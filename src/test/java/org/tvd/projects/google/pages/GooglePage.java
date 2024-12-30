package org.tvd.projects.google.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class GooglePage {

	private static final Logger logger = LogManager.getLogger(GooglePage.class);
	// Locator for the Google search input
	private final SelenideElement searchInput = $("[name='q']");

	// Locator for the first search result link
	private final SelenideElement firstSearchResult = $("h3");

	@Step
	public GooglePage openPage() {
		logger.info("Open the Google homepage");
		open("https://www.google.com");
		return this;
	}

	@Step
	public void search(String query) {
		logger.info("Searching...");
		searchInput.setValue(query).pressEnter();
	}

	@Step
	public boolean isFirstResultContains(String expectedText) {
		logger.info("Verify that the first search result contains the expected text");
		return firstSearchResult.getText().contains(expectedText);
	}
}
