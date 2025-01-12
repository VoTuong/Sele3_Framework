package org.tvd.utilities;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class ScreenshotUtils {

	@Attachment(value = "{description}", type = "image/png")
	public static byte[] takeScreenshot(String description) {
		String screenshotPath = Selenide.screenshot(description);
		try {
			assert screenshotPath != null;
			URI uri = new URI(screenshotPath);
			File screenshotFile = Paths.get(uri).toFile();
			String absolutePath = screenshotFile.getAbsolutePath();
			LogUtils.info("Screenshot saved to: " + absolutePath);
			return Files.readAllBytes(screenshotFile.toPath());
		} catch (Exception e) {
			LogUtils.error("Failed to take screenshot: ", e.getMessage(), e);
			return null;
		}
	}

	public static void takeScreenshotAndAddToAllure(String description) {
		byte[] screenshot = takeScreenshot(description);
		if (screenshot != null) {
			Allure.addAttachment(description, "image/png", Arrays.toString(screenshot));
		} else {
			Allure.addAttachment("Screenshot failed", "text/plain", "Screenshot was not taken due to an error.");
		}
	}
}
