package org.tvd.projects.vietjet.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Selenide.*;

public class HomePageVJ {
	private static final Logger logger = LogManager.getLogger(HomePageVJ.class);

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
	private static final SelenideElement toTextField = $x("//input[@class='MuiInputBase-input MuiOutlinedInput-input'" +
			" and @id='arrivalPlaceDesktop']");
	private static final SelenideElement numberAdultField = $x("//input[starts-with(@id, 'input-base-custom')]");
	private static final SelenideElement departureDateTxt = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and not(@id='arrivalPlaceDesktop')]//ancestor::div[.//div[@role='button']]/div[@role='button']//p");
	private static final SelenideElement returnDateTxt = $x("//input[@class='MuiInputBase-input " +
			"MuiOutlinedInput-input' and @id='arrivalPlaceDesktop']//ancestor::div[.//div[@role='button']]/div[@role='button']//p");
	private static final SelenideElement lowestPriceChx = $x("//span[@class='MuiIconButton-label']//input[@type" +
			"='checkbox']");

	public HomePageVJ openHomePage(){
		logger.info("Open the VietJet Air homepage");
		open("https://www.vietjetair.com/");
		return this;
	}

	public void searchTicket(String ticketType, Integer adultNumber){
		selectTicketType(ticketType);
//		selectDepartureDateAndReturnDate();
		selectNumberOfAdults(adultNumber);
	}

	private void selectNumberOfAdults(Integer adultNumber) {
		numberAdultField.click();
		for (int i = 1; i < adultNumber; i++) {
			$x("//img[@alt='adults']//parent::div//parent::div//parent::div//button[2]").click();
		}
	}

	private void selectDepartureDateAndReturnDate() {
		LocalDate tomorrow = LocalDate.now().plusDays(1);
		LocalDate returnDate = tomorrow.plusDays(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		String tomorrowStr = tomorrow.format(formatter);
		logger.info("Set date take off {}", tomorrowStr);
		departureDateTxt.click();
		setDate(tomorrowStr);
		//TODO: create function to select the date

		String returnDateStr = returnDate.format(formatter);
		logger.info("Set return date take off {}", returnDateStr);
		returnDateTxt.setValue(returnDateStr);
	}

	private void setDate(String date) {
		//TODO: create function to select the date
		//TODO: create function to split date string to be selected
		if($x("//div[@class='rdrMonths rdrMonthsHorizontal']").isDisplayed()){
			ElementsCollection days = $$x("//div[contains(text(),'tháng 01 2025')" +
					"]//following-sibling::div[@class='rdrDays']//span[@class='rdrDayNumber']/span"); // Lấy tất cả các ngày của tháng 1

			//Tìm index của ngày 1 tháng 1
			int startIndex =
					days.stream().filter(element -> element.getText().equals("1")).findFirst().map(element -> days.texts().indexOf(element)).orElse(-1);

			if(startIndex != -1){
				for (int i = 0; i < 4; i++) {
					days.get(startIndex + i).click(); // Chọn 4 ngày tiếp theo
				}
			}

		}
	}

	private void selectTicketType(String ticketType) {
		if (ticketType.equalsIgnoreCase("RoundTrip")){
			logger.info("Select Round Trip");
			roundTripRad.click();
		}else {
			logger.info("Select One Way");
			oneWayRad.click();
		}
	}

	public void selectDeniedAdButton(){
		if (adAlert.isDisplayed()) {
			logger.info("Select Denied Ad button");
			deniedAddBtn.click();
		}
		logger.info("Ad alert is closed");
		adAlert.shouldBe(disappear);
	}

	public void selectAcceptCookiesButton(){
		if (cookiePopUp.isDisplayed()){
			logger.info("Select Accept Cookie button");
			acceptCookiesBtn.click();
		}
		logger.info("Cookie Pop up is closed");
		cookiePopUp.shouldBe(disappear);
	}

}
