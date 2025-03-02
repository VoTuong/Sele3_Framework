package org.tvd.projects.leapfrog.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.tvd.projects.leapfrog.model.GameData;
import org.tvd.utilities.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class GameVerifier {

	/**
	 * Compares all games found in the HTML document with the expected game data.
	 *
	 * @param doc           the parsed HTML document.
	 * @param expectedGames list of expected game data.
	 * @return a list of error messages if mismatches are found.
	 */
	@Step
	public static List<String> compareAllGames(Document doc, List<GameData> expectedGames) {
		List<String> errors = new ArrayList<>();
		// Select game elements using the specified CSS selector (adjust as needed)
		Elements gameElements = doc.select(".resultList .product-inner");

		// Iterate over each expected game data
		for (GameData expectedGame : expectedGames) {
			boolean found = false;
			// Loop through each game element in the HTML document
			for (Element gameEl : gameElements) {
				// Assuming the game title is located in "p.heading > a"
				String title = gameEl.select("p.heading > a").text().trim();
				if (title.equalsIgnoreCase(expectedGame.getTitle())) {
					found = true;
					// Extract actual age and price from the HTML element
					String actualAge = gameEl.select("p.ageDisplay").text().trim();
					String actualPrice = gameEl.select("p.prices").text().trim();

					// Compare age and log error if there is a mismatch
					if (!actualAge.equals(expectedGame.getAge())) {
						String error = String.format("Incorrect age for game '%s': Expected='%s', Actual='%s'",
								expectedGame.getTitle(), expectedGame.getAge(), actualAge);
						errors.add(error);
						LogUtils.error(error);
					}
					// Compare price and log error if there is a mismatch
					if (!actualPrice.equals(expectedGame.getPrice())) {
						String error = String.format("Incorrect price for game '%s': Expected='%s', Actual='%s'",
								expectedGame.getTitle(), expectedGame.getPrice(), actualPrice);
						errors.add(error);
						LogUtils.error(error);
					}
					break;
				}
			}
			// If the game element is not found in the HTML, log an error
			if (!found) {
				String error = "Game not found with title: " + expectedGame.getTitle();
				errors.add(error);
				LogUtils.error(error);
			}
		}
		return errors;
	}

	/**
	 * Performs an assertion that all games in the HTML match the expected game data.
	 * This method encapsulates comparison, logging, Allure attachment, and the final assertion.
	 *
	 * @param doc           the parsed HTML document.
	 * @param expectedGames list of expected game data.
	 */
	@Step
	public static void assertAllGames(Document doc, List<GameData> expectedGames) {
		List<String> errors = compareAllGames(doc, expectedGames);
		// Create an Allure attachment containing the error details
		StringJoiner joiner = new StringJoiner("\n");
		errors.forEach(joiner::add);
		Allure.addAttachment("Error List", "text/plain", joiner.toString());
		// Perform assertion: test fails if there are any errors
		Assert.assertTrue(errors.isEmpty(), "Errors found during game verification: " + errors);
	}
}
