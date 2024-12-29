package org.tvd.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.tvd.utilities.PropertiesUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

	public static void setupDriver(String browser, String mode) throws MalformedURLException {

		if ("grid".equalsIgnoreCase(mode)) {
			RemoteWebDriver driver = getRemoteWebDriver(browser);
			WebDriverRunner.setWebDriver(driver);
//			Configuration.remote = PropertiesUtils.getValue("GRID_URL");
		} else {
			switch (browser.toLowerCase()) {
				case "chrome":
					Configuration.browser = "chrome";
					break;
				case "firefox":
					Configuration.browser = "firefox";
					break;
				case "edge":
					Configuration.browser = "edge";
					break;
				default:
					throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
		}

	}

	private static RemoteWebDriver getRemoteWebDriver(String browser) throws MalformedURLException {
		URL gridUrl = new URL(PropertiesUtils.getValue("GRID_URL")); // URL of Selenium Grid

		MutableCapabilities options;

		if (browser.equalsIgnoreCase("chrome")) {
			options = new ChromeOptions();
		} else if (browser.equalsIgnoreCase("edge")) {
			options = new EdgeOptions();
		} else {
			throw new IllegalArgumentException("Invalid browser: " + browser);
		}

		return new RemoteWebDriver(gridUrl, options);

	}
}
