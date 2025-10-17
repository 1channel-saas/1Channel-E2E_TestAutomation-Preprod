package com.test.channelplay.mobile.config_Helper;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generic helper class for dropdown operations in mobile automation
 * Provides reusable methods for any dropdown interaction
 */
public class BaseDropdownHelper {

    private static final Logger log = LoggerFactory.getLogger(BaseDropdownHelper.class);
    private final AppiumDriver driver;
    private final String dropdownName;

    /**
     * Constructor for BaseDropdownHelper
     * @param driver AppiumDriver instance
     * @param dropdownName Name of the dropdown for logging purposes
     */
    public BaseDropdownHelper(AppiumDriver driver, String dropdownName) {
        this.driver = driver;
        this.dropdownName = dropdownName;
    }


    /**
     * Get all options from a dropdown list
     * @param dropdownListXPath XPath of the dropdown list elements
     * @return List of WebElements representing dropdown options
     */
    public List<WebElement> getDropdownList(String dropdownListXPath) {
        log.debug("Getting dropdown list for '{}' using XPath: {}", dropdownName, dropdownListXPath);
        List<WebElement> options = driver.findElements(By.xpath(dropdownListXPath));
        log.info("Found {} options in '{}' dropdown", options.size(), dropdownName);
        return options;
    }


    /**
     * Select an option from dropdown list
     * @param optionsList List of dropdown options
     * @param optionValue Value to select (if null/empty, selects first option)
     */
    public void selectFromDropdown(List<WebElement> optionsList, String optionValue) {
        if (optionsList.isEmpty()) {
            log.warn("No options found in '{}' dropdown", dropdownName);
            return;
        }

        if (isEmptyValue(optionValue)) {
            clickFirstOption(optionsList);
        } else {
            selectSpecificOption(optionsList, optionValue);
        }
    }


    /**
     * Check if a value is empty or null
     * @param value Value to check
     * @return true if value is null or empty
     */
    public boolean isEmptyValue(String value) {
        return value == null || value.trim().isEmpty();
    }


    /**
     * Click the first option in the list
     * @param optionsList List of dropdown options
     */
    public void clickFirstOption(List<WebElement> optionsList) {
        try {
            WebElement firstOption = optionsList.get(0);
            firstOption.click();
            String optionText = getElementText(firstOption);
            log.info("Selected first option in '{}' dropdown: {}",
                dropdownName, optionText != null ? optionText : "Option 1");
        } catch (Exception e) {
            log.error("Failed to click first option in '{}' dropdown", dropdownName, e);
        }
    }


    /**
     * Select a specific option from the list
     * @param optionsList List of dropdown options
     * @param optionValue Value to select
     */
    public void selectSpecificOption(List<WebElement> optionsList, String optionValue) {
        WebElement targetOption = findOptionByText(optionsList, optionValue);

        if (targetOption != null) {
            clickOption(targetOption, optionValue);
        } else {
            handleOptionNotFound(optionsList, optionValue);
        }
    }


    /**
     * Find an option by its text content
     * @param optionsList List of dropdown options
     * @param searchText Text to search for
     * @return WebElement if found, null otherwise
     */
    public WebElement findOptionByText(List<WebElement> optionsList, String searchText) {
        log.debug("Searching for '{}' in '{}' dropdown", searchText, dropdownName);

        for (WebElement option : optionsList) {
            String optionText = getElementText(option);
            if (optionText != null && optionText.contains(searchText)) {
                log.debug("Found matching option: {}", optionText);
                return option;
            }
        }

        log.debug("Option '{}' not found in '{}' dropdown", searchText, dropdownName);
        return null;
    }


    /**
     * Click on a specific option
     * @param option WebElement to click
     * @param optionText Text of the option for logging
     */
    public void clickOption(WebElement option, String optionText) {
        try {
            option.click();
            log.info("Selected '{}' from '{}' dropdown", optionText, dropdownName);
        } catch (Exception e) {
            log.error("Failed to click '{}' in '{}' dropdown", optionText, dropdownName, e);
        }
    }


    /**
     * Handle case when option is not found
     * @param optionsList List of dropdown options
     * @param searchValue Value that was searched for
     */
    public void handleOptionNotFound(List<WebElement> optionsList, String searchValue) {
        log.warn("Option '{}' not found in '{}' dropdown. Total available options: {}",
            searchValue, dropdownName, optionsList.size());

        // Log available options for debugging
        if (log.isDebugEnabled()) {
            logAvailableOptions(optionsList);
        }

        // Select first option as fallback
        log.info("Selecting first option as fallback for '{}' dropdown", dropdownName);
        clickFirstOption(optionsList);
    }


    /**
     * Get text from an element (tries content-desc first, then text)
     * @param element WebElement to get text from
     * @return Text content or null if not found
     */
    public String getElementText(WebElement element) {
        try {
            String text = element.getAttribute("content-desc");
            if (text == null || text.isEmpty()) {
                text = element.getText();
            }
            return text;
        } catch (Exception e) {
            log.debug("Could not get text from element in '{}' dropdown", dropdownName, e);
            return null;
        }
    }


    /**
     * Find option by index
     * @param optionsList List of dropdown options
     * @param index Index of the option (0-based)
     * @return WebElement at the specified index
     */
    public WebElement getOptionByIndex(List<WebElement> optionsList, int index) {
        if (index >= 0 && index < optionsList.size()) {
            log.debug("Getting option at index {} from '{}' dropdown", index, dropdownName);
            return optionsList.get(index);
        }
        log.warn("Invalid index {} for '{}' dropdown with {} options",
            index, dropdownName, optionsList.size());
        return null;
    }


    /**
     * Select option by index
     * @param optionsList List of dropdown options
     * @param index Index of the option to select (0-based)
     */
    public void selectByIndex(List<WebElement> optionsList, int index) {
        WebElement option = getOptionByIndex(optionsList, index);
        if (option != null) {
            String optionText = getElementText(option);
            clickOption(option, optionText != null ? optionText : "Option at index " + index);
        }
    }


    /**
     * Check if dropdown has specific option
     * @param optionsList List of dropdown options
     * @param searchText Text to search for
     * @return true if option exists
     */
    public boolean hasOption(List<WebElement> optionsList, String searchText) {
        return findOptionByText(optionsList, searchText) != null;
    }


    /**
     * Get count of options in dropdown
     * @param optionsList List of dropdown options
     * @return Number of options
     */
    public int getOptionsCount(List<WebElement> optionsList) {
        int count = optionsList.size();
        log.debug("'{}' dropdown has {} options", dropdownName, count);
        return count;
    }


    /**
     * Log all available options for debugging
     * @param optionsList List of dropdown options
     */
    private void logAvailableOptions(List<WebElement> optionsList) {
        log.debug("Available options in '{}' dropdown:", dropdownName);
        for (int i = 0; i < optionsList.size(); i++) {
            String optionText = getElementText(optionsList.get(i));
            log.debug("  Option {}: {}", i + 1, optionText != null ? optionText : "Unknown");
        }
    }


    /**
     * Wait and retry getting dropdown list if empty
     * @param dropdownListXPath XPath of the dropdown list
     * @param maxRetries Maximum number of retries
     * @param waitMillis Wait time between retries in milliseconds
     * @return List of WebElements
     */
    public List<WebElement> getDropdownListWithRetry(String dropdownListXPath, int maxRetries, long waitMillis) {
        List<WebElement> options = null;
        int attempts = 0;

        while (attempts < maxRetries) {
            options = getDropdownList(dropdownListXPath);
            if (!options.isEmpty()) {
                break;
            }

            attempts++;
            log.debug("Retry {} of {} for '{}' dropdown", attempts, maxRetries, dropdownName);

            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        return options;
    }


    /**
     * Select multiple options from dropdown (for multi-select dropdowns)
     * @param optionsList List of dropdown options
     * @param valuesToSelect Array of values to select
     */
    public void selectMultipleOptions(List<WebElement> optionsList, String[] valuesToSelect) {
        log.info("Selecting {} options from '{}' dropdown", valuesToSelect.length, dropdownName);

        for (String value : valuesToSelect) {
            WebElement option = findOptionByText(optionsList, value);
            if (option != null) {
                clickOption(option, value);
            } else {
                log.warn("Could not find '{}' in '{}' dropdown", value, dropdownName);
            }
        }
    }


    /**
     * Clear selection in dropdown (if applicable)
     * @param clearButtonXPath XPath of clear button
     */
    public void clearSelection(String clearButtonXPath) {
        try {
            WebElement clearButton = driver.findElement(By.xpath(clearButtonXPath));
            clearButton.click();
            log.info("Cleared selection in '{}' dropdown", dropdownName);
        } catch (Exception e) {
            log.debug("Could not clear '{}' dropdown selection", dropdownName, e);
        }
    }

}