package org.tvd.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtils {
    //Initialize Log4j2 instance
    private static final Logger LOGGER = LogManager.getLogger(LogUtils.class);

    //Info Level Logs
    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String message, Object... params) {
        LOGGER.info(message, params);
    }

    //Warn Level Logs
    public static void warn(String message) {
        LOGGER.warn(message);
    }

    //Error Level Logs
    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void logException(Exception e) {
	    LOGGER.error("Exception: {}", e.getMessage(), e);
    }
}
