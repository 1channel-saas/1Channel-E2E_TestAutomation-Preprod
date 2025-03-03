package com.test.channelplay.stepDefinition;

import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private WebDriver driver;
    //private DriverBase driverBase;


    @Before
    public void driverSetup() {
        //driverBase = new DriverBase();
        //driver = driverBase.initialize(System.getProperty("browser"));
        clearCacheForBrowser(System.getProperty("browser", "chrome"));
        driver = initialize(System.getProperty("browser"));
        logger.info("Driver initialized: {}", driver);
    }

    @After(order = 0)
    public void addDataAndClose(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            addScreenshot(scenario);
        }
        addPageLink(scenario);
    }

    @After(order = 1)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            killChromeDriverProcess();
            logger.info("Closing driver...");
        }
    }
    private void killChromeDriverProcess() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
        } catch (Exception e) {
            logger.warn("Failed to kill chromedriver process: {}", e.getMessage());
        }
    }

    private void addPageLink(io.cucumber.java.Scenario scenario) {
        scenario.log(String.format("Test page: %s", driver.getCurrentUrl()));
        scenario.log(String.format("Test Browser: %s", System.getProperty("browser")));
    }

    private void addScreenshot(io.cucumber.java.Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Screenshot");
    }

    public void clearCacheForBrowser(String browserName) {
        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().clearDriverCache();
                logger.info("ChromeDriver cache cleared.");
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().clearDriverCache();
                logger.info("FirefoxDriver cache cleared.");
                break;
            default:
                logger.info("No cache clearing available for browser: {}", browserName);
        }
    }


}

