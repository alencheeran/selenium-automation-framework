package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    // Static block - runs once when class is loaded
    static {
        try {
            FileInputStream file = new FileInputStream(
                    System.getProperty("user.dir") + "/config.properties"
            );
            properties = new Properties();
            properties.load(file);
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("config.properties file not found: " + e.getMessage());
        }
    }

    public static String getBrowser() {
        return properties.getProperty("browser");
    }

    public static String getBaseUrl() {
        return properties.getProperty("baseUrl");
    }

    public static String getValidUsername() {
        return properties.getProperty("validUsername");
    }

    public static String getValidPassword() {
        return properties.getProperty("validPassword");
    }

    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicitWait"));
    }
}