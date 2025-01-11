package org.tvd.utilities;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class ScreenshotUtils {

	@Attachment(value = "Screenshot on failure", type = "image/png")
	public static byte[] takeScreenshot() {
		try {
			File screenshotFile = new File(Objects.requireNonNull(Selenide.screenshot("screenshot.png")));
			byte[] fileContent = Files.readAllBytes(screenshotFile.toPath());
			LogUtils.info("Take a screenshot");
			return fileContent;
		} catch (IOException e) {
			LogUtils.error("Error when try get a screenshot " + e);
			return null;
		}
	}

	public static void takeScreenshotAndAddToAllure(String description) {
		byte[] screenshot = takeScreenshot();
		if(screenshot != null) {
			saveScreenshotToAllure(screenshot, description);
		}
	}

	@Attachment(value = "screenshot", type = "image/png")
	@SuppressWarnings("unused")
	private static byte[] saveScreenshotToAllure(byte[] content, String description) {
		LogUtils.info("Add screenshot to report: " + description);
		return content;
	}
}
