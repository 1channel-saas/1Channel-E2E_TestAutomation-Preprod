package com.test.channelplay.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static java.lang.String.format;


public class WebDriverUtils extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger( WebDriverUtils.class);

    public <T> T until(ExpectedCondition<T> condition) {
        WebDriverWait webDriverWait = new WebDriverWait(getDriver(), Duration.ofSeconds(Constants.TIMINGS_EXPLICIT_TIMEOUT));
        long start = System.currentTimeMillis();
        try {
            return webDriverWait.until(condition);
        } finally {
            long end = System.currentTimeMillis();
            logger.debug("Waiting for condition took {} ms.", (end - start));
        }

    }

    public WebElement waitUntilVisible(WebDriver webDriver, WebElement element, Duration wait) {
        try {
            new FluentWait<>(webDriver)
                    .withTimeout(wait)
                    .pollingEvery(Duration.ofMillis(2))
                    .ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
                    .until(driver -> element.isDisplayed());
        } catch (org.openqa.selenium.TimeoutException te) {
            System.out.println(format("Element %s still not visible after %s", element, wait));
        }
        return element;
    }

    public void actionsToMoveToElement(WebDriver driver,WebElement element){
        Actions action =new Actions(driver);
        action.moveToElement(element);
        action.build().perform();
    }

    public void actionToClickElement(WebDriver driver, WebElement element) {
        Actions action =new Actions(driver);
        action.moveToElement(element).click().perform();
    }

    public void selectDropDownByText(WebElement element,String text){
        Select select= new Select(element);
        select.selectByVisibleText(text);
    }

}
