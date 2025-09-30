package com.test.channelplay.stepDefinition;

import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebTestFlowScreenshotManager;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Separate hooks class for web screenshot management
 * Works alongside existing Hooks.java without any modifications
 * Only activates for @web tagged scenarios
 */
public class WebScreenshotHooks extends DriverBase {

    private static final Logger log = LoggerFactory.getLogger(WebScreenshotHooks.class);
    private WebTestFlowScreenshotManager screenshotManager;
    private String currentScenarioName;
    private String currentStepName;

    /**
     * Initialize screenshot manager after web driver is created
     * This runs AFTER Hooks.java @Before("@web") which creates the driver
     * Order is set to 20000 to run after main hooks (default order is 10000)
     */
    @Before(value = "@web", order = 20000)
    public void initializeScreenshotManager(Scenario scenario) {
        log.debug("WebScreenshotHooks @Before executing for scenario: {}", scenario.getName());
        try {
            // Get the driver that was initialized by Hooks.java
            WebDriver driver = getDriver();

            if (driver != null) {
                currentScenarioName = scenario.getName();
                log.debug("WebDriver found, initializing screenshot manager for: {}", currentScenarioName);

                // Initialize screenshot manager
                screenshotManager = new WebTestFlowScreenshotManager(driver);
                String folderName = WebTestFlowScreenshotManager.initializeScenario(currentScenarioName);

                log.debug("Web screenshot manager initialized for scenario: {} in folder: {}", currentScenarioName, folderName);
            } else {
                log.warn("WebDriver not available for screenshot manager initialization");
            }
        } catch (Exception e) {
            log.error("Failed to initialize web screenshot manager: {}", e.getMessage());
            // Don't fail the test for screenshot issues
        }
    }

    /**
     * Capture screenshot after each step
     * Only runs for @web tagged scenarios
     */
    @AfterStep("@web")
    public void captureStepScreenshot(Scenario scenario) {
        if (screenshotManager != null && getDriver() != null) {
            try {
                // Use a generic step name since Cucumber doesn't provide step text in AfterStep
                String stepDescription = "step";

                // Capture the screenshot
                screenshotManager.captureStep(currentScenarioName, stepDescription);

                log.debug("Web screenshot captured for step in scenario: {}", currentScenarioName);
            } catch (Exception e) {
                log.debug("Failed to capture web screenshot: {}", e.getMessage());
                // Don't fail the test for screenshot issues
            }
        }
    }

    /**
     * Clean up screenshot manager after scenario
     * Order is set to 5000 to run before main hooks cleanup (default order is 0)
     */
    @After(value = "@web", order = 5000)
    public void cleanupScreenshotManager(Scenario scenario) {
        try {
            // Clean up scenario tracking
            if (currentScenarioName != null) {
                WebTestFlowScreenshotManager.cleanupScenario(currentScenarioName);
                log.debug("Web screenshot manager cleaned up for scenario: {}", currentScenarioName);
            }

            // Clear references
            screenshotManager = null;
            currentScenarioName = null;
            currentStepName = null;

        } catch (Exception e) {
            log.error("Error during web screenshot manager cleanup: {}", e.getMessage());
            // Don't fail the test for cleanup issues
        }
    }

    /**
     * Public method to capture custom screenshots
     * Can be called from step definitions when specific screenshots are needed
     */
    public void captureCustomScreenshot(String description) {
        if (screenshotManager != null && currentScenarioName != null) {
            try {
                screenshotManager.captureCustomStep(currentScenarioName, description);
                log.debug("Custom web screenshot captured: {}", description);
            } catch (Exception e) {
                log.debug("Failed to capture custom web screenshot: {}", e.getMessage());
            }
        }
    }

    /**
     * Check if screenshot manager is initialized and enabled
     */
    public boolean isScreenshotManagerEnabled() {
        return screenshotManager != null && screenshotManager.isEnabled();
    }

    /**
     * Enable or disable screenshots dynamically
     * Can be called from step definitions if needed
     */
    public void setScreenshotsEnabled(boolean enabled) {
        if (screenshotManager != null) {
            screenshotManager.setEnabled(enabled);
        }
    }
}