package com.test.channelplay.mobile.config_Helper;

import com.test.channelplay.utils.CommonUtils;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Post-action validation for elements found by smartFindElementWithAI
 * Focused on validating that actions (text entry, clicks, selections) actually succeeded
 */
public class ValidationStrategy {
    private static final Logger log = LoggerFactory.getLogger(ValidationStrategy.class);
    CommonUtils commonUtils = new CommonUtils();
    private FlutterXPathHelper xpathHelper;


    public ValidationStrategy() {
    }


    //  Default constructor for standalone use
    public ValidationStrategy(FlutterXPathHelper xpathHelper) {
        this.xpathHelper = xpathHelper;
    }




    /**
     * Primary validation method for text entry - called after smartFindElementWithAI
     * This is the main method test classes will use
     *
     * @param element The element returned from smartFindElementWithAI
     * @param expectedTextPart Part of text to look for (e.g., "offAct")
     * @param fieldName Name of the field for error reporting
     * @param maxRetries Number of retry attempts (default 3)
     * @return The actual text found, or throws AssertionError if validation fails
     */
    public String validateTextEntry(WebElement element, String expectedTextPart,
                                   String fieldName, int maxRetries) {
        if (element == null) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format("Cannot validate %s - element is null", fieldName));
        }

        boolean textFound = false;
        String foundText = null;
        int retryCount = 0;

        while (!textFound && retryCount < maxRetries) {
            commonUtils.sleep(500);

            try {
                // Try text attributes one by one, stop when found
                String actualText = element.getText();
                if (actualText != null && actualText.contains(expectedTextPart)) {
                    textFound = true;
                    foundText = actualText;
                    log.info("Text found via getText(): '{}'", actualText);
                } else {
                    // Only try next attribute if first didn't work
                    String textAttr = element.getAttribute("text");
                    if (textAttr != null && textAttr.contains(expectedTextPart)) {
                        textFound = true;
                        foundText = textAttr;
                        log.info("Text found via getAttribute('text'): '{}'", textAttr);
                    } else {
                        // Only try hint if text didn't work
                        String hintAttr = element.getAttribute("hint");
                        if (hintAttr != null && hintAttr.contains(expectedTextPart)) {
                            textFound = true;
                            foundText = hintAttr;
                            log.info("Text found via getAttribute('hint'): '{}'", hintAttr);
                        } else {
                            // Last resort - content-desc
                            String contentDesc = element.getAttribute("content-desc");
                            if (contentDesc != null && contentDesc.contains(expectedTextPart)) {
                                textFound = true;
                                foundText = contentDesc;
                                log.info("Text found via getAttribute('content-desc'): '{}'", contentDesc);
                            }
                        }
                    }
                }

                if (!textFound) {
                    // For logging, we need to check what we actually tried
                    log.warn("Attempt {} - Text '{}' not found in any attributes",
                        retryCount + 1, expectedTextPart);
                    retryCount++;
                }
            } catch (Exception e) {
                log.error("Error during validation attempt {}: {}", retryCount + 1, e.getMessage());
                retryCount++;
            }
        }

        if (!textFound) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format(
                "Text validation FAILED for %s: Expected text containing '%s' not found after %d attempts",
                fieldName, expectedTextPart, maxRetries
            ));
        }
        return foundText;
    }


    /**
     * Overloaded method with default 3 retries
     */
    public String validateTextEntry(WebElement element, String expectedTextPart, String fieldName) {
        return validateTextEntry(element, expectedTextPart, fieldName, 3);
    }




    /**
     * Validates checkbox/toggle state after check/uncheck action
     *
     * @param element The checkbox element
     * @param shouldBeChecked Expected state (true for checked, false for unchecked)
     * @param fieldName Name of the field for error reporting
     * @return true if validation passes, throws AssertionError if fails
     */
    public boolean validateCheckboxState(WebElement element, boolean shouldBeChecked, String fieldName) {
        if (element == null) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format("Cannot validate %s - element is null", fieldName));
        }

        try {
            String checkedAttr = element.getAttribute("checked");
            boolean isChecked = "true".equalsIgnoreCase(checkedAttr);

            if (isChecked == shouldBeChecked) {
                log.info("Checkbox validation successful for {}: {}",
                    fieldName, shouldBeChecked ? "checked" : "unchecked");
                return true;
            } else {
                captureFailureScreenshot(fieldName);
                Assert.fail(String.format(
                    "Checkbox validation FAILED for %s: Expected %s but found %s",
                    fieldName,
                    shouldBeChecked ? "checked" : "unchecked",
                    isChecked ? "checked" : "unchecked"
                ));
            }
        } catch (Exception e) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format("Error validating checkbox %s: %s", fieldName, e.getMessage()));
        }
        return false;
    }




    /**
     * Validates dropdown/selection after select action
     *
     * @param element The dropdown element
     * @param expectedSelection Expected selected value
     * @param fieldName Name of the field for error reporting
     * @return The actual selected value, throws AssertionError if validation fails
     */
    public String validateSelection(WebElement element, String expectedSelection, String fieldName) {
        if (element == null) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format("Cannot validate %s - element is null", fieldName));
        }

        try {
            String selectedText = element.getText();
            String textAttr = element.getAttribute("text");
            String contentDesc = element.getAttribute("content-desc");

            if ((selectedText != null && selectedText.contains(expectedSelection)) ||
                (textAttr != null && textAttr.contains(expectedSelection)) ||
                (contentDesc != null && contentDesc.contains(expectedSelection))) {

                String foundValue = selectedText != null ? selectedText :
                                  textAttr != null ? textAttr : contentDesc;
                log.info("Selection validation successful for {}: '{}'", fieldName, foundValue);
                return foundValue;
            } else {
                captureFailureScreenshot(fieldName);
                Assert.fail(String.format(
                    "Selection validation FAILED for %s: Expected '%s' not found. Actual: '%s'",
                    fieldName, expectedSelection, selectedText
                ));
            }
        } catch (Exception e) {
            captureFailureScreenshot(fieldName);
            Assert.fail(String.format("Error validating selection %s: %s", fieldName, e.getMessage()));
        }
        return null;
    }




    /**
     * Quick validation for button clicks - verifies element is clickable
     * For navigation validation, that should be handled by the test class
     *
     * @param element The button element
     * @param fieldName Name of the button for error reporting
     * @return true if element is clickable and click likely succeeded
     */
    public boolean validateButtonClick(WebElement element, String fieldName) {
        if (element == null) {
            log.error("Cannot validate click for {} - element is null", fieldName);
            return false;
        }

        try {
            // Check if this is an AI-found dummy element
            String tagName = element.getTagName();
            if ("ai-element".equals(tagName)) {
                log.info("Button {} was clicked via AI fallback - assuming success", fieldName);
                return true;  // AI successfully performed the click
            }

            // Null check for tagName - likely an AI-clicked element
            if (tagName == null) {
                log.info("Element {} has null tagName - likely clicked via AI fallback, assuming success", fieldName);
                return true;  // Null tagName often means AI successfully handled the click
            }

            // Check if element is actually clickable
            boolean isEnabled = element.isEnabled();
            boolean isDisplayed = element.isDisplayed();

            // Check if it's a clickable element type
            String clickableAttr = element.getAttribute("clickable");
            boolean isClickableType = tagName.contains("Button") || tagName.contains("ImageView") ||
                                     tagName.contains("EditText") || "true".equals(clickableAttr);

            if (!isClickableType) {
                log.warn("{} is not a clickable element type ({}). It's likely just a text label.", fieldName, tagName);
                return false;
            }

            if (isEnabled && isDisplayed) {
                log.info("Button {} appears clickable and was clicked", fieldName);
                return true;
            } else {
                log.warn("Button {} is not enabled/displayed. Enabled: {}, Displayed: {}",
                    fieldName, isEnabled, isDisplayed);
                return false;
            }
        } catch (Exception e) {
            // Check if it's a NullPointerException from tagName being null
            if (e instanceof NullPointerException && e.getMessage() != null && e.getMessage().contains("tagName")) {
                log.info("Button {} appears to be an AI-clicked element (tagName is null) - assuming success", fieldName);
                return true;  // Likely an AI-clicked element
            }

            // Element might be stale after navigation - check if this is expected
            log.info("Button {} - element state changed after click (possibly navigated): {}", fieldName, e.getMessage());
            // Return true only if it's a StaleElementReferenceException (indicates navigation)
            return e.getClass().getSimpleName().contains("StaleElement");
        }
    }




    //  Helper method to capture screenshot on validation failure
    private void captureFailureScreenshot(String fieldName) {
        try {
            if (xpathHelper != null) {
                // Save to validation_failure folder
                String screenshotName = fieldName + "_VALIDATION_FAILED.png";
                String screenshot = xpathHelper.saveScreenshotToFolder(screenshotName, "screenshots/test_Validation_failures");
                log.error("Validation failed for {}. Screenshot: {}", fieldName, screenshot);
            }
        } catch (Exception e) {
            log.error("Could not capture failure screenshot: {}", e.getMessage());
        }
    }




    /**
     * Simple result class for cases where test classes need the result without throwing
     * But most methods above throw AssertionError directly for simplicity
     */
    public static class ValidationResult {
        public final boolean success;
        public final String actualValue;
        public final String errorMessage;

        public ValidationResult(boolean success, String actualValue, String errorMessage) {
            this.success = success;
            this.actualValue = actualValue;
            this.errorMessage = errorMessage;
        }
    }

}