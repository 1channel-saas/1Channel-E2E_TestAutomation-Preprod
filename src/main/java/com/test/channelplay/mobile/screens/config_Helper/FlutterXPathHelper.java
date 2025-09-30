package com.test.channelplay.mobile.screens.config_Helper;

import com.test.channelplay.utils.CommonUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class FlutterXPathHelper {
    
    private static final Logger log = LoggerFactory.getLogger(FlutterXPathHelper.class);
    CommonUtils commonUtils = new CommonUtils();
    private final AppiumDriver driver;
    private AIElementFinder aiFinder;
    private final AutoTemplateManager templateManager;


    public FlutterXPathHelper(AppiumDriver driver) {
        this.driver = driver;
        // Don't initialize AIElementFinder until actually needed
        this.templateManager = new AutoTemplateManager(driver);

        // Index any existing manual templates on initialization
        templateManager.indexManualTemplates();
    }

    //  ## Lazy initialization for AIElementFinder to prevent driver state issues
    private AIElementFinder getAIFinder() {
        if (aiFinder == null) {
            aiFinder = new AIElementFinder(driver);
        }
        return aiFinder;
    }




    //  ## xpath retrieval strategies

    public WebElement findByText(String text) {
        List<String> strategies = List.of(
            // Strategy 1: Exact text match
            "//*[@text='" + text + "' or @content-desc='" + text + "']",
            // Strategy 2: Direct content-desc match
            "//*[@content-desc='" + text + "']",
            // Strategy 3: Partial text match (fallback)
            "//*[contains(@text,'" + text + "') or contains(@content-desc,'" + text + "')]"
        );

        for (String xpath : strategies) {
            try {
                WebElement element = driver.findElement(AppiumBy.xpath(xpath));
                if (element != null && element.isDisplayed()) {
                    log.info("Found element with text '{}' using strategy: {}", text, xpath);
                    return element;
                }
            } catch (Exception e) {
                // Try next strategy
            }
        }
        throw new RuntimeException("Could not find element with text: " + text);
    }

    public WebElement findInputAfterLabel(String labelText) {
        List<String> strategies = List.of(
            // Strategy 1: Direct following sibling
            "//*[@text='" + labelText + "' or @content-desc='" + labelText + "']/following-sibling::*[contains(@class,'EditText') or @class='android.view.View'][1]",
            // Strategy 2: Parent's next sibling
            "//*[@text='" + labelText + "' or @content-desc='" + labelText + "']/parent::*/following-sibling::*[1]//*[contains(@class,'EditText')]",
            // Strategy 3: Child of parent with text (from smartFindField)
            "//*[*[@text='" + labelText + "']]//*[@clickable='true' or contains(@class,'EditText')]"
        );

        for (String xpath : strategies) {
            try {
                WebElement element = driver.findElement(AppiumBy.xpath(xpath));
                if (element != null && element.isDisplayed()) {
                    log.info("Found input after label '{}' using strategy: {}", labelText, xpath);
                    return element;
                }
            } catch (Exception e) {
                // Try next strategy
            }
        }
        throw new RuntimeException("Could not find input after label: " + labelText);
    }

    public WebElement findButtonAfterText(String text) {
        List<String> strategies = List.of(
            // Strategy 1: Following clickable element (original)
            "//*[@text='" + text + "' or @content-desc='" + text + "']/following::*[@clickable='true'][1]",
            // Strategy 2: Following sibling clickable
            "//*[@text='" + text + "' or @content-desc='" + text + "']/following-sibling::*[@clickable='true'][1]",
            // Strategy 3: Clickable with partial text match
            "//*[@clickable='true' and (contains(@text,'" + text + "') or contains(@content-desc,'" + text + "'))]"
        );

        for (String xpath : strategies) {
            try {
                WebElement element = driver.findElement(AppiumBy.xpath(xpath));
                if (element != null && element.isDisplayed()) {
                    log.info("Found button after text '{}' using strategy: {}", text, xpath);
                    return element;
                }
            } catch (Exception e) {
                // Try next strategy
            }
        }
        throw new RuntimeException("Could not find button after text: " + text);
    }

    public WebElement findByRelativePosition(String referenceText, String direction, int position) {
        String xpath = "";
        switch (direction.toLowerCase()) {
            case "below":
                xpath = "//*[@text='" + referenceText + "']/following::*[" + position + "]";
                break;
            case "above":
                xpath = "//*[@text='" + referenceText + "']/preceding::*[" + position + "]";
                break;
            case "right":
                xpath = "//*[@text='" + referenceText + "']/following-sibling::*[" + position + "]";
                break;
            case "left":
                xpath = "//*[@text='" + referenceText + "']/preceding-sibling::*[" + position + "]";
                break;
            default: break;
        }
        return driver.findElement(AppiumBy.xpath(xpath));
    }

    public WebElement findNthElementOfType(String elementType, int index) {
        String xpath = "(//*[contains(@class,'" + elementType + "')])[" + index + "]";
        return driver.findElement(AppiumBy.xpath(xpath));
    }

    public WebElement findInContainerWithText(String containerText, String elementClass) {
        String xpath = "//android.view.ViewGroup[.//*[@text='" + containerText + "' or " +
                      "@content-desc='" + containerText + "']]//" + 
                      "*[contains(@class,'" + elementClass + "')]";
        return driver.findElement(AppiumBy.xpath(xpath));
    }

    public WebElement findClickableWithText(String partialText) {
        List<String> strategies = List.of(
            // Strategy 1: Exact clickable text match
            "//*[@clickable='true' and (@text='" + partialText + "' or @content-desc='" + partialText + "')]",
            // Strategy 2: Partial clickable text match (original)
            "//*[@clickable='true' and (contains(@text,'" + partialText + "') or contains(@content-desc,'" + partialText + "'))]",
            // Strategy 3: Any element with exact text that's clickable
            "//*[@text='" + partialText + "' and @clickable='true']",
            // Strategy 4: Content-desc exact match that's clickable
            "//*[@content-desc='" + partialText + "' and @clickable='true']"
        );

        for (String xpath : strategies) {
            try {
                WebElement element = driver.findElement(AppiumBy.xpath(xpath));
                if (element != null && element.isDisplayed()) {
                    log.info("Found clickable element with text '{}' using strategy: {}", partialText, xpath);
                    return element;
                }
            } catch (Exception e) {
                // Try next strategy
            }
        }
        throw new RuntimeException("Could not find clickable element with text: " + partialText);
    }

    public List<WebElement> findElementsBetween(String startText, String endText) {
        String xpath = "//*[@text='" + startText + "']/following-sibling::*" +
                      "[following::*[@text='" + endText + "']]";
        return driver.findElements(AppiumBy.xpath(xpath));
    }

    public List<WebElement> findFormFields() {
        // Find all EditText or clickable Views in order
        return driver.findElements(AppiumBy.xpath(
            "//*[contains(@class,'EditText') or (@clickable='true' and @class='android.view.View')]"
        ));
    }



    
    /**
     * Find all elements in a list
     * @param containerXPath - The parent container xpath
     * @return List of all elements
     */
    public List <WebElement> findAllClickableViews(String containerXPath) {
        //  Find all clickable views with content-desc pattern like "rest@1", "rest@2", etc.
        String xpath = containerXPath + "//*[@content-desc and @clickable='true']";
        return driver.findElements(AppiumBy.xpath(xpath));
    }

    public void clickOptionsByIndex(String containerXPath, int index) {
        List <WebElement> options = findAllClickableViews(containerXPath);
        if (index >= 0 && index < options.size()) {
            options.get(index).click();
            log.info("Clicked on dropdown clickable Options at index: {}", index);
        } else {
            throw new RuntimeException("Dropdown index " + index + " out of bounds. Found " + options.size() + " options");
        }
    }

    public void clickOptionsByName(String containerXPath, String optionName) {
        String xpath = containerXPath + "//*[@content-desc='" + optionName + "' and @clickable='true']";
        WebElement option = driver.findElement(AppiumBy.xpath(xpath));
        option.click();
        log.info("Clicked on company: {}", optionName);
    }

    public List <String> getAllOptionNames(String containerXPath) {
        List <WebElement> options = findAllClickableViews(containerXPath);
        return options.stream().map(element -> element.getAttribute("content-desc"))
                .collect(java.util.stream.Collectors.toList());
    }



    
    //  ## Close hamburger menu by clicking on right side of screen
    public void closeHamburgerMenu() {
        //  Get screen dimensions
        org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
        int screenWidth = screenSize.getWidth();
        int screenHeight = screenSize.getHeight();
        
        //  Click on right side of screen (75% from left, middle of screen vertically)
        int clickX = (int) (screenWidth * 0.75);
        int clickY = screenHeight / 2;
        
        //  Using W3C Actions API for tap
        org.openqa.selenium.interactions.PointerInput finger =
            new org.openqa.selenium.interactions.PointerInput(
                org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
        org.openqa.selenium.interactions.Sequence tapSequence = 
            new org.openqa.selenium.interactions.Sequence(finger, 1);
        
        tapSequence.addAction(finger.createPointerMove(
            java.time.Duration.ZERO, 
            org.openqa.selenium.interactions.PointerInput.Origin.viewport(), 
            clickX, clickY));
        tapSequence.addAction(finger.createPointerDown(
            org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
        tapSequence.addAction(finger.createPointerUp(
            org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(List.of(tapSequence));
        log.info("Clicked to close hamburger menu at coordinates: ({}, {})", clickX, clickY);
    }
    
    //  ## Close hamburger menu by finding and clicking the overlay/backdrop
    public void closeHamburgerMenuByOverlay() {
        try {
            //  find the overlay/backdrop element that covers the right side
            WebElement overlay = driver.findElement(AppiumBy.xpath(
                "//android.view.View[contains(@class,'Overlay') or contains(@resource-id,'overlay')]"));
            overlay.click();
            log.info("Clicked on overlay to close hamburger menu");
        } catch (Exception e) {
            //  Fallback to coordinate click
            closeHamburgerMenu();
        }
    }




    //  ## AI-Enhanced Element Finding and Interaction

    /**
     * Smart element finder with multiple XPath strategies and AI fallback
     * @param fieldName Name of the field for logging (e.g., "Description", "Title", "Username")
     * @param xpathStrategies Array of XPath strategies to try in order
     * @param templateImage Path to template image for AI fallback (can be null)
     * @param searchText Text to search via OCR for AI fallback (can be null)
     * @param action Action to perform: "click", "focus", "shutter_click", or "none" (default: "none")
     * @param textToSend Text to send after finding element (null for non-text fields)
     * @return WebElement if found
     */
    public WebElement smartFindElementWithAI(String fieldName, String[] xpathStrategies, String templateImage, String searchText,
                                            String action, String textToSend) {

        log.info("Looking for {} field with {} XPath strategies, action: {}", fieldName, xpathStrategies.length, action);

        // Special handling for shutter_click action - doesn't need XPath or AI fallback
        if ("shutter_click".equals(action)) {
            log.info("Performing direct shutter click action for {}", fieldName);
            if (performCameraShutterClick()) {
                return createDummyElement();  // Return dummy element to indicate success
            }
            throw new RuntimeException("Failed to perform shutter click for " + fieldName);
        }

        // Try XPath strategies first
        WebElement element = findXPathStrategies(fieldName, xpathStrategies);
        if (element != null) {
            if (performAction(element, action, textToSend, fieldName)) {
                return element;
            }
            // If action failed, fall through to AI
            log.info("Action '{}' failed on XPath element, trying AI fallback", action);
        }

        // Try AI fallback if configured
        element = AIFallback(fieldName, templateImage, searchText, action, textToSend);
        if (element != null) {
            return element;
        }

        throw new RuntimeException("Could not find " + fieldName + " field with any strategy");
    }


    //  * Overloaded method for smartFindElementWithAI() (no action)
    public WebElement smartFindElementWithAI(String fieldName, String[] xpathStrategies,
                                            String templateImage, String searchText) {
        return smartFindElementWithAI(fieldName, xpathStrategies, templateImage, searchText, "none", null);
    }


    //  * Helper method -> XPath strategies for smartFindElementWithAI()
    private WebElement findXPathStrategies(String fieldName, String[] xpathStrategies) {
        for (int i = 0; i < xpathStrategies.length; i++) {
            String xpath = xpathStrategies[i];
            try {
                WebElement element = driver.findElement(AppiumBy.xpath(xpath));
                if (element != null && element.isDisplayed()) {
                    log.info("{} field found with XPath strategy {}: {}", fieldName, (i+1), xpath);
                    // Note: Auto-capture for XPath elements is handled by AutoTemplateManager
                    // It will be saved to templates/screens folder for XPath-found elements
                    templateManager.autoCapture(fieldName, element);
                    return element;
                }
            } catch (Exception e) {
                log.debug("XPath strategy {} failed for {}: {}", (i+1), fieldName, xpath);
            }
        }
        return null;
    }


    //  * Helper method -> Perform action on element for smartFindElementWithAI()
    private boolean performAction(WebElement element, String action, String textToSend, String fieldName) {
        try {
            if ("click".equals(action)) {
                // Validate if element is actually clickable before clicking
                boolean isClickable = validateClickableElement(element, fieldName);

                if (!isClickable) {
                    log.warn("{} is not a clickable element, will trigger AI fallback", fieldName);
                    return false;  // This will trigger AI fallback
                }

                // Special handling for calendar date buttons
                if (isCalendarDateElement(fieldName, element)) {
                    return performCalendarDateClick(element, fieldName);
                }

                // Store page source before click to detect changes
                String beforeClick = driver.getPageSource().hashCode() + "";
                element.click();

                // Brief wait for any UI changes
                Thread.sleep(500);

                // Check if click caused any UI change
                String afterClick = driver.getPageSource().hashCode() + "";

                if (beforeClick.equals(afterClick)) {
                    log.warn("Click on {} did not cause any UI changes, might be wrong element", fieldName);
                    return false;  // Trigger AI fallback
                }

                log.info("Clicked {} successfully", fieldName);
                return true;

            } else if ("focus".equals(action)) {
                element.click();  // Click to focus
                log.info("Focused on {} field", fieldName);
                if (textToSend != null && !textToSend.isEmpty()) {
                    element.clear();
                    element.sendKeys(textToSend);
                    log.info("performAction -> Text sent to {}: {}", fieldName, textToSend);
                }
                return true;
            } else if ("shutter_click".equals(action)) {
                // Special handling for camera shutter button
                log.info("Performing camera shutter click for {}", fieldName);
                return performCameraShutterClick();
            } else if ("none".equals(action) || action == null) {
                // No action, just return the element
                return true;
            }
        } catch (Exception e) {
            log.warn("Failed to perform action '{}' on {}: {}", action, fieldName, e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Check if this is a calendar date element that needs special handling
     */
    private boolean isCalendarDateElement(String fieldName, WebElement element) {
        // Check by field name
        if (fieldName != null && (
            fieldName.toLowerCase().contains("calendar") ||
            fieldName.toLowerCase().contains("date") && fieldName.toLowerCase().contains("today") ||
            fieldName.contains("performDate_calendar")
        )) {
            return true;
        }

        // Check by element attributes
        try {
            String contentDesc = element.getAttribute("content-desc");
            if (contentDesc != null) {
                // Check if it's a date format like "22, Monday, September 22, 2025, Today"
                return contentDesc.matches("\\d+,.*") || contentDesc.contains("Today");
            }
        } catch (Exception e) {
            // Ignore attribute check errors
        }

        return false;
    }

    /**
     * Perform click on calendar date button with adjusted coordinates
     */
    private boolean performCalendarDateClick(WebElement element, String fieldName) {
        try {
            log.info("Detected calendar date element: {}, using precise click", fieldName);

            // Get element bounds
            org.openqa.selenium.Rectangle rect = element.getRect();

            // For calendar dates, we want to click precisely in the center
            // Instead of using the full element bounds which might be too large
            int centerX = rect.x + (rect.width / 2);
            int centerY = rect.y + (rect.height / 2);

            // If the element bounds seem too large for a date button (>100px),
            // adjust to click in a smaller area
            if (rect.width > 100 || rect.height > 100) {
                log.debug("Element bounds seem large for date button ({}x{}), adjusting click area", rect.width, rect.height);
                // Assume the actual date button is smaller and centered
                // Reduce the click area to about 50x50 pixels in the center
                int adjustedWidth = Math.min(50, rect.width);
                int adjustedHeight = Math.min(50, rect.height);

                // Recalculate center based on adjusted bounds
                centerX = rect.x + (adjustedWidth / 2);
                centerY = rect.y + (adjustedHeight / 2);
            }

            // Perform tap at calculated coordinates
            performTapAtCoordinates(centerX, centerY);

            log.info("Clicked calendar date at coordinates: ({}, {})", centerX, centerY);

            // Wait for UI update
            Thread.sleep(500);
            return true;

        } catch (Exception e) {
            log.warn("Failed to perform calendar date click for {}: {}", fieldName, e.getMessage());
            // Fall back to regular click
            try {
                element.click();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    /**
     * Perform camera shutter click using intelligent coordinate detection
     * Based on common camera UI patterns where shutter is center-bottom
     */
    private boolean performCameraShutterClick() {
        try {
            // Get screen dimensions
            org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            // Camera shutter button is typically:
            // - Horizontally centered
            // - Located in bottom 20-25% of screen
            // - Has a circular shape with radius ~40-60px

            // Calculate shutter button coordinates
            // Based on actual UI Automator bounds [461,2114][619,2271]
            // The shutter button center is at approximately (540, 2192) on 1080x2400 screen
            int shutterX = screenWidth / 2;  // Center horizontally
            int shutterY = (int)(screenHeight * 0.913);  // 91.3% from top (based on actual bounds)

            log.info("Camera screen dimensions: {}x{}", screenWidth, screenHeight);
            log.info("Calculated shutter coordinates: ({}, {})", shutterX, shutterY);

            // First attempt - click at calculated position
            Thread.sleep(1000);  // Brief wait before clicking
            performTapAtCoordinates(shutterX, shutterY);

            // Wait for camera to capture
            Thread.sleep(2000);

            // Check if we're still on camera screen (shutter click might have failed)
            // If camera UI has specific elements, we can check for them
            try {
                // Try to find common camera UI elements to verify we're still on camera
                driver.findElement(AppiumBy.xpath("//android.widget.ImageView[@content-desc='Switch camera']"));

                // Still on camera, try alternative coordinates
                log.warn("First shutter click might have failed, trying alternative position");
                shutterY = (int)(screenHeight * 0.91);  // Try slightly higher (91% instead of 91.3%)
                performTapAtCoordinates(shutterX, shutterY);
                Thread.sleep(1000);
            } catch (Exception e) {
                // Camera UI element not found - likely means photo was taken successfully
                log.info("Camera shutter click successful - camera UI closed");
            }

            return true;

        } catch (Exception e) {
            log.error("Failed to perform camera shutter click: {}", e.getMessage());

            // Fallback to multiple coordinate attempts
            return fallbackCameraShutterClick();
        }
    }

    /**
     * Fallback method that tries multiple common shutter button positions
     */
    private boolean fallbackCameraShutterClick() {
        try {
            org.openqa.selenium.Dimension screenSize = driver.manage().window().getSize();
            int centerX = screenSize.width / 2;

            // Common shutter button positions (as percentage of screen height)
            // Primary position is 91.3%, then try nearby positions
            double[] shutterPositions = {0.913, 0.91, 0.92, 0.90, 0.88, 0.85};

            for (double position : shutterPositions) {
                int shutterY = (int)(screenSize.height * position);
                log.info("Trying shutter click at position: ({}, {})", centerX, shutterY);

                Thread.sleep(1000);
                performTapAtCoordinates(centerX, shutterY);
                Thread.sleep(1000);

                // Check if camera closed (photo taken)
                try {
                    driver.findElement(AppiumBy.xpath("//android.widget.ImageView[@content-desc='Switch camera']"));
                    // Still on camera, continue trying
                } catch (Exception e) {
                    // Camera closed, success!
                    log.info("Shutter click successful at position {}%", position * 100);
                    return true;
                }
            }

            log.warn("All shutter click attempts completed, camera may still be open");
            return false;

        } catch (Exception e) {
            log.error("Fallback shutter click failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Perform tap at specific coordinates
     */
    private void performTapAtCoordinates(int x, int y) {
        org.openqa.selenium.interactions.PointerInput finger =
            new org.openqa.selenium.interactions.PointerInput(
                org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");

        org.openqa.selenium.interactions.Sequence tapSequence =
            new org.openqa.selenium.interactions.Sequence(finger, 1);

        tapSequence.addAction(finger.createPointerMove(
            java.time.Duration.ZERO,
            org.openqa.selenium.interactions.PointerInput.Origin.viewport(),
            x, y));
        tapSequence.addAction(finger.createPointerDown(
            org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
        tapSequence.addAction(finger.createPointerUp(
            org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(java.util.Arrays.asList(tapSequence));
    }

    /**
     * Validate if element is actually clickable
     */
    private boolean validateClickableElement(WebElement element, String fieldName) {
        try {
            // Check if element is enabled and displayed
            if (!element.isEnabled() || !element.isDisplayed()) {
                log.debug("{} is not enabled/displayed", fieldName);
                return false;
            }

            // Check tag name - Views are typically not clickable
            String tagName = element.getTagName();
            if (tagName.equals("android.view.View")) {
                // Check if it has clickable attribute
                String clickable = element.getAttribute("clickable");
                if (!"true".equals(clickable)) {
                    log.debug("{} is a View element with clickable={}", fieldName, clickable);
                    return false;
                }
            }

            // Check for clickable types
            boolean isClickableType = tagName.contains("Button") ||
                                     tagName.contains("ImageView") ||
                                     tagName.contains("EditText") ||
                                     tagName.contains("TextView") && "true".equals(element.getAttribute("clickable"));

            if (!isClickableType) {
                log.debug("{} tag '{}' is not typically clickable", fieldName, tagName);
                return false;
            }

            return true;

        } catch (Exception e) {
            log.debug("Error validating clickability of {}: {}", fieldName, e.getMessage());
            return true;  // Assume clickable if we can't determine
        }
    }


    //  * Helper method -> AI fallback for smartFindElementWithAI()
    private WebElement AIFallback(String fieldName, String templateImage, String searchText,
                                 String action, String textToSend) {
        // Check if at least one fallback method is configured
        if (!isAIFallbackConfigured(templateImage, searchText)) {
            log.debug("No AI fallback configured for {} field", fieldName);
            return null;
        }

        log.info("All XPath strategies failed for {}, trying AI fallback...", fieldName);

        // Build template search hierarchy
        String effectiveTemplate = findBestTemplate(fieldName, templateImage);

        // If no template found by name, try visual matching
        if (effectiveTemplate == null && templateManager.getVisualIndex() != null) {
            effectiveTemplate = tryVisualMatching(fieldName);
        }

        // Try smart click with the best template found
        if (!getAIFinder().smartClick(effectiveTemplate, searchText)) {
            return null;
        }

        log.info("{} field clicked with AI, finding focused element...", fieldName);
        commonUtils.sleep(1000);

        WebElement focusedElement = findFocusedElement();
        if (focusedElement != null) {
            log.info("Found focused element after AI click for {}", fieldName);

            // Auto-capture ONLY for focus/sendKeys actions (text input fields)
            // Skip auto-capture for click actions to avoid capturing wrong screen state
            if (effectiveTemplate != null && ("focus".equals(action) || textToSend != null)) {
                try {
                    // Get element bounds and save for future use
                    org.openqa.selenium.Rectangle rect = focusedElement.getRect();
                    templateManager.autoCaptureByBounds(fieldName, rect.x, rect.y, rect.width, rect.height);
                    log.info("Auto-captured AI template for text field: {}", fieldName);
                } catch (Exception e) {
                    log.debug("Could not auto-capture AI template: {}", e.getMessage());
                }
            } else if ("click".equals(action)) {
                log.debug("Skipping auto-capture for click action on {}", fieldName);
            }

            // Perform additional action if needed (for text fields)
            if ("focus".equals(action) && textToSend != null) {
                try {
                    focusedElement.sendKeys(textToSend);
                    log.info("AIFallback -> Text sent to {} field via AI: {}", fieldName, textToSend);
                } catch (Exception e) {
                    log.warn("Failed to send text to AI-found element: {}", e.getMessage());
                }
            }

            return focusedElement;
        }

        log.warn("Could not find focused element for {}, returning dummy element", fieldName);
        return createDummyElement();
    }


    //  ** Helper method -> Find best template from hierarchy
    private String findBestTemplate(String fieldName, String manualTemplate) {
        String sanitizedName = fieldName.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();

        // Get visual index from template manager
        VisualTemplateIndex visualIndex = templateManager.getVisualIndex();

        // Search for templates by name across all folders and versions
        List<String> templates = visualIndex.findTemplatesByName(sanitizedName);

        // Priority 1: Check XPath captures (screens folder)
        for (String template : templates) {
            if (template.contains("/screens/") && template.endsWith("_auto.png")) {
                log.info("Found template in screens folder: {}", template);
                return template;
            }
        }

        // Priority 2: Check AI captures (AI_images folder)
        for (String template : templates) {
            if (template.contains("/AI_images/") && template.endsWith("_ai.png")) {
                log.info("Found template in AI_images folder: {}", template);
                return template;
            }
        }

        // Priority 3: Check manual captures (manual_captured_images folder)
        for (String template : templates) {
            if (template.contains("/manual_captured_images/")) {
                log.info("Found template in manual_captured_images folder: {}", template);
                return template;
            }
        }

        // Priority 4: Use manually provided template if exists (with full path)
        if (manualTemplate != null && !manualTemplate.isEmpty()) {
            if (java.nio.file.Files.exists(java.nio.file.Paths.get(manualTemplate))) {
                log.info("Using manually provided template: {}", manualTemplate);
                return manualTemplate;
            } else {
                log.warn("Manual template not found: {}", manualTemplate);
            }
        }

        // Priority 4: Try visual matching if we can capture current screen region
        // This will be called from AIFallback method with current element bounds

        log.debug("No template found by name for: {}", fieldName);
        return null;  // Will try visual matching or OCR
    }

    //  ** Helper method -> is AI fallback is configured for AIFallback()
    private boolean isAIFallbackConfigured(String templateImage, String searchText) {
        return (templateImage != null && !templateImage.isEmpty()) ||
               (searchText != null && !searchText.isEmpty());
    }

    
    //  ** Helper method -> Find the currently focused element after AI interaction for AIFallback()
    private WebElement findFocusedElement() {
        WebElement element;

        // Try each strategy in order
        element = findActiveElement();
        if (element != null) return element;

        element = findByFocusedAttribute();
        if (element != null) return element;

        element = findFocusedEditText();
        if (element != null) return element;

        element = findLastResortEditText();
        if (element != null) return element;

        log.warn("Could not find focused element with any strategy");
        return null;
    }


    //  *** Helper method of findFocusedElement()
    private WebElement findActiveElement() {
        try {
            WebElement activeElement = driver.switchTo().activeElement();
            if (activeElement != null && activeElement.isDisplayed()) {
                log.debug("Found active element via switchTo().activeElement()");
                return activeElement;
            }
        } catch (Exception e) {
            log.debug("switchTo().activeElement() failed: {}", e.getMessage());
        }
        return null;
    }

    //  *** Helper method of findFocusedElement()
    private WebElement findByFocusedAttribute() {
        try {
            WebElement focusedElement = driver.findElement(AppiumBy.xpath("//*[@focused='true']"));
            if (focusedElement.isDisplayed()) {
                log.debug("Found focused element via @focused='true'");
                return focusedElement;
            }
        } catch (Exception e) {
            log.debug("@focused='true' strategy failed: {}", e.getMessage());
        }
        return null;
    }

    //  *** Helper method of findFocusedElement()
    private WebElement findFocusedEditText() {
        try {
            List <WebElement> editTexts = driver.findElements(AppiumBy.xpath("//android.widget.EditText"));
            for (WebElement editText : editTexts) {
                if (isElementFocused(editText)) {
                    log.debug("Found focused EditText via attribute check");
                    return editText;
                }
            }
        } catch (Exception e) {
            log.debug("EditText focused search failed: {}", e.getMessage());
        }
        return null;
    }

    //  **** Helper method of findFocusedEditText()
    private boolean isElementFocused(WebElement element) {
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                String focused = element.getAttribute("focused");
                return "true".equals(focused);
            }
        } catch (Exception e) {
            // Element not suitable, return false
        }
        return false;
    }

    //  *** Helper method of findFocusedElement()
    private WebElement findLastResortEditText() {
        try {
            WebElement anyEditText = driver.findElement(AppiumBy.xpath("//android.widget.EditText[@clickable='true'][last()]"));
            if (anyEditText.isDisplayed()) {
                log.debug("Found EditText as last resort");
                return anyEditText;
            }
        } catch (Exception e) {
            log.debug("Last resort EditText search failed: {}", e.getMessage());
        }
        return null;
    }




    //  ## Take screenshot img of current app state (e.g. whether text actually entered)
    /**
     * independent method
     * Trigger - manual call
     * Purpose - Debug/validate app state, error capture
     * Directory - screenshots/
     *
     * @return
     */
    public String saveScreenshotForTemplate(String filename) {
        getAIFinder().saveScreenshot(filename);
        log.info("Screenshot saved for template creation: {}", filename);
        return filename;
    }




    /**
     * Save screenshot to a specific folder
     * @param filename The name of the screenshot file
     * @param folderPath The folder path relative to project root (e.g., "screenshots/validation_failure")
     * @return The full path where screenshot was saved
     */
    public String saveScreenshotToFolder(String filename, String folderPath) {
        try {
            // Create folder if it doesn't exist
            java.nio.file.Path folder = java.nio.file.Paths.get(folderPath);
            if (!java.nio.file.Files.exists(folder)) {
                java.nio.file.Files.createDirectories(folder);
                log.info("Created folder: {}", folderPath);
            }

            // Take screenshot
            java.io.File screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);

            // Save to specified folder
            java.io.File destFile = new java.io.File(folderPath + "/" + filename);
            org.apache.commons.io.FileUtils.copyFile(screenshot, destFile);

            String fullPath = destFile.getAbsolutePath();
            log.info("Screenshot saved to: {}", fullPath);
            return fullPath;

        } catch (Exception e) {
            log.error("Failed to save screenshot to folder: {}", e.getMessage());
            // Fallback to default location
            return saveScreenshotForTemplate(filename);
        }
    }




    //  ## methods for flutter scroll feature
    /**
     * Scroll down in the current view
     * @param scrollTimes Number of times to scroll
     */
    public void scrollDown(int scrollTimes) {
        try {
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.7);
            int endY = (int) (size.height * 0.3);

            for (int i = 0; i < scrollTimes; i++) {
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");

                org.openqa.selenium.interactions.Sequence swipe =
                    new org.openqa.selenium.interactions.Sequence(finger, 1);

                swipe.addAction(finger.createPointerMove(java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
                swipe.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                swipe.addAction(finger.createPointerMove(java.time.Duration.ofMillis(300),
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
                swipe.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

                driver.perform(java.util.Arrays.asList(swipe));
                Thread.sleep(500);
            }
            log.info("Scrolled down {} times", scrollTimes);
        } catch (Exception e) {
            log.error("Failed to scroll: {}", e.getMessage());
        }
    }


    /**
     * Scroll up in the current view
     * @param scrollTimes Number of times to scroll
     */
    public void scrollUp(int scrollTimes) {
        try {
            org.openqa.selenium.Dimension size = driver.manage().window().getSize();
            int startX = size.width / 2;
            int startY = (int) (size.height * 0.3);
            int endY = (int) (size.height * 0.7);

            for (int i = 0; i < scrollTimes; i++) {
                org.openqa.selenium.interactions.PointerInput finger =
                    new org.openqa.selenium.interactions.PointerInput(
                        org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");

                org.openqa.selenium.interactions.Sequence swipe =
                    new org.openqa.selenium.interactions.Sequence(finger, 1);

                swipe.addAction(finger.createPointerMove(java.time.Duration.ZERO,
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, startY));
                swipe.addAction(finger.createPointerDown(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                swipe.addAction(finger.createPointerMove(java.time.Duration.ofMillis(300),
                    org.openqa.selenium.interactions.PointerInput.Origin.viewport(), startX, endY));
                swipe.addAction(finger.createPointerUp(
                    org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));

                driver.perform(java.util.Arrays.asList(swipe));
                Thread.sleep(500);
            }
            log.info("Scrolled up {} times", scrollTimes);
        } catch (Exception e) {
            log.error("Failed to scroll up: {}", e.getMessage());
        }
    }


    /**
     * Scroll to make an element visible
     * @param element The element to scroll to
     * @param maxAttempts Maximum scroll attempts
     * @return true if element is found and visible, false otherwise
     */
    public boolean scrollToElement(WebElement element, int maxAttempts) {
        int scrollCount = 0;

        while (scrollCount < maxAttempts) {
            try {
                if (element != null && element.isDisplayed()) {
                    log.info("Element is visible after {} scroll attempts", scrollCount);
                    return true;
                }
            } catch (Exception e) {
                // Element not visible yet, continue scrolling
            }
            scrollDown(1);
            scrollCount++;
        }
        log.warn("Could not find element after {} scroll attempts", maxAttempts);
        return false;
    }


    /**
     * Overloaded method with default 5 attempts
     */
    public boolean scrollToElement(WebElement element) {
        return scrollToElement(element, 5);
    }




    //  ## Saves templates in templates/Screens dir when elements found via XPath)
    /**  independent method
     *   Trigger - manual call
     *   Purpose - Toggle automatic template capture on/off
     *   Directory - N/A
     *   When enabled: Elements found via XPath are automatically saved as templates.
     *   When disabled: No automatic template creation occurs
     */
    public void enableAutoTemplates(boolean enabled) {
        templateManager.setAutoTemplateEnabled(enabled);
    }




    //  **independent method** Clean up auto-captured templates at test end
    public void cleanupAutoTemplates() {
        templateManager.cleanupAutoTemplates();
    }

    
    //  Keep specific templates (don't delete)
    public void keepTemplate(String templatePath) {
        templateManager.keepTemplate(templatePath);
    }

    
    //  Create dummy element for cases where AI performs the action directly
    /**
     * Try visual matching when name-based search fails
     */
    private String tryVisualMatching(String fieldName) {
        try {
            // Take screenshot of current screen
            byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            java.awt.image.BufferedImage currentScreen = javax.imageio.ImageIO.read(
                    new java.io.ByteArrayInputStream(screenshot)
            );

            // Try to find visually similar template with element name filtering
            String matchedTemplate = templateManager.getVisualIndex()
                    .findTemplateByVisualMatch(currentScreen, fieldName);

            if (matchedTemplate != null) {
                log.info("Found template by visual matching for {}: {}", fieldName, matchedTemplate);

                // Update metadata to record this successful match
                TemplateMetadata metadata = TemplateMetadata.load(matchedTemplate);
                if (metadata != null) {
                    metadata.recordUsage(true);
                    // Add current field name as alias for future searches
                    metadata.aliases.add(fieldName.toLowerCase());
                    metadata.save();
                }

                return matchedTemplate;
            }

        } catch (Exception e) {
            log.debug("Visual matching failed for {}: {}", fieldName, e.getMessage());
        }

        return null;
    }

    /**
     * Find templates using multiple strategies including visual similarity
     */
    public List<String> findAllMatchingTemplates(String fieldName) {
        List<String> allTemplates = new java.util.ArrayList<>();

        // Get templates by name
        VisualTemplateIndex visualIndex = templateManager.getVisualIndex();
        allTemplates.addAll(visualIndex.findTemplatesByName(fieldName));

        // Try to find visually similar templates
        String visualMatch = tryVisualMatching(fieldName);
        if (visualMatch != null && !allTemplates.contains(visualMatch)) {
            allTemplates.add(visualMatch);
        }

        return allTemplates;
    }

    private WebElement createDummyElement() {
        // Return a simple element that represents "action completed"
        return new WebElement() {
            @Override
            public void click() { /* Already clicked by AI */ }
            
            @Override
            public void submit() { }
            
            @Override
            public void sendKeys(CharSequence... keysToSend) {
                // Could implement AI-based text input here
                log.warn("sendKeys called on AI-found element - not implemented");
            }
            
            @Override
            public void clear() { }
            
            @Override
            public String getTagName() { return "ai-element"; }
            
            @Override
            public String getAttribute(String name) { return null; }
            
            @Override
            public boolean isSelected() { return false; }
            
            @Override
            public boolean isEnabled() { return true; }
            
            @Override
            public String getText() { return ""; }
            
            @Override
            public java.util.List<WebElement> findElements(org.openqa.selenium.By by) { 
                return java.util.Collections.emptyList(); 
            }
            
            @Override
            public WebElement findElement(org.openqa.selenium.By by) { 
                throw new org.openqa.selenium.NoSuchElementException("Dummy element"); 
            }
            
            @Override
            public boolean isDisplayed() { return true; }
            
            @Override
            public org.openqa.selenium.Point getLocation() { 
                return new org.openqa.selenium.Point(0, 0); 
            }
            
            @Override
            public org.openqa.selenium.Dimension getSize() { 
                return new org.openqa.selenium.Dimension(0, 0); 
            }
            
            @Override
            public org.openqa.selenium.Rectangle getRect() { 
                return new org.openqa.selenium.Rectangle(0, 0, 0, 0); 
            }
            
            @Override
            public String getCssValue(String propertyName) { return ""; }
            
            @Override
            public <X> X getScreenshotAs(org.openqa.selenium.OutputType<X> target) { 
                return null; 
            }
        };
    }

}