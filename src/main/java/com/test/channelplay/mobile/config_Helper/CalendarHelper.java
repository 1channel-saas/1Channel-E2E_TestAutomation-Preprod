package com.test.channelplay.mobile.config_Helper;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive calendar-specific utility for handling all calendar operations
 * with built-in validation, logging, and multiple fallback strategies
 */
public class CalendarHelper {

    private static final Logger log = LoggerFactory.getLogger(CalendarHelper.class);
    private final WebDriver driver;
    private final ValidationStrategy validator;
    private final FlutterXPathHelper xpathHelper;

    public CalendarHelper(WebDriver driver, ValidationStrategy validator, FlutterXPathHelper xpathHelper) {
        this.driver = driver;
        this.validator = validator;
        this.xpathHelper = xpathHelper;
    }

    /**
     * Select today's date in a calendar
     */
    public CalendarActionResult selectTodayDate(String fieldName) {
        log.info("Starting calendar operation: selectTodayDate for field '{}'", fieldName);
        long startTime = System.currentTimeMillis();

        try {
            return selectDateByDescription("Today", fieldName + "_today", startTime);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Failed to select today's date for field '{}': {}", fieldName, e.getMessage());
            xpathHelper.saveScreenshotToFolder(fieldName + "_today_date_error.png", "screenshots/calendar_errors");
            return CalendarActionResult.failure("Failed to select today's date: " + e.getMessage(), fieldName, executionTime);
        }
    }

    /**
     * Select a date by searching for specific text in content-desc
     */
    public CalendarActionResult selectDateByDescription(String descriptionContains, String fieldName) {
        return selectDateByDescription(descriptionContains, fieldName, System.currentTimeMillis());
    }

    private CalendarActionResult selectDateByDescription(String descriptionContains, String fieldName, long startTime) {
        log.info("Selecting calendar date containing '{}' for field '{}'", descriptionContains, fieldName);

        try {
            AndroidDriver androidDriver = (AndroidDriver) driver;

            // Build UiSelector query
            String uiSelectorQuery = String.format(
                "new UiSelector().className(\"android.widget.Button\").descriptionContains(\"%s\")",
                descriptionContains
            );

            log.debug("Using UiSelector query: {}", uiSelectorQuery);

            // Find element using UiSelector
            WebElement dateElement = androidDriver.findElement(AppiumBy.androidUIAutomator(uiSelectorQuery));

            if (dateElement != null) {
                log.info("Found calendar date element for '{}' in field '{}'", descriptionContains, fieldName);

                // Extract date before clicking (in case click changes the element)
                String selectedDate = extractSelectedDate(dateElement, fieldName);

                // Perform multi-strategy click
                String actionTaken = performMultiStrategyClick(dateElement, fieldName);

                // Validate the click was successful
                boolean clickValidated = validator.validateButtonClick(dateElement, fieldName);

                long executionTime = System.currentTimeMillis() - startTime;

                if (clickValidated || actionTaken.contains("coordinate") || actionTaken.contains("elementId")) {
                    log.info("Successfully selected calendar date '{}' using '{}' for field '{}' in {}ms",
                           selectedDate, actionTaken, fieldName, executionTime);
                    return CalendarActionResult.success(selectedDate, dateElement, actionTaken, fieldName, executionTime);
                } else {
                    log.warn("Click validation failed for calendar date '{}' in field '{}'", selectedDate, fieldName);
                    xpathHelper.saveScreenshotToFolder(fieldName + "_click_validation_failed.png", "screenshots/calendar_errors");
                    return CalendarActionResult.failure("Click validation failed", fieldName, executionTime);
                }
            } else {
                long executionTime = System.currentTimeMillis() - startTime;
                log.warn("Calendar date element not found for '{}' in field '{}'", descriptionContains, fieldName);
                return CalendarActionResult.failure("Element not found", fieldName, executionTime);
            }

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Error selecting calendar date '{}' for field '{}': {}", descriptionContains, fieldName, e.getMessage());
            xpathHelper.saveScreenshotToFolder(fieldName + "_selection_error.png", "screenshots/calendar_errors");
            return CalendarActionResult.failure("Selection error: " + e.getMessage(), fieldName, executionTime);
        }
    }

    /**
     * Select a specific date by day of month
     */
    public CalendarActionResult selectDateByValue(int dayOfMonth, String fieldName) {
        log.info("Selecting calendar date with day '{}' for field '{}'", dayOfMonth, fieldName);
        long startTime = System.currentTimeMillis();

        try {
            // Try multiple patterns for day matching
            String[] dayPatterns = {
                String.valueOf(dayOfMonth) + ",",  // "22,"
                String.valueOf(dayOfMonth) + " ",  // "22 "
                String.valueOf(dayOfMonth)         // Just "22"
            };

            for (String pattern : dayPatterns) {
                try {
                    CalendarActionResult result = selectDateByDescription(pattern, fieldName + "_day_" + dayOfMonth, startTime);
                    if (result.isSuccess()) {
                        return result;
                    }
                } catch (Exception e) {
                    log.debug("Pattern '{}' failed for day {}: {}", pattern, dayOfMonth, e.getMessage());
                }
            }

            long executionTime = System.currentTimeMillis() - startTime;
            log.warn("Could not find calendar date with day '{}' using any pattern for field '{}'", dayOfMonth, fieldName);
            return CalendarActionResult.failure("Day " + dayOfMonth + " not found with any pattern", fieldName, executionTime);

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Error selecting calendar date by day '{}' for field '{}': {}", dayOfMonth, fieldName, e.getMessage());
            return CalendarActionResult.failure("Day selection error: " + e.getMessage(), fieldName, executionTime);
        }
    }

