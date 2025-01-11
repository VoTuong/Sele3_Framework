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

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {

	private static final SelenideElement cookiePopUp = $x("//div[@id='popup-dialog-description']");
	private static final SelenideElement acceptCookiesBtn = $x("//div[@id='popup-dialog-description']/following" +
			"-sibling" +
			"::div/button");
	private static final SelenideElement adAlert = $x("//div[@id='NC_background_color']");
	private static final SelenideElement deniedAddBtn = $x("//div[@id='NC_background_color']//button[@id = " +
			"'NC_CTA_TWO']");
	private static final SelenideElement roundTripRad = $x("//input[@value='roundTrip']");
	private static final SelenideElement oneWayRad = $x("//input[@value='oneway']");
	private static final SelenideElement fromTextField = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and not(@id='arrivalPlaceDesktop')]");
//	private static final SelenideElement toTextField = $x("//input[@class='MuiInputBase-input MuiOutlinedInput-input'" +
//			" and @id='arrivalPlaceDesktop']");
	private static final SelenideElement destinationMenu = $x("//div[contains(@class, 'scrollCustom')]");
	private static final SelenideElement passengerDropDown = $x("//input[starts-with(@id, 'input-base-custom')]");
	private static final SelenideElement departureDateButton = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and not(@id='arrivalPlaceDesktop')]//ancestor::div[.//div[@role='button']]/div[@role='button']");
//	private static final SelenideElement returnDateTxt = $x("//input[@class='MuiInputBase-input " +
//			"MuiOutlinedInput-input' and @id='arrivalPlaceDesktop']//ancestor::div[.//div[@role='button']]/div[@role='button']//p");
//	private static final SelenideElement lowestPriceChx = $x("//span[@class='MuiIconButton-label']//input[@type" +
//			"='checkbox']");
	private static final SelenideElement findFlightButton = $x("//button[contains(@class, 'MuiButtonBase-root MuiButton-root " +
		"MuiButton-contained')]/span[@class='MuiButton-label']");

	public HomePage openHomePage(){
		LogUtils.info("Open the VietJet Air homepage");
		open("https://www.vietjetair.com/");
		return this;
	}

	@Step
	public void searchTicket(TicketModel ticket){
		selectTicketType(ticket.getTicketType());
		selectDeparture(ticket.getFlightFrom());
		selectDestination(ticket.getFlightTo());
		selectDepartureDateAndReturnDate();
		selectNumberOfAdults(ticket.getPassengerCount().getAdults());
		selectFindFlight();

	}

	private void clickOnFormToDismissDropdown(){
		$x("(//*[@class='MuiSvgIcon-root'])[3]").shouldBe(visible).click();
	}

	@Step
	private void selectFindFlight(){
		clickOnFormToDismissDropdown();
		findFlightButton.shouldBe(visible).click();
	}

	@Step
	private void selectDestination(String to) {
		selectInDestinationMenu(to);
	}

	@Step
	private void selectDeparture(String from) {
		fromTextField.click();
		selectInDestinationMenu(from);
	}

	@Step
	private void selectInDestinationMenu(String destination) {
		if (destinationMenu.isDisplayed()){
			$x(String.format("//div[contains(text(), '%s')]", destination)).click();
		}
	}

	@Step
	private void selectNumberOfAdults(Integer adultNumber) {
		clickOnFormToDismissDropdown();
		passengerDropDown.shouldBe(visible).click();
		LogUtils.info("Clicked on Passengers button");
		for (int i = 1; i < adultNumber; i++) {
			$x("//img[@alt='adults']//parent::div//parent::div//parent::div//button[2]").click();
		}
	}

	@Step
	private void selectDepartureDateAndReturnDate() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate returnDate = tomorrow.plusDays(Long.parseLong(PropertiesUtils.getValue("RETURN_DAYS")));

		DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");
		String tomorrowStr = tomorrow.format(dayFormatter);
		String returnDateStr = returnDate.format(dayFormatter);
		LogUtils.info("Set date take off {}", tomorrowStr);
		selectDepartureDateButton();

		DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
		LogUtils.info("Selecting month {}", tomorrow.format(monthYearFormatter));
		selectMonthAndYear(tomorrow.format(monthYearFormatter));

		selectDayInCalendar(tomorrowStr);
		selectDayInCalendar(returnDateStr);
	}

	@Step
	private static void selectDepartureDateButton(){
		departureDateButton.hover();
		departureDateButton.shouldBe(visible).click();
		LogUtils.info("Selected the departure date button");
	}


	private static void selectMonthAndYear(String targetMonthAndYear) {
		String currentMonthAndYear = $x("(//div[@class='rdrMonthName'])[1]").getText();
		LogUtils.info("Month and Year got from Calender {}", currentMonthAndYear);
		String lang = LanguageUtils.detectLanguage(currentMonthAndYear);
		LogUtils.info("Language is {}", lang);

		String[] currentParts = currentMonthAndYear.split(" ");
		LogUtils.info("Length of month and year {}", currentParts.length);
		String currentMonth = "";
		if (lang.equalsIgnoreCase("en")){
			currentMonth = currentParts[0];
			LogUtils.info("Current month {}", currentMonth);
		} else if (lang.equalsIgnoreCase("vi")){
			LogUtils.info(currentParts[0] + " " + currentParts[1]);
			currentMonth = MonthTranslation.getOriginalName(currentParts[0] + " " + currentParts[1], lang);
			LogUtils.info("Current month {}", currentMonth);
		}
		String currentYear = currentParts[currentParts.length - 1];
		LogUtils.info("Current year {}", currentYear);

		String[] parts = targetMonthAndYear.split(" ");
		LogUtils.info("month and year: {} {}", parts[0], parts[1]);
		String targetMonth;
		targetMonth =  parts[0];
		LogUtils.info("target month {}", targetMonth);
		String targetYear = parts[parts.length - 1];
		LogUtils.info("target year {}", targetYear);

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
		// Sử dụng XPath để tìm nút ngày dựa trên giá trị
		String xpath = "//button[contains(@class, 'rdrDay')]//span[contains(@class, 'rdrDayNumber')]/span[text()='" + day + "']";
		$(By.xpath(xpath)).shouldBe(visible).click();
	}

	@Step
	private void selectTicketType(String ticketType) {
		if (ticketType.equalsIgnoreCase("round trip")){
			LogUtils.info("Select Round Trip");
			roundTripRad.click();
		}else if (ticketType.equalsIgnoreCase("one way")){
			LogUtils.info("Select One Way");
			oneWayRad.click();
		}
	}

	public void selectDeniedAdButton(){
		if (adAlert.isDisplayed()) {
			LogUtils.info("Select Denied Ad button");
			deniedAddBtn.click();
		}
		LogUtils.info("Ad alert is closed");
		adAlert.shouldBe(disappear);
	}

	public void selectAcceptCookiesButton(){
		if (cookiePopUp.isDisplayed()){
			LogUtils.info("Select Accept Cookie button");
			acceptCookiesBtn.click();
		}
		LogUtils.info("Cookie Pop up is closed");
		cookiePopUp.shouldBe(disappear);
	}

}
