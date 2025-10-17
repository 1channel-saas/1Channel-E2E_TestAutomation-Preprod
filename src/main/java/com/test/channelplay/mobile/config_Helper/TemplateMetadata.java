package com.test.channelplay.mobile.config_Helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TemplateMetadata {

    private static final Logger log = LoggerFactory.getLogger(TemplateMetadata.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Core metadata
    public String templatePath;
    public String hash;
    public int width;
    public int height;
    public double aspectRatio;
    public String elementType;
    public Date capturedDate;
    public String elementName;

    // Visual fingerprint
    public List<String> dominantColors;
    public RelativePosition relativePosition;
    public int pixelDensity;
    public boolean hasText;
    public boolean hasIcon;

    // Aliases for name changes
    public Set<String> aliases = new HashSet<>();

    // Basic performance metrics (kept for backward compatibility)
    public int successCount = 0;
    public int failureCount = 0;
    public Date lastUsed;
    public double successRate = 0.0;

    // ========== ENHANCED: Advanced Tracking ==========
    public UsageStats usage = new UsageStats();
    public PerformanceMetrics performance = new PerformanceMetrics();
    public HealthStatus health = new HealthStatus();
    public ContextInfo context = new ContextInfo();
    public List<String> tags = new ArrayList<>();

    public TemplateMetadata() {
        this.capturedDate = new Date();
        this.lastUsed = new Date();
    }

    /**
     * Create metadata from image and element info
     */
    public static TemplateMetadata createFromImage(String templatePath, BufferedImage image,
                                                   String elementName, String elementType,
                                                   Rectangle elementBounds, Dimension screenSize) {
        TemplateMetadata metadata = new TemplateMetadata();

        try {
            metadata.templatePath = templatePath;
            metadata.elementName = elementName;
            metadata.elementType = elementType;

            // Basic dimensions
            metadata.width = image.getWidth();
            metadata.height = image.getHeight();
            metadata.aspectRatio = (double) metadata.width / metadata.height;

            // Generate hash
            metadata.hash = generateImageHash(image);

            // Extract colors
            metadata.dominantColors = extractDominantColors(image);

            // Calculate relative position on screen
            if (elementBounds != null && screenSize != null) {
                metadata.relativePosition = new RelativePosition(
                    (double) elementBounds.x / screenSize.width,
                    (double) elementBounds.y / screenSize.height,
                    (double) elementBounds.width / screenSize.width,
                    (double) elementBounds.height / screenSize.height
                );
            }

            // Analyze image characteristics
            metadata.pixelDensity = calculatePixelDensity(image);
            metadata.hasText = detectTextPresence(image);
            metadata.hasIcon = detectIconPresence(image);

            // Add common aliases
            metadata.generateAliases(elementName);

            // Save metadata file
            metadata.save();

        } catch (Exception e) {
            log.error("Failed to create template metadata: {}", e.getMessage());
        }

        return metadata;
    }

    /**
     * Load metadata from file
     */
    public static TemplateMetadata load(String templatePath) {
        try {
            String metadataPath = getMetadataPath(templatePath);
            if (Files.exists(Paths.get(metadataPath))) {
                return objectMapper.readValue(new File(metadataPath), TemplateMetadata.class);
            }
        } catch (Exception e) {
            log.debug("Could not load metadata for {}: {}", templatePath, e.getMessage());
        }
        return null;
    }

    /**
     * Save metadata to JSON file
     */
    public void save() {
        try {
            String metadataPath = getMetadataPath(templatePath);
            Path path = Paths.get(metadataPath);
            Files.createDirectories(path.getParent());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), this);
            log.debug("Saved metadata for: {}", templatePath);
        } catch (Exception e) {
            log.error("Failed to save metadata: {}", e.getMessage());
        }
    }

    /**
     * Update performance metrics
     */
    public void recordUsage(boolean success) {
        if (success) {
            successCount++;
        } else {
            failureCount++;
        }

        lastUsed = new Date();
        int total = successCount + failureCount;
        successRate = total > 0 ? (double) successCount / total : 0.0;

        save();
    }

    /**
     * Check visual similarity with another template
     */
    public double calculateSimilarity(TemplateMetadata other) {
        double score = 0.0;
        double weightSum = 0.0;

        // Aspect ratio similarity (weight: 0.3)
        if (aspectRatio > 0 && other.aspectRatio > 0) {
            double aspectSimilarity = 1.0 - Math.abs(aspectRatio - other.aspectRatio) /
                                      Math.max(aspectRatio, other.aspectRatio);
            score += aspectSimilarity * 0.3;
            weightSum += 0.3;
        }

        // Size similarity (weight: 0.2)
        int areaDiff = Math.abs((width * height) - (other.width * other.height));
        int maxArea = Math.max(width * height, other.width * other.height);
        if (maxArea > 0) {
            double sizeSimilarity = 1.0 - (double) areaDiff / maxArea;
            score += sizeSimilarity * 0.2;
            weightSum += 0.2;
        }

        // Color similarity (weight: 0.2)
        if (dominantColors != null && other.dominantColors != null) {
            int matches = 0;
            for (String color : dominantColors) {
                if (other.dominantColors.contains(color)) {
                    matches++;
                }
            }
            double colorSimilarity = (double) matches / Math.max(
                dominantColors.size(), other.dominantColors.size()
            );
            score += colorSimilarity * 0.2;
            weightSum += 0.2;
        }

        // Position similarity (weight: 0.2)
        if (relativePosition != null && other.relativePosition != null) {
            double posSimilarity = relativePosition.calculateSimilarity(other.relativePosition);
            score += posSimilarity * 0.2;
            weightSum += 0.2;
        }

        // Element type match (weight: 0.1)
        if (elementType != null && elementType.equals(other.elementType)) {
            score += 0.1;
            weightSum += 0.1;
        }

        return weightSum > 0 ? score / weightSum : 0.0;
    }

    /**
     * Check if this template matches the given criteria
     */
    public boolean matches(String searchName, String searchType, RelativePosition searchPosition) {
        // Check name or aliases
        boolean nameMatch = elementName != null && elementName.contains(searchName.toLowerCase()) ||
                           aliases.contains(searchName.toLowerCase());

        // Check type if provided
        boolean typeMatch = searchType == null || searchType.isEmpty() ||
                           (elementType != null && elementType.equals(searchType));

        // Check position if provided
        boolean positionMatch = searchPosition == null ||
                               (relativePosition != null &&
                                relativePosition.calculateSimilarity(searchPosition) > 0.8);

        return nameMatch && typeMatch && positionMatch;
    }

    private void generateAliases(String elementName) {
        if (elementName == null) return;

        String lower = elementName.toLowerCase();
        aliases.add(lower);

        // Add common variations
        if (lower.contains("mobile") || lower.contains("phone")) {
            aliases.add("mobile");
            aliases.add("phone");
            aliases.add("mobileno");
            aliases.add("phoneno");
            aliases.add("contact");
            aliases.add("number");
        }

        if (lower.contains("email") || lower.contains("mail")) {
            aliases.add("email");
            aliases.add("mail");
            aliases.add("emailid");
            aliases.add("emailaddress");
        }

        if (lower.contains("user") || lower.contains("username")) {
            aliases.add("user");
            aliases.add("username");
            aliases.add("userid");
            aliases.add("login");
        }

        if (lower.contains("pass") || lower.contains("password")) {
            aliases.add("password");
            aliases.add("pass");
            aliases.add("pwd");
            aliases.add("secret");
        }

        // Add camelCase and snake_case variations
        aliases.add(camelToSnake(elementName));
        aliases.add(snakeToCamel(elementName));
    }

    private static String camelToSnake(String camel) {
        return camel.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private static String snakeToCamel(String snake) {
        StringBuilder result = new StringBuilder();
        boolean capitalize = false;

        for (char c : snake.toCharArray()) {
            if (c == '_') {
                capitalize = true;
            } else if (capitalize) {
                result.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    private static String generateImageHash(BufferedImage image) {
        // Use same perceptual hash as VisualTemplateIndex
        try {
            BufferedImage resized = resizeImage(image, 8, 8);
            StringBuilder hashBuilder = new StringBuilder();
            int total = 0;

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    int rgb = resized.getRGB(x, y);
                    int gray = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF)) / 3;
                    total += gray;
                }
            }

            int average = total / 64;

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    int rgb = resized.getRGB(x, y);
                    int gray = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF)) / 3;
                    hashBuilder.append(gray > average ? "1" : "0");
                }
            }

            return hashBuilder.toString();

        } catch (Exception e) {
            log.error("Failed to generate image hash: {}", e.getMessage());
            return UUID.randomUUID().toString();
        }
    }

    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();
        return resized;
    }

    private static List<String> extractDominantColors(BufferedImage image) {
        Map<String, Integer> colorFrequency = new HashMap<>();

        // Sample pixels
        int step = Math.max(1, Math.min(image.getWidth(), image.getHeight()) / 10);

        for (int y = 0; y < image.getHeight(); y += step) {
            for (int x = 0; x < image.getWidth(); x += step) {
                int rgb = image.getRGB(x, y);
                String colorHex = String.format("#%06X", (rgb & 0xFFFFFF));
                colorFrequency.merge(colorHex, 1, Integer::sum);
            }
        }

        // Get top 3 colors
        return colorFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }

    private static int calculatePixelDensity(BufferedImage image) {
        int changesCount = 0;
        int lastPixel = 0;

        // Check horizontal changes
        for (int y = 0; y < image.getHeight(); y += 5) {
            for (int x = 1; x < image.getWidth(); x++) {
                int current = image.getRGB(x, y);
                int previous = image.getRGB(x - 1, y);
                if (Math.abs(current - previous) > 50) {
                    changesCount++;
                }
            }
        }

        return changesCount / (image.getWidth() * image.getHeight() / 25);
    }

    private static boolean detectTextPresence(BufferedImage image) {
        // Simple heuristic: high pixel density often indicates text
        return calculatePixelDensity(image) > 10;
    }

    private static boolean detectIconPresence(BufferedImage image) {
        // Simple heuristic: square aspect ratio and low pixel density
        double aspectRatio = (double) image.getWidth() / image.getHeight();
        return Math.abs(aspectRatio - 1.0) < 0.2 && calculatePixelDensity(image) < 5;
    }

    private static String getMetadataPath(String templatePath) {
        // Replace extension with .json
        return templatePath.replaceAll("\\.[^.]+$", ".json");
    }

    /**
     * Relative position on screen
     */
    public static class RelativePosition {
        public double x;  // 0.0 to 1.0
        public double y;  // 0.0 to 1.0
        public double width;  // 0.0 to 1.0
        public double height; // 0.0 to 1.0

        public RelativePosition() {}

        public RelativePosition(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public double calculateSimilarity(RelativePosition other) {
            double xDiff = Math.abs(x - other.x);
            double yDiff = Math.abs(y - other.y);
            double wDiff = Math.abs(width - other.width);
            double hDiff = Math.abs(height - other.height);

            double totalDiff = xDiff + yDiff + wDiff + hDiff;
            return Math.max(0, 1.0 - totalDiff / 4.0);
        }
    }

    // ========== ENHANCED: Inner Classes for Advanced Tracking ==========

    /**
     * Enhanced usage statistics
     */
    public static class UsageStats {
        public int totalAttempts = 0;
        public int successCount = 0;
        public int failureCount = 0;
        public double successRate = 0.0;
        public long lastUsed = 0; // milliseconds timestamp
        public String usageFrequency = "never"; // never, low, medium, high
        public long avgMatchTime = 0; // milliseconds

        public void recordAttempt(boolean success, long matchTime) {
            totalAttempts++;

            // Update average match time
            if (avgMatchTime == 0) {
                avgMatchTime = matchTime;
            } else {
                avgMatchTime = (avgMatchTime + matchTime) / 2;
            }

            // Calculate usage frequency
            if (totalAttempts == 0) usageFrequency = "never";
            else if (totalAttempts < 5) usageFrequency = "low";
            else if (totalAttempts < 20) usageFrequency = "medium";
            else usageFrequency = "high";
        }
    }

    /**
     * Performance metrics for different strategies
     */
    public static class PerformanceMetrics {
        public double xpathSuccessRate = 0.0;
        public double opencvSuccessRate = 0.0;
        public double ocrFallbackRate = 0.0;
        public double avgConfidence = 0.0;
        public double lastConfidence = 0.0;
        public List<Double> confidenceHistory = new ArrayList<>();

        public int xpathAttempts = 0;
        public int xpathSuccess = 0;
        public int opencvAttempts = 0;
        public int opencvSuccess = 0;
        public int ocrAttempts = 0;

        public void recordOpenCVMatch(double confidence, boolean success) {
            opencvAttempts++;
            if (success) opencvSuccess++;
            opencvSuccessRate = opencvAttempts > 0 ? (double) opencvSuccess / opencvAttempts : 0.0;

            lastConfidence = confidence;
            confidenceHistory.add(confidence);

            // Keep only last 10 confidence scores
            if (confidenceHistory.size() > 10) {
                confidenceHistory.remove(0);
            }

            // Update average confidence
            avgConfidence = confidenceHistory.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        }

        public void recordXPathAttempt(boolean success) {
            xpathAttempts++;
            if (success) xpathSuccess++;
            xpathSuccessRate = xpathAttempts > 0 ? (double) xpathSuccess / xpathAttempts : 0.0;
        }

        public void recordOCRFallback() {
            ocrAttempts++;
            ocrFallbackRate = (double) ocrAttempts / (xpathAttempts + opencvAttempts + ocrAttempts);
        }
    }

    /**
     * Context information about the template
     */
    public static class ContextInfo {
        public String elementType = ""; // Manual, Auto, AI
        public String screenName = "";
        public String fieldType = ""; // button, dropdown, textfield, etc.
        public List<String> relatedFields = new ArrayList<>();
        public List<String> testScenarios = new ArrayList<>();

        public void addRelatedField(String fieldName) {
            if (!relatedFields.contains(fieldName)) {
                relatedFields.add(fieldName);
            }
        }

        public void addTestScenario(String scenario) {
            if (!testScenarios.contains(scenario)) {
                testScenarios.add(scenario);
            }
        }
    }

    /**
     * Health status monitoring
     */
    public static class HealthStatus {
        public String status = "unknown"; // healthy, warning, critical, unknown
        public boolean needsRecapture = false;
        public double staleness = 0.0; // 0.0 (fresh) to 1.0 (very stale)
        public long lastHealthCheck = 0;
        public List<String> warnings = new ArrayList<>();

        public void checkHealth(UsageStats usage, PerformanceMetrics performance, double successRate, long lastUsedTime, long templateAge) {
            warnings.clear();
            lastHealthCheck = System.currentTimeMillis();

            // Check success rate
            if (successRate < 0.5) {
                status = "critical";
                needsRecapture = true;
                warnings.add("Success rate below 50%");
            } else if (successRate < 0.7) {
                status = "warning";
                needsRecapture = true;
                warnings.add("Success rate below 70%");
            }

            // Check confidence scores
            if (performance.avgConfidence < 0.75 && performance.opencvAttempts > 3) {
                status = "warning";
                warnings.add("Average OpenCV confidence below 0.75");
            }

            // Check staleness (30 days = full staleness)
            long daysSinceUsed = (System.currentTimeMillis() - lastUsedTime) / (1000 * 60 * 60 * 24);
            staleness = Math.min(1.0, daysSinceUsed / 30.0);

            if (staleness > 0.8) {
                warnings.add("Template not used in " + daysSinceUsed + " days");
            }

            // If no warnings, mark as healthy
            if (warnings.isEmpty()) {
                status = "healthy";
                needsRecapture = false;
            }
        }
    }

    // ========== ENHANCED: Advanced Methods ==========

    /**
     * Load or create metadata (enhanced version with auto-migration)
     */
    public static TemplateMetadata loadOrCreate(String templatePath) {
        TemplateMetadata metadata = load(templatePath);

        if (metadata != null) {
            boolean needsSave = false;

            // ========== MIGRATION 1: Fix wrong templatePath ==========
            if (!metadata.templatePath.equals(templatePath)) {
                log.warn("Fixing wrong templatePath: {} -> {}", metadata.templatePath, templatePath);
                metadata.templatePath = templatePath;
                needsSave = true;
            }

            // ========== MIGRATION 2: Convert old flat structure to new nested ==========
            if (metadata.usage.totalAttempts == 0 && (metadata.successCount > 0 || metadata.failureCount > 0)) {
                log.debug("Migrating old flat structure to new nested structure for: {}", templatePath);
                metadata.usage.successCount = metadata.successCount;
                metadata.usage.failureCount = metadata.failureCount;
                metadata.usage.successRate = metadata.successRate;
                metadata.usage.lastUsed = metadata.lastUsed != null ? metadata.lastUsed.getTime() : 0;
                metadata.usage.totalAttempts = metadata.successCount + metadata.failureCount;
                needsSave = true;
            }

            // ========== MIGRATION 3: Initialize missing enhanced fields ==========
            if (metadata.performance == null) {
                metadata.performance = new PerformanceMetrics();
                needsSave = true;
            }
            if (metadata.health == null) {
                metadata.health = new HealthStatus();
                needsSave = true;
            }
            if (metadata.context == null) {
                metadata.context = new ContextInfo();
                needsSave = true;
            }
            if (metadata.tags == null) {
                metadata.tags = new ArrayList<>();
                needsSave = true;
            }

            // ========== MIGRATION 4: Set context.elementType from old elementType field ==========
            if (metadata.context.elementType == null || metadata.context.elementType.isEmpty()) {
                if (metadata.elementType != null && !metadata.elementType.isEmpty()) {
                    metadata.context.elementType = metadata.elementType;
                    needsSave = true;
                }
            }

            // ========== MIGRATION 5: Recalculate hasText and hasIcon from actual image ==========
            try {
                Path imagePath = Paths.get(templatePath);
                if (Files.exists(imagePath)) {
                    BufferedImage image = ImageIO.read(imagePath.toFile());
                    if (image != null) {
                        boolean newHasText = detectTextPresence(image);
                        boolean newHasIcon = detectIconPresence(image);

                        // Only update if values changed
                        if (metadata.hasText != newHasText || metadata.hasIcon != newHasIcon) {
                            log.debug("Recalculating hasText/hasIcon for: {} (hasText: {} -> {}, hasIcon: {} -> {})",
                                templatePath, metadata.hasText, newHasText, metadata.hasIcon, newHasIcon);
                            metadata.hasText = newHasText;
                            metadata.hasIcon = newHasIcon;
                            needsSave = true;
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Could not recalculate hasText/hasIcon for {}: {}", templatePath, e.getMessage());
            }

            // Save if any migration occurred
            if (needsSave) {
                metadata.save();
                log.info("Auto-migrated metadata for: {}", templatePath);
            }

            return metadata;
        }

        // Create new metadata if doesn't exist
        metadata = new TemplateMetadata();
        metadata.templatePath = templatePath;
        metadata.capturedDate = new Date();
        metadata.lastUsed = new Date();

        // Initialize enhanced fields for new metadata
        metadata.usage = new UsageStats();
        metadata.performance = new PerformanceMetrics();
        metadata.health = new HealthStatus();
        metadata.context = new ContextInfo();
        metadata.tags = new ArrayList<>();

        return metadata;
    }

    /**
     * Record successful match with details
     */
    public void recordSuccess(String strategy, double confidence, long matchTime) {
        // Update basic metrics (backward compatible)
        recordUsage(true);

        // Update enhanced metrics
        usage.recordAttempt(true, matchTime);

        if ("opencv".equalsIgnoreCase(strategy)) {
            performance.recordOpenCVMatch(confidence, true);
        } else if ("xpath".equalsIgnoreCase(strategy)) {
            performance.recordXPathAttempt(true);
        } else if ("ocr".equalsIgnoreCase(strategy)) {
            performance.recordOCRFallback();
        }

        // Update health status
        long templateAge = System.currentTimeMillis() - capturedDate.getTime();
        health.checkHealth(usage, performance, successRate, lastUsed.getTime(), templateAge);

        save();
    }

    /**
     * Record failed match
     */
    public void recordFailure(String strategy, long matchTime) {
        // Update basic metrics (backward compatible)
        recordUsage(false);

        // Update enhanced metrics
        usage.recordAttempt(false, matchTime);

        if ("opencv".equalsIgnoreCase(strategy)) {
            performance.recordOpenCVMatch(0.0, false);
        } else if ("xpath".equalsIgnoreCase(strategy)) {
            performance.recordXPathAttempt(false);
        }

        // Update health status
        long templateAge = System.currentTimeMillis() - capturedDate.getTime();
        health.checkHealth(usage, performance, successRate, lastUsed.getTime(), templateAge);

        save();
    }

    /**
     * Add tag for categorization
     */
    public void addTag(String tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            save();
        }
    }

    /**
     * Get human-readable summary
     */
    public String getSummary() {
        return String.format(
            "Template: %s | Success: %.1f%% (%d/%d) | Confidence: %.2f | Status: %s | Last used: %s",
            elementName != null ? elementName : "Unknown",
            successRate * 100,
            successCount,
            successCount + failureCount,
            performance.avgConfidence,
            health.status,
            lastUsed != null ? lastUsed.toString() : "Never"
        );
    }

    /**
     * Check if template needs attention
     */
    public boolean needsAttention() {
        return health.status.equals("warning") ||
               health.status.equals("critical") ||
               health.needsRecapture;
    }
}