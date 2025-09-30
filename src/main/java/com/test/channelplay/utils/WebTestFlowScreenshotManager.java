package com.test.channelplay.utils;

import org.openqa.selenium.WebDriver;
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
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Test flow screenshot manager for web tests
 * Captures screenshots after each step for test documentation
 * Works alongside mobile MobileTestFlowScreenshotManager without interference
 */
public class WebTestFlowScreenshotManager {

    private static final Logger log = LoggerFactory.getLogger(WebTestFlowScreenshotManager.class);
    private static final String TEST_FLOW_DIR = "screenshots/test_flow";
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static final SimpleDateFormat TIME_ONLY_FORMAT = new SimpleDateFormat("HH-mm-ss-SSS");

    // Track step counts per scenario
    private static final Map<String, AtomicInteger> scenarioStepCounters = new HashMap<>();
    private static final Map<String, String> scenarioFolders = new HashMap<>();

    private final WebDriver driver;
    private boolean enabled = true;


    public WebTestFlowScreenshotManager(WebDriver driver) {
        this.driver = driver;
        ensureTestFlowDirectoryExists();

        // Check if screenshot capture is enabled from properties
        String captureEnabled = GetProperty.value("screenshot.capture.web");
        if (captureEnabled != null) {
            this.enabled = Boolean.parseBoolean(captureEnabled);
        }
        log.info("Web test flow screenshots {}", enabled ? "ENABLED" : "DISABLED");
    }


    /**
     * Initialize a new test scenario folder
     * Uses the same folder structure as mobile tests for hybrid scenarios
     */
    public static String initializeScenario(String scenarioName) {
        try {
            // Clean scenario name for folder
            String cleanName = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_")
                                         .replaceAll("_{2,}", "_")
                                         .replaceAll("^_|_$", "");

            // Limit scenario name length
            if (cleanName.length() > 50) {
                cleanName = cleanName.substring(0, 50);
            }

            // Check if mobile already created a folder for this scenario
            String existingFolder = findExistingScenarioFolder(cleanName);

            if (existingFolder != null) {
                // Reuse the existing folder (for hybrid tests)
                scenarioFolders.put(scenarioName, existingFolder);
                scenarioStepCounters.put(scenarioName, new AtomicInteger(getExistingStepCount(existingFolder)));
                log.info("Reusing existing test flow folder for hybrid test: {}", existingFolder);
                return existingFolder;
            }

            // Create new folder if none exists
            String timestamp = TIMESTAMP_FORMAT.format(new Date());
            String folderName = timestamp + "_" + cleanName;

            // Create the scenario folder
            Path scenarioPath = Paths.get(TEST_FLOW_DIR, folderName);
            Files.createDirectories(scenarioPath);

            // Clean up old test runs
            cleanupOldTestRuns();

            // Initialize step counter for this scenario
            scenarioStepCounters.put(scenarioName, new AtomicInteger(0));
            scenarioFolders.put(scenarioName, folderName);

            log.info("Initialized web test flow folder: {}", scenarioPath);
            return folderName;

        } catch (Exception e) {
            log.error("Failed to initialize scenario folder: {}", e.getMessage());
            return null;
        }
    }


    /**
     * Find existing scenario folder (for hybrid tests)
     */
    private static String findExistingScenarioFolder(String cleanName) {
        try {
            Path testFlowDir = Paths.get(TEST_FLOW_DIR);
            if (!Files.exists(testFlowDir)) {
                return null;
            }

            // Look for folders ending with the same scenario name
            List<Path> matchingFolders = Files.list(testFlowDir)
                .filter(Files::isDirectory)
                .filter(path -> path.getFileName().toString().endsWith("_" + cleanName))
                .sorted(Comparator.reverseOrder()) // Get most recent
                .collect(Collectors.toList());

            if (!matchingFolders.isEmpty()) {
                // Check if folder was created recently (within last 5 minutes)
                Path recentFolder = matchingFolders.get(0);
                long folderAge = System.currentTimeMillis() - Files.getLastModifiedTime(recentFolder).toMillis();
                if (folderAge < 5 * 60 * 1000) { // 5 minutes
                    return recentFolder.getFileName().toString();
                }
            }
        } catch (Exception e) {
            log.debug("Error finding existing folder: {}", e.getMessage());
        }
        return null;
    }