    /**
     * Select a specific date
     */
    public CalendarActionResult selectSpecificDate(LocalDate targetDate, String fieldName) {
        log.info("Selecting specific calendar date '{}' for field '{}'", targetDate, fieldName);

        // Try by day of month first
        CalendarActionResult result = selectDateByValue(targetDate.getDayOfMonth(), fieldName);

        if (!result.isSuccess()) {
            // Fallback to formatted date search if day search fails
            String formattedDate = targetDate.toString();
            result = selectDateByDescription(formattedDate, fieldName);
        }

        return result;
    }

    /**
     * Perform multi-strategy click with detailed logging
     */
    private String performMultiStrategyClick(WebElement element, String fieldName) {
        AndroidDriver androidDriver = (AndroidDriver) driver;

        // Strategy 1: Click with element ID using mobile gesture
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("elementId", ((RemoteWebElement) element).getId());

            androidDriver.executeScript("mobile: clickGesture", args);
            log.debug("Successfully clicked calendar element '{}' using elementId strategy", fieldName);
            return "elementId_click";

        } catch (Exception e) {
            log.debug("ElementId click failed for '{}': {}", fieldName, e.getMessage());
        }

        // Strategy 2: Click with coordinates using mobile gesture
        try {
            Rectangle rect = element.getRect();
            int x = rect.x + rect.width / 2;
            int y = rect.y + rect.height / 2;

            Map<String, Object> args = new HashMap<>();
            args.put("x", x);
            args.put("y", y);

            androidDriver.executeScript("mobile: clickGesture", args);
            log.debug("Successfully clicked calendar element '{}' using coordinate strategy at ({}, {})", fieldName, x, y);
            return "coordinate_click";

        } catch (Exception e) {
            log.debug("Coordinate click failed for '{}': {}", fieldName, e.getMessage());
        }

        // Strategy 3: Regular WebElement click
        try {
            element.click();
            log.debug("Successfully clicked calendar element '{}' using regular click strategy", fieldName);
            return "regular_click";

        } catch (Exception e) {
            log.warn("All click strategies failed for calendar element '{}': {}", fieldName, e.getMessage());
            throw new RuntimeException("All click strategies failed: " + e.getMessage());
        }
    }

    /**
     * Extract the selected date from the element with smart attribute detection
     */
    public String extractSelectedDate(WebElement element, String fieldName) {
        log.debug("Extracting selected date from calendar element '{}'", fieldName);

        try {
            // Priority order for attribute extraction
            String[] attributes = {"content-desc", "text", "name"};

            for (String attr : attributes) {
                try {
                    String value = element.getAttribute(attr);
                    if (value != null && !value.trim().isEmpty()) {
                        log.debug("Extracted date '{}' from attribute '{}' for field '{}'", value, attr, fieldName);
                        return value;
                    }
                } catch (Exception e) {
                    log.debug("Failed to get attribute '{}' for field '{}': {}", attr, fieldName, e.getMessage());
                }
            }

            // Fallback to current date
            String fallbackDate = LocalDate.now().toString();
            log.warn("Could not extract date from element '{}', using fallback: {}", fieldName, fallbackDate);
            return fallbackDate;

        } catch (Exception e) {
            log.error("Error extracting date from calendar element '{}': {}", fieldName, e.getMessage());
            return LocalDate.now().toString();
        }
    }

    /**
     * Validate that a calendar is open and ready for interaction
     */
    public boolean validateCalendarOpened(String fieldName) {
        log.debug("Validating calendar is open for field '{}'", fieldName);

        try {
            // Look for common calendar indicators
            AndroidDriver androidDriver = (AndroidDriver) driver;

            // Try to find calendar-specific elements
            String[] calendarIndicators = {
                "new UiSelector().className(\"android.widget.Button\").descriptionMatches(\".*[0-9]+.*\")",  // Date buttons
                "new UiSelector().textContains(\"OK\")",  // OK button
                "new UiSelector().textContains(\"Cancel\")"  // Cancel button
            };

            for (String indicator : calendarIndicators) {
                try {
                    WebElement calendarElement = androidDriver.findElement(AppiumBy.androidUIAutomator(indicator));
                    if (calendarElement != null && calendarElement.isDisplayed()) {
                        log.debug("Calendar validation successful for field '{}' using indicator", fieldName);
                        return true;
                    }
                } catch (Exception e) {
                    // Try next indicator
                }
            }

            log.warn("Calendar validation failed for field '{}' - no calendar indicators found", fieldName);
            return false;

        } catch (Exception e) {
            log.error("Error validating calendar for field '{}': {}", fieldName, e.getMessage());
            return false;
        }
    }

    /**
     * Get all available calendar dates for debugging/validation
     */
    public List<WebElement> getAllCalendarDates(String fieldName) {
        log.debug("Getting all calendar dates for field '{}'", fieldName);

        try {
            AndroidDriver androidDriver = (AndroidDriver) driver;

            // Find all buttons that look like calendar dates
            List<WebElement> dateButtons = androidDriver.findElements(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\")")
            );

            log.debug("Found {} potential calendar date buttons for field '{}'", dateButtons.size(), fieldName);
            return dateButtons;

        } catch (Exception e) {
            log.error("Error getting calendar dates for field '{}': {}", fieldName, e.getMessage());
            return List.of();
        }
    }
}