package com.test.channelplay.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Standalone utility class for capturing manual screenshots
 * Saves to disk for HTML report viewing (not attached to Allure)
 */
public class ScreenshotHelper {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotHelper.class);
    private static int screenshotCounter = 0;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH-mm-ss-SSS");
    private static final SimpleDateFormat FOLDER_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final String MANUAL_SCREENSHOTS_DIR = "screenshots/test_flow";

    // Track current scenario folder
    private static String currentScenarioFolder = null;
    private static Map<String, Integer> scenarioCounters = new HashMap<>();

    // SHARED GLOBAL STEP COUNTER - Used by all screenshot managers
    // Key: folder name, Value: current step count
    private static final Map<String, Integer> globalStepCounters = new HashMap<>();

    // Private constructor to prevent instantiation
    private ScreenshotHelper() {}

    /**
     * Set the current scenario folder (called from hooks or test setup)
     */
    public static void setScenarioFolder(String folderName) {
        currentScenarioFolder = folderName;
        if (!scenarioCounters.containsKey(folderName)) {
            scenarioCounters.put(folderName, 0);
        }
    }

    /**
     * Capture screenshot and save to disk (for HTML report)
     * @param driver - WebDriver instance
     * @param description - Description of the screenshot
     */
    public static void captureScreenshot(WebDriver driver, String description) {
        if (driver != null) {
            try {
                // Ensure we have a folder to save to
                if (currentScenarioFolder == null) {
                    initializeDefaultFolder();
                }

                // Get current step count from the GLOBAL counter
                // This ensures manual screenshots are numbered after automatic ones
                int globalStepCount = getGlobalStepCount(currentScenarioFolder);

                // Get our local counter for this scenario
                Integer stepNum = scenarioCounters.get(currentScenarioFolder);

                // Use the higher of global count or local count
                if (stepNum == null || stepNum < globalStepCount) {
                    stepNum = globalStepCount;
                }

                // Increment for the new screenshot
                stepNum++;

                // Update both local and global counters
                scenarioCounters.put(currentScenarioFolder, stepNum);
                updateGlobalStepCounter(currentScenarioFolder, stepNum);

                // Clean description for filename
                String cleanDesc = description.replaceAll("[^a-zA-Z0-9_-]", "_")
                                           .replaceAll("_{2,}", "_")
                                           .replaceAll("^_|_$", "");
                if (cleanDesc.length() > 50) {
                    cleanDesc = cleanDesc.substring(0, 50);
                }

                // Create filename with manual_ prefix to distinguish from auto captures
                String timestamp = TIME_FORMAT.format(new Date());
                String fileName = String.format("step_%03d_manual_%s_%s.png",
                    stepNum, cleanDesc, timestamp);

                // Capture and save screenshot
                File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Path targetPath = Paths.get(MANUAL_SCREENSHOTS_DIR, currentScenarioFolder, fileName);

                // Ensure directory exists
                Files.createDirectories(targetPath.getParent());

                // Copy screenshot to target location
                Files.copy(screenshot.toPath(), targetPath);

                log.info("Manual screenshot saved: {}", fileName);

            } catch (Exception e) {
                log.error("Failed to capture screenshot: {}", e.getMessage());
            }
        } else {
            log.warn("Driver is null, cannot capture screenshot");
        }
    }

    /**
     * Initialize a default folder if none is set
     */
    private static void initializeDefaultFolder() {
        String timestamp = FOLDER_FORMAT.format(new Date());
        currentScenarioFolder = timestamp + "_Manual_Screenshots";
        scenarioCounters.put(currentScenarioFolder, 0);

        // Create the folder
        try {
            Path folderPath = Paths.get(MANUAL_SCREENSHOTS_DIR, currentScenarioFolder);
            Files.createDirectories(folderPath);
            log.debug("Created default folder for manual screenshots: {}", currentScenarioFolder);
        } catch (IOException e) {
            log.error("Failed to create default folder: {}", e.getMessage());
        }
    }

    /**
     * Capture screenshot using ThreadLocal driver from DriverBase
     * @param description - Description of the screenshot
     */
    public static void captureScreenshot(String description) {
        WebDriver driver = DriverBase.tdriver.get();
        captureScreenshot(driver, description);
    }

    /**
     * Reset counter for new test
     */
    public static void resetCounter() {
        screenshotCounter = 0;
        log.debug("Screenshot counter reset");
    }

    /**
     * Clean up for next scenario
     */
    public static void cleanup() {
        currentScenarioFolder = null;
        scenarioCounters.clear();
    }

    /**
     * Update global step counter - called by test flow managers after each screenshot
     * @param folderName - The scenario folder name
     * @param stepNumber - The current step number
     */
    public static synchronized void updateGlobalStepCounter(String folderName, int stepNumber) {
        Integer currentMax = globalStepCounters.get(folderName);
        if (currentMax == null || stepNumber > currentMax) {
            globalStepCounters.put(folderName, stepNumber);
            log.debug("Updated global step counter for {}: {}", folderName, stepNumber);
        }
    }

    /**
     * Get the current global step count for a folder
     * @param folderName - The scenario folder name
     * @return The current step count, or 0 if not found
     */
    public static synchronized int getGlobalStepCount(String folderName) {
        return globalStepCounters.getOrDefault(folderName, 0);
    }

    /**
     * Clear global step counter for a folder
     * @param folderName - The scenario folder name
     */
    public static synchronized void clearGlobalStepCounter(String folderName) {
        globalStepCounters.remove(folderName);
        log.debug("Cleared global step counter for {}", folderName);
    }

}