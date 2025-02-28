package tests;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.google.common.collect.ImmutableMap;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.tvd.projects.vietjet.pages.HomePage;
import org.tvd.utilities.FileUtils;
import org.tvd.utilities.LogUtils;

import static com.codeborne.selenide.Selenide.getUserAgent;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.isHeadless;
import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

public class BaseTest {

	protected HomePage homePage;
	protected String langCode;

	@Parameters({"browser", "executionMode"})
	@BeforeClass
	public void setUp(String browser, @Optional("") String executionMode) {
		Configuration.browser = browser;
		Configuration.headless = false;
		SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
				.screenshots(true)
				.savePageSource(true)
		);
		langCode = System.getProperty("lang", "en");
		homePage = new HomePage(langCode);
		open(Configuration.baseUrl + langCode);
		getWebDriver().manage().window().maximize();
		LogUtils.info("Start TestNG testcases in ", getClass().getName(), browser);
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
						.build(), FileUtils.getCurrentDir() + "allure-results/");

		Selenide.closeWebDriver();
	}

}
