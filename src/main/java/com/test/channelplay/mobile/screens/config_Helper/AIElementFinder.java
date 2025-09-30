package com.test.channelplay.mobile.screens.config_Helper;

import io.appium.java_client.AppiumDriver;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class AIElementFinder {

    private static final Logger log = LoggerFactory.getLogger(AIElementFinder.class);
    private final AppiumDriver driver;
    private final Tesseract tesseract;

    static {
        // Load OpenCV native library
        nu.pattern.OpenCV.loadShared();
    }

    public AIElementFinder(AppiumDriver driver) {
        this.driver = driver;
        this.tesseract = new Tesseract();

        // Configure Tesseract (adjust path as needed)
        try {
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");
        } catch (Exception e) {
            log.warn("Tesseract not configured properly. OCR features will not work: " + e.getMessage());
        }
    }
    
    /**
     * Find element by matching a template image
     * @param templateImagePath Path to template image file
     * @param threshold Matching threshold (0.0 to 1.0, default 0.8)
     * @return Point location of matched element
     */
    public Point findElementByImage(String templateImagePath, double threshold) {
        try {
            // Take screenshot
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));

            // Convert to OpenCV Mat
            Mat screenMat = bufferedImageToMat(screenshot);
            Mat templateMat = Imgcodecs.imread(templateImagePath);
            
            if (templateMat.empty()) {
                throw new RuntimeException("Template image not found: " + templateImagePath);
            }
            
            // Template matching
            Mat result = new Mat();
            Imgproc.matchTemplate(screenMat, templateMat, result, Imgproc.TM_CCOEFF_NORMED);
            
            // Find best match
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            
            if (mmr.maxVal >= threshold) {
                Point matchLocation = mmr.maxLoc;
                log.info("Image match found at: ({}, {}) with confidence: {}", 
                        matchLocation.x, matchLocation.y, mmr.maxVal);
                
                // Return center of matched area
                return new Point(
                    matchLocation.x + templateMat.cols() / 2,
                    matchLocation.y + templateMat.rows() / 2
                );
            } else {
                log.warn("Image match not found. Best confidence: {}", mmr.maxVal);
                return null;
            }
            
        } catch (Exception e) {
            log.error("Error in image matching: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find element by image with default threshold
     */
    public Point findElementByImage(String templateImagePath) {
        return findElementByImage(templateImagePath, 0.8);
    }

    /**
     * Click on element found by image matching
     * @param templateImagePath Path to template image
     * @return true if click was successful
     */
    public boolean clickByImage(String templateImagePath) {
        return clickByImage(templateImagePath, 0.8);
    }
    
    /**
     * Click on element found by image matching with custom threshold
     */
    public boolean clickByImage(String templateImagePath, double threshold) {
        Point location = findElementByImage(templateImagePath, threshold);
        if (location != null) {
            clickAtCoordinates((int) location.x, (int) location.y);
            return true;
        }
        return false;
    }
    
    /**
     * Find text on screen using OCR
     * @param searchText Text to find
     * @return Point location of found text (center)
     */
    public Point findTextByOCR(String searchText) {
        try {
            // Take screenshot
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));

            // Strategy 1: Two-pass OCR - Look for label and hint/value text
            Point coordinates = tryTwoPassOCR(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via Two-pass OCR strategy at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }

            // Strategy 2: Color-based detection - Find input area by background color
            coordinates = tryColorBasedDetection(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via Color-based strategy at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }

            // Strategy 3: Smart offset with text bounds
            coordinates = trySmartOffset(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via Smart offset strategy at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }

            // Fallback: Original simple OCR
            String ocrResult = tesseract.doOCR(screenshot);
            if (ocrResult.toLowerCase().contains(searchText.toLowerCase())) {
                log.info("FALLBACK: Text '{}' found via simple OCR - returning center", searchText);
                Dimension screenSize = driver.manage().window().getSize();
                return new Point(screenSize.getWidth() / 2, screenSize.getHeight() / 2);
            }

            log.warn("Text '{}' not found via any OCR strategy", searchText);
            return null;

        } catch (Exception e) {
            log.error("OCR error: " + e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Click on text found via OCR
     */
    public boolean clickByOCR(String searchText) {
        Point location = findTextByOCR(searchText);
        if (location != null) {
            clickAtCoordinates((int) location.x, (int) location.y);
            return true;
        }
        return false;
    }
    
    /**
     * Smart element finder - tries multiple AI strategies
     * @param templateImage Path to template image (can be null)
     * @param searchText Text to search via OCR (can be null)
     * @return true if element found and clicked
     */
    public boolean smartClick(String templateImage, String searchText) {
        log.info("Starting smart element finding...");
        
        // Strategy 1: Image matching
        if (templateImage != null && !templateImage.isEmpty()) {
            log.debug("Trying image matching strategy...");
            if (clickByImage(templateImage)) {
                log.info("Success with image matching");
                return true;
            }
        }
        
        // Strategy 2: OCR text finding
        if (searchText != null && !searchText.isEmpty()) {
            log.debug("Trying OCR text finding strategy...");
            if (clickByOCR(searchText)) {
                log.info("Success with OCR text finding");
                return true;
            }
        }
        
        log.warn("All AI strategies failed");
        return false;
    }
    
    /**
     * Click at specific coordinates
     */
    private void clickAtCoordinates(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tapSequence = new Sequence(finger, 1);
        
        tapSequence.addAction(finger.createPointerMove(
            Duration.ZERO, 
            PointerInput.Origin.viewport(), 
            x, y));
        tapSequence.addAction(finger.createPointerDown(
            PointerInput.MouseButton.LEFT.asArg()));
        tapSequence.addAction(finger.createPointerUp(
            PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(tapSequence));
        log.info("Clicked at coordinates: ({}, {})", x, y);
    }
    
    /**
     * Convert BufferedImage to OpenCV Mat
     */
    private Mat bufferedImageToMat(BufferedImage bufferedImage) {
        byte[] pixels = new byte[bufferedImage.getWidth() * bufferedImage.getHeight() * 3];
        int index = 0;
        
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                pixels[index++] = (byte) ((rgb >> 16) & 0xFF); // Blue
                pixels[index++] = (byte) ((rgb >> 8) & 0xFF);  // Green
                pixels[index++] = (byte) (rgb & 0xFF);         // Red
            }
        }
        
        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, pixels);
        return mat;
    }
    
    /**
     * Take and save screenshot for debugging
     */
    public void saveScreenshot(String filename) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            File file = new File("screenshots/" + filename);
            file.getParentFile().mkdirs();
            java.nio.file.Files.write(file.toPath(), screenshot);
            log.info("Screenshot saved: {}", file.getAbsolutePath());
        } catch (Exception e) {
            log.error("Failed to save screenshot: " + e.getMessage(), e);
        }
    }
    
    /**
     * Strategy 1: Two-pass OCR - Find label text, then look for hint/value text below
     */
    private Point tryTwoPassOCR(BufferedImage screenshot, String searchText) {
        try {
            List<Word> words = tesseract.getWords(screenshot, 1);
            Word labelWord = null;

            // First pass: Find the label text
            for (Word word : words) {
                if (word.getText().toLowerCase().contains(searchText.toLowerCase())) {
                    labelWord = word;
                    break;
                }
            }

            if (labelWord == null) {
                log.debug("Two-pass OCR: Label '{}' not found", searchText);
                return null;
            }

            // Second pass: Look for hint text or values below the label
            int labelBottom = labelWord.getBoundingBox().y + labelWord.getBoundingBox().height;
            int labelCenterX = labelWord.getBoundingBox().x + labelWord.getBoundingBox().width / 2;

            // Also try to detect field bounds for height-based clicking
            int fieldTop = labelWord.getBoundingBox().y;
            int fieldBottom = fieldTop;

            for (Word word : words) {
                Rectangle wordBounds = word.getBoundingBox();

                // Look for words below the label (within reasonable distance)
                if (wordBounds.y > labelBottom &&
                    wordBounds.y < labelBottom + 550 && // Increased to capture tall fields
                    Math.abs(wordBounds.x - labelCenterX) < 300) { // Horizontally aligned

                    // Track the bottom-most related element
                    fieldBottom = Math.max(fieldBottom, wordBounds.y + wordBounds.height);

                    String wordText = word.getText().trim();
                    // Look for hints like "Enter", numbers, or placeholder text
                    if (wordText.toLowerCase().matches(".*\\d.*") || // Contains numbers
                        wordText.toLowerCase().contains("enter") ||
                        wordText.toLowerCase().contains("placeholder") ||
                        wordText.length() < 10) { // Short text likely to be hint

                        // Calculate field height for smart positioning
                        int estimatedFieldHeight = fieldBottom - fieldTop;
                        int targetX = wordBounds.x + wordBounds.width / 2;
                        int targetY;

                        if (estimatedFieldHeight > 400) {
                            // Multi-line field - click at 40% from top of hint area
                            targetY = wordBounds.y + (int)(wordBounds.height * 0.4);
                            log.debug("Two-pass OCR: Multi-line field detected (height: {}), clicking lower", estimatedFieldHeight);
                        } else {
                            // Single-line field - click at center of hint
                            targetY = wordBounds.y + wordBounds.height / 2;
                            log.debug("Two-pass OCR: Single-line field detected (height: {})", estimatedFieldHeight);
                        }

                        log.debug("Two-pass OCR: Found hint text '{}' below '{}' at ({}, {})", wordText, searchText, targetX, targetY);
                        return new Point(targetX, targetY);
                    }
                }
            }

            log.debug("Two-pass OCR: No hint text found below '{}'", searchText);
            return null;

        } catch (Exception e) {
            log.debug("Two-pass OCR failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Strategy 2: Color-based detection - Find input area by background color changes
     */
    private Point tryColorBasedDetection(BufferedImage screenshot, String searchText) {
        try {
            List<Word> words = tesseract.getWords(screenshot, 1);
            Word labelWord = null;

            // Find the label text first
            for (Word word : words) {
                if (word.getText().toLowerCase().contains(searchText.toLowerCase())) {
                    labelWord = word;
                    break;
                }
            }

            if (labelWord == null) {
                log.debug("Color-based: Label '{}' not found", searchText);
                return null;
            }

            Rectangle labelBounds = labelWord.getBoundingBox();
            int startY = labelBounds.y + labelBounds.height + 5; // Start just below label
            int searchHeight = 550; // Increased to capture tall fields
            int searchWidth = Math.max(200, labelBounds.width); // Search width
            int startX = Math.max(0, labelBounds.x - 50); // Start a bit left of label

            // Sample colors to find background change
            int labelColor = screenshot.getRGB(labelBounds.x + 10, labelBounds.y + 10);

            // Track colored region bounds
            int coloredRegionTop = -1;
            int coloredRegionBottom = -1;

            for (int y = startY; y < Math.min(startY + searchHeight, screenshot.getHeight() - 1); y += 5) {
                for (int x = startX; x < Math.min(startX + searchWidth, screenshot.getWidth() - 1); x += 10) {
                    int currentColor = screenshot.getRGB(x, y);

                    // Check if color is significantly different (input field background)
                    if (isColorDifferent(labelColor, currentColor, 30)) {
                        if (coloredRegionTop == -1) {
                            coloredRegionTop = y;
                        }
                        coloredRegionBottom = y;
                    }
                }
            }

            if (coloredRegionTop != -1) {
                // Calculate field height
                int fieldHeight = coloredRegionBottom - coloredRegionTop;
                int targetX = labelBounds.x + labelBounds.width / 2;
                int targetY;

                if (fieldHeight > 400) {
                    // Multi-line field - click at 40% from top
                    targetY = coloredRegionTop + (int)(fieldHeight * 0.4);
                    log.debug("Color-based: Multi-line field detected (height: {}), clicking at 40%", fieldHeight);
                } else {
                    // Single-line field - click at 25% from top
                    targetY = coloredRegionTop + (int)(fieldHeight * 0.25);
                    log.debug("Color-based: Single-line field detected (height: {}), clicking at 25%", fieldHeight);
                }

                log.debug("Color-based: Found colored region for '{}' at ({}, {})", searchText, targetX, targetY);
                return new Point(targetX, targetY);
            }

            log.debug("Color-based: No color change found below '{}'", searchText);
            return null;

        } catch (Exception e) {
            log.debug("Color-based detection failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Strategy 3: Smart offset based on text bounds
     */
    private Point trySmartOffset(BufferedImage screenshot, String searchText) {
        try {
            List<Word> words = tesseract.getWords(screenshot, 1);
            Word labelWord = null;

            // Find the label
            for (Word word : words) {
                if (word.getText().toLowerCase().contains(searchText.toLowerCase())) {
                    labelWord = word;
                    break;
                }
            }

            if (labelWord == null) {
                log.debug("Smart offset: Label '{}' not found", searchText);
                return null;
            }

            Rectangle bounds = labelWord.getBoundingBox();

            // Try to estimate field height by looking for next element below
            int estimatedFieldBottom = bounds.y + bounds.height + 300; // Default assumption

            // Look for any text below this label to estimate field bounds
            for (Word word : words) {
                Rectangle wordBounds = word.getBoundingBox();
                // Find next unrelated element below (likely next field label)
                if (wordBounds.y > bounds.y + bounds.height + 100 && // Not immediately below
                    Math.abs(wordBounds.x - bounds.x) < 100 && // Roughly aligned
                    word.getText().length() > 2 && // Not just a hint
                    !word.getText().toLowerCase().contains("enter")) { // Not placeholder text

                    estimatedFieldBottom = wordBounds.y - 20; // Field ends before next label
                    break;
                }
            }

            // Calculate estimated field height
            int estimatedFieldHeight = estimatedFieldBottom - bounds.y;
            int targetX = bounds.x + bounds.width / 2; // Center X of label
            int targetY;

            if (estimatedFieldHeight > 400) {
                // Multi-line field - click at 40% from top of field
                targetY = bounds.y + (int)(estimatedFieldHeight * 0.4);
                log.debug("Smart offset: Multi-line field detected (est. height: {}), clicking at 40%", estimatedFieldHeight);
            } else {
                // Single-line field - click at 25% from top of field
                targetY = bounds.y + (int)(estimatedFieldHeight * 0.25);
                log.debug("Smart offset: Single-line field detected (est. height: {}), clicking at 25%", estimatedFieldHeight);
            }

            // Ensure coordinates are within screen bounds
            if (targetY < screenshot.getHeight() && targetX < screenshot.getWidth()) {
                log.debug("Smart offset: Calculated position ({}, {}) for '{}'", targetX, targetY, searchText);
                return new Point(targetX, targetY);
            }

            log.debug("Smart offset: Coordinates out of bounds for '{}'", searchText);
            return null;

        } catch (Exception e) {
            log.debug("Smart offset failed: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Helper method to check if two colors are significantly different
     */
    private boolean isColorDifferent(int color1, int color2, int threshold) {
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;

        int diff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
        return diff > threshold;
    }

    /**
     * Wait for element to appear via image matching
     */
    public boolean waitForImageElement(String templateImagePath, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            Point location = findElementByImage(templateImagePath);
            if (location != null) {
                return true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("Element not found within {} seconds: {}", timeoutSeconds, templateImagePath);
        return false;
    }
}