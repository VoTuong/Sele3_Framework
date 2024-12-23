package org.tvd.base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {

	public static void setupDriver(String browser, String mode) throws MalformedURLException {

		if ("grid".equalsIgnoreCase(mode)) {
			RemoteWebDriver driver = getRemoteWebDriver(browser);
			WebDriverRunner.setWebDriver(driver);
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
		URL gridUrl = new URL("http://localhost:4444/wd/hub"); // URL of Selenium Grid
		MutableCapabilities capabilities = new DesiredCapabilities();

		switch (browser.toLowerCase()) {
			case "chrome":
				capabilities.setCapability("browserName", "chrome");
				break;
			case "firefox":
				capabilities.setCapability("browserName", "firefox");
				break;
			case "edge":
				capabilities.setCapability("browserName", "MicrosoftEdge");
				break;
			default:
				throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		return new RemoteWebDriver(gridUrl, capabilities);
	}
}
