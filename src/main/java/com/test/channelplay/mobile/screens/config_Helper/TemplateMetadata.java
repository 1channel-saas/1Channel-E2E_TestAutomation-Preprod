package com.test.channelplay.mobile.screens.config_Helper;

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

    // Performance metrics
    public int successCount = 0;
    public int failureCount = 0;
    public Date lastUsed;
    public double successRate = 0.0;

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
}