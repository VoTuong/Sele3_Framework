package org.tvd.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
	private static final Logger LOGGER = LogManager.getLogger(LogUtils.class);

	public static void info(String message) {
		LOGGER.info("Class: {} - Message: {}", getCallerClassName(), message);
	}

	public static void info(String message, Object... params) {
		LOGGER.info("Class: {} - Message: {} {}", getCallerClassName(), message, params);
	}

	public static void warn(String message) {
		LOGGER.warn("Class: {} - Message: {}", getCallerClassName(), message);
	}

	private static String getCallerClassName() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		return stackTrace[3].getClassName();
	}
}
