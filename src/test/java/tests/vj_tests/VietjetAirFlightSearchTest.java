package tests.vj_tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.tvd.projects.vietjet.models.PassengerCount;
import org.tvd.projects.vietjet.models.TicketModel;
import org.tvd.projects.vietjet.pages.ChooseFlightPage;
import org.tvd.projects.vietjet.pages.HomePage;
import tests.BaseTest;


@Feature("Flight Search")
public class VietjetAirFlightSearchTest extends BaseTest {

	@DataProvider(name = "flightSearchDataProvider")
	public Object[][] flightSearchDataProvider() {
		return new Object[][]{
				{new TicketModel("round trip", "SGN", "HAN", new PassengerCount(2))}
		};
	}

	@Test(dataProvider = "flightSearchDataProvider")
	@Description("Verify flight search functionality on Vietjet website")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Search for the cheapest round-trip flight in the next 3 days")
	public void testSearchForFlights(TicketModel ticket) {
		HomePage homePage = new HomePage();
		ChooseFlightPage chooseFlightPage = new ChooseFlightPage();
		homePage.openHomePage();
		homePage.searchTicket(ticket);
		chooseFlightPage.shouldDisplayTicketFlight();
		chooseFlightPage.chooseTheLowestPriceTicket();
		chooseFlightPage.selectContinue();
	}
}
