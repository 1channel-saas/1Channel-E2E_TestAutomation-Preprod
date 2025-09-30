package com.test.channelplay.mobile.screens.config_Helper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Dimension;
import org.openqa.selenium.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to generate metadata for manually captured screenshots
 * Ensures consistency with auto-captured templates
 */
public class ManualTemplateHelper {

    private static final Logger log = LoggerFactory.getLogger(ManualTemplateHelper.class);
    private static final String MANUAL_TEMPLATE_DIR = "templates/manual_captured_images";

    // Emulator specifications
    private static final int EMULATOR_WIDTH = 1080;
    private static final int EMULATOR_HEIGHT = 2400;
    private static final int EMULATOR_DPI = 420;

    /**
     * Process a manually captured screenshot and generate metadata
     * @param imagePath Path to the screenshot image
     * @param elementName Name of the UI element
     * @param x X coordinate of element (optional, -1 if unknown)
     * @param y Y coordinate of element (optional, -1 if unknown)
     */
    public static void processManualTemplate(String imagePath, String elementName, int x, int y) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                log.error("Image file not found: {}", imagePath);
                return;
            }

            // Read the image
            BufferedImage image = ImageIO.read(imageFile);

            // Create proper file name and path
            String sanitizedName = sanitizeFileName(elementName);
            String targetImagePath = MANUAL_TEMPLATE_DIR + "/" + sanitizedName + ".png";
            String targetMetadataPath = MANUAL_TEMPLATE_DIR + "/" + sanitizedName + ".json";

            // Ensure directory exists
            Files.createDirectories(Paths.get(MANUAL_TEMPLATE_DIR));

            // Copy image to templates directory if not already there
            if (!imagePath.equals(targetImagePath)) {
                File targetFile = new File(targetImagePath);
                ImageIO.write(image, "png", targetFile);
                log.info("Copied image to: {}", targetImagePath);
            }

            // Calculate relative position if coordinates provided
            Rectangle bounds = null;
            if (x >= 0 && y >= 0) {
                bounds = new Rectangle(x, y, image.getHeight(), image.getWidth());
            }

            // Create metadata with emulator screen dimensions
            Dimension screenSize = new Dimension(EMULATOR_WIDTH, EMULATOR_HEIGHT);
            TemplateMetadata metadata = TemplateMetadata.createFromImage(
                targetImagePath,
                image,
                elementName,
                "Manual",
                bounds,
                screenSize
            );

            log.info("Successfully created metadata for manual template: {}", elementName);
            log.info("Image dimensions: {}x{}", image.getWidth(), image.getHeight());
            log.info("Metadata saved to: {}", targetMetadataPath);

        } catch (IOException e) {
            log.error("Failed to process manual template: {}", e.getMessage());
        }
    }

    /**
     * Process all PNG images in a directory
     */
    public static void processAllManualTemplates() {
        try {
            Path manualPath = Paths.get(MANUAL_TEMPLATE_DIR);
            if (!Files.exists(manualPath)) {
                Files.createDirectories(manualPath);
                log.info("Created manual templates directory");
                return;
            }

            Files.walk(manualPath, 1)
                .filter(path -> path.toString().toLowerCase().endsWith(".png"))
                .forEach(imagePath -> {
                    String fileName = imagePath.getFileName().toString();
                    String elementName = fileName.replace(".png", "");

                    // Check if metadata already exists
                    String metadataPath = imagePath.toString().replace(".png", ".json");
                    if (!Files.exists(Paths.get(metadataPath))) {
                        log.info("Processing: {}", fileName);
                        processManualTemplate(imagePath.toString(), elementName, -1, -1);
                    } else {
                        log.debug("Metadata already exists for: {}", fileName);
                    }
                });

            log.info("Finished processing manual templates");

        } catch (IOException e) {
            log.error("Failed to process manual templates: {}", e.getMessage());
        }
    }

    /**
     * Validate that manual template matches auto-capture standards
     */
    public static boolean validateTemplate(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            // Check image is not full screen
            if (image.getWidth() == EMULATOR_WIDTH && image.getHeight() == EMULATOR_HEIGHT) {
                log.warn("Image appears to be full screen. Should be cropped to element only.");
                return false;
            }

            // Check reasonable dimensions (not too small)
            if (image.getWidth() < 20 || image.getHeight() < 20) {
                log.warn("Image dimensions too small: {}x{}", image.getWidth(), image.getHeight());
                return false;
            }

            // Check aspect ratio is reasonable
            double aspectRatio = (double) image.getWidth() / image.getHeight();
            if (aspectRatio > 10 || aspectRatio < 0.1) {
                log.warn("Unusual aspect ratio: {}", aspectRatio);
                return false;
            }

            log.info("Template validation passed");
            return true;

        } catch (IOException e) {
            log.error("Failed to validate template: {}", e.getMessage());
            return false;
        }
    }

    private static String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
    }

    /**
     * Main method for command-line usage
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  Process single image: java ManualTemplateHelper <imagePath> <elementName> [x] [y]");
            System.out.println("  Process all images: java ManualTemplateHelper --all");
            System.out.println("  Validate image: java ManualTemplateHelper --validate <imagePath>");
            return;
        }

        if ("--all".equals(args[0])) {
            processAllManualTemplates();
        } else if ("--validate".equals(args[0]) && args.length > 1) {
            validateTemplate(args[1]);
        } else {
            String imagePath = args[0];
            String elementName = args.length > 1 ? args[1] : "unnamed_element";
            int x = args.length > 2 ? Integer.parseInt(args[2]) : -1;
            int y = args.length > 3 ? Integer.parseInt(args[3]) : -1;
            processManualTemplate(imagePath, elementName, x, y);
        }
    }
}