package com.test.channelplay.object.userAuthentication;

import com.test.channelplay.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.slf4j.Logger;
import utilities_API.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class ForgotPasswordPage extends DriverBase {

    @FindBy(xpath = "//div/a[@id=\"kt_login_forgot\"]")
    WebElement forgot_pass_link;
    @FindBy(xpath = "//p/span[text()='Email Id']")
    WebElement email_identify_text;
    @FindBy(xpath = "//p/span[text()='Mobile Number']")
    WebElement mobileNo_identify_text;
    @FindBy(xpath = "//p[text()='Important!']")
    WebElement important_text;
    @FindBy(xpath = "//div[@class=\"mat-form-field-infix\"]/input[@formcontrolname=\"email\"]")
    WebElement email_field;
    @FindBy(xpath = "//input[@formcontrolname='mobile']")
    WebElement mobileNo_field;
    @FindBy(xpath = "//input[@formcontrolname='otp']")
    WebElement enterOtp_textField;
    @FindBy(xpath = "//button[text()=\"Submit\"]")
    WebElement submit_button;
    @FindBy(xpath = "//div/button/span[@class=\"mat-button-wrapper\" and text()=\"Okay\"]")
    WebElement confirm_popup_message;
    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement login_usrname_field;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement login_passwd_field;
    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement login_button;
    @FindBy(xpath = "//h4[text()=\"Confirm Login\"]")
    WebElement confirmLogin_window;
    @FindBy(xpath = ".//button[text()=' Yes ']")
    WebElement confirmLogin_YES_button;


    //  Emailer xpath expressions
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
    @FindBy(xpath = "//button/descendant::span[text()='Home']/ancestor::div[@class='VvU3M']/preceding-sibling::div/button")
    WebElement mailer_Show_Navigation_button;
    @FindBy(xpath = "//a[text()='Reset Password']")
    WebElement resetPasswordLinkInEmail;
    @FindBy(xpath = "//div/p[contains(normalize-space(text()), 'Please create your new password')]")
    WebElement resetPassWindowHeaderText;
    @FindBy(xpath = "//input[@formcontrolname='password']")
    WebElement resetPassWindow_PasswordField;
    @FindBy(xpath = "//input[@formcontrolname='confirmPassword']")
    WebElement resetPassWindow_ConfirmPasswordField;
    @FindBy(xpath = "//button[text()='Sign In']")
    WebElement resetPassWindow_SignInButton;



    CommonUtils commonutils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordPage.class);
    LocalDateTime emailSentTime;
    LocalDateTime otpSentTime;
    private Connection preprodConnection;
    ResultSet resultSet;
    String otp;
    private final String newPassword = "K(460848703994az";


    public ForgotPasswordPage() {
        PageFactory.initElements(getDriver(), this);
    }




    //  scenario - forgotPassword
    //  portal UI

    public void Click_on_forgot_password_link() {
        forgot_pass_link.click();
        commonutils.sleep(1000);
        //  check whether identifier popup is displayed or not
        Validate_identifierPageTitle();
    }

    public void navigateToEmailerAndPerformPasswordResetOperationWithSelecting(String identifierType) {
        //  reset password using email id
        if (identifierType.equalsIgnoreCase("Email")) {
            email_identify_text.click();
            commonutils.sleep(2000);
            wait.until(ExpectedConditions.visibilityOf(email_field));
            Assert.assertTrue(email_field.isDisplayed());
            commonutils.sleep(1000);
            email_field.clear();
            email_field.sendKeys(GetProperty.value("testEmailId"));
            ScreenshotHelper.captureScreenshot("resetIdentifier_email");
            submit_button.click();
            confirm_popup_message.click();
            
            //  Capture the time when email is sent
            emailSentTime = LocalDateTime.now();
            log.info("reset password email sent at: {}", emailSentTime);
            
            commonutils.sleep(1000);
            navigateToEmailerAndPerformPasswordResetOperation();
        }
        //  reset password using mobile number
        else if (identifierType.equalsIgnoreCase("Mobile")) {
            mobileNo_identify_text.click();
            commonutils.sleep(2000);
            wait.until(ExpectedConditions.visibilityOf(mobileNo_field));
            Assert.assertTrue(mobileNo_field.isDisplayed());
            commonutils.sleep(1000);
            mobileNo_field.clear();
            mobileNo_field.sendKeys(GetProperty.value("testMobileNo"));
            ScreenshotHelper.captureScreenshot("resetIdentifier_mobile");
            submit_button.click();
            commonutils.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(confirm_popup_message));
            ScreenshotHelper.captureScreenshot("resetIdentifier_mobile_confirmPopup");
            confirm_popup_message.click();

            //  Capture the time when moble otp is sent
            otpSentTime = LocalDateTime.now();
            log.info("reset password otp sent at: {}", otpSentTime);

            commonutils.sleep(1000);
            fetchOtpFromDBForResetPasswordWithMobileNumber();
            log.info("fetch otp completed from DB. Current OTP: {}", otp);
            //  enter otp in otp field
            wait.until(ExpectedConditions.visibilityOf(enterOtp_textField));
            enterOtp_textField.sendKeys(otp);
            commonutils.sleep(1000);
            ScreenshotHelper.captureScreenshot("resetIdentifier_mobile_enterOtp");
            submit_button.click();
            wait.until(ExpectedConditions.visibilityOf(resetPassWindowHeaderText));
            Assert.assertTrue(resetPassWindowHeaderText.isDisplayed());
            commonutils.sleep(1000);
            resetPassWindow_PasswordField.clear();
            resetPassWindow_PasswordField.sendKeys(GetProperty.value("testEmailPassword"));
            commonutils.sleep(1000);
            resetPassWindow_ConfirmPasswordField.clear();
            resetPassWindow_ConfirmPasswordField.sendKeys(GetProperty.value("testEmailPassword"));
            commonutils.sleep(1000);
            ScreenshotHelper.captureScreenshot("resetIdentifier_mobile_enterNewPassword");
            resetPassWindow_SignInButton.click();

            System.out.println("Reset password using mobile number completed successfully");
        }
    }


    public boolean Validate_identifierPageTitle() {
        return important_text.isDisplayed();
    }

    public void navigateToEmailerAndPerformPasswordResetOperation() {
        String parentWindow = getDriver().getWindowHandle();
        String oneChannelWindowURL = getDriver().getCurrentUrl();

        String url = GetProperty.value("mailVerifyURL");
        getDriver().navigate().to(url);

        wait.until(ExpectedConditions.visibilityOf(mailer_HomeSignIn_button));
        webDriverUtils.actionsToMoveToElement(getDriver(), mailer_HomeSignIn_button);
        mailer_HomeSignIn_button.click();
        commonutils.sleep(1000);

        // Switch to emailer window
        Set<String> handles =  getDriver().getWindowHandles();
        for(String windowHandle  : handles)
        {
            if(!windowHandle.equals(parentWindow))
            {
                getDriver().switchTo().window(windowHandle);
                commonutils.sleep(1000);
                log.info("Entered into emailer window");

                //  perform emailer operations on new window
                performEmailerTask();
                log.info("performEmailerTask method completed");

                //  close the child window
                commonutils.sleep(1000);
                getDriver().close();
                getDriver().switchTo().window(parentWindow);
                log.info("Switched back to parent window");
                getDriver().navigate().to(oneChannelWindowURL);
                log.info("Navigated back to 1Channel window: {}", oneChannelWindowURL);
            }
        }
    }

    private void performEmailerTask() {
        log.info("Entered into performEmailerTask method");
        webDriverUtils.waitUntilVisible(getDriver(), mailer_EnterEmailId, Duration.ofSeconds(5));
        mailer_EnterEmailId.sendKeys(GetProperty.value("testEmailId"));
        mailer_EnterEmailId_Next_button.click();

        webDriverUtils.waitUntilVisible(getDriver(), mailer_EnterPassword, Duration.ofSeconds(5));
        mailer_EnterPassword.sendKeys(GetProperty.value("testEmailPassword"));
        mailer_SignIn_button.click();

        webDriverUtils.waitUntilVisible(getDriver(), staySignedIn_Yes_button, Duration.ofSeconds(5));
        staySignedIn_Yes_button.click();
        webDriverUtils.waitUntilVisible(getDriver(), mailer_Logo, Duration.ofSeconds(10));
        ScreenshotHelper.captureScreenshot("mailer_homepage");

        String currMailerUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currMailerUrl.contains("outlook.office.com/mail"));

        if (!(mailer_Inbox.isDisplayed())) {
            mailer_Show_Navigation_button.click();
            mailer_Inbox.click();
        } else {
            mailer_Inbox.click();
        }
        commonutils.sleep(1000);

        WebElement resetPassword_latestEmail = getDriver().findElement(By.xpath("(//span[text()='Reset Your 1Channel Password'])[1]"));
        
        // Verify email arrived after sending the reset request
        LocalDateTime emailReceivedTime = LocalDateTime.now();
        System.out.println("Checking email at: " + emailReceivedTime);
        
        if (emailSentTime != null && emailReceivedTime.isAfter(emailSentTime)) {
            log.info("Email verification successful - Email received after reset request was sent");
            System.out.println("Time difference: " + Duration.between(emailSentTime, emailReceivedTime).getSeconds() + " seconds");
        } else {
            log.warn("WARNING: Could not verify email timing");
        }
        
        commonutils.sleep(1000);
        //  click on latest email containing reset password text
        resetPassword_latestEmail.click();
        commonutils.sleep(1000);

        //  get window handle of current emailer window
        String currentEmailerWindow = getDriver().getWindowHandle();

        //  click on reset password link in email
        wait.until(ExpectedConditions.visibilityOf(resetPasswordLinkInEmail));
        resetPasswordLinkInEmail.click();
        ScreenshotHelper.captureScreenshot("resetPassword_email");
        commonutils.sleep(2000);

        //  switch to reset password window
        Set <String> handles =  getDriver().getWindowHandles();
        for(String windowHandle  : handles) {
            if(!windowHandle.equals(currentEmailerWindow))
            {
                getDriver().switchTo().window(windowHandle);
                commonutils.sleep(2000);
                if (getDriver().getCurrentUrl().contains("preprod.1channel.co/auth/resetpassword-token")) {
                    log.info("Switched to reset password window");
                    ScreenshotHelper.captureScreenshot("resetPassword_window");
                }
                commonutils.sleep(2000);
            }
        }
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(resetPassWindowHeaderText)));
        Assert.assertTrue(resetPassWindowHeaderText.isDisplayed());
        //  enter new password and confirm password
        resetPassWindow_PasswordField.sendKeys(newPassword);
        commonutils.sleep(1000);
        resetPassWindow_ConfirmPasswordField.sendKeys(newPassword);
        commonutils.sleep(1000);
        ScreenshotHelper.captureScreenshot("resetPassword_enterNewPassword");
        resetPassWindow_SignInButton.click();
        commonutils.sleep(1000);
    }

    //  reset password via mobile   (** no feature file created for this)
    public void fetchOtpFromDBForResetPasswordWithMobileNumber() {
        preprodConnection = DBConnection.getControllerConnection();
        long startTime = System.currentTimeMillis();
        int maxWaitTimeInSeconds = 60;     // Maximum wait time
        int pollingIntervalInSeconds = 3;
        boolean otpReceived = true;

        String tableNameOtp = "channelplay_aurora.b2b_otp_reset_password";
        String otpMobileNo = GetProperty.value("testMobileNo");

        while ((System.currentTimeMillis() - startTime) < maxWaitTimeInSeconds * 1000L) {
            String fetchOtpFromDB_query = "SELECT otp\n" +
                    "FROM " + tableNameOtp + " \n" +
                    "WHERE phone_number = '" + otpMobileNo + "'\n" +
                    "ORDER BY id DESC LIMIT 1";
            System.out.println("Executing query: " + fetchOtpFromDB_query);

            String otpFromDB = null;
            try {
                resultSet = executeQuery(fetchOtpFromDB_query, preprodConnection);
                if (resultSet.next()) {
                    otpFromDB = resultSet.getString("otp");
                    System.out.println("OTP fetched from DB: ------------1" + otpFromDB);

                    if (otpFromDB == null || "null".equals(otpFromDB)) {
                        log.info("OTP not received yet. Value in DB is still NULL. Waiting and retrying...");
                    } else {
                        //  OTP received successfully
                        log.info("OTP received from DB: {}", otpFromDB);
                        otp = otpFromDB;
                        System.out.println("OTP fetched from DB: ------------2" + otp);
                        otpReceived = true;
                        break;
                    }
                } else {
                    log.info("No record found for phone number. Waiting and retrying...");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to fetch data from database after query execution", e);
            } finally {
                closeResultSet(resultSet);
            }

            try {
                Thread.sleep(pollingIntervalInSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Polling was interrupted", e);
            }
            System.out.println("Fetched OTP from DB: " + otpFromDB);
            otp = String.valueOf(otpFromDB);
        }
    }


    //  using methods (executeQuery() and closeResultSet()) from CommonUtils_API class to avoid cyclic dependency
    public static ResultSet executeQuery(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void enterUserEmailAndNewPasswordAtLoginPageAndClickOnSigninButton() {
        wait.until(ExpectedConditions.elementToBeClickable(login_usrname_field));
        login_usrname_field.clear();
        login_usrname_field.sendKeys(GetProperty.value("testEmailId"));
        login_passwd_field.clear();
        login_passwd_field.sendKeys(newPassword);
        login_button.click();
        commonutils.sleep(2000);

        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2));
            wait.until(ExpectedConditions.visibilityOf(confirmLogin_window));
            confirmLogin_YES_button.click();
            commonutils.sleep(2000);
        } catch (Exception e) {
            log.info("Confirm Login popup not displayed. Proceeding...");
        }

        //  validate whether user landed on dashboard page or not
        String currURL = getDriver().getCurrentUrl();
        if (currURL.equalsIgnoreCase("https://preprod.1channel.co/dashboard")) {
            System.out.println("Forgot Password operation completed successfully. User logged in to dashboard page");
        } else if (currURL.equalsIgnoreCase("https://preprod.1channel.co/settings-assistant")) {
            System.out.println("Forgot Password operation completed successfully. User logged in to Settings Assistant page");
        } else {
            System.out.println("Forgot Password operation might have failed. User not landed on dashboard page");
        }
        ScreenshotHelper.captureScreenshot("login_after_forgotPassword");
    }

}