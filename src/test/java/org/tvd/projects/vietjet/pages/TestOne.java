package org.tvd.projects.vietjet.pages;

import org.testng.annotations.Test;
import org.tvd.projects.BaseTest;


public class TestOne extends BaseTest {


  @Test
  public void TC_01() {
    HomePageVJ homePage = new HomePageVJ();

    homePage.openHomePage().selectAcceptCookiesButton();
    homePage.selectDeniedAdButton();
    homePage.searchTicket("RoundTrip",2);

  }
}
