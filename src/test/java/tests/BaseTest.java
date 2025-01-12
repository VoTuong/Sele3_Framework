package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.google.common.collect.ImmutableMap;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.tvd.base.DriverFactory;
import org.tvd.utilities.LogUtils;
import org.tvd.utilities.ScreenshotUtils;

import java.net.MalformedURLException;

import static com.codeborne.selenide.Selenide.getUserAgent;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.isHeadless;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class BaseTest {
//	static {
//		SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
//	}

	@Parameters({"browser", "executionMode"})
	@BeforeClass
	public void setUp(String browser, @Optional("") String executionMode) throws MalformedURLException {
		DriverFactory.setupDriver(browser, executionMode);

		Configuration.headless = false;
//		SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
		LogUtils.info("Start TestNG testcases in {}", getClass().getName(), Configuration.browser);
	}


	@BeforeMethod
	public void launch() {
		open();
		getWebDriver().manage().window().maximize();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		// Close the browser after each test
		allureEnvironmentWriter(
				ImmutableMap.<String, String>builder()
						.put("BASE_URL", Configuration.baseUrl)
						.put("WebDriver", String.valueOf(getWebDriver()))
						.put("UserAgent", getUserAgent())
						.put("isHeadless", String.valueOf(isHeadless()))
						.build(), System.getProperty("user.dir") + "/allure-results/");

		if (result.getStatus() == ITestResult.FAILURE) {
			LogUtils.error("Test case failed: " + result.getName());
			ScreenshotUtils.takeScreenshotAndAddToAllure(result.getName());
		}
		Selenide.closeWebDriver();
	}

}
