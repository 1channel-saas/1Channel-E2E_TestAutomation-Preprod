package com.test.channelplay.mobile.config_Helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to track template usage and update enhanced metadata
 *
 * Integrates with existing AI system to record performance metrics
 */
public class TemplateUsageTracker {

    private static final Logger log = LoggerFactory.getLogger(TemplateUsageTracker.class);
    private static boolean enabled = true;

    /**
     * Enable/disable usage tracking
     */
    public static void setEnabled(boolean enable) {
        enabled = enable;
        log.info("Template usage tracking {}", enable ? "ENABLED" : "DISABLED");
    }

    /**
     * Record successful OpenCV match
     *
     * @param templatePath Path to template that matched
     * @param confidence Match confidence (0.0 to 1.0)
     * @param matchTime Time taken to find match (milliseconds)
     */
    public static void recordOpenCVSuccess(String templatePath, double confidence, long matchTime) {
        if (!enabled || templatePath == null) return;

        try {
            TemplateMetadata metadata = TemplateMetadata.loadOrCreate(templatePath);
            metadata.recordSuccess("opencv", confidence, matchTime);
            log.debug("Recorded OpenCV success for {} (confidence: {}, time: {}ms)",
                templatePath, confidence, matchTime);
        } catch (Exception e) {
            log.warn("Failed to record OpenCV success: {}", e.getMessage());
        }
    }

    /**
     * Record failed OpenCV match
     *
     * @param templatePath Path to template that failed
     * @param matchTime Time taken before failure (milliseconds)
     */
    public static void recordOpenCVFailure(String templatePath, long matchTime) {
        if (!enabled || templatePath == null) return;

        try {
            TemplateMetadata metadata = TemplateMetadata.loadOrCreate(templatePath);
            metadata.recordFailure("opencv", matchTime);
            log.debug("Recorded OpenCV failure for {} (time: {}ms)", templatePath, matchTime);
        } catch (Exception e) {
            log.warn("Failed to record OpenCV failure: {}", e.getMessage());
        }
    }

    /**
     * Record successful XPath find (before OpenCV)
     *
     * @param fieldName Element name
     * @param matchTime Time taken to find via XPath
     */
    public static void recordXPathSuccess(String fieldName, long matchTime) {
        if (!enabled || fieldName == null) return;

        try {
            // Find template by name (if it exists)
            String templatePath = findTemplatePathByName(fieldName);
            if (templatePath != null) {
                TemplateMetadata metadata = TemplateMetadata.loadOrCreate(templatePath);
                metadata.recordSuccess("xpath", 1.0, matchTime);
                log.debug("Recorded XPath success for {} (time: {}ms)", fieldName, matchTime);
            }
        } catch (Exception e) {
            log.warn("Failed to record XPath success: {}", e.getMessage());
        }
    }

    /**
     * Record OCR fallback usage
     *
     * @param fieldName Element name
     * @param success Whether OCR succeeded
     * @param matchTime Time taken for OCR
     */
    public static void recordOCRUsage(String fieldName, boolean success, long matchTime) {
        if (!enabled || fieldName == null) return;

        try {
            String templatePath = findTemplatePathByName(fieldName);
            if (templatePath != null) {
                TemplateMetadata metadata = TemplateMetadata.loadOrCreate(templatePath);
                if (success) {
                    metadata.recordSuccess("ocr", 0.8, matchTime);
                } else {
                    metadata.recordFailure("ocr", matchTime);
                }
                log.debug("Recorded OCR {} for {} (time: {}ms)",
                    success ? "success" : "failure", fieldName, matchTime);
            }
        } catch (Exception e) {
            log.warn("Failed to record OCR usage: {}", e.getMessage());
        }
    }

    /**
     * Add context information to template
     *
     * @param templatePath Path to template
     * @param screenName Screen/page name
     * @param fieldType Type of field (button, dropdown, etc.)
     * @param tags Additional tags
     */
    public static void addContext(String templatePath, String screenName, String fieldType, String... tags) {
        if (!enabled || templatePath == null) return;

        try {
            TemplateMetadata metadata = TemplateMetadata.loadOrCreate(templatePath);
            metadata.context.screenName = screenName;
            metadata.context.fieldType = fieldType;

            for (String tag : tags) {
                metadata.addTag(tag);
            }

            metadata.save();
            log.debug("Added context to template: {}", templatePath);
        } catch (Exception e) {
            log.warn("Failed to add context: {}", e.getMessage());
        }
    }

    /**
     * Helper: Find template path by element name
     */
    private static String findTemplatePathByName(String fieldName) {
        // Try manual_captured_images first
        String manualPath = "templates/manual_captured_images/" + fieldName + ".png";
        if (java.nio.file.Files.exists(java.nio.file.Paths.get(manualPath))) {
            return manualPath;
        }

        // Try screens folder
        String autoPath = "templates/screens/current/" + fieldName.toLowerCase() + "_auto.png";
        if (java.nio.file.Files.exists(java.nio.file.Paths.get(autoPath))) {
            return autoPath;
        }

        // Try AI_images folder
        String aiPath = "templates/AI_images/current/" + fieldName.toLowerCase() + "_ai.png";
        if (java.nio.file.Files.exists(java.nio.file.Paths.get(aiPath))) {
            return aiPath;
        }

        return null;
    }

    // ========== Convenience Methods ==========

    /**
     * Record complete interaction (convenience method)
     */
    public static void recordInteraction(String fieldName, String strategy, boolean success,
                                        double confidence, long matchTime) {
        if (strategy.equalsIgnoreCase("opencv")) {
            String templatePath = findTemplatePathByName(fieldName);
            if (success) {
                recordOpenCVSuccess(templatePath, confidence, matchTime);
            } else {
                recordOpenCVFailure(templatePath, matchTime);
            }
        } else if (strategy.equalsIgnoreCase("xpath")) {
            if (success) {
                recordXPathSuccess(fieldName, matchTime);
            }
        } else if (strategy.equalsIgnoreCase("ocr")) {
            recordOCRUsage(fieldName, success, matchTime);
        }
    }
}