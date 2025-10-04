package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.CRMPortalLoginObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class CommonStepDefinitions_UI extends DriverBase {

    @FindBy(xpath = "//span[text()='Hi, ']/parent::div")
    WebElement userProfile;
    @FindBy(xpath = "//a[text()=' Sign Out ']")
    WebElement signOut;
    @FindBy(xpath = "//h3[text()='Sign In']")
    WebElement signInText_loginpage;
    @FindBy(xpath = "//h4[text()=\"Confirm Login\"]")
    WebElement confirmLoginWindow;
    @FindBy(xpath = ".//button[text()=' Yes ']")
    WebElement confirmLogin_YES_button;


    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    CRMPortalLoginObject login = new CRMPortalLoginObject();

    public CommonStepDefinitions_UI() {
        PageFactory.initElements(getDriver(), this);
    }


    //  gherkin: User launches 1Channel CRM
    @Given("User launches 1Channel CRM portal")
    public void userLaunches1ChannelCRM() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        commonUtils.sleep(2000);
    }


    //  gherkin: User logs in with generic credentials
    @When("User logs in with generic credentials")
    public void userLogsInWithGenericCredentials() {
        login.loginToCRM(GetProperty.value("genericUsername"),GetProperty.value("genericPassword"));
//        try {
//            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
//            wait.until(ExpectedConditions.visibilityOf(confirmLoginWindow));
//            confirmLogin_YES_button.click();
//            commonUtils.sleep(2000);
//        } catch (Exception e) {
//            System.out.println("Confirm Login window not displayed, proceeding with login.");
//        }
    }


    //  gherkin: User logs in with test credentials
    @When("User logs in with test credentials")
    public void userLogsInWithTestCredentials() {
        login.loginToCRM(GetProperty.value("testUsername"),GetProperty.value("testPassword"));
    /*    try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(confirmLoginWindow));
            confirmLogin_YES_button.click();
        } catch (Exception e) {
            System.out.println("Confirm Login window not displayed, proceeding with login.");
        } */
    }


    //  gherkin: User logs Out from portal
    @Then("User logs Out from portal")
    public void userLogsOutFromPortal() {
        webDriverUtils.waitUntilVisible(getDriver(), userProfile, Duration.ofSeconds(3));
        userProfile.click();
        commonUtils.sleep(1000);
        signOut.click();
        webDriverUtils.waitUntilVisible(getDriver(), signInText_loginpage, Duration.ofSeconds(5));
        Assert.assertTrue(signInText_loginpage.isDisplayed());
    }

}
