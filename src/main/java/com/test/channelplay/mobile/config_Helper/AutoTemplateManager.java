package com.test.channelplay.mobile.config_Helper;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AutoTemplateManager {

    private static final Logger log = LoggerFactory.getLogger(AutoTemplateManager.class);
    private final AppiumDriver driver;
    private final List<String> capturedTemplates = new ArrayList<>();
    private boolean autoTemplateEnabled;
    private final VisualTemplateIndex visualIndex;
    private final Map<String, Integer> elementVersionCount = new ConcurrentHashMap<>();
    private final int maxVersionsPerElement;
    
    public AutoTemplateManager(AppiumDriver driver) {
        this.driver = driver;
        this.visualIndex = new VisualTemplateIndex();

        // Load configuration
        this.autoTemplateEnabled = TemplateConfig.isAutoCaptureEnabled();
        this.maxVersionsPerElement = TemplateConfig.getMaxVersionsPerElement();

        ensureTemplateDirectoryExists();

        log.info("AutoTemplateManager initialized with auto-capture: {}, max versions: {}",
                 autoTemplateEnabled, maxVersionsPerElement);
    }
    
    /**
     * Enable/disable auto template capture
     */
    public void setAutoTemplateEnabled(boolean enabled) {
        this.autoTemplateEnabled = enabled;
        log.info("Auto template capture {}", enabled ? "ENABLED" : "DISABLED");
    }
    
    /**
     * Auto-capture template when element is found via XPath
     * Call this after successfully finding an element
     */
    public void autoCapture(String elementName, WebElement element) {
        if (!autoTemplateEnabled) return;

        try {
            // Manage versioning
            String sanitizedName = sanitizeFileName(elementName);
            int version = getNextVersion(sanitizedName);

            String templateName = version == 1 ?
                sanitizedName + "_auto.png" :
                sanitizedName + "_auto_v" + version + ".png";

            String templatePath = getCurrentFolder("templates/screens") + "/" + templateName;

            // Check for duplicates using visual hash
            BufferedImage elementImage = captureElementImage(element);
            if (isDuplicate(elementImage, sanitizedName)) {
                log.debug("Skipping duplicate template for: {}", elementName);
                return;
            }

            // Save the template
            File templateFile = new File(templatePath);
            templateFile.getParentFile().mkdirs();
            ImageIO.write(elementImage, "png", templateFile);

            // Add to visual index
            visualIndex.addTemplate(templatePath, elementName, elementImage);

            // Create metadata
            Rectangle bounds = element.getRect();
            org.openqa.selenium.Dimension seleniumSize = driver.manage().window().getSize();
            // Convert to java.awt.Dimension
            java.awt.Dimension screenSize = new java.awt.Dimension(seleniumSize.width, seleniumSize.height);
            TemplateMetadata metadata = TemplateMetadata.createFromImage(
                templatePath, elementImage, elementName, "XPath", bounds, screenSize
            );

            capturedTemplates.add(templatePath);
            manageElementVersions(sanitizedName, templatePath);

            log.info("Auto-captured template v{}: {} -> {}", version, elementName, templatePath);

        } catch (Exception e) {
            log.warn("Failed to auto-capture template for {}: {}", elementName, e.getMessage());
        }
    }
    
    /**
     * Auto-capture by element bounds (for AI-found elements)
     */
    public void autoCaptureByBounds(String elementName, int x, int y, int width, int height) {
        if (!autoTemplateEnabled) return;

        try {
            // Manage versioning
            String sanitizedName = sanitizeFileName(elementName);
            int version = getNextVersion(sanitizedName + "_ai");

            String templateName = version == 1 ?
                sanitizedName + "_ai.png" :
                sanitizedName + "_ai_v" + version + ".png";

            String templatePath = getCurrentFolder("templates/AI_images") + "/" + templateName;

            // Take full screenshot
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage fullImage = ImageIO.read(new ByteArrayInputStream(screenshot));

            // Crop to element bounds
            BufferedImage croppedImage = fullImage.getSubimage(x, y, width, height);

            // Check for duplicates
            if (isDuplicate(croppedImage, sanitizedName + "_ai")) {
                log.debug("Skipping duplicate AI template for: {}", elementName);
                return;
            }

            // Save template
            File templateFile = new File(templatePath);
            templateFile.getParentFile().mkdirs();
            ImageIO.write(croppedImage, "png", templateFile);

            // Add to visual index
            visualIndex.addTemplate(templatePath, elementName, croppedImage);

            // Create metadata
            Rectangle bounds = new Rectangle(x, y, height, width);
            org.openqa.selenium.Dimension seleniumSize = driver.manage().window().getSize();
            // Convert to java.awt.Dimension
            java.awt.Dimension screenSize = new java.awt.Dimension(seleniumSize.width, seleniumSize.height);
            TemplateMetadata metadata = TemplateMetadata.createFromImage(
                templatePath, croppedImage, elementName, "AI", bounds, screenSize
            );

            capturedTemplates.add(templatePath);
            manageElementVersions(sanitizedName + "_ai", templatePath);

            log.info("Auto-captured AI template v{}: {} -> {}", version, elementName, templatePath);

        } catch (Exception e) {
            log.warn("Failed to auto-capture template by bounds for {}: {}", elementName, e.getMessage());
        }
    }

    /**
     * Capture template from element
     */
    private void captureElementTemplate(WebElement element, String templatePath) throws IOException {
        // Method 1: Try element screenshot (if supported)
        try {
            byte[] elementScreenshot = element.getScreenshotAs(OutputType.BYTES);
            File templateFile = new File(templatePath);
            templateFile.getParentFile().mkdirs();
            Files.write(templateFile.toPath(), elementScreenshot);
            return;
        } catch (Exception e) {
            log.debug("Element screenshot not supported, using bounds method");
        }
        
        // Method 2: Crop from full screenshot using element bounds
        try {
            Rectangle bounds = element.getRect();
            
            byte[] fullScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage fullImage = ImageIO.read(new ByteArrayInputStream(fullScreenshot));
            
            // Ensure bounds are within image
            int x = Math.max(0, bounds.x);
            int y = Math.max(0, bounds.y);
            int width = Math.min(bounds.width, fullImage.getWidth() - x);
            int height = Math.min(bounds.height, fullImage.getHeight() - y);
            
            if (width > 0 && height > 0) {
                BufferedImage croppedImage = fullImage.getSubimage(x, y, width, height);
                
                File templateFile = new File(templatePath);
                templateFile.getParentFile().mkdirs();
                ImageIO.write(croppedImage, "png", templateFile);
            }
            
        } catch (Exception e) {
            throw new IOException("Failed to capture element template: " + e.getMessage());
        }
    }
    
    /**
     * Clean up auto-captured templates at test end
     */
    public void cleanupAutoTemplates() {
        log.info("Cleaning up {} auto-captured templates", capturedTemplates.size());
        
        for (String templatePath : capturedTemplates) {
            try {
                Files.deleteIfExists(Paths.get(templatePath));
                log.debug("Deleted auto template: {}", templatePath);
            } catch (Exception e) {
                log.warn("Failed to delete auto template {}: {}", templatePath, e.getMessage());
            }
        }
        
        capturedTemplates.clear();
        log.info("Auto template cleanup completed");
    }
    
    /**
     * Keep specific templates (don't delete these)
     */
    public void keepTemplate(String templatePath) {
        capturedTemplates.remove(templatePath);
        log.info("Template marked to keep: {}", templatePath);
    }
    
    /**
     * Get list of captured templates
     */
    public List<String> getCapturedTemplates() {
        return new ArrayList<>(capturedTemplates);
    }
    
    private void ensureTemplateDirectoryExists() {
        try {
            Files.createDirectories(Paths.get("templates"));
            Files.createDirectories(Paths.get("templates/screens/current"));     // Current XPath captures
            Files.createDirectories(Paths.get("templates/AI_images/current"));   // Current AI captures
            Files.createDirectories(Paths.get("templates/manual_captured_images")); // Manual templates (no versioning)
            Files.createDirectories(Paths.get("templates/visual_index"));        // Visual index data
        } catch (Exception e) {
            log.warn("Failed to create template directories: {}", e.getMessage());
        }
    }

    /**
     * Index existing manual templates
     */
    public void indexManualTemplates() {
        try {
            Path manualPath = Paths.get("templates/manual_captured_images");
            if (!Files.exists(manualPath)) {
                return;
            }

            Files.walk(manualPath)
                .filter(path -> path.toString().toLowerCase().endsWith(".png") ||
                               path.toString().toLowerCase().endsWith(".jpg"))
                .forEach(imagePath -> {
                    try {
                        BufferedImage image = ImageIO.read(imagePath.toFile());
                        String elementName = imagePath.getFileName().toString().replaceAll("\\.(png|jpg)$", "");

                        // Add to visual index
                        visualIndex.addTemplate(imagePath.toString(), elementName, image);

                        // Create metadata if not exists
                        String metadataPath = imagePath.toString().replace(".png", ".json").replace(".jpg", ".json");
                        if (!Files.exists(Paths.get(metadataPath))) {
                            org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
                            java.awt.Dimension awtScreenSize = new java.awt.Dimension(screenSize.width, screenSize.height);
                            TemplateMetadata metadata = TemplateMetadata.createFromImage(
                                imagePath.toString(), image, elementName, "Manual", null, awtScreenSize
                            );
                        }

                        log.info("Indexed manual template: {}", imagePath.getFileName());
                    } catch (Exception e) {
                        log.warn("Failed to index manual template {}: {}", imagePath, e.getMessage());
                    }
                });
        } catch (Exception e) {
            log.warn("Failed to index manual templates: {}", e.getMessage());
        }
    }

    /**
     * Get current active folder (manages size-based rotation)
     */
    private String getCurrentFolder(String basePath) {
        Path currentPath = Paths.get(basePath, "current");

        // Ensure current folder exists
        try {
            if (!Files.exists(currentPath)) {
                Files.createDirectories(currentPath);
            }
        } catch (IOException e) {
            log.error("Failed to create current folder: {}", e.getMessage());
        }

        return currentPath.toString();
    }

    /**
     * Check if image is duplicate using visual hash
     */
    private boolean isDuplicate(BufferedImage image, String elementName) {
        if (!TemplateConfig.isDuplicateDetectionEnabled()) {
            return false;
        }

        String existingTemplate = visualIndex.findTemplateByVisualMatch(image);
        return existingTemplate != null && existingTemplate.contains(elementName);
    }

    /**
     * Get next version number for element
     */
    private int getNextVersion(String elementName) {
        Integer count = elementVersionCount.get(elementName);
        if (count == null) {
            count = 0;
        }
        count++;
        elementVersionCount.put(elementName, count);
        return count;
    }

    /**
     * Manage versions per element (keep max 3)
     */
    private void manageElementVersions(String elementName, String newTemplatePath) {
        List<String> elementTemplates = visualIndex.findTemplatesByName(elementName);

        if (elementTemplates.size() > maxVersionsPerElement) {
            // Sort by creation time (oldest first)
            elementTemplates.sort((a, b) -> {
                try {
                    return Files.getLastModifiedTime(Paths.get(a))
                            .compareTo(Files.getLastModifiedTime(Paths.get(b)));
                } catch (IOException e) {
                    return 0;
                }
            });

            // Delete oldest versions
            int toDelete = elementTemplates.size() - maxVersionsPerElement;
            for (int i = 0; i < toDelete; i++) {
                try {
                    Files.deleteIfExists(Paths.get(elementTemplates.get(i)));
                    capturedTemplates.remove(elementTemplates.get(i));
                    log.info("Deleted old version: {}", elementTemplates.get(i));
                } catch (IOException e) {
                    log.warn("Failed to delete old version: {}", elementTemplates.get(i));
                }
            }
        }
    }

    /**
     * Capture element as BufferedImage
     */
    private BufferedImage captureElementImage(WebElement element) throws IOException {
        try {
            // Try element screenshot first
            byte[] elementScreenshot = element.getScreenshotAs(OutputType.BYTES);
            return ImageIO.read(new ByteArrayInputStream(elementScreenshot));
        } catch (Exception e) {
            // Fall back to cropping from full screenshot
            Rectangle bounds = element.getRect();
            byte[] fullScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage fullImage = ImageIO.read(new ByteArrayInputStream(fullScreenshot));

            int x = Math.max(0, bounds.x);
            int y = Math.max(0, bounds.y);
            int width = Math.min(bounds.width, fullImage.getWidth() - x);
            int height = Math.min(bounds.height, fullImage.getHeight() - y);

            return fullImage.getSubimage(x, y, width, height);
        }
    }

    /**
     * Get visual index for advanced template search
     */
    public VisualTemplateIndex getVisualIndex() {
        return visualIndex;
    }
    
    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
    }
}