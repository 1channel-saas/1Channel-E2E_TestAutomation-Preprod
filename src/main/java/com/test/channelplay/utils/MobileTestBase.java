package com.test.channelplay.utils;

import com.test.channelplay.mobile.config_Helper.FlutterFinderUtils;
import com.test.channelplay.mobile.config_Helper.TemplateUsageTracker;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

public class MobileTestBase {

    private static final Logger log = LoggerFactory.getLogger(MobileTestBase.class);
    
    protected AppiumDriver driver;
    protected FlutterFinderUtils flutterFinder;

    public AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    private String platform;
    private String appPath;
    private String deviceName;


    @BeforeClass
    @Parameters({"platform", "deviceName"})
    public void setupMobileDriver(@Optional("android") String platform, @Optional("emulator") String deviceName) {
        this.platform = platform;
        this.deviceName = deviceName;
        
        // Get app path from properties file
        if (platform.equalsIgnoreCase("android")) {
            this.appPath = GetProperty.value("android.app.path");
            if (appPath == null || appPath.isEmpty()) {
                // Default path if not in properties
                this.appPath = System.getProperty("user.dir") + "/apps/app-debug.apk";
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            this.appPath = GetProperty.value("ios.app.path");
            if (appPath == null || appPath.isEmpty()) {
                // Default path if not in properties
                this.appPath = System.getProperty("user.dir") + "/apps/app-debug.ipa";
            }
        }
        
        initializeDriver();
    }

    
    private void initializeDriver() {
        try {
            if (platform.equalsIgnoreCase("android")) {
                MobileDriverManager.initializeAndroidDriver(appPath, deviceName);
            } else if (platform.equalsIgnoreCase("ios")) {
                String platformVersion = GetProperty.value("ios.platform.version");
                if (platformVersion == null) {
                    platformVersion = "15.0"; // Default iOS version
                }
                MobileDriverManager.initializeIOSDriver(appPath, deviceName, platformVersion);
            } else {
                throw new IllegalArgumentException("Platform must be either 'android' or 'ios'");
            }
            
            driver = MobileDriverManager.getDriver();
            flutterFinder = new FlutterFinderUtils(driver);

            // Enable template usage tracking for enhanced metadata
            TemplateUsageTracker.setEnabled(true);
            log.info("Template usage tracking enabled");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize mobile driver: " + e.getMessage());
        }
    }

    
    @AfterClass
    public void tearDown() {
        MobileDriverManager.quitDriver();
    }


    
    // Helper methods for common mobile actions
    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    protected void hideKeyboard() {
        try {
            getDriver().navigate().back();
            Thread.sleep(500);
        } catch (Exception e) {
            log.error("Failed to hide keyboard: {}", e.getMessage());
        }
    }
    
    protected boolean isElementPresent(String key) {
        try {
            return flutterFinder.findByValueKey(key) != null;
        } catch (Exception e) {
            return false;
        }
    }

}