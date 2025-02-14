package utilities_API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetProperty_API {
    private static final Logger Log = LoggerFactory.getLogger(GetProperty_API.class);
    private static final String ENVIRONMENT = "environment";
    private static String value = null;
    String envValue;

    public static String value(String key) {
        Properties prop = new Properties();
        GetProperty_API gp = new GetProperty_API();

        try {
            FileReader reader = new FileReader("application_API.properties");
            prop.load(reader);
            value = prop.getProperty(gp.getValueForEnv(key));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }   catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private String getValueForEnv(String key) {

        if (System.getProperty(ENVIRONMENT).equals("QA")) {
            envValue = "qa." + key;
        }
        if (System.getProperty(ENVIRONMENT).equals("PreProd")) {
            envValue = "preprod." + key;
        }
        if (System.getProperty(ENVIRONMENT).equals("Production")) {
            envValue = "prod." + key;
        }

        return envValue;
    }

/*    // Modified version of getValueForEnv(). not in use currently.
    private static String getEnvironmentValueForLogger(String key) {
        String env = System.getProperty(ENVIRONMENT);

        if ("QA".equals(env)) {
            return "qa." + key;
        }
        else if ("preProd".equals(env)) {
            return "preProd." + key;
        }
        else if ("Production".equals(env)) {
            return "prod." + key;
        }
        else {
            throw new IllegalArgumentException("Unknown environment: " + env);
        }
    }   */

}