    /**
     * Get existing step count from folder (for continuing hybrid tests)
     */
    private static int getExistingStepCount(String folderName) {
        try {
            Path folderPath = Paths.get(TEST_FLOW_DIR, folderName);
            if (Files.exists(folderPath)) {
                return (int) Files.list(folderPath)
                    .filter(path -> path.getFileName().toString().endsWith(".png"))
                    .count();
            }
        } catch (Exception e) {
            log.debug("Error counting existing steps: {}", e.getMessage());
        }
        return 0;
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

            // Create filename with web_ prefix to distinguish from mobile
            String fileName = String.format("%s_web_%s_%s.png", stepPrefix, cleanStep, timeStamp);

            // Capture and save screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path filePath = Paths.get(TEST_FLOW_DIR, folderName, fileName);
            Files.write(filePath, screenshot);

            // Update the global step counter in ScreenshotHelper
            ScreenshotHelper.updateGlobalStepCounter(folderName, stepNumber);

            log.debug("Web test flow screenshot captured: {}", fileName);

        } catch (Exception e) {
            log.debug("Failed to capture web test flow screenshot: {}", e.getMessage());
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

            // Add timestamp to custom name
            String timeStamp = TIME_ONLY_FORMAT.format(new Date());
            String fileName = String.format("web_%s_%s.png", customName, timeStamp);

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Path filePath = Paths.get(TEST_FLOW_DIR, folderName, fileName);
            Files.write(filePath, screenshot);

            log.debug("Custom web test flow screenshot captured: {}", fileName);

        } catch (Exception e) {
            log.debug("Failed to capture custom web screenshot: {}", e.getMessage());
        }
    }


    /**
     * Clean up old test runs, keeping only the most recent N folders
     */
    private static void cleanupOldTestRuns() {
        try {
            // Get max folders from properties
            String maxFoldersStr = GetProperty.value("screenshot.max.folders");
            int maxFolders = maxFoldersStr != null ? Integer.parseInt(maxFoldersStr) : 5;

            Path testFlowDir = Paths.get(TEST_FLOW_DIR);
            if (!Files.exists(testFlowDir)) {
                return;
            }

            // Find ALL folders in test_flow directory
            List<Path> allFolders = Files.list(testFlowDir)
                .filter(Files::isDirectory)
                .collect(Collectors.toList());

            // If we have more than maxFolders, delete the oldest ones
            if (allFolders.size() >= maxFolders) {
                // Sort by folder name (which includes timestamp) in ascending order (oldest first)
                allFolders.sort(Comparator.comparing(path -> path.getFileName().toString()));

                // Calculate how many folders to delete
                int foldersToDelete = allFolders.size() - maxFolders + 1; // +1 because we're about to create a new one

                // Delete the oldest folders
                for (int i = 0; i < foldersToDelete && i < allFolders.size(); i++) {
                    Path folderToDelete = allFolders.get(i);
                    deleteDirectory(folderToDelete);
                    log.info("Deleted old test run folder: {}", folderToDelete.getFileName());
                }
            }

        } catch (Exception e) {
            log.warn("Failed to cleanup old test runs: {}", e.getMessage());
            // Don't throw exception - cleanup is non-critical
        }
    }


    /**
     * Recursively delete a directory and all its contents
     */
    private static void deleteDirectory(Path directory) {
        try {
            if (Files.exists(directory)) {
                Files.walk(directory)
                    .sorted(Comparator.reverseOrder())  // Delete files before directories
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (Exception e) {
                            log.debug("Could not delete: {}", path);
                        }
                    });
            }
        } catch (Exception e) {
            log.warn("Failed to delete directory {}: {}", directory, e.getMessage());
        }
    }


    /**
     * Clean up scenario tracking
     */
    public static void cleanupScenario(String scenarioName) {
        scenarioStepCounters.remove(scenarioName);
        scenarioFolders.remove(scenarioName);
        log.debug("Cleaned up tracking for web scenario: {}", scenarioName);
    }


    /**
     * Enable/disable test flow screenshots
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        log.info("Web test flow screenshots {}", enabled ? "ENABLED" : "DISABLED");
    }


    /**
     * Check if test flow screenshots are enabled
     */
    public boolean isEnabled() {
        return enabled;
    }


    /**
     * Get current step count for a scenario
     */
    public static int getStepCount(String scenarioName) {
        AtomicInteger counter = scenarioStepCounters.get(scenarioName);
        return counter != null ? counter.get() : 0;
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

}