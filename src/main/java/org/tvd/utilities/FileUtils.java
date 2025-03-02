package org.tvd.utilities;

import java.io.File;

public class FileUtils {

	public static String getCurrentDir() {
		return System.getProperty("user.dir") + File.separator;
	}

	public static String getFilePath(String fileName) {
		return getCurrentDir() + "src/test/resources/" + fileName;
	}
}
