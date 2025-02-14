package com.test.channelplay.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toCollection;

public class CommonUtils extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);


    public void validatePage(String title) {
        waitForPageToLoad();
        verifyOnCorrectPage(title);
        logger.info("On page titled {}", getDriver().getTitle());
    }

    public void waitForPageToLoad() {
        new WebDriverWait(getDriver(), Duration.ofSeconds(com.test.channelplay.utils.Constants.TIMINGS_EXPLICIT_TIMEOUT))
                .until(driver -> (JavascriptExecutor) driver)
                .executeScript("return document.readyState == 'complete'");
    }

    private void verifyOnCorrectPage(String title) {
        boolean correctPageDisplayed = getDriver().getTitle().equalsIgnoreCase(title);
        if (correctPageDisplayed) {
            return;
        }
        List<String> lines = Stream.of(
                "Failed to create " + getClass().getSimpleName(),
                "Expected title: " + title,
                "Actual title: " + getDriver().getTitle(),
                "Actual url: " + getDriver().getCurrentUrl()
        ).collect(toCollection(ArrayList::new));
        throw new RuntimeException(lines.stream().collect(joining(lineSeparator())));
    }


    public String generateRandomString(int n){
        String alphaString = "abcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index  = (int)(alphaString.length() * Math.random());
            sb.append(alphaString.charAt(index));
        }
        return sb.toString();
    }


    public boolean emailPatternMatches(String email) {
        String regexPattern = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }


    //  extract token from UI, web application local storage
    public String getAuthTokenFromUI() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        // Get all keys in Local Storage
        List<String> keys = (List<String>) js.executeScript("return Object.keys(localStorage);");

        String authTokenKey = null;
        for (String key : keys) {
            if (key.startsWith("authce")) {  // Identify key dynamically
                authTokenKey = key;
                break;
            }
        }

        if (authTokenKey == null) {
            throw new RuntimeException("Authentication token key not found in Local Storage!");
        }

        // Extract token using the dynamically found key
        String authTokenUI = (String) js.executeScript("return localStorage.getItem(arguments[0]);", authTokenKey);

        if (authTokenUI == null || authTokenUI.isEmpty()) {
            throw new RuntimeException("Authentication token not found in Local Storage!");
        }

        logger.info("Extracted Token from UI: {}", authTokenUI);
        return authTokenUI;
    }


    public void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

}
