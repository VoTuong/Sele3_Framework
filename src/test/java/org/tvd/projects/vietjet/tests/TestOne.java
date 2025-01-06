package org.tvd.projects.vietjet.tests;

import org.testng.annotations.Test;
import org.tvd.projects.BaseTest;
import org.tvd.projects.vietjet.models.PassengerCount;
import org.tvd.projects.vietjet.models.TicketModel;
import org.tvd.projects.vietjet.pages.HomePage;
import org.tvd.projects.vietjet.pages.SelectFlightPage;


public class TestOne extends BaseTest {

  TicketModel ticket = new TicketModel("round trip","SGN", "HAN",new PassengerCount(2));

  @Test
  public void TC_01() {
    HomePage homePage = new HomePage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();

    homePage.openHomePage().selectAcceptCookiesButton();
    homePage.selectDeniedAdButton();
    homePage.searchTicket(ticket);
    selectFlightPage.shouldDisplayTicketFlight();
  }
}
