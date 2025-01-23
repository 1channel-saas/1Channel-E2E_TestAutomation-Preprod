package com.test.channelplay.object;

import com.test.channelplay.utils.DriverBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class CRMloginObject extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement email_field;


    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement SignIn_button;

    @FindBy(xpath = "//h5[text()=\" Settings Assistant \"]")
    private WebElement SettingAssistant_homepage;

    @FindBy(xpath = "//div[@class=\"alert-text\"]")
    private WebElement AlertMessage;


    public CRMloginObject(){
        PageFactory.initElements(getDriver(),this);
    }

    public void UserEnterEmail(String username)
    {
        sleep(2000);
        email_field.sendKeys(username);
    }


    public void UserEnterPassword (String password){
            password_field.sendKeys(password);
        }

        public void UserClickSignin () {
            SignIn_button.click();
            sleep(8000);
        }

        public void UserWillBeOnTheHomepage () {
            sleep(4000);
            Assert.assertTrue(SettingAssistant_homepage.isDisplayed());
        }

        public void UserEnterUsernameandPassword () {
            email_field.sendKeys();
        }

        public void UserEntersValidAndInvalid (String username, String password){
            email_field.sendKeys(username);
            password_field.sendKeys(password);
        }

        public void UserShouldNotLoggedInAndValidationMessageShouldBeShownAs () {
            String expectedMessage = "Username or Password is incorrect.";
            String ActualMessage = AlertMessage.getText();
            Assert.assertEquals(expectedMessage, ActualMessage);
        }

        public void sleep( long s){
            try {
                Thread.sleep(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
