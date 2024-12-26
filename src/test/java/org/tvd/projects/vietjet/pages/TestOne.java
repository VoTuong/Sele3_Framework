package org.tvd.projects.vietjet.pages;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import static com.codeborne.selenide.Selenide.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestOne {

  @BeforeClass
  public void setUp() {
    Configuration.browser = "chrome";
//    Map<String, Object> vars = new HashMap<>();
  }
  @AfterClass
  public void tearDown() {
  }
  @Test
  public void One1() {
    open("https://www.vietjetair.com/vi");
    WebDriverRunner.getWebDriver().manage().window().maximize();
//    switchTo().frame(12);
    if ($("#NC_CTA_TWO").isDisplayed()) $("#NC_CTA_TWO").click();
//    switchTo().defaultContent();
    $(".jss703 .MuiTypography-root").click();
    executeJavaScript("window.scrollTo(0,1)");
    $(".jss362:nth-child(2) .MuiInputBase-root").click();
    actions().moveToElement($(".jss358")).release().perform();
    WebDriverRunner.closeWindow();
  }
}
