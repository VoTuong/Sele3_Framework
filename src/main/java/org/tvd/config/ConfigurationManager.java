package org.tvd.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationManager {

	private static final Properties properties = new Properties();

	static {
		loadProperties("config.properties");
		loadProperties("environments.properties");
	}

	private static void loadProperties(String fileName) {
		try (InputStream input = ConfigurationManager.class.getClassLoader().getResourceAsStream(fileName)) {
			if (input == null) {
				System.out.println("Unable to find " + fileName);
				return;
			}
			properties.load(input);
		} catch (IOException ex) {
			System.out.println("Error load file " + fileName + " " + ex);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}
