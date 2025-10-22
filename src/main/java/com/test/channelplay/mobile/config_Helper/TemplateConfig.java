package com.test.channelplay.mobile.config_Helper;

import com.test.channelplay.utils.GetProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for template management
 */
public class TemplateConfig {

    private static final Logger log = LoggerFactory.getLogger(TemplateConfig.class);

    // Configuration keys
    private static final String AUTO_CAPTURE_ENABLED = "template.auto.capture.enabled";
    private static final String MAX_VERSIONS_PER_ELEMENT = "template.max.versions.per.element";
    private static final String MAX_FOLDER_SIZE_MB = "template.max.folder.size.mb";
    private static final String MAX_VERSION_FOLDERS = "template.max.version.folders";
    private static final String DUPLICATE_DETECTION_ENABLED = "template.duplicate.detection.enabled";
    private static final String VISUAL_SIMILARITY_THRESHOLD = "template.visual.similarity.threshold";
    private static final String CLEANUP_AGE_DAYS = "template.cleanup.age.days";
    private static final String DEBUG_MODE_ENABLED = "template.debug.mode.enabled";
    private static final String DEBUG_FOLDER = "template.debug.folder";
    private static final String DEBUG_ORGANIZE_BY_SCENARIO = "template.debug.organize.by.scenario";
    private static final String DEBUG_VIEWER_AUTO_GENERATE = "template.debug.viewer.auto.generate";
    private static final String DEBUG_VIEWER_OUTPUT_PATH = "template.debug.viewer.output.path";

    // Default values
    private static final boolean DEFAULT_AUTO_CAPTURE_ENABLED = true;
    private static final int DEFAULT_MAX_VERSIONS = 3;
    private static final long DEFAULT_MAX_FOLDER_SIZE_MB = 100;
    private static final int DEFAULT_MAX_VERSION_FOLDERS = 5;
    private static final boolean DEFAULT_DUPLICATE_DETECTION = true;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.85;
    private static final int DEFAULT_CLEANUP_AGE_DAYS = 30;
    private static final boolean DEFAULT_DEBUG_MODE = false;
    private static final String DEFAULT_DEBUG_FOLDER = "screenshots/debug_matches";
    private static final boolean DEFAULT_DEBUG_ORGANIZE_BY_SCENARIO = true;
    private static final boolean DEFAULT_DEBUG_VIEWER_AUTO_GENERATE = true;
    private static final String DEFAULT_DEBUG_VIEWER_OUTPUT_PATH = "screenshots/debug_matches_viewer.html";

    /**
     * Check if auto-capture is enabled
     */
    public static boolean isAutoCaptureEnabled() {
        String value = GetProperty.value(AUTO_CAPTURE_ENABLED);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return DEFAULT_AUTO_CAPTURE_ENABLED;
    }

    /**
     * Get maximum versions to keep per element
     */
    public static int getMaxVersionsPerElement() {
        String value = GetProperty.value(MAX_VERSIONS_PER_ELEMENT);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid max versions config, using default: {}", DEFAULT_MAX_VERSIONS);
            }
        }
        return DEFAULT_MAX_VERSIONS;
    }

    /**
     * Get maximum folder size in MB
     */
    public static long getMaxFolderSizeMB() {
        String value = GetProperty.value(MAX_FOLDER_SIZE_MB);
        if (value != null && !value.isEmpty()) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid max folder size config, using default: {}", DEFAULT_MAX_FOLDER_SIZE_MB);
            }
        }
        return DEFAULT_MAX_FOLDER_SIZE_MB;
    }

    /**
     * Get maximum number of version folders
     */
    public static int getMaxVersionFolders() {
        String value = GetProperty.value(MAX_VERSION_FOLDERS);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid max version folders config, using default: {}", DEFAULT_MAX_VERSION_FOLDERS);
            }
        }
        return DEFAULT_MAX_VERSION_FOLDERS;
    }

    /**
     * Check if duplicate detection is enabled
     */
    public static boolean isDuplicateDetectionEnabled() {
        String value = GetProperty.value(DUPLICATE_DETECTION_ENABLED);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return DEFAULT_DUPLICATE_DETECTION;
    }

    /**
     * Get visual similarity threshold
     */
    public static double getVisualSimilarityThreshold() {
        String value = GetProperty.value(VISUAL_SIMILARITY_THRESHOLD);
        if (value != null && !value.isEmpty()) {
            try {
                double threshold = Double.parseDouble(value);
                if (threshold >= 0.0 && threshold <= 1.0) {
                    return threshold;
                } else {
                    log.warn("Similarity threshold must be between 0.0 and 1.0, using default: {}",
                             DEFAULT_SIMILARITY_THRESHOLD);
                }
            } catch (NumberFormatException e) {
                log.warn("Invalid similarity threshold config, using default: {}",
                         DEFAULT_SIMILARITY_THRESHOLD);
            }
        }
        return DEFAULT_SIMILARITY_THRESHOLD;
    }

    /**
     * Get cleanup age in days
     */
    public static int getCleanupAgeDays() {
        String value = GetProperty.value(CLEANUP_AGE_DAYS);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.warn("Invalid cleanup age config, using default: {}", DEFAULT_CLEANUP_AGE_DAYS);
            }
        }
        return DEFAULT_CLEANUP_AGE_DAYS;
    }

    /**
     * Check if debug mode is enabled
     */
    public static boolean isDebugModeEnabled() {
        String value = GetProperty.value(DEBUG_MODE_ENABLED);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return DEFAULT_DEBUG_MODE;
    }

    /**
     * Get debug folder path
     */
    public static String getDebugFolder() {
        String value = GetProperty.value(DEBUG_FOLDER);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return DEFAULT_DEBUG_FOLDER;
    }

    /**
     * Check if debug images should be organized by scenario
     */
    public static boolean isDebugOrganizeByScenario() {
        String value = GetProperty.value(DEBUG_ORGANIZE_BY_SCENARIO);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return DEFAULT_DEBUG_ORGANIZE_BY_SCENARIO;
    }

    /**
     * Check if debug viewer should be auto-generated
     */
    public static boolean isDebugViewerAutoGenerate() {
        String value = GetProperty.value(DEBUG_VIEWER_AUTO_GENERATE);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return DEFAULT_DEBUG_VIEWER_AUTO_GENERATE;
    }

    /**
     * Get debug viewer output path
     */
    public static String getDebugViewerOutputPath() {
        String value = GetProperty.value(DEBUG_VIEWER_OUTPUT_PATH);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return DEFAULT_DEBUG_VIEWER_OUTPUT_PATH;
    }

    /**
     * Log current configuration
     */
    public static void logConfiguration() {
        log.info("Template Management Configuration:");
        log.info("  Auto-capture enabled: {}", isAutoCaptureEnabled());
        log.info("  Max versions per element: {}", getMaxVersionsPerElement());
        log.info("  Max folder size (MB): {}", getMaxFolderSizeMB());
        log.info("  Max version folders: {}", getMaxVersionFolders());
        log.info("  Duplicate detection enabled: {}", isDuplicateDetectionEnabled());
        log.info("  Visual similarity threshold: {}", getVisualSimilarityThreshold());
        log.info("  Cleanup age (days): {}", getCleanupAgeDays());
        log.info("  Debug mode enabled: {}", isDebugModeEnabled());
        log.info("  Debug folder: {}", getDebugFolder());
        log.info("  Debug organize by scenario: {}", isDebugOrganizeByScenario());
        log.info("  Debug viewer auto-generate: {}", isDebugViewerAutoGenerate());
        log.info("  Debug viewer output path: {}", getDebugViewerOutputPath());
    }
}