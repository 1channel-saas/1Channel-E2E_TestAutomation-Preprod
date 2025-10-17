package com.test.channelplay.mobile.config_Helper;

import org.openqa.selenium.WebElement;

/**
 * Result object for calendar operations containing all relevant information
 * about the action performed and its outcome
 */
public class CalendarActionResult {
    private final boolean success;
    private final String selectedDate;
    private final WebElement element;
    private final String errorMessage;
    private final String actionTaken;
    private final String fieldName;
    private final long executionTimeMs;

    public CalendarActionResult(boolean success, String selectedDate, WebElement element,
                               String errorMessage, String actionTaken, String fieldName, long executionTimeMs) {
        this.success = success;
        this.selectedDate = selectedDate;
        this.element = element;
        this.errorMessage = errorMessage;
        this.actionTaken = actionTaken;
        this.fieldName = fieldName;
        this.executionTimeMs = executionTimeMs;
    }

    // Success result factory
    public static CalendarActionResult success(String selectedDate, WebElement element,
                                             String actionTaken, String fieldName, long executionTimeMs) {
        return new CalendarActionResult(true, selectedDate, element, null, actionTaken, fieldName, executionTimeMs);
    }

    // Failure result factory
    public static CalendarActionResult failure(String errorMessage, String fieldName, long executionTimeMs) {
        return new CalendarActionResult(false, null, null, errorMessage, "failed", fieldName, executionTimeMs);
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public WebElement getElement() {
        return element;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public String getFieldName() {
        return fieldName;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    // Validation helper
    public CalendarActionResult validateSuccess() {
        if (!success) {
            throw new RuntimeException("Calendar action failed for " + fieldName + ": " + errorMessage);
        }
        return this;
    }

    @Override
    public String toString() {
        return String.format("CalendarActionResult{success=%s, fieldName='%s', selectedDate='%s', actionTaken='%s', executionTime=%dms, error='%s'}",
                           success, fieldName, selectedDate, actionTaken, executionTimeMs, errorMessage);
    }
}