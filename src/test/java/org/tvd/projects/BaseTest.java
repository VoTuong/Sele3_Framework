package org.tvd.projects;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.epam.reportportal.selenide.ReportPortalSelenideEventListener;
import com.epam.reportportal.service.ReportPortal;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.tvd.base.DriverFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.screenshot;
import static java.lang.invoke.MethodHandles.lookup;

public class BaseTest {
	private static final Logger log = LoggerFactory.getLogger(lookup().lookupClass());


	static {
		SelenideLogger.addListener(
				"ReportPortal logger",
				new ReportPortalSelenideEventListener()
						.enableSeleniumLogs(LogType.BROWSER, Level.FINER)
						.logScreenshots(true)
						.logPageSources(false)
		);
	}

	@Parameters({"browser", "executionMode"})
	@BeforeClass
	public void setUp(String browser, @Optional("") String executionMode) throws MalformedURLException {
		DriverFactory.setupDriver(browser, executionMode);

		Configuration.headless = false;
		log.info("Start {} TestNG testcases in {}", getClass().getName(), Configuration.browser);
	}


	@BeforeMethod
	public void launch() {
		open();
		WebDriverRunner.getWebDriver().manage().window().maximize();
	}

	@AfterMethod
	public void tearDown() {
		// Close the browser after each test
		Selenide.closeWebDriver();
	}

	@AfterMethod
	public void addAttachmentOnFailure(ITestResult testResult) {
		if (!testResult.isSuccess()) {
			if (WebDriverRunner.getWebDriver() instanceof TakesScreenshot) {
				File screenshot = screenshot(OutputType.FILE);
				ReportPortal.emitLog("Test failed - Screenshot attached " + testResult.getMethod().getMethodName(), "ERROR", new Date(), screenshot);
			}
		}
	}

}
