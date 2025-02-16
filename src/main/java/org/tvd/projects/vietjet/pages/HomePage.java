package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.tvd.projects.vietjet.data.MonthTranslation;
import org.tvd.projects.vietjet.models.TicketModel;
import org.tvd.utilities.LanguageUtils;
import org.tvd.utilities.LogUtils;
import org.tvd.utilities.PropertiesUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {
	private final ResourceBundle messages;
	private final String langCode;

	private static final SelenideElement cookiePopUp = $x("//div[@id='popup-dialog-description']");
	private static final SelenideElement acceptCookiesBtn = $x("//div[@id='popup-dialog-description']" +
			"/following-sibling::div/button");
	private static final SelenideElement notificationBanner = $x("//div[@id='st_notification_banner']");
	private static final SelenideElement notificationIframe = $x("//iframe[@id='preview-notification-frame']");
	private static final SelenideElement roundTripRad = $x("//input[@value='roundTrip']");
	private static final SelenideElement oneWayRad = $x("//input[@value='oneway']");
	private static final SelenideElement departureAirportTxt = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and not(@id='arrivalPlaceDesktop')]");
	private static final SelenideElement arrivalAirportTxt = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input'" + " and @id='arrivalPlaceDesktop']");
	private static final SelenideElement cityMenu = $x("//div[contains(@class, 'scrollCustom')]");
	private static final SelenideElement passengerDropDown = $x("//input[starts-with(@id, 'input-base-custom')]");
	private static final SelenideElement departureDateButton = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and not(@id='arrivalPlaceDesktop')]//ancestor::div[.//div[@role='button']]/div[@role='button']");
	private static final SelenideElement addAdultButton = $x("//img[@alt='adults']//parent::div//parent::div" +
			"//parent::div//button[2]");

	private static final String findFlightButton = "//div[contains(@class, 'MuiBox-root')" +
			"]/following-sibling::div//button[contains(span, \"%s\")]";

	public HomePage(String langCode) {
		this.langCode = langCode;
		Locale locale =  Locale.of(langCode);
		this.messages = ResourceBundle.getBundle("vj_locator", locale);
	}

	private String getLangCode() {
		return this.langCode;
	}

	@Step
	private static void selectDepartureDateButton() {
		departureDateButton.hover();
		departureDateButton.shouldBe(visible).click();
		LogUtils.info("Selected the departure date button");
	}

	private static void selectMonthAndYear(String targetMonthAndYear) {
		String currentMonthAndYear = $x("(//div[@class='rdrMonthName'])[1]").getText();
		LogUtils.info("Month and Year got from Calender ", currentMonthAndYear);
		String lang = LanguageUtils.detectLanguage(currentMonthAndYear);
		LogUtils.info("Language is ", lang);

		String[] currentParts = currentMonthAndYear.split(" ");
		LogUtils.info("Length of month and year ", currentParts.length);
		String currentMonth = "";
		if (lang.equalsIgnoreCase("en")) {
			currentMonth = currentParts[0];
			LogUtils.info("Current month ", currentMonth);
		} else if (lang.equalsIgnoreCase("vi")) {
			LogUtils.info(currentParts[0] + " " + currentParts[1]);
			currentMonth = MonthTranslation.getOriginalName(currentParts[0] + " " + currentParts[1], lang);
			LogUtils.info("Current month ", currentMonth);
		}
		String currentYear = currentParts[currentParts.length - 1];
		LogUtils.info("Current year ", currentYear);

		String[] parts = targetMonthAndYear.split(" ");
		LogUtils.info("month and year: ", parts[0], parts[1]);
		String targetMonth;
		targetMonth = parts[0];
		LogUtils.info("target month ", targetMonth);
		String targetYear = parts[parts.length - 1];
		LogUtils.info("target year ", targetYear);

		while (!currentMonth.equalsIgnoreCase(targetMonth) || !currentYear.equals(targetYear)) {
			if (Integer.parseInt(targetYear) > Integer.parseInt(currentYear) ||
					(currentMonth.compareTo(targetMonth) < 0 && currentYear.equals(targetYear))) {
				$(".rdrNextPrevButton.rdrNextButton").click();
			} else {
				$(By.cssSelector(".rdrNextPrevButton.rdrPprevButton")).click();
			}
			currentMonthAndYear = $(".rdrMonthAndYearPickers").getText();
			currentParts = currentMonthAndYear.split(" ");
			currentMonth = currentParts[0];
			currentYear = currentParts[1];
		}
	}

	private static void selectDayInCalendar(String day) {
		LogUtils.info("select Day In Calendar");
		String xpath = "//button[contains(@class, 'rdrDay')]//span[contains(@class, 'rdrDayNumber')]/span[text()='" + day + "']";
		$(By.xpath(xpath)).shouldBe(visible).click();
	}

	public void openHomePage() {
		LogUtils.info("Open the VietJet Air homepage");
//		open("https://www.vietjetair.com/");
		selectAcceptCookiesButton();
		closeNotificationBanner();
	}

	@Step
	public ChooseFlightPage searchTicket(TicketModel ticket) {
		selectTicketType(ticket.getTicketType());
		selectDepartureCity(ticket.getFlightFrom());
		selectArrivalCity(ticket.getFlightTo());
		selectDepartureDateAndReturnDate();
		selectNumberOfAdults(ticket.getPassengerCount().getAdults());
		selectFindFlight();
		return new ChooseFlightPage();
	}

	private void clickOnFormToDismissDropdown() {
		$x("(//*[@class='MuiSvgIcon-root'])[3]").shouldBe(visible).click();
	}

	@Step
	private void selectArrivalCity(String to) {
		clickOnFormToDismissDropdown();
		arrivalAirportTxt.shouldBe(visible).click();
		arrivalAirportTxt.shouldBe(visible).setValue(to);
		selectCityMenu(to);
	}

	@Step
	private void selectDepartureCity(String from) {
		clickOnFormToDismissDropdown();
		departureAirportTxt.shouldBe(visible).click();
		departureAirportTxt.shouldBe(visible).setValue(from);
		selectCityMenu(from);
	}

	@Step
	private void selectCityMenu(String city) {
		if (cityMenu.isDisplayed()) {
			$x(String.format("//div[contains(text(), '%s')]", city)).click();
		}
	}

	@Step
	private void selectNumberOfAdults(Integer adultNumber) {
		clickOnFormToDismissDropdown();
		passengerDropDown.shouldBe(visible).click();
		LogUtils.info("Clicked on Passengers button");
		for (int i = 1; i < adultNumber; i++) {
			addAdultButton.shouldBe(visible).click();
		}
	}

	@Step
	private void selectDepartureDateAndReturnDate() {

		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate returnDate = tomorrow.plusDays(Long.parseLong(PropertiesUtils.getValue("RETURN_DAYS")));

		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
		String tomorrowStr = tomorrow.format(dayFormatter);
		String returnDateStr = returnDate.format(dayFormatter);
		LogUtils.info("Set date take off ", tomorrowStr);
		selectDepartureDateButton();

//		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
//		LogUtils.info("Selecting month ", tomorrow.format(monthYearFormatter));
//		selectMonthAndYear(tomorrow.format(monthYearFormatter));

		selectDayInCalendar(tomorrowStr);
		selectDayInCalendar(returnDateStr);
	}

	@Step
	private void selectFindFlight() {
		clickOnFormToDismissDropdown();
		String findFlight = messages.getString("ticket.find_flight_button");
		$x(String.format(findFlightButton,findFlight)).shouldBe(visible).click();
	}

	@Step
	private void selectTicketType(String ticketType) {
		String roundTripText = messages.getString("ticket.round_trip");
		String oneWayText = messages.getString("ticket.one_way");

		if (ticketType.equalsIgnoreCase(roundTripText)) {
			LogUtils.info("Select {}", roundTripText);
			roundTripRad.click();
		} else if (ticketType.equalsIgnoreCase(oneWayText)) {
			LogUtils.info("Select {}", oneWayText);
			oneWayRad.click();
		}
	}

	public void closeNotificationBanner() {
		if (notificationBanner.isDisplayed()) {
			switchTo().frame(notificationIframe);
			LogUtils.info("Switched to notification iframe.");
			SelenideElement closeButton = $x("//button[@id ='NC_CTA_TWO']");
			if (closeButton.exists()) {
				closeButton.shouldBe(visible).click();
				LogUtils.info("Notification banner closed successfully.");
			} else {
				LogUtils.warn("Close button not found inside iframe.");
			}
			switchTo().defaultContent();
		} else {
			LogUtils.info("Notification banner is not displayed.");
		}
	}

	public void selectAcceptCookiesButton() {
		if (cookiePopUp.isDisplayed()) {
			LogUtils.info("Select Accept Cookie button");
			acceptCookiesBtn.click();
		}
		LogUtils.info("Cookie Pop up is closed");
		cookiePopUp.shouldBe(disappear);
	}

}
