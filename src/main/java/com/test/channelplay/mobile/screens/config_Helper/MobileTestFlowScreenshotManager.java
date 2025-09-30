package com.test.channelplay.mobile.screens.config_Helper;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MobileTestFlowScreenshotManager {

    private static final Logger log = LoggerFactory.getLogger(MobileTestFlowScreenshotManager.class);
    private static final String TEST_FLOW_DIR = "screenshots/test_flow";
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final SimpleDateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("HH-mm-ss-SSS");

    // Track step counts per scenario
    private static final Map<String, AtomicInteger> scenarioStepCounters = new HashMap<>();
    private static final Map<String, String> scenarioFolders = new HashMap<>();

    private final AppiumDriver driver;
    private boolean enabled = true;

    public MobileTestFlowScreenshotManager(AppiumDriver driver) {
        this.driver = driver;
        ensureTestFlowDirectoryExists();
    }

    /**
     * Initialize a new test scenario folder
     */
    public static String initializeScenario(String scenarioName) {
        try {
            // Clean scenario name for folder
            String cleanName = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_")
                                         .replaceAll("_{2,}", "_")
                                         .replaceAll("^_|_$", "");

            // Create timestamp and folder name
            String timestamp = TIMESTAMP_FORMAT.format(new Date());
            String folderName = timestamp + "_" + cleanName;

            // Create the scenario folder
            Path scenarioPath = Paths.get(TEST_FLOW_DIR, folderName);
            Files.createDirectories(scenarioPath);

            // Initialize step counter for this scenario
            scenarioStepCounters.put(scenarioName, new AtomicInteger(0));
            scenarioFolders.put(scenarioName, folderName);

            log.info("Initialized test flow folder: {}", scenarioPath);
            return folderName;

        } catch (Exception e) {
            log.error("Failed to initialize scenario folder: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture screenshot for current step
     */
    public void captureStep(String scenarioName, String stepDescription) {
        if (!enabled || driver == null) {
            return;
        }

        try {
            // Get or create scenario folder
            String folderName = scenarioFolders.get(scenarioName);
            if (folderName == null) {
                folderName = initializeScenario(scenarioName);
            }

            // Get step counter
            AtomicInteger counter = scenarioStepCounters.computeIfAbsent(
                scenarioName, k -> new AtomicInteger(0)
            );

            // Increment and format step number
            int stepNumber = counter.incrementAndGet();
            String stepPrefix = String.format("step_%03d", stepNumber);

            // Clean step description
            String cleanStep = stepDescription.replaceAll("[^a-zA-Z0-9_-]", "_")
                                            .replaceAll("_{2,}", "_")
                                            .replaceAll("^_|_$", "")
                                            .toLowerCase();

            // Limit description length
            if (cleanStep.length() > 50) {
                cleanStep = cleanStep.substring(0, 50);
            }

            // Add timestamp for uniqueness
            String timeStamp = TIME_ONLY_FORMAT.format(new Date());

            // Create filename with app_ prefix to distinguish from web screenshots
            String fileName = String.format("%s_app_%s_%s.png", stepPrefix, cleanStep, timeStamp);

            // Capture and save screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path filePath = Paths.get(TEST_FLOW_DIR, folderName, fileName);
            Files.write(filePath, screenshot);

            // Update the global step counter in ScreenshotHelper
            com.test.channelplay.utils.ScreenshotHelper.updateGlobalStepCounter(folderName, stepNumber);

            log.debug("Test flow screenshot captured: {}", fileName);

        } catch (Exception e) {
            log.debug("Failed to capture test flow screenshot: {}", e.getMessage());
            // Don't throw exception - this is non-critical functionality
        }
    }

    /**
     * Capture screenshot with custom name
     */
    public void captureCustomStep(String scenarioName, String customName) {
        if (!enabled || driver == null) {
            return;
        }

        try {
            String folderName = scenarioFolders.get(scenarioName);
            if (folderName == null) {
                folderName = initializeScenario(scenarioName);
            }

            // Add timestamp to custom name with app_ prefix
            String timeStamp = TIME_ONLY_FORMAT.format(new Date());
            String fileName = String.format("app_%s_%s.png", customName, timeStamp);

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path filePath = Paths.get(TEST_FLOW_DIR, folderName, fileName);
            Files.write(filePath, screenshot);

            log.debug("Custom test flow screenshot captured: {}", fileName);

        } catch (Exception e) {
            log.debug("Failed to capture custom screenshot: {}", e.getMessage());
        }
    }

    /**
     * Clean up scenario tracking
     */
    public static void cleanupScenario(String scenarioName) {
        scenarioStepCounters.remove(scenarioName);
        scenarioFolders.remove(scenarioName);
        log.debug("Cleaned up tracking for scenario: {}", scenarioName);
    }

    /**
     * Enable/disable test flow screenshots
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        log.info("Test flow screenshots {}", enabled ? "ENABLED" : "DISABLED");
    }

    /**
     * Check if test flow screenshots are enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Ensure test flow directory exists
     */
    private void ensureTestFlowDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(TEST_FLOW_DIR));
        } catch (Exception e) {
            log.warn("Failed to create test flow directory: {}", e.getMessage());
        }
    }

    /**
     * Get current step count for a scenario
     */
    public static int getStepCount(String scenarioName) {
        AtomicInteger counter = scenarioStepCounters.get(scenarioName);
        return counter != null ? counter.get() : 0;
    }

    /**
     * Clear all test flow screenshots (for manual cleanup)
     */
    public static void clearAllTestFlowScreenshots() {
        try {
            Path testFlowPath = Paths.get(TEST_FLOW_DIR);
            if (Files.exists(testFlowPath)) {
                Files.walk(testFlowPath)
                    .sorted((a, b) -> b.compareTo(a))  // Delete files before directories
                    .forEach(path -> {
                        try {
                            if (!path.equals(testFlowPath)) {
                                Files.deleteIfExists(path);
                            }
                        } catch (Exception e) {
                            log.debug("Could not delete: {}", path);
                        }
                    });
                log.info("Cleared all test flow screenshots");
            }
        } catch (Exception e) {
            log.error("Failed to clear test flow screenshots: {}", e.getMessage());
        }
    }
}