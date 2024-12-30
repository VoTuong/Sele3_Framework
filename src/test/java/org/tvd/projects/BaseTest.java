package org.tvd.projects;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.selenide.AllureSelenide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import org.tvd.base.DriverFactory;

import java.net.MalformedURLException;

import static com.codeborne.selenide.Selenide.getUserAgent;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.isHeadless;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;
import static java.lang.invoke.MethodHandles.lookup;

public class BaseTest {
	private static final Logger log = LoggerFactory.getLogger(lookup().lookupClass());

//	static {
//		SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
//	}

	@Parameters({"browser", "executionMode"})
	@BeforeClass
	public void setUp(String browser, @Optional("") String executionMode) throws MalformedURLException {
		DriverFactory.setupDriver(browser, executionMode);

		Configuration.headless = false;
		SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
		log.info("Start {} TestNG testcases in {}", getClass().getName(), Configuration.browser);
	}


	@BeforeMethod
	public void launch() {
		open();
		getWebDriver().manage().window().maximize();
	}

	@AfterMethod
	public void tearDown() {
		// Close the browser after each test
//		screenshot();
		allureEnvironmentWriter(
				ImmutableMap.<String, String>builder()
						.put("BASE_URL", Configuration.baseUrl)
						.put("WebDriver", String.valueOf(getWebDriver()))
						.put("UserAgent", getUserAgent())
						.put("isHeadless", String.valueOf(isHeadless()))
						.build(), System.getProperty("user.dir") + "/allure-results/");
		Selenide.closeWebDriver();
	}

//	@Attachment(type = "image/png")
//	public byte[] screenshot() throws IOException {
//		File screenshot = new File(Screenshots.saveScreenshotAndPageSource());
//		return Files.toByteArray(screenshot);
//	}

}
