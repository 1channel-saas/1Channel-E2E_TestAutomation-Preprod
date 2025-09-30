package com.test.channelplay.stepDefinition_Mobile;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.test.channelplay.utils.MobileDriverManager;
import com.test.channelplay.utils.MobileTestBase;
import com.test.channelplay.utils.ScreenshotHelper;
import com.test.channelplay.mobile.screens.config_Helper.MobileTestFlowScreenshotManager;

import java.io.ByteArrayInputStream;

/**
 * Allure hooks for Mobile Cucumber tests
 * Handles screenshot capture on failure and adds metadata to Allure reports
 */
public class AllureHooks_Mobile {

    private static final Logger log = LoggerFactory.getLogger(AllureHooks_Mobile.class);

    @Before(value = "@mobile", order = 1)
    public void beforeMobileScenario(Scenario scenario) {
        // Initialize test flow folder (for HTML report only)
        String folderName = MobileTestFlowScreenshotManager.initializeScenario(scenario.getName());

        // Set folder for manual screenshots too
        ScreenshotHelper.setScenarioFolder(folderName);

        log.info("Starting mobile scenario: {}", scenario.getName());
    }

    @After(value = "@mobile", order = 10)
    public void afterMobileScenario(Scenario scenario) {
        // Capture screenshot on failure
        if (scenario.isFailed()) {
            captureMobileScreenshot(scenario);
        }

        log.info("Mobile scenario '{}' finished with status: {}", scenario.getName(),
                 scenario.isFailed() ? "failed" : "passed");
    }

    private void captureMobileScreenshot(Scenario scenario) {
        try {
            AppiumDriver driver = MobileDriverManager.getDriver();
            if (driver != null) {
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);

                // Get platform name
                String platform = driver.getCapabilities().getPlatformName().toString();

                // Attach to Allure report with platform info
                Allure.addAttachment(
                    String.format("Failed Screenshot [%s] - %s", platform, scenario.getName()),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
                );

                // Also attach to Cucumber report
                scenario.attach(screenshot, "image/png", "mobile_screenshot");

                // Add device info
                Allure.addAttachment("Device Info", "text/plain",
                    String.format("Platform: %s\nDevice: %s\nApp: %s",
                        platform,
                        driver.getCapabilities().getCapability("deviceName"),
                        driver.getCapabilities().getCapability("app"))
                );

                log.info("Mobile screenshot captured for failed scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            log.error("Failed to capture mobile screenshot: {}", e.getMessage());
        }
    }

    private String getFeatureName(Scenario scenario) {
        String uri = scenario.getUri().toString();
        String featureName = uri.substring(uri.lastIndexOf("/") + 1);
        return featureName.replace(".feature", "");
    }
}