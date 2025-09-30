package com.test.channelplay.stepDefinition_Mobile;

import com.test.channelplay.utils.MobileTestBase;
import com.test.channelplay.utils.MobileDriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLaunchSteps extends MobileTestBase {
    
    private static final Logger log = LoggerFactory.getLogger(AppLaunchSteps.class);
    
    @Given("I launch the 1Channel CRM application")
    public void iLaunchThe1ChannelCRMApplication() {
        driver = MobileDriverManager.getDriver();
        
        if (driver == null) {
            log.error("Driver is null - app failed to launch");
            Assert.fail("Failed to launch 1Channel CRM app - driver is null");
        }
        
        log.info("1Channel CRM app launch initiated");
        
        // Wait for app to fully load
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Then("the app should launch successfully")
    public void theAppShouldLaunchSuccessfully() {
        Assert.assertNotNull(driver, "Driver should not be null");
        
        try {
            // Check if app is running by getting current package
            if (driver instanceof AndroidDriver) {
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                log.info("Current package: " + currentPackage);
                Assert.assertNotNull(currentPackage, "App should be running");
            }
            
            log.info("1Channel CRM app launched successfully");
        } catch (Exception e) {
            log.error("Error checking app launch: " + e.getMessage());
            Assert.fail("App launch verification failed: " + e.getMessage());
        }
    }
    
    @And("I wait for {int} seconds")
    public void iWaitForSeconds(int seconds) {
        try {
            log.info("Waiting for {} seconds", seconds);
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            log.error("Wait interrupted: " + e.getMessage());
        }
    }
    
    @Then("I should see the app is running")
    public void iShouldSeeTheAppIsRunning() {
        try {
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                
                // Check current package
                String currentPackage = androidDriver.getCurrentPackage();
                log.info("App package running: " + currentPackage);
                
                // Check current activity
                String currentActivity = androidDriver.currentActivity();
                log.info("Current activity: " + currentActivity);
                
                // Verify it's our app
                Assert.assertTrue(
                    currentPackage.contains("onechannelcrm") || 
                    currentPackage.contains("assistive"),
                    "1Channel CRM app should be running"
                );
                
                log.info("App is running successfully");
            }
        } catch (Exception e) {
            log.error("Error checking app status: " + e.getMessage());
            Assert.fail("Failed to verify app is running: " + e.getMessage());
        }
    }
    
    @Then("the app package should be {string}")
    public void theAppPackageShouldBe(String expectedPackage) {
        try {
            if (driver instanceof AndroidDriver) {
                String currentPackage = ((AndroidDriver) driver).getCurrentPackage();
                log.info("Expected package: {}, Actual package: {}", expectedPackage, currentPackage);
                Assert.assertEquals(currentPackage, expectedPackage, 
                    "App package should match expected");
            }
        } catch (Exception e) {
            log.error("Error verifying package: " + e.getMessage());
            Assert.fail("Package verification failed: " + e.getMessage());
        }
    }
    
    @And("the app should be in foreground")
    public void theAppShouldBeInForeground() {
        try {
            if (driver instanceof AndroidDriver) {
                AndroidDriver androidDriver = (AndroidDriver) driver;
                
                // Get app state
                String currentPackage = androidDriver.getCurrentPackage();
                
                // Check if our app is in foreground
                Assert.assertEquals(currentPackage, "com.onechannelcrm.assistive",
                    "1Channel CRM app should be in foreground");
                
                log.info("App is in foreground");
                
                // Optional: Take a screenshot for verification
                // File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                // log.info("Screenshot taken for verification");
            }
        } catch (Exception e) {
            log.error("Error checking foreground status: " + e.getMessage());
            Assert.fail("Foreground check failed: " + e.getMessage());
        }
    }

}