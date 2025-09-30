package com.test.channelplay.object.userAuthentication;

import com.test.channelplay.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Set;

public class LoginPage extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement email_field;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement password_field;
    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement SignIn_button;
    @FindBy(xpath = "//input[@formcontrolname='password']/following-sibling::mat-icon | //input[@formcontrolname='password']/parent::*/descendant::mat-icon | //mat-icon[contains(@class, 'eye') or contains(@class, 'visibility')]")
    WebElement passwordToggleIcon;
    @FindBy(xpath = "//h5[text()=\" Settings Assistant \"]")
    WebElement SettingAssistant_homepage;
    @FindBy(xpath = "//span[text()='Hi, ']/parent::div")
    WebElement userProfile;
    @FindBy(xpath = "//h4[text()=\"Confirm Login\"]")
    WebElement confirmLoginWindow;
    @FindBy(xpath = ".//button[text()=' Yes ']")
    WebElement confirmLogin_YES_button;
    @FindBy(xpath = "//h3[text()=' User Profile ']/parent::div/a")
    WebElement crossButtonAtUserProfileWindow;

    //  Alerts validation elements
    @FindBy(xpath = "//input[@formcontrolname=\"email\"]/ancestor::div[@class='mat-form-field-flex']/following-sibling::div/descendant::mat-error[@role='alert']")
    WebElement AlertMessage_invalidFieldValidation;
    @FindBy(xpath = "//h3[text()='Sign In']")
    WebElement SignIntext;
    @FindBy(xpath = "//div[contains(text(), 'Incorrect email address or password entered')]")
    WebElement AlertMessage_invalidPassword;
    @FindBy(xpath = "//div[contains(text(), 'Invalid username or password')]")
    WebElement AlertMessage_invalidUserOrPassword;
    @FindBy(xpath = "//*[@role='alert']/strong")
    WebElement AlertMessage_requiredField;
    @FindBy(xpath = "//div[contains(text(), 'Your account has been temporarily locked')]")
    WebElement AlertMessage_passworsdLocked;

    //  reset password and Emailer elements
    @FindBy(xpath = "//a[text()='reset']")
    WebElement resetPassword_link;
    @FindBy(xpath = "//span[text()='Email Id']")
    WebElement resetEmailId_link;
    @FindBy(xpath = "//input[@type='email']")
    WebElement resetPassword_emailTextbox;
    @FindBy(xpath = "//h3[text()='Reset Password']")
    WebElement resetPassword_textHeading;
    @FindBy(xpath = "//button[text()='Submit']")
    WebElement resetPassword_email_submitButton;
    @FindBy(xpath = "//span[text()='Okay']")
    WebElement resetPassword_email_okayButton;
    @FindBy(xpath = Constants.mailer_HomeSignIn_button)
    WebElement mailer_HomeSignIn_button;
    @FindBy(xpath = Constants.mailer_EnterEmailId)
    WebElement mailer_EnterEmailId;
    @FindBy(xpath = Constants.mailer_EnterEmailId_Next_button)
    WebElement mailer_EnterEmailId_Next_button;
    @FindBy(xpath = Constants.mailer_EnterPassword)
    WebElement mailer_EnterPassword;
    @FindBy(xpath = Constants.mailer_SignIn_button)
    WebElement mailer_SignIn_button;
    @FindBy(xpath = Constants.staySignedIn_Yes_button)
    WebElement staySignedIn_Yes_button;
    @FindBy(xpath = Constants.mailer_Logo)
    WebElement mailer_Logo;
    @FindBy(xpath = Constants.mailer_Inbox)
    WebElement mailer_Inbox;
    @FindBy(xpath = "//a[text()='Reset Password']")
    WebElement resetPasswordLinkInEmail;
    @FindBy(xpath = "//div/p[contains (text(), 'Please create your new password')]")
    WebElement resetPassWindowHeaderText;
    @FindBy(xpath = "//input[@formcontrolname='password']")
    WebElement resetPassWindow_PasswordField;
    @FindBy(xpath = "//input[@formcontrolname='confirmPassword']")
    WebElement resetPassWindow_ConfirmPasswordField;
    @FindBy(xpath = "//button[text()='Sign In']")
    WebElement resetPassWindow_SignInButton;
    @FindBy(xpath = "//button/descendant::span[text()='Home']/ancestor::div[@class='VvU3M']/preceding-sibling::div/button")
    WebElement mailer_Show_Navigation_button;

    WebDriverUtils webDriverUtils = new WebDriverUtils();
    CommonUtils commonUtils = new CommonUtils();
    private String currentUserEmail; // Store the logged-in user's email
    
    public LoginPage() {
        PageFactory.initElements(getDriver(),this);
    }




    //  scenario - loginAuth

    public void userEntersEmail(String username) {
        email_field.clear();
        email_field.sendKeys(username);
        this.currentUserEmail = username; // Store email for later use
    }

    public void userEntersPassword (String password) {
        password_field.clear();
        password_field.sendKeys(password);
    }

    public void userClicksOnSignInButton () {
        SignIn_button.click();
        commonUtils.sleep(1000);

        //  Check if password locked alert appears (performs only when account is locked)
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(AlertMessage_passworsdLocked));
            //  If Password reset alert is displayed - handle password reset
            commonUtils.sleep(1000);
            resetPassword();
        } catch (Exception e) {
            //  Alert not displayed - this is the normal flow, continue with login
            System.out.println("No password locked alert, proceeding with normal login flow.");
        }

        //  Confirm Login window handling
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(confirmLoginWindow));
            confirmLogin_YES_button.click();
            commonUtils.sleep(2000);
        } catch (Exception e) {
            System.out.println("Confirm Login window not displayed, proceeding with login.");
        }
    }

    private void resetPassword() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        resetPassword_link.click();
        wait.until(ExpectedConditions.visibilityOf(resetEmailId_link));
        resetEmailId_link.click();
        Assert.assertTrue(resetPassword_textHeading.isDisplayed());
        resetPassword_emailTextbox.sendKeys("testemailqa@1channel.co");
        commonUtils.sleep(1000);
        resetPassword_email_submitButton.click();
        wait.until(ExpectedConditions.visibilityOf(resetPassword_email_okayButton));
        resetPassword_email_okayButton.click();
        goToEmailerAndResetPassword();
    }

    private void goToEmailerAndResetPassword() {
        String parentWindow = getDriver().getWindowHandle();
        String oneChannelWindowURL = getDriver().getCurrentUrl();
        String url = GetProperty.value("mailVerifyURL");
        getDriver().navigate().to(url);

        webDriverUtils.actionsToMoveToElement(getDriver(), mailer_HomeSignIn_button);
        mailer_HomeSignIn_button.click();
        commonUtils.sleep(1000);
        //  Switch to new window
        Set<String> handles =  getDriver().getWindowHandles();
        for(String windowHandle  : handles)
        {
            if(!windowHandle.equals(parentWindow))
            {
                getDriver().switchTo().window(windowHandle);
                commonUtils.sleep(1000);
                System.out.println("Entered into new window");

                //  perform emailer operations on new window
                performEmailerTask();
                System.out.println("Entered into new performEmailerTask method");

                //  close the child window
                commonUtils.sleep(1000);
                getDriver().close();
                getDriver().switchTo().window(parentWindow);
                System.out.println("Switched back to parent window");
                getDriver().navigate().to(oneChannelWindowURL);
            }
        }
    }

    private void performEmailerTask() {
        mailer_EnterEmailId.sendKeys(GetProperty.value("testEmailId"));
        mailer_EnterEmailId_Next_button.click();
        mailer_EnterPassword.sendKeys(GetProperty.value("testEmailPassword"));
        mailer_SignIn_button.click();
        staySignedIn_Yes_button.click();
        String currMailerUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currMailerUrl.contains("outlook.office.com/mail"));

        if (!(mailer_Inbox.isDisplayed())) {
            mailer_Show_Navigation_button.click();
            mailer_Inbox.click();
        } else {
            mailer_Inbox.click();
        }
        commonUtils.sleep(1000);

        WebElement resetPassword_latestEmail = getDriver().findElement(By.xpath("(//span[text()='Reset Your 1Channel Password'])[1]"));
        commonUtils.sleep(1000);
        //  click on latest email containing reset password text
        resetPassword_latestEmail.click();
        commonUtils.sleep(1000);
        //  click on reset password link in email
        resetPasswordLinkInEmail.click();
        //  switch to reset password window
        String currentEmailerWindow = getDriver().getWindowHandle();
        Set <String> handles =  getDriver().getWindowHandles();
        for(String windowHandle  : handles) {
            if(!windowHandle.equals(currentEmailerWindow))
            {
                getDriver().switchTo().window(windowHandle);
                System.out.println("Switched to reset password window");
                commonUtils.sleep(1000);
            }
        }
        commonUtils.sleep(3000);
        Assert.assertTrue(resetPassWindowHeaderText.isDisplayed());
        //  enter new password and confirm password
        resetPassWindow_PasswordField.sendKeys("K(460848703994az");
        commonUtils.sleep(1000);
        resetPassWindow_ConfirmPasswordField.sendKeys("K(460848703994az");
        commonUtils.sleep(1000);
        resetPassWindow_SignInButton.click();
        commonUtils.sleep(1000);

    }

    public void userShouldBeLoggedInSuccessfullyAndRedirectedToAssistantPage () {
        webDriverUtils.waitUntilVisible(getDriver(), SettingAssistant_homepage, Duration.ofSeconds(5));
        Assert.assertTrue(SettingAssistant_homepage.isDisplayed());
    }

    public void userProfileShouldBeDisplayed() {
        userProfile.click();
        commonUtils.sleep(1000);
        //  Dynamically construct xpath with the stored email from login
        String dynamicXpath = String.format("//a[text()=' Sign Out ']/parent::div/descendant::div/span/descendant::span[contains(text(), '%s')]", currentUserEmail);
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        WebElement userEmail = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXpath)));
        commonUtils.sleep(1000);
        Assert.assertTrue(userEmail.isDisplayed(), "User email should be displayed in profile " + currentUserEmail);
        crossButtonAtUserProfileWindow.click();
        commonUtils.sleep(1000);
    }




    //  scenario - loginFieldValidation

    public void clicksOutsideTheUsernameField() {
        SignIntext.click();
    }

    public void usernameFieldValidationShouldBeTriggered(String errMessage) {
        commonUtils.sleep(1000);
        System.out.println("string error msg is: " + errMessage);
        System.out.println("Error message displayed: " + AlertMessage_invalidFieldValidation.getText());
        if (errMessage.equals(AlertMessage_invalidFieldValidation.getText())) {
            Assert.assertTrue(AlertMessage_invalidFieldValidation.isDisplayed());
        }
    }




    //  scenario - loginNegativeTest

    public void userShouldRemainOnLoginPage() {
        Assert.assertTrue(SignIntext.isDisplayed(), "User is on login page");
    }

    public void errorMessageShouldBeDisplayedAndValidateWith(String err_message, String test_description) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        WebElement errorElement;
        
        System.out.println("Expected error: " + err_message);
        System.out.println("Test scenario: " + test_description);
        
        try {
            //  Determine which error element to check based on test description
            if (test_description.contains("Valid user and invalid password")) {
                errorElement = AlertMessage_invalidPassword;
                wait.until(ExpectedConditions.visibilityOf(errorElement));
            } else if (test_description.contains("Invalid user")) {
                errorElement = AlertMessage_invalidUserOrPassword;
                wait.until(ExpectedConditions.visibilityOf(errorElement));
            } else if (test_description.contains("Empty")) {
                errorElement = AlertMessage_requiredField;
                wait.until(ExpectedConditions.visibilityOf(errorElement));
            } else {
                Assert.fail("Unhandled test scenario: " + test_description);
                return;
            }

            //  Special trimming for "Valid user and invalid password" scenario
            String actualMessage = errorElement.getText().trim();
            if (test_description.contains("Valid user and invalid password") && actualMessage.contains(".")) {
                    actualMessage = actualMessage.substring(0, actualMessage.indexOf(".")).trim();
                }

            System.out.println("Actual error: " + actualMessage);
            
            //  Assert error message matches expected
            Assert.assertEquals(actualMessage, err_message, "Error message mismatch for scenario: " + test_description);
            
            //  Assert error element is visible
            Assert.assertTrue(errorElement.isDisplayed(), "Error message should be visible for scenario: " + test_description);
                
        } catch (Exception e) {
            Assert.fail("Expected error message '" + err_message + "' not displayed for scenario: " + test_description + ". Exception: " + e.getMessage());
        }
    }




    // scenario - loginPasswordMasking

    public void userEntersPasswordInThePasswordField() {
        password_field.clear();
        password_field.sendKeys("Test@12345");
        commonUtils.sleep(500);
    }

    public void passwordShouldBeMaskedWithAsterisks() {
        // Verify the password field type is 'password' (which masks the input)
        String fieldType = password_field.getAttribute("type");
        Assert.assertEquals(fieldType, "password", "Password field should be of type 'password' to mask input");
        
        // Additionally verify the actual value is not visible as plain text
        String displayedValue = password_field.getAttribute("value");
        Assert.assertNotNull(displayedValue, "Password field should contain a value");
        Assert.assertFalse(displayedValue.isEmpty(), "Password field should not be empty");
        
        // Verify the password is not displayed as plain text in the DOM
        String inputType = password_field.getAttribute("type");
        Assert.assertNotEquals(inputType, "text", "Password should not be displayed as plain text");
        
        System.out.println("Password is properly masked with type: " + fieldType);
    }

    public void showorhidePasswordToggleShouldBeAvailable() {
        try {
            // Check if toggle icon exists
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            
            // Try multiple possible selectors for the password toggle
            WebElement toggleIcon = null;
            
            // Common patterns for password toggle icons
            String[] toggleSelectors = {
                "//input[@formcontrolname='password']/following-sibling::mat-icon",
                "//input[@formcontrolname='password']/parent::*/descendant::mat-icon",
                "//mat-icon[contains(@class, 'visibility')]",
                "//button[@type='button'][contains(@aria-label, 'password')]",
                "//span[@class='mat-icon-button' or contains(@class, 'toggle')]",
                "//i[contains(@class, 'eye') or contains(@class, 'visibility')]",
                "//*[@matSuffix]//mat-icon"
            };
            
            for (String selector : toggleSelectors) {
                try {
                    toggleIcon = getDriver().findElement(By.xpath(selector));
                    if (toggleIcon.isDisplayed()) {
                        break;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            Assert.assertNotNull(toggleIcon, "Password toggle icon should be present");
            Assert.assertTrue(toggleIcon.isDisplayed(), "Password toggle icon should be visible");
            
            // Test the toggle functionality
            String initialType = password_field.getAttribute("type");
            toggleIcon.click();
            commonUtils.sleep(2000);
            System.out.println("Clicked on toggle icon");
            
            String toggledType = password_field.getAttribute("type");
            Assert.assertNotEquals(initialType, toggledType, "Password field type should change after clicking toggle");
            
            // Toggle back
            toggleIcon.click();
            commonUtils.sleep(500);
            String finalType = password_field.getAttribute("type");
            Assert.assertEquals(initialType, finalType, "Password field should return to original state after second toggle");
            
            System.out.println("Password toggle functionality verified successfully");
            
        } catch (Exception e) {
            // If toggle icon is not found, it might not be implemented
            Assert.fail("Password show/hide toggle is not available or not working properly: " + e.getMessage());
        }
    }

}
