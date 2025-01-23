package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class Assistive_Login extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement login_username;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement login_password;
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
    public Assistive_Login() {
        PageFactory.initElements(getDriver(), this);
    }

    public void loginToCRM(String uid, String pass) {
        login_username.clear();
        login_username.sendKeys(uid);
        login_password.clear();
        login_password.sendKeys(pass);
        SignIn.click();

        webDriverUtils.waitUntilVisible(getDriver(), Analytics_menu, Duration.ofSeconds(60000));
        webDriverUtils.waitUntilVisible(getDriver(), CRM_menu, Duration.ofSeconds(60000));
        webDriverUtils.waitUntilVisible(getDriver(), Admin_menu, Duration.ofSeconds(60000));
        webDriverUtils.waitUntilVisible(getDriver(), Settings_menu, Duration.ofSeconds(60000));
        commonUtils.sleep(3000);
    }
}
