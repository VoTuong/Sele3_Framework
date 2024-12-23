package org.tvd.utilities;

import java.io.File;

public class FileUtils {

	public static String getCurrentDir() {
		return System.getProperty("user.dir") + File.separator;
	}
}
