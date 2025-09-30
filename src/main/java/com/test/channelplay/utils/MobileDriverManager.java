package com.test.channelplay.utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;

public class MobileDriverManager {

    private static final Logger log = LoggerFactory.getLogger(MobileDriverManager.class);

    private MobileDriverManager() {}

    private static final ThreadLocal<AppiumDriver> ldriver = new ThreadLocal<>();

    // Appium 3.x doesn't use /wd/hub
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";


    public static AppiumDriver getDriver() {
        return ldriver.get();
    }

    public static void setDriver(AppiumDriver appiumDriver) {
        ldriver.set(appiumDriver);
    }



    //  Default method - determines automation type based on property or defaults to UiAutomator2
    public static void initializeAndroidDriver(String appPath, String deviceName) {
        String automationType = GetProperty.value("mobile.automation.type");
        if (automationType == null || automationType.isEmpty()) {
            automationType = "uiautomator2"; // Default to UiAutomator2
        }

        if (automationType.equalsIgnoreCase("flutter")) {
            initializeAndroidFlutterDriver(appPath, deviceName);
        } else {
            initializeAndroidUiAutomator2Driver(appPath, deviceName);
        }
    }
    

    // Method for Flutter automation
    public static void initializeAndroidFlutterDriver(String appPath, String deviceName) {
        try {
            UiAutomator2Options options = new UiAutomator2Options();

            // Basic Android capabilities
            options.setDeviceName(deviceName);
            options.setApp(appPath);
            options.setAutomationName("Flutter");
            options.setNewCommandTimeout(Duration.ofSeconds(300));

            // Flutter specific capabilities
            options.setCapability("appium:retryBackoffTime", 3000);
            options.setCapability("appium:enableFlutterDriverExtension", true);

            // Timeout settings
            options.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
            options.setCapability("appium:adbExecTimeout", 60000);

            // Additional capabilities
            options.setCapability("appium:autoGrantPermissions", true);
            options.setCapability("appium:noReset", false);
            options.setCapability("appium:fullReset", false);

            URL appiumServerURL = new URL(APPIUM_SERVER_URL);
            System.out.println("Connecting to Appium server at: " + APPIUM_SERVER_URL);
            System.out.println("Device Name: " + deviceName);
            System.out.println("App Path: " + appPath);
            System.out.println("Automation: Flutter");

            AndroidDriver androidDriver = new AndroidDriver(appiumServerURL, options);
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            setDriver(androidDriver);
            log.info("Android Flutter Driver initialized successfully");

        } catch (Exception e) {
            System.err.println("=== APPIUM CONNECTION ERROR ===");
            System.err.println("Failed to connect to Appium server at: " + APPIUM_SERVER_URL);
            System.err.println("Make sure:");
            System.err.println("1. Appium server is running (execute 'appium' in terminal)");
            System.err.println("2. Android device/emulator is connected (check with 'adb devices')");
            System.err.println("3. App file exists at: " + appPath);
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Android Flutter driver: " + e.getMessage());
        }
    }

    // Method for UiAutomator2 automation
    public static void initializeAndroidUiAutomator2Driver(String appPath, String deviceName) {
        try {
            UiAutomator2Options options = new UiAutomator2Options();

            // Basic Android capabilities
            options.setDeviceName(deviceName);
            options.setApp(appPath);
            options.setAutomationName("UiAutomator2");
            options.setNewCommandTimeout(Duration.ofSeconds(300));

            // Timeout settings
            options.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);
            options.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
            options.setCapability("appium:adbExecTimeout", 60000);

            // Additional capabilities
            options.setCapability("appium:autoGrantPermissions", true);
            options.setCapability("appium:noReset", false);
            options.setCapability("appium:fullReset", false);
            options.setCapability("appium:ensureWebviewsHavePages", true);
            
            // Emulator keyboard settings
            options.setCapability("appium:unicodeKeyboard", true);
            options.setCapability("appium:resetKeyboard", true);

            URL appiumServerURL = new URL(APPIUM_SERVER_URL);
            System.out.println("Connecting to Appium server at: " + APPIUM_SERVER_URL);
            System.out.println("Device Name: " + deviceName);
            System.out.println("App Path: " + appPath);
            System.out.println("Automation: UiAutomator2");

            AndroidDriver androidDriver = new AndroidDriver(appiumServerURL, options);
            androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            setDriver(androidDriver);
            log.info("Android UiAutomator2 Driver initialized successfully");

        } catch (Exception e) {
            System.err.println("=== APPIUM CONNECTION ERROR ===");
            System.err.println("Failed to connect to Appium server at: " + APPIUM_SERVER_URL);
            System.err.println("Make sure:");
            System.err.println("1. Appium server is running (execute 'appium' in terminal)");
            System.err.println("2. Android device/emulator is connected (check with 'adb devices')");
            System.err.println("3. App file exists at: " + appPath);
            System.err.println("Error details: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Android UiAutomator2 driver: " + e.getMessage());
        }
    }

    public static void initializeIOSDriver(String appPath, String deviceName, String platformVersion) {

        try {
            XCUITestOptions options = new XCUITestOptions();

            // Basic iOS capabilities
            options.setDeviceName(deviceName);
            options.setPlatformVersion(platformVersion);
            options.setApp(appPath);
            options.setAutomationName("Flutter");
            options.setNewCommandTimeout(Duration.ofSeconds(60));

            // Flutter specific capabilities
            options.setCapability("appium:retryBackoffTime", 3000);
            options.setCapability("appium:enableFlutterDriverExtension", true);

            // Additional iOS capabilities
            options.setCapability("appium:autoAcceptAlerts", true);
            options.setCapability("appium:noReset", false);
            options.setCapability("appium:fullReset", false);

            URL appiumServerURL = new URL(APPIUM_SERVER_URL);
            IOSDriver iosDriver = new IOSDriver(appiumServerURL, options);
            iosDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            setDriver(iosDriver);
            System.out.println("iOS Driver initialized successfully");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize iOS driver: " + e.getMessage());
        }
    }
    

    public static void quitDriver() {
        if (ldriver.get() != null) {
            ldriver.get().quit();
            ldriver.remove();
            log.info("Mobile driver closed successfully");
        }
    }

}