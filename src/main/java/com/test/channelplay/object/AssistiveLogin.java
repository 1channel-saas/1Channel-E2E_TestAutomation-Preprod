package com.test.channelplay.object;

import com.test.channelplay.utils.AuthManager_API;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

public class AssistiveLogin extends DriverBase {
    private static final Logger log = LoggerFactory.getLogger(AssistiveLogin.class);

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement login_username;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement login_password;
    @FindBy(xpath = "//h4[text()=\"Confirm Login\"]")
    WebElement confirmLogin_window;
    @FindBy(xpath = ".//button[text()=' Yes ']")
    WebElement confirmLogin_YES_button;
    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement SignIn;
    @FindBy(xpath = "//span[text()=\" Analytics \"]")
    WebElement Analytics_menu;
    @FindBy(xpath = "//span[text()=\" CRM \"]")
    WebElement CRM_menu;
    @FindBy(xpath = "//span[text()=\" Admin \"]")
    WebElement Admin_menu;
    @FindBy(xpath = "//span[text()=\" Settings \"]")
    WebElement Settings_menu;


    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    String UIAuthToken;
    public AssistiveLogin() {
        PageFactory.initElements(getDriver(), this);
    }

    public void loginToCRM(String uid, String pass) {
        login_username.clear();
        login_username.sendKeys(uid);
        login_password.clear();
        login_password.sendKeys(pass);
        SignIn.click();
        commonUtils.sleep(3000);

    /*    if (confirmLogin_window.isDisplayed()) {
            confirmLogin_YES_button.click();
            commonUtils.sleep(2000);
        }
    */
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(confirmLogin_window));
            confirmLogin_YES_button.click();
            commonUtils.sleep(2000);
        } catch (Exception e) {
            log.info("Confirm Login popup not displayed. Proceeding...");
        }

        webDriverUtils.waitUntilVisible(getDriver(), Analytics_menu, Duration.ofSeconds(2));
        webDriverUtils.waitUntilVisible(getDriver(), CRM_menu, Duration.ofSeconds(2));
        webDriverUtils.waitUntilVisible(getDriver(), Admin_menu, Duration.ofSeconds(2));
        webDriverUtils.waitUntilVisible(getDriver(), Settings_menu, Duration.ofSeconds(2));
        commonUtils.sleep(2000);

        UIAuthToken = commonUtils.getAuthTokenFromUI();
        AuthManager_API.setAuthToken(UIAuthToken);
    }




}
