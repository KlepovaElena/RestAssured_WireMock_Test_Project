package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties properties = new Properties();
    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getBaseUrl() {
        return properties.getProperty("base-url");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
