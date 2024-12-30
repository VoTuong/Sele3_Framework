package org.tvd.listeners;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class AllureListener implements TestLifecycleListener {

	protected static final String LOG_PATH = "build/logs/";
	private static final ThreadLocal<String> TL_TEST_UUID = new ThreadLocal<>();
	private static final String LOG_FILE_PATTERN = "test-%s.log";
	private final Logger LOGGER = LogManager.getLogger();

	@Override
	public void beforeTestSchedule(TestResult result) {
	}

	@Override
	public void afterTestSchedule(TestResult result) {
	}

	@Override
	public void beforeTestUpdate(TestResult result) {
	}

	@Override
	public void afterTestUpdate(TestResult result) {
	}

	@Override
	public void beforeTestStart(TestResult result) {
	}

	@Override
	public void afterTestStart(TestResult result) {
		startTestLogging();
	}

	@Override
	public void beforeTestStop(TestResult result) {
		stopTestLogging();
		if (result.getStatus().equals(Status.FAILED)) {
			if (WebDriverRunner.hasWebDriverStarted()) {
				Allure.addAttachment(result.getName() + "_Failed_Screenshot", new ByteArrayInputStream(Objects.requireNonNull(Selenide.screenshot(OutputType.BYTES))));
			}
		}
	}

	@Override
	public void afterTestStop(TestResult result) {

	}

	@Override
	public void beforeTestWrite(TestResult result) {
	}

	@Override
	public void afterTestWrite(TestResult result) {
	}

	private void startTestLogging() {
		TL_TEST_UUID.set(UUID.randomUUID().toString());
		ThreadContext.put("uuid", TL_TEST_UUID.get());
	}

	private void stopTestLogging() {
		ThreadContext.remove("uuid");

		String logFileName = String.format(LOG_FILE_PATTERN, TL_TEST_UUID.get());
		File logFile = Paths.get(LOG_PATH, logFileName).toFile();
		addLogAttachment(logFile);
	}

	private void addLogAttachment(File logFile) {
		try {
			Allure.addAttachment("Log", new FileInputStream(
					logFile));
		} catch (FileNotFoundException e) {
			LOGGER.warn("There is no log file {}", logFile.getName());


		}
	}
}
