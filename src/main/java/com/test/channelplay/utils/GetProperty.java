package com.test.channelplay.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GetProperty {

    private static final Logger Log = LoggerFactory.getLogger(GetProperty.class);
    private static final String ENVIRONMENT = "environment";
    private static String value = null;
    String envValue;

    public static String value(String key) {
        Properties prop = new Properties();
        GetProperty gp = new GetProperty();
        try {
            FileReader reader = new FileReader("application.properties");
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

    /*private String getValueForEnv(String key) {

        if (System.getProperty(ENVIRONMENT).equals("QA")) {
            envValue = "qa." + key;
        }
        if (System.getProperty(ENVIRONMENT).equals("PreProd")) {
            envValue = "preprod." + key;
        }
        if (System.getProperty(ENVIRONMENT).equals("Production")) {
            envValue = "prod." + key;
        } else {
            if (System.getProperty(ENVIRONMENT).equals("") || System.getProperty(ENVIRONMENT).equals(" ")) {
                Log.warn("*** Please pass a valid environment in System.getProperties");
            }
            else
                Log.warn("Unknown environment");
        }
        System.out.println("Environment value is: " + System.getProperty(ENVIRONMENT));

        return envValue;
    }   */

    private String getValueForEnv(String key) {
        String env = System.getProperty(ENVIRONMENT);

        //  Prevents NullPointerException
        if (env == null || env.trim().isEmpty()) {
            Log.warn("*** Please pass a valid environment in System properties");
            return null;
        }

        switch (env) {
            case "QA":
                envValue = "qa." + key;
                break;
            case "PreProd":
                envValue = "preprod." + key;
                break;
            case "Production":
                envValue = "prod." + key;
                break;
            default:
                Log.warn("Unknown environment: {}", env);
                envValue = null;
                break;
        }
        Log.info("Environment value is: {}", env);
        return envValue;
    }

}
