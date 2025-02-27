package org.tvd.utilities;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CalendarUtils {
	/**
	 * Select a day in the calendar widget based on day, month, year, and locale (supports multiple languages).
	 *
	 * @param day    The day to select (1-31)
	 * @param month  The month to select (1-12)
	 * @param year   The year to select
	 * @param locale The locale of the calendar display (e.g., Locale.ENGLISH, Locale.JAPANESE, etc.)
	 */
	public static void selectDate(int day, int month, int year, Locale locale) {
		// Ensure the calendar widget is visible
		$(".rdrCalendarWrapper").shouldBe(Condition.visible);

		// Retrieve the month names based on the provided locale (e.g., "February" or "2月")
		DateFormatSymbols dfs = new DateFormatSymbols(locale);
		String[] monthNames = dfs.getMonths();
		String expectedMonthName = monthNames[month - 1]; // Note: the array is 0-indexed

		while (true) {
			// Get all the displayed month panels
			ElementsCollection monthPanels = $$(".rdrMonth");

			for (SelenideElement panel : monthPanels) {
				// Retrieve the header text that contains the month and year (e.g., "2月 2025" or "February 2025")
				String monthYearText = panel.$(".rdrMonthName").getText().trim();

				// Check if the year matches
				boolean yearMatches = monthYearText.contains(String.valueOf(year));

				// Compare the month name based on the locale (case-insensitive)
				boolean monthMatches = monthYearText.toLowerCase().contains(expectedMonthName.toLowerCase());

				// If not matching, try extracting a numeric month from the text (e.g., "2月 2025")
				if (!monthMatches) {
					Pattern pattern = Pattern.compile("(\\d+)");
					Matcher matcher = pattern.matcher(monthYearText);
					if (matcher.find()) {
						int numericMonth = Integer.parseInt(matcher.group(1));
						monthMatches = (numericMonth == month);
					}
				}

				if (yearMatches && monthMatches) {
					// Find the button representing the desired day and exclude buttons with the class 'rdrDayPassive'
					SelenideElement dayButton = panel.$x(
							".//button[.//span[@class='rdrDayNumber']//span[text()='" + day + "'] " +
									"and not(contains(@class,'rdrDayPassive'))]"
					);
					dayButton.shouldBe(Condition.visible).click();
					return; // Exit after successfully selecting the day
				}
			}
			// If the correct month/year is not yet displayed, click the "Next" button to move to the next month
			$(".rdrNextButton").shouldBe(Condition.visible).click();
		}
	}
}
