package org.tvd.utilities;


import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtils {

    private static Properties properties;
    private static FileInputStream file;

    private static final String propertiesFilePath = "src/test/resources/properties/configs.properties";

    public static String getValue(String key) {
        String keyValue = null;
        try {
            if (file == null) {
                properties = new Properties();
                String linkFile = FileUtils.getCurrentDir() + propertiesFilePath;
                file = new FileInputStream(linkFile);
                properties.load(file);
                file.close();
            }
            // Get value from file
            keyValue = properties.getProperty(key);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return keyValue;
    }

}
