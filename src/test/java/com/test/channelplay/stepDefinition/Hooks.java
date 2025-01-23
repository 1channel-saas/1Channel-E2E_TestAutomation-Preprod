package com.test.channelplay.stepDefinition;

import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class Hooks extends DriverBase {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private WebDriver driver;
    private DriverBase driverBase;

    @Before
    public void driverSetup()
    {
        driverBase = new DriverBase();
        driver = driverBase.initialize(System.getProperty("browser"));
        System.out.println("Getting the Driver information");
    }

    @After(order = 2)
    public void addDataAndClose(io.cucumber.java.Scenario scenario) {
        if (scenario.isFailed() && driver instanceof TakesScreenshot) {
            addScreenshot(scenario);
        }
        addPageLink(scenario);
    }

    @After(order = 1)
    public void tearDown() {

        /*ExtentReports extent = new ExtentReports();
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport/spark.html");
        extent.attachReporter(spark); */

        driver.quit();
    }

    private void addPageLink(io.cucumber.java.Scenario scenario) {
        scenario.log(String.format("Test page: %s", driver.getCurrentUrl()));
        scenario.log(String.format("Test Browser: %s", System.getProperty("browser")));
    }

    private void addScreenshot(io.cucumber.java.Scenario scenario) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Screenshot");
    }
}

