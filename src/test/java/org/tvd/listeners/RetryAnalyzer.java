package org.tvd.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.tvd.utilities.PropertiesUtils;

public class RetryAnalyzer implements IRetryAnalyzer {

	private static final int maxRetryCount = Integer.parseInt(PropertiesUtils.getValue("RETRY_TEST_FAIL"));  // Number of retries
	private int retryCount = 0;

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			System.out.println("Retrying test " + result.getName() + " with status " +
					getResultStatusName(result.getStatus()) + " for the " + retryCount + " time(s).");
			return true;
		}
		return false;
	}

	public String getResultStatusName(int status) {
		String resultName = null;
		if (status == 1)
			resultName = "SUCCESS";
		if (status == 2)
			resultName = "FAILURE";
		if (status == 3)
			resultName = "SKIP";
		return resultName;
	}
}
