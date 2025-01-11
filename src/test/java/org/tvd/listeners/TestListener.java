package org.tvd.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.tvd.utilities.LogUtils;
import org.tvd.utilities.ScreenshotUtils;

public class TestListener implements ITestListener {
	@Override
	public void onTestStart(ITestResult result) {
		LogUtils.info("Starting test: " + result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		LogUtils.info("Test passed: " + result.getName());
	}

	@Override
	public void onTestFailure(ITestResult result) {
		LogUtils.error("Test failed: " + result.getName());
		LogUtils.logException((Exception) result.getThrowable());
		String description = "Failed test: " + result.getName();
		ScreenshotUtils.takeScreenshotAndAddToAllure(description);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		LogUtils.warn("Test skipped: " + result.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		LogUtils.warn("Test failed but within success percentage: " + result.getName());
	}

	@Override
	public void onStart(ITestContext context) {
		LogUtils.info("Starting test suite: " + context.getName());
	}

	@Override
	public void onFinish(ITestContext context) {
		LogUtils.info("Finish test suite: " + context.getName());
	}
}
