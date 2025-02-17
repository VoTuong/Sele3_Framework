package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.tvd.projects.vietjet.models.TicketModel;
import org.tvd.utilities.CalendarUtils;
import org.tvd.utilities.LogUtils;
import org.tvd.utilities.PropertiesUtils;

import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.switchTo;

public class HomePage {
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
	private final ResourceBundle messages;
	private final String langCode;

	public HomePage(String langCode) {
		this.langCode = langCode;
		Locale locale = Locale.of(langCode);
		this.messages = ResourceBundle.getBundle("vj_locator", locale);
	}

	@Step
	private static void selectDepartureDateButton() {
		departureDateButton.hover();
		departureDateButton.shouldBe(visible).click();
		LogUtils.info("Selected the departure date button");
	}

	private String getLangCode() {
		return this.langCode;
	}

	public void openHomePage() {
		LogUtils.info("Open the VietJet Air homepage");
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

		LocalDate tomorrow = LocalDate.now().plusDays(Long.parseLong(PropertiesUtils.getValue("DEPARTURE_DATE")));
		LocalDate returnDate = tomorrow.plusDays(Long.parseLong(PropertiesUtils.getValue("RETURN_DATE")));

		LogUtils.info("Set date take off ", tomorrow.getDayOfMonth());
		selectDepartureDateButton();

		CalendarUtils.selectDate(tomorrow.getDayOfMonth(), tomorrow.getMonthValue(), tomorrow.getYear(), Locale.of(getLangCode()));
		CalendarUtils.selectDate(returnDate.getDayOfMonth(), returnDate.getMonthValue(), returnDate.getYear(), Locale.of(getLangCode()));
	}

	@Step
	private void selectFindFlight() {
		clickOnFormToDismissDropdown();
		String findFlight = messages.getString("ticket.find_flight_button");
		$x(String.format(findFlightButton, findFlight)).shouldBe(visible).click();
	}

	@Step
	private void selectTicketType(String ticketType) {
		String roundTripText = messages.getString("ticket.round_trip");
		String oneWayText = messages.getString("ticket.one_way");

		if (ticketType.equalsIgnoreCase(roundTripText)) {
			LogUtils.info("Select ", roundTripText);
			roundTripRad.click();
		} else if (ticketType.equalsIgnoreCase(oneWayText)) {
			LogUtils.info("Select ", oneWayText);
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
