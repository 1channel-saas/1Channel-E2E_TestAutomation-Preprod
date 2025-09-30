package com.test.channelplay.stepDefinition;

import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.test.channelplay.utils.ScreenshotHelper;
import com.test.channelplay.utils.WebTestFlowScreenshotManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Hooks extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private WebDriver driver;
    //private DriverBase driverBase;


    @Before(value = "@web")
    public void driverSetup() {
        //driverBase = new DriverBase();
        //driver = driverBase.initialize(System.getProperty("browser"));
        try {
            clearCacheForBrowser(System.getProperty("browser", "chrome"));
            driver = initialize(System.getProperty("browser"));
            logger.info("Driver initialized: {}", driver);
        } catch (Exception e) {
            logger.error("Failed to initialize Web driver: {}", e.getMessage());
            throw new RuntimeException("Web driver initialization failed, NullPointerException", e);
        }
    }


    @After(order = 1)
    public void addDataAndClose(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            addScreenshot(scenario);
        }
        addPageLink(scenario);
    }


    @After(order = 0)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            killChromeDriverProcess();
            driver = null;
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
        if (driver != null) {
            scenario.log(String.format("Test page: %s", driver.getCurrentUrl()));
            scenario.log(String.format("Test Browser: %s", System.getProperty("browser")));
        }
    }

    private void addScreenshot(io.cucumber.java.Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Screenshot");
    }

    public void clearCacheForBrowser(String browserName) {
        String cachePath = System.getProperty("user.home") + "/.cache/selenium";
        File cacheDir = new File(cachePath);

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().clearDriverCache();
                logger.info("clearing ChromeDriver cache...");
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().clearDriverCache();
                logger.info("clearing GeckoDriver cache...");
                break;
            default:
                logger.info("No cache clearing available for browser: {}", browserName);
        }
        //  Verify if cache was actually deleted
        if (!cacheDir.exists() || cacheDir.list().length == 0) {
            logger.info("Cache successfully deleted for browser: {}", browserName);
        } else {
            logger.warn("Cache directory still exists after clearing: {}", cachePath);
        }
    }


}

