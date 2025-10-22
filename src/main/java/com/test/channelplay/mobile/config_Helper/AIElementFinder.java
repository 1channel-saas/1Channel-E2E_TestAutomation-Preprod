package com.test.channelplay.mobile.config_Helper;

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
    private static String currentScenario = "unknown";

    static {
        // Load OpenCV native library
        nu.pattern.OpenCV.loadShared();
    }

    /**
     * Set current test scenario name for debug image organization
     * @param scenarioName Name of the test scenario
     */
    public static void setCurrentScenario(String scenarioName) {
        // Sanitize and shorten to prevent Windows MAX_PATH issues (260 char limit)
        String sanitized = scenarioName.replaceAll("[^a-zA-Z0-9_-]", "_");

        // Truncate to max 50 characters to keep paths short
        if (sanitized.length() > 50) {
            currentScenario = sanitized.substring(0, 50);
        } else {
            currentScenario = sanitized;
        }

        log.debug("Current scenario set to: {}", currentScenario);
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
        long startTime = System.currentTimeMillis();
        double bestConfidence = 0.0;
        MatchResult bestResult = null;

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

            // Preprocess images: Convert to grayscale and apply Gaussian blur to reduce noise
            log.debug("Preprocessing images: Converting to grayscale and applying Gaussian blur");
            Mat screenProcessed = preprocessImage(screenMat);
            Mat templateProcessed = preprocessImage(templateMat);

            // Strategy 1: Normalized Correlation Coefficient (best for general purpose)
            log.info("Trying OpenCV Strategy 1: TM_CCOEFF_NORMED for template '{}'", templateImagePath);
            MatchResult result = tryTemplateMatchingWithConfidence(screenProcessed, templateProcessed, Imgproc.TM_CCOEFF_NORMED, threshold, false);
            if (result.point != null) {
                log.info("SUCCESS: Template matched via OpenCV Strategy 1 (TM_CCOEFF_NORMED) at ({}, {})", result.point.x, result.point.y);

                // Save debug image with rectangle (Phase 1)
                saveDebugImage(screenProcessed, templateProcessed, result.matchLocation, templateImagePath, result.confidence, "TM_CCOEFF_NORMED");

                long matchTime = System.currentTimeMillis() - startTime;
                TemplateUsageTracker.recordOpenCVSuccess(templateImagePath, result.confidence, matchTime);
                return result.point;
            }
            // Track best result for failure debug (Phase 3)
            if (result.confidence > bestConfidence) {
                bestConfidence = result.confidence;
                bestResult = result;
            }
            log.info("OpenCV Strategy 1 failed, trying next strategy");

            // Strategy 2: Normalized Cross-Correlation (better for brightness variations)
            log.info("Trying OpenCV Strategy 2: TM_CCORR_NORMED for template '{}'", templateImagePath);
            result = tryTemplateMatchingWithConfidence(screenProcessed, templateProcessed, Imgproc.TM_CCORR_NORMED, threshold * 0.9, false);
            if (result.point != null) {
                log.info("SUCCESS: Template matched via OpenCV Strategy 2 (TM_CCORR_NORMED) at ({}, {})", result.point.x, result.point.y);

                // Save debug image with rectangle (Phase 1)
                saveDebugImage(screenProcessed, templateProcessed, result.matchLocation, templateImagePath, result.confidence, "TM_CCORR_NORMED");

                long matchTime = System.currentTimeMillis() - startTime;
                TemplateUsageTracker.recordOpenCVSuccess(templateImagePath, result.confidence, matchTime);
                return result.point;
            }
            // Track best result for failure debug (Phase 3)
            if (result.confidence > bestConfidence) {
                bestConfidence = result.confidence;
                bestResult = result;
            }
            log.info("OpenCV Strategy 2 failed, trying next strategy");

            // Strategy 3: Normalized Squared Difference (better for exact pixel matching)
            log.info("Trying OpenCV Strategy 3: TM_SQDIFF_NORMED for template '{}'", templateImagePath);
            result = tryTemplateMatchingWithConfidence(screenProcessed, templateProcessed, Imgproc.TM_SQDIFF_NORMED, 0.2, true);
            if (result.point != null) {
                log.info("SUCCESS: Template matched via OpenCV Strategy 3 (TM_SQDIFF_NORMED) at ({}, {})", result.point.x, result.point.y);

                // Save debug image with rectangle (Phase 1)
                saveDebugImage(screenProcessed, templateProcessed, result.matchLocation, templateImagePath, result.confidence, "TM_SQDIFF_NORMED");

                long matchTime = System.currentTimeMillis() - startTime;
                TemplateUsageTracker.recordOpenCVSuccess(templateImagePath, result.confidence, matchTime);
                return result.point;
            }
            // Track best result for failure debug (Phase 3)
            if (result.confidence > bestConfidence) {
                bestConfidence = result.confidence;
                bestResult = result;
            }
            log.info("OpenCV Strategy 3 failed");

            log.warn("Template '{}' not found via any OpenCV strategy (best confidence: {})", templateImagePath, bestConfidence);

            // Save debug image for failure with best attempt (Phase 3)
            if (bestResult != null && bestResult.matchLocation != null) {
                saveDebugFailure(screenProcessed, templateProcessed, bestResult.matchLocation, templateImagePath, bestConfidence);
            }

            // Record failure
            long matchTime = System.currentTimeMillis() - startTime;
            TemplateUsageTracker.recordOpenCVFailure(templateImagePath, matchTime);

            return null;

        } catch (Exception e) {
            log.error("Error in image matching: " + e.getMessage(), e);
            long matchTime = System.currentTimeMillis() - startTime;
            TemplateUsageTracker.recordOpenCVFailure(templateImagePath, matchTime);
            return null;
        }
    }

    /**
     * Helper method to try a specific template matching algorithm (returns confidence)
     */
    private MatchResult tryTemplateMatchingWithConfidence(Mat screenMat, Mat templateMat, int method, double threshold, boolean inverseResult) {
        try {
            Mat result = new Mat();
            Imgproc.matchTemplate(screenMat, templateMat, result, method);

            // Find best match
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

            // For SQDIFF methods, lower values are better (inverse)
            double confidence = inverseResult ? mmr.minVal : mmr.maxVal;
            Point matchLocation = inverseResult ? mmr.minLoc : mmr.maxLoc;

            boolean isMatch = inverseResult ? (confidence <= threshold) : (confidence >= threshold);

            log.debug("Template matching result - Method: {}, Confidence: {}, Threshold: {}, Match: {}",
                     getMethodName(method), confidence, threshold, isMatch);

            if (isMatch) {
                // Return center of matched area with confidence and match location
                Point center = new Point(
                    matchLocation.x + templateMat.cols() / 2,
                    matchLocation.y + templateMat.rows() / 2
                );
                return new MatchResult(center, matchLocation, confidence);
            }

            // Even for failures, return the best match location found (for debug visualization)
            Point center = new Point(
                matchLocation.x + templateMat.cols() / 2,
                matchLocation.y + templateMat.rows() / 2
            );
            return new MatchResult(null, matchLocation, confidence);

        } catch (Exception e) {
            log.debug("Template matching failed for method {}: {}", getMethodName(method), e.getMessage());
            return new MatchResult(null, null, 0.0);
        }
    }

    /**
     * Helper method to try a specific template matching algorithm
     */
    private Point tryTemplateMatching(Mat screenMat, Mat templateMat, int method, double threshold, boolean inverseResult) {
        try {
            Mat result = new Mat();
            Imgproc.matchTemplate(screenMat, templateMat, result, method);

            // Find best match
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

            // For SQDIFF methods, lower values are better (inverse)
            double confidence = inverseResult ? mmr.minVal : mmr.maxVal;
            Point matchLocation = inverseResult ? mmr.minLoc : mmr.maxLoc;

            boolean isMatch = inverseResult ? (confidence <= threshold) : (confidence >= threshold);

            log.debug("Template matching result - Method: {}, Confidence: {}, Threshold: {}, Match: {}",
                     getMethodName(method), confidence, threshold, isMatch);

            if (isMatch) {
                // Return center of matched area
                return new Point(
                    matchLocation.x + templateMat.cols() / 2,
                    matchLocation.y + templateMat.rows() / 2
                );
            }

            return null;

        } catch (Exception e) {
            log.debug("Template matching failed for method {}: {}", getMethodName(method), e.getMessage());
            return null;
        }
    }

    /**
     * Inner class to hold match result with confidence
     */
    private static class MatchResult {
        Point point;           // Center point (for clicking)
        Point matchLocation;   // Top-left corner (for drawing rectangle)
        double confidence;

        MatchResult(Point point, Point matchLocation, double confidence) {
            this.point = point;
            this.matchLocation = matchLocation;
            this.confidence = confidence;
        }
    }

    /**
     * Get method name for logging
     */
    private String getMethodName(int method) {
        switch (method) {
            case Imgproc.TM_CCOEFF_NORMED: return "TM_CCOEFF_NORMED";
            case Imgproc.TM_CCORR_NORMED: return "TM_CCORR_NORMED";
            case Imgproc.TM_SQDIFF_NORMED: return "TM_SQDIFF_NORMED";
            case Imgproc.TM_CCOEFF: return "TM_CCOEFF";
            case Imgproc.TM_CCORR: return "TM_CCORR";
            case Imgproc.TM_SQDIFF: return "TM_SQDIFF";
            default: return "UNKNOWN";
        }
    }

    /**
     * Preprocess image for better template matching
     * Converts to grayscale and applies Gaussian blur to reduce noise
     */
    private Mat preprocessImage(Mat image) {
        Mat processed = new Mat();

        // Step 1: Convert to grayscale (eliminates color variations)
        if (image.channels() == 3) {
            Imgproc.cvtColor(image, processed, Imgproc.COLOR_BGR2GRAY);
        } else {
            processed = image.clone();
        }

        // Step 2: Apply Gaussian blur to reduce noise and smooth edges
        // Kernel size (3,3) is good for small noise, (5,5) for larger noise
        // Using (3,3) for subtle smoothing without losing too much detail
        Mat blurred = new Mat();
        Imgproc.GaussianBlur(processed, blurred, new Size(3, 3), 0);

        return blurred;
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
            log.info("Trying OCR Strategy 1: Two-pass OCR for '{}'", searchText);
            Point coordinates = tryTwoPassOCR(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via OCR Strategy 1 (Two-pass OCR) at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }
            log.info("OCR Strategy 1 failed, trying next strategy");

            // Strategy 2: Color-based detection - Find input area by background color
            log.info("Trying OCR Strategy 2: Color-based detection for '{}'", searchText);
            coordinates = tryColorBasedDetection(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via OCR Strategy 2 (Color-based) at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }
            log.info("OCR Strategy 2 failed, trying next strategy");

            // Strategy 3: Smart offset with text bounds
            log.info("Trying OCR Strategy 3: Smart offset for '{}'", searchText);
            coordinates = trySmartOffset(screenshot, searchText);
            if (coordinates != null) {
                log.info("SUCCESS: Text '{}' found via OCR Strategy 3 (Smart offset) at ({}, {})", searchText, coordinates.x, coordinates.y);
                return coordinates;
            }
            log.info("OCR Strategy 3 failed, trying ultimate fallback");

            // Ultimate Fallback: Fixed offset from label
            log.info("Trying Ultimate Fallback: Fixed offset from label for '{}'", searchText);
            try {
                List<Word> words = tesseract.getWords(screenshot, 1);
                Word labelWord = null;

                // Try to find the label
                for (Word word : words) {
                    if (word.getText().toLowerCase().contains(searchText.toLowerCase())) {
                        labelWord = word;
                        break;
                    }
                }

                if (labelWord != null) {
                    // Use label position with standard offset
                    Rectangle bounds = labelWord.getBoundingBox();
                    int targetX = bounds.x + bounds.width / 2;
                    int targetY = bounds.y + bounds.height + 120;  // 120px below label

                    log.info("SUCCESS: Text '{}' found via Ultimate Fallback (Fixed offset) at ({}, {})", searchText, targetX, targetY);
                    return new Point(targetX, targetY);
                }
            } catch (Exception e) {
                log.debug("Ultimate fallback failed: {}", e.getMessage());
            }

            // Final Fallback: Original simple OCR (returns screen center)
            log.info("Trying Final Fallback: Simple OCR for '{}'", searchText);
            String ocrResult = tesseract.doOCR(screenshot);
            if (ocrResult.toLowerCase().contains(searchText.toLowerCase())) {
                log.info("SUCCESS: Text '{}' found via OCR Fallback (Simple OCR) - returning center", searchText);
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

                    // Extract first line/word from multi-line OCR results
                    String firstPart = wordText.split("\n")[0].trim();

                    // Look for hints like "Enter", numbers, or placeholder text
                    // Check first part length to handle multi-line OCR merging
                    if (firstPart.length() < 10 || // Short text likely to be hint (check first line)
                        wordText.toLowerCase().matches(".*\\d.*") || // Contains numbers
                        wordText.toLowerCase().contains("enter") ||
                        wordText.toLowerCase().contains("placeholder")) {

                        // Click at the position where hint text was detected
                        // The hint text is rendered inside the field, so clicking there is accurate
                        int targetX = wordBounds.x + wordBounds.width / 2;
                        int targetY = wordBounds.y + 20;  // Small offset from top of detected hint text

                        log.debug("Two-pass OCR: Found hint text '{}' below '{}' at ({}, {})", firstPart, searchText, targetX, targetY);

                        // Save debug image (OCR debug)
                        Point clickPoint = new Point(targetX, targetY);
                        saveDebugOCR(screenshot, searchText, labelWord.getBoundingBox(), wordBounds, null, clickPoint, "Two-pass_OCR", true);

                        return clickPoint;
                    }
                }
            }

            log.debug("Two-pass OCR: No hint text found below '{}'", searchText);

            // Save debug image for failure (OCR debug)
            if (labelWord != null) {
                saveDebugOCR(screenshot, searchText, labelWord.getBoundingBox(), null, null, null, "Two-pass_OCR", false);
            }

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

                if (fieldHeight > 300) {
                    // Very tall field (like textarea) - click at 30% from top
                    targetY = coloredRegionTop + (int)(fieldHeight * 0.3);
                    log.debug("Color-based: Tall field detected (height: {}), clicking at 30%", fieldHeight);
                } else {
                    // Normal field - click at center (50%)
                    targetY = coloredRegionTop + (int)(fieldHeight * 0.5);
                    log.debug("Color-based: Normal field detected (height: {}), clicking at center", fieldHeight);
                }

                log.debug("Color-based: Found colored region for '{}' at ({}, {})", searchText, targetX, targetY);

                // Save debug image (OCR debug) with color region
                Point clickPoint = new Point(targetX, targetY);
                Rectangle colorRegion = new Rectangle(startX, coloredRegionTop, searchWidth, fieldHeight);
                saveDebugOCR(screenshot, searchText, labelBounds, null, colorRegion, clickPoint, "Color-based", true);

                return clickPoint;
            }

            log.debug("Color-based: No color change found below '{}'", searchText);

            // Save debug image for failure (OCR debug)
            if (labelWord != null) {
                saveDebugOCR(screenshot, searchText, labelBounds, null, null, null, "Color-based", false);
            }

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

            // Check if this looks like a multi-line field based on label text
            boolean isMultiLineField = searchText.toLowerCase().matches(".*(description|notes|comment|message|address|details|remarks).*");

            // Try to estimate field height by looking for next element below
            // Use adaptive default height based on field type
            int estimatedFieldBottom;
            if (isMultiLineField) {
                estimatedFieldBottom = bounds.y + bounds.height + 500;  // Taller field for text areas
                log.debug("Smart offset: Detected multi-line field type for '{}', using 500px default", searchText);
            } else {
                estimatedFieldBottom = bounds.y + bounds.height + 200;  // Standard field
                log.debug("Smart offset: Detected standard field type for '{}', using 200px default", searchText);
            }

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

                // Save debug image (OCR debug)
                Point clickPoint = new Point(targetX, targetY);
                saveDebugOCR(screenshot, searchText, bounds, null, null, clickPoint, "Smart_offset", true);

                return clickPoint;
            }

            log.debug("Smart offset: Coordinates out of bounds for '{}'", searchText);

            // Save debug image for failure (OCR debug)
            if (labelWord != null) {
                saveDebugOCR(screenshot, searchText, bounds, null, null, null, "Smart_offset", false);
            }

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
     * Save debug image with rectangle showing matched region (Phase 1 + 2)
     * @param screenMat Original screen image
     * @param templateMat Template image that was matched
     * @param matchLoc Top-left point of match location
     * @param templatePath Path to template file
     * @param confidence Match confidence score
     * @param method Matching method name
     */
    private void saveDebugImage(Mat screenMat, Mat templateMat, Point matchLoc,
                                String templatePath, double confidence, String method) {
        if (!TemplateConfig.isDebugModeEnabled()) {
            return;
        }

        try {
            // Clone screen image for drawing
            Mat debugImage = screenMat.clone();

            // Convert from grayscale back to color for colored rectangle
            if (debugImage.channels() == 1) {
                Mat colorImage = new Mat();
                Imgproc.cvtColor(debugImage, colorImage, Imgproc.COLOR_GRAY2BGR);
                debugImage = colorImage;
            }

            // Calculate rectangle bounds
            Point topLeft = matchLoc;
            Point bottomRight = new Point(
                matchLoc.x + templateMat.cols(),
                matchLoc.y + templateMat.rows()
            );

            // Color based on confidence: Green (high), Yellow (medium), Red (low)
            Scalar color = confidence >= 0.9 ? new Scalar(0, 255, 0) :  // Green
                          confidence >= 0.8 ? new Scalar(0, 255, 255) : // Yellow
                                             new Scalar(0, 0, 255);      // Red

            // Draw rectangle (thickness 3)
            Imgproc.rectangle(debugImage, topLeft, bottomRight, color, 3);

            // Add text label with method and confidence
            String label = String.format("%s: %.2f", method, confidence);
            Imgproc.putText(debugImage, label,
                           new Point(topLeft.x, topLeft.y - 10),
                           Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, color, 2);

            // Add center crosshair
            Point center = new Point(
                matchLoc.x + templateMat.cols() / 2,
                matchLoc.y + templateMat.rows() / 2
            );
            Imgproc.drawMarker(debugImage, center, color, Imgproc.MARKER_CROSS, 20, 2);

            // Determine subfolder based on confidence
            String subfolder = confidence >= 0.8 ? "success" : "failures";

            // Build file path
            String filename = new File(templatePath).getName().replace(".png", "");
            String debugPath;

            if (TemplateConfig.isDebugOrganizeByScenario()) {
                // Option 2: Organize by scenario
                debugPath = String.format("%s/%s/%s/opencv_%s_match_%.2f_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        currentScenario,
                                        subfolder,
                                        filename,
                                        confidence,
                                        System.currentTimeMillis());
            } else {
                // Option 1: Flat structure
                debugPath = String.format("%s/%s/opencv_%s_match_%.2f_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        subfolder,
                                        filename,
                                        confidence,
                                        System.currentTimeMillis());
            }

            // Create directories and save
            File debugFile = new File(debugPath);
            debugFile.getParentFile().mkdirs();
            boolean success = Imgcodecs.imwrite(debugFile.getAbsolutePath(), debugImage);

            if (success) {
                log.info("Debug match image saved: {}", debugFile.getAbsolutePath());
            } else {
                log.error("FAILED to save debug image: {} (imwrite returned false)", debugFile.getAbsolutePath());
            }

        } catch (Exception e) {
            log.warn("Failed to save debug image: {}", e.getMessage());
        }
    }

    /**
     * Save debug image for failed match showing best attempt (Phase 3)
     * @param screenMat Original screen image
     * @param templateMat Template image that failed to match
     * @param bestMatchLoc Best match location found (even though confidence was too low)
     * @param templatePath Path to template file
     * @param bestConfidence Best confidence score achieved
     */
    private void saveDebugFailure(Mat screenMat, Mat templateMat, Point bestMatchLoc,
                                  String templatePath, double bestConfidence) {
        if (!TemplateConfig.isDebugModeEnabled()) {
            return;
        }

        try {
            // Clone screen image for drawing
            Mat debugImage = screenMat.clone();

            // Ensure image is in BGR format (3 channels) for OpenCV imwrite
            if (debugImage.channels() == 1) {
                // Grayscale -> BGR
                Mat colorImage = new Mat();
                Imgproc.cvtColor(debugImage, colorImage, Imgproc.COLOR_GRAY2BGR);
                debugImage.release();  // Release old Mat
                debugImage = colorImage;
            } else if (debugImage.channels() == 4) {
                // BGRA -> BGR (remove alpha channel which can cause imwrite issues)
                Mat bgrImage = new Mat();
                Imgproc.cvtColor(debugImage, bgrImage, Imgproc.COLOR_BGRA2BGR);
                debugImage.release();  // Release old Mat
                debugImage = bgrImage;
            }

            // Calculate rectangle bounds
            Point topLeft = bestMatchLoc;
            Point bottomRight = new Point(
                bestMatchLoc.x + templateMat.cols(),
                bestMatchLoc.y + templateMat.rows()
            );

            // Red color for failures
            Scalar color = new Scalar(0, 0, 255);

            // Draw rectangle (thickness 3)
            Imgproc.rectangle(debugImage, topLeft, bottomRight, color, 3);

            // Add text label indicating failure with best confidence
            String label = String.format("FAIL: Best=%.2f", bestConfidence);
            Imgproc.putText(debugImage, label,
                           new Point(topLeft.x, topLeft.y - 10),
                           Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, color, 2);

            // Add center crosshair
            Point center = new Point(
                bestMatchLoc.x + templateMat.cols() / 2,
                bestMatchLoc.y + templateMat.rows() / 2
            );
            Imgproc.drawMarker(debugImage, center, color, Imgproc.MARKER_CROSS, 20, 2);

            // Build file path (always goes to failures folder)
            String filename = new File(templatePath).getName().replace(".png", "");
            String debugPath;

            if (TemplateConfig.isDebugOrganizeByScenario()) {
                // Option 2: Organize by scenario
                debugPath = String.format("%s/%s/failures/opencv_%s_fail_%.2f_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        currentScenario,
                                        filename,
                                        bestConfidence,
                                        System.currentTimeMillis());
            } else {
                // Option 1: Flat structure
                debugPath = String.format("%s/failures/opencv_%s_fail_%.2f_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        filename,
                                        bestConfidence,
                                        System.currentTimeMillis());
            }

            // Create directories and save
            File debugFile = new File(debugPath);
            debugFile.getParentFile().mkdirs();
            boolean success = Imgcodecs.imwrite(debugFile.getAbsolutePath(), debugImage);

            if (success) {
                log.info("Debug failure image saved: {}", debugFile.getAbsolutePath());
            } else {
                log.error("FAILED to save debug failure image: {} (imwrite returned false)", debugFile.getAbsolutePath());
            }

        } catch (Exception e) {
            log.warn("Failed to save debug failure image: {}", e.getMessage());
        }
    }

    /**
     * Save debug image for OCR detection showing bounding boxes and click point
     * @param screenshot Original screenshot as BufferedImage
     * @param searchText Text being searched for
     * @param labelBounds Bounding box of the label text (can be null)
     * @param hintBounds Bounding box of the hint/value text (can be null)
     * @param colorRegion Colored region detected by color-based strategy (can be null)
     * @param clickPoint Final calculated click coordinates
     * @param strategy Strategy name that found the element
     * @param success Whether OCR successfully found and clicked the element
     */
    private void saveDebugOCR(BufferedImage screenshot, String searchText,
                             java.awt.Rectangle labelBounds, java.awt.Rectangle hintBounds,
                             java.awt.Rectangle colorRegion, Point clickPoint,
                             String strategy, boolean success) {
        if (!TemplateConfig.isDebugModeEnabled()) {
            return;
        }

        try {
            // Convert BufferedImage to OpenCV Mat for drawing
            Mat debugImage = bufferedImageToMat(screenshot);

            // Convert from BGR to color if needed
            if (debugImage.channels() == 1) {
                Mat colorImage = new Mat();
                Imgproc.cvtColor(debugImage, colorImage, Imgproc.COLOR_GRAY2BGR);
                debugImage = colorImage;
            } else if (debugImage.channels() == 3) {
                // Already in BGR format, ensure it's color
                Mat colorImage = new Mat();
                Imgproc.cvtColor(debugImage, colorImage, Imgproc.COLOR_RGB2BGR);
                debugImage = colorImage;
            }

            // Draw label bounding box (Blue)
            if (labelBounds != null) {
                Point topLeft = new Point(labelBounds.x, labelBounds.y);
                Point bottomRight = new Point(labelBounds.x + labelBounds.width,
                                             labelBounds.y + labelBounds.height);
                Scalar blueColor = new Scalar(255, 0, 0); // Blue in BGR
                Imgproc.rectangle(debugImage, topLeft, bottomRight, blueColor, 2);
                Imgproc.putText(debugImage, "LABEL",
                               new Point(topLeft.x, topLeft.y - 5),
                               Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, blueColor, 2);
            }

            // Draw hint/value text bounding box (Green)
            if (hintBounds != null) {
                Point topLeft = new Point(hintBounds.x, hintBounds.y);
                Point bottomRight = new Point(hintBounds.x + hintBounds.width,
                                             hintBounds.y + hintBounds.height);
                Scalar greenColor = new Scalar(0, 255, 0); // Green in BGR
                Imgproc.rectangle(debugImage, topLeft, bottomRight, greenColor, 2);
                Imgproc.putText(debugImage, "HINT",
                               new Point(topLeft.x, topLeft.y - 5),
                               Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, greenColor, 2);
            }

            // Draw color-detected region (Orange)
            if (colorRegion != null) {
                Point topLeft = new Point(colorRegion.x, colorRegion.y);
                Point bottomRight = new Point(colorRegion.x + colorRegion.width,
                                             colorRegion.y + colorRegion.height);
                Scalar orangeColor = new Scalar(0, 165, 255); // Orange in BGR
                Imgproc.rectangle(debugImage, topLeft, bottomRight, orangeColor, 2);
                Imgproc.putText(debugImage, "COLOR REGION",
                               new Point(topLeft.x, topLeft.y - 5),
                               Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, orangeColor, 2);
            }

            // Draw click point with crosshair (Red for success, Dark red for failure)
            if (clickPoint != null) {
                Scalar clickColor = success ? new Scalar(0, 0, 255) : new Scalar(0, 0, 139); // Red/Dark red
                Imgproc.drawMarker(debugImage, clickPoint, clickColor,
                                  Imgproc.MARKER_CROSS, 30, 3);

                // Add strategy label
                String label = String.format("%s: %s", strategy, success ? "SUCCESS" : "FAIL");
                Imgproc.putText(debugImage, label,
                               new Point(clickPoint.x - 50, clickPoint.y - 40),
                               Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, clickColor, 2);
            }

            // Determine subfolder
            String subfolder = success ? "success" : "failures";

            // Sanitize search text for filename
            String sanitizedName = searchText.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();

            // Build file path
            String debugPath;
            if (TemplateConfig.isDebugOrganizeByScenario()) {
                debugPath = String.format("%s/%s/%s/ocr_%s_%s_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        currentScenario,
                                        subfolder,
                                        sanitizedName,
                                        strategy.replaceAll(" ", "_"),
                                        System.currentTimeMillis());
            } else {
                debugPath = String.format("%s/%s/ocr_%s_%s_%d.png",
                                        TemplateConfig.getDebugFolder(),
                                        subfolder,
                                        sanitizedName,
                                        strategy.replaceAll(" ", "_"),
                                        System.currentTimeMillis());
            }

            // Create directories and save
            File debugFile = new File(debugPath);
            debugFile.getParentFile().mkdirs();
            boolean writeSuccess = Imgcodecs.imwrite(debugFile.getAbsolutePath(), debugImage);

            if (writeSuccess) {
                log.info("Debug OCR image saved: {}", debugFile.getAbsolutePath());
            } else {
                log.error("FAILED to save debug OCR image: {} (imwrite returned false)", debugFile.getAbsolutePath());
            }

        } catch (Exception e) {
            log.warn("Failed to save debug OCR image: {}", e.getMessage());
        }
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