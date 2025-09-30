package com.test.channelplay.mobile;

import com.test.channelplay.mobile.screens.config_Helper.FlutterXPathHelper;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.MobileTestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;

public class CRMAppLoginObject extends MobileTestBase {
    private static final Logger log = LoggerFactory.getLogger(CRMAppLoginObject.class);


    @FindBy(xpath = "//android.widget.ScrollView/android.view.View/android.widget.EditText[1]")
    WebElement appLogin_username;
    @FindBy(xpath = "//android.widget.ScrollView/android.view.View/android.widget.EditText[2]")
    WebElement appLlogin_password;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Log In\"]")
    WebElement appLogIn;
    @FindBy(xpath = "//android.view.View[@content-desc=\"You are already logged into another device. If you login in this device, you will be logged out from the other device. Do you want to continue?\"]")
    WebElement confirmLogin_AlertText;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"YES\"]")
    WebElement confirmLogin_YES_button;
    @FindBy(xpath = "//android.view.View[contains(@content-desc, \"4 Digit PIN\")]")
    WebElement pinWindowHeader;
    @FindBy(xpath = "(//android.widget.EditText[@password='true'])[1]")
    WebElement enterPinField;
    @FindBy(xpath = "(//android.widget.EditText[@password='true'])[2]")
    WebElement confirmPinField;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"Save\"]")
    WebElement pinSaveButton;
    @FindBy(xpath = "//android.view.View[contains(@content-desc, \"Syncing data\")]")
    WebElement syncingData;
    @FindBy(xpath = "//android.view.View//android.widget.ImageView[2]")
    WebElement hamburgerMenuIcon;
    @FindBy(className = "android.widget.Button")
    WebElement notification_button;




    CommonUtils commonUtils = new CommonUtils();
    WebDriverWait wait;
    FlutterXPathHelper xpathHelper;


    private boolean initialized = false;
    private void setupPageElements() {
        if (!initialized && getDriver() != null) {
            driver = getDriver();
            PageFactory.initElements(driver, this);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            xpathHelper = new FlutterXPathHelper(driver);
            initialized = true;
            log.debug("setupPageElements initialized with driver");
        }
    }


    public void loginToMobile(String uid, String pass) {
        setupPageElements();
        System.out.println("CRM App login initiated");

        try {
            System.out.println("Waiting for app to load completely...");
            commonUtils.sleep(5000);

            System.out.println("Waiting for login elements to be available...");
            wait.until(ExpectedConditions.visibilityOf(appLogin_username));
            wait.until(ExpectedConditions.elementToBeClickable(appLogin_username));

            System.out.println("At login screen, attempting to interact...");
            appLogin_username.click();
            appLogin_username.clear();
            appLogin_username.sendKeys(uid);
            commonUtils.sleep(1000);

            wait.until(ExpectedConditions.elementToBeClickable(appLlogin_password));
            appLlogin_password.click();
            appLlogin_password.clear();
            appLlogin_password.sendKeys(pass);
            commonUtils.sleep(1000);

            //  Wait for login button
            if (!appLogIn.isDisplayed()) {
                hideKeyboard();
                wait.until(ExpectedConditions.elementToBeClickable(appLogIn));
            }

            appLogIn.click();
            commonUtils.sleep(1000);

            //  Wait for sync data to appear
            wait.until(ExpectedConditions.visibilityOf(syncingData));
            Assert.assertTrue(syncingData.isDisplayed());
            System.out.println("Login done...Syncing data displayed");

        } catch (Exception e) {
            System.out.println("Login failed with standard approach: " + e.getMessage());

            //  Try with enhanced debugging
            debugLoginScreen();

            throw new RuntimeException("Login failed after debugging: " + e.getMessage());
        }
    }


    //  Debug method to understand current screen state
    private void debugLoginScreen() {
        System.out.println("=== LOGIN SCREEN DEBUGGING ===");

        // Take screenshot for debugging
        xpathHelper.saveScreenshotForTemplate("login_debug_screen.png");

        // Try to get page source
        String pageSource = driver.getPageSource();
        System.out.println("Current page contains 'EditText': " + pageSource.contains("EditText"));
        System.out.println("Current page contains 'ScrollView': " + pageSource.contains("ScrollView"));
        System.out.println("Current page contains 'Button': " + pageSource.contains("Button"));
    }


    public void verifyOnLandingPage(String projectNameEle, String firstNameEle) {
        setupPageElements();

        WebElement landingPageHeader = driver.findElement(By.xpath("//android.view.View[@content-desc='" + projectNameEle +"']"));

        wait.until(ExpectedConditions.elementToBeClickable(landingPageHeader));
        Assert.assertTrue(landingPageHeader.isDisplayed());
        commonUtils.sleep(1000);

        hamburgerMenuIcon.click();

        WebElement firstNameUser = driver.findElement(By.xpath("//android.view.View[@content-desc='Hi, " + firstNameEle + "!']"));
        wait.until(ExpectedConditions.elementToBeClickable(firstNameUser));
        Assert.assertTrue(firstNameUser.isDisplayed());
        commonUtils.sleep(1000);
        System.out.println("landing page verification passed");

    }

    public void closeHamburgerMenu() {
        setupPageElements();
        xpathHelper.closeHamburgerMenu();
        wait.until(ExpectedConditions.elementToBeClickable(notification_button));
        Assert.assertTrue(notification_button.isDisplayed());
    }

    /*    if (confirmLogin_window.isDisplayed()) {
            confirmLogin_YES_button.click();
            commonUtils.sleep(2000);
        }
    */
        /*try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(confirmLogin_window));
            confirmLogin_YES_button.click();
        } catch (Exception e) {
            log.info("Confirm Login popup not displayed. Proceeding...");
        }*/


}
