package com.test.channelplay.stepDefinition_Mobile;

import com.test.channelplay.mobile.screens.config_Helper.MobileTestFlowScreenshotManager;
import com.test.channelplay.utils.MobileTestBase;
import com.test.channelplay.utils.MobileDriverManager;
import com.test.channelplay.utils.GetProperty;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks_Mobile extends MobileTestBase {

    private static final Logger log = LoggerFactory.getLogger(Hooks_Mobile.class);
    private static String platform;
    private static String appPath;
    private static String deviceName;
    private MobileTestFlowScreenshotManager testFlowManager;
    private String currentStepName;

    
    @Before("@mobile")
    public void setUpMobile(Scenario scenario) {
        System.out.println("Starting mobile test scenario: " + scenario.getName());

        // Reset the environment log flag for each new mobile scenario
        GetProperty.resetEnvironmentLog();

        // Initialize the mobile driver before each mobile scenario
        try {
            // Get configuration from properties
            platform = GetProperty.value("mobile.platform");
            deviceName = GetProperty.value("mobile.device.name");
            
            if (platform == null) platform = "android";
            if (deviceName == null) deviceName = "emulator";
            
            // Get app path from properties file
            if (platform.equalsIgnoreCase("android")) {
                appPath = GetProperty.value("android.app.path");
                if (appPath == null || appPath.isEmpty()) {
                    appPath = System.getProperty("user.dir") + "/apps/app-debug.apk";
                }
                MobileDriverManager.initializeAndroidDriver(appPath, deviceName);
            } else if (platform.equalsIgnoreCase("ios")) {
                appPath = GetProperty.value("ios.app.path");
                if (appPath == null || appPath.isEmpty()) {
                    appPath = System.getProperty("user.dir") + "/apps/app-debug.ipa";
                }
                String platformVersion = GetProperty.value("ios.platform.version");
                if (platformVersion == null) platformVersion = "15.0";
                MobileDriverManager.initializeIOSDriver(appPath, deviceName, platformVersion);
            }
            
            driver = getDriver();  // Use inherited getDriver() method
            log.info("Mobile driver initialized for platform: {}, device: {}", platform, deviceName);

            // Initialize test flow screenshot manager
            testFlowManager = new MobileTestFlowScreenshotManager(driver);
            // Scenario initialization is handled by AllureHooks_Mobile to avoid duplicates
            // MobileTestFlowScreenshotManager.initializeScenario(scenario.getName());
            log.info("Test flow screenshot manager initialized for scenario: {}", scenario.getName());

        } catch (Exception e) {
            log.error("Failed to initialize mobile driver: {}", e.getMessage());
            throw new RuntimeException("Mobile driver initialization failed", e);
        }
    }
    
    @After("@mobile")
    public void tearDownMobile(Scenario scenario) {
        System.out.println("Ending mobile test scenario: " + scenario.getName());
        
        // NOTE: Auto-captured templates are NOT cleaned up automatically
        // This allows the AI to learn from successful finds and build a template library
        // Templates persist across test runs for better AI fallback performance
        // To manually clean templates, call: xpathHelper.cleanupAutoTemplates()
        log.info("Preserving auto-captured templates for AI learning");
        
        // Capture screenshot if scenario fails
        if (scenario.isFailed() && getDriver() != null) {
            try {
                TakesScreenshot screenshot = (TakesScreenshot) getDriver();
                byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshotBytes, "image/png", "Mobile_Screenshot_" + scenario.getName());
                System.out.println("Screenshot captured for failed scenario");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
        
        // Clean up test flow tracking
        MobileTestFlowScreenshotManager.cleanupScenario(scenario.getName());

        // Tear down the mobile driver after each scenario
        try {
            MobileDriverManager.quitDriver();
            log.info("Mobile driver closed successfully from Hooks");
        } catch (Exception e) {
            log.error("Error during mobile driver teardown: {}", e.getMessage());
        }
    }
    
    @Before(value = "@mobile and @resetApp")
    public void resetApp() {
        System.out.println("Resetting mobile application to initial state");
        try {
            if (getDriver() != null) {
                // For Appium 2.x, resetApp is deprecated. Use terminateApp and activateApp instead
                String appPackage = GetProperty.value("app.package.name");
                if (appPackage == null || appPackage.isEmpty()) {
                    appPackage = "com.channelplay.app"; // Default package name
                }
                if (getDriver() instanceof AndroidDriver) {
                    ((AndroidDriver) getDriver()).terminateApp(appPackage);
                    Thread.sleep(1000);
                    ((AndroidDriver) getDriver()).activateApp(appPackage);
                } else if (getDriver() instanceof IOSDriver) {
                    ((IOSDriver) getDriver()).terminateApp(appPackage);
                    Thread.sleep(1000);
                    ((IOSDriver) getDriver()).activateApp(appPackage);
                }
                System.out.println("Mobile app reset successful");
            }
        } catch (Exception e) {
            System.err.println("Failed to reset app: " + e.getMessage());
        }
    }
    
    @After(value = "@mobile and @clearAppData")
    public void clearAppData() {
        System.out.println("Clearing mobile application data");
        try {
            if (getDriver() != null) {
                String appPackage = GetProperty.value("app.package.name");
                if (appPackage == null || appPackage.isEmpty()) {
                    appPackage = "com.channelplay.app"; // Default package name
                }
                if (getDriver() instanceof AndroidDriver) {
                    ((AndroidDriver) getDriver()).terminateApp(appPackage);
                    ((AndroidDriver) getDriver()).activateApp(appPackage);
                } else if (getDriver() instanceof IOSDriver) {
                    ((IOSDriver) getDriver()).terminateApp(appPackage);
                    ((IOSDriver) getDriver()).activateApp(appPackage);
                }
                System.out.println("App data cleared successfully");
            }
        } catch (Exception e) {
            System.err.println("Failed to clear app data: " + e.getMessage());
        }
    }

    @BeforeStep("@mobile")
    public void beforeStep(Scenario scenario) {
        // Capture step name for screenshot naming
        currentStepName = "step";  // Default name
        // Note: Cucumber doesn't provide direct access to step text in BeforeStep
        // The actual step name will be captured in AfterStep if available
    }

    @AfterStep("@mobile")
    public void afterStep(Scenario scenario) {
        // Capture screenshot after each step for test flow documentation
        if (testFlowManager != null && driver != null) {
            try {
                // Get step information if available
                String stepInfo = currentStepName != null ? currentStepName : "step";

                // Capture the screenshot
                testFlowManager.captureStep(scenario.getName(), stepInfo);

                log.debug("Mobile screenshot captured for step: {}", stepInfo);
            } catch (Exception e) {
                // Don't fail the test if screenshot capture fails
                log.debug("Could not capture test flow screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Public method to capture custom test flow screenshots
     * Can be called from step definitions when needed
     */
    public void captureTestFlowScreenshot(String description) {
        if (testFlowManager != null) {
            Scenario scenario = getCurrentScenario();
            if (scenario != null) {
                testFlowManager.captureStep(scenario.getName(), description);
            }
        }
    }

    /**
     * Enable or disable test flow screenshots
     * Can be called from step definitions
     */
    public void setTestFlowScreenshotsEnabled(boolean enabled) {
        if (testFlowManager != null) {
            testFlowManager.setEnabled(enabled);
        }
    }

    // Store current scenario for access in helper methods
    private static ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

    @Before("@mobile")
    public void storeScenario(Scenario scenario) {
        currentScenario.set(scenario);
    }

    @After("@mobile")
    public void clearScenario() {
        currentScenario.remove();
    }

    private Scenario getCurrentScenario() {
        return currentScenario.get();
    }

}