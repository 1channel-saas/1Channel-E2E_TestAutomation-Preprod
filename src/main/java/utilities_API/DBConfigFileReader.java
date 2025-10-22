package utilities_API;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBConfigFileReader {
    private static final Properties properties;
    private DBConfigFileReader() {}


    static {
        try {
            FileInputStream fileInputStream = new FileInputStream("database.properties");
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Database configuration file");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
