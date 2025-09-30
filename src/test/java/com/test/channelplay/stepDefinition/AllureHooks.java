package com.test.channelplay.stepDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.test.channelplay.utils.DriverBase;

import java.io.ByteArrayInputStream;

/**
 * Allure hooks for Cucumber tests
 * Handles screenshot capture on failure and adds metadata to Allure reports
 */
public class AllureHooks extends DriverBase {

    private static final Logger log = LoggerFactory.getLogger(AllureHooks.class);
    private WebDriver driver;

    // Temporarily disabled to avoid UUID conflicts with Allure Cucumber plugin
    // @Before(value = "@web and not @mobile", order = 10)
    public void beforeScenario(Scenario scenario) {
        // Disabled - Using Allure Cucumber plugin instead
        log.info("Starting scenario: {}", scenario.getName());
    }

    // Temporarily disabled to avoid conflicts with Allure Cucumber plugin
    // The Allure Cucumber plugin handles test lifecycle automatically
    // @After(value = "@web and not @mobile", order = 5)
    public void afterScenario(Scenario scenario) {
        // Disabled - The Allure Cucumber plugin handles this
        // If you need custom screenshot handling, use the regular Hooks class instead
        log.info("Scenario '{}' finished", scenario.getName());
    }

    private void captureScreenshot(Scenario scenario) {
        try {
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Attach to Allure report
                Allure.addAttachment(
                    "Failed Screenshot - " + scenario.getName(),
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
                );

                // Also attach to Cucumber report
                scenario.attach(screenshot, "image/png", "screenshot");

                log.info("Screenshot captured for failed scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
        }
    }

    private String getFeatureName(Scenario scenario) {
        String uri = scenario.getUri().toString();
        String featureName = uri.substring(uri.lastIndexOf("/") + 1);
        return featureName.replace(".feature", "");
    }
}