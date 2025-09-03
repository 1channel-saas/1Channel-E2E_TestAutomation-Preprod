package com.test.channelplay.object.userAuthentication;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ForgotPasswordPage extends DriverBase {

    @FindBy(xpath = "//div/a[@id=\"kt_login_forgot\"]")
    WebElement forgot_pass_link;

    @FindBy(xpath = "//div/h3[text()=\"Reset Password\"]")
    WebElement reset_pass_text;

    @FindBy(xpath = "//div[@class=\"mat-form-field-infix\"]/input[@formcontrolname=\"email\"]")
    WebElement email_field;

    @FindBy(xpath = "//button[text()=\"Submit\"]")
    WebElement submit_button;

    @FindBy(xpath = "//div/button/span[@class=\"mat-button-wrapper\" and text()=\"Okay\"]")
    WebElement confirm_popup_message;

    @FindBy(xpath = "//div/table[@class=\"table-striped jambo_table\"]/tbody/tr/td[contains(text(), 'Reset Your 1Channel Password')]")
    WebElement mail_verify;

    @FindBy(xpath = "//div/input[@class=\"primary-input\" and @id=\"inbox_field\"]")
    WebElement inbox_mailId_searchBox;

    @FindBy(xpath = "//button[@class=\"primary-btn\" and contains(text(), \"GO\")]")
    WebElement inbox_mailId_search_button;

    @FindBy(xpath = "//div[@class=\"tab-pane fade m-b-20 show active\"]/iframe[@id=\"html_msg_body\"]")
    WebElement iframe_element;

    @FindBy(xpath = "//a[text()='Reset Password' and @target='_other']")
    WebElement reset_pass_link_email_verify;

    @FindBy(xpath = "//mat-label[text()=\"Username\"]/ancestor::div[1]/input")
    WebElement reset_pass_username;

    @FindBy(xpath = "//div[@class=\"mat-form-field-wrapper\"]/div/div[@class=\"mat-form-field-infix\"]/input[@formcontrolname=\"password\"]")
    WebElement password_field;

    @FindBy(xpath = "//div[@class=\"mat-form-field-infix\"]/input[@placeholder=\"Confirm Password\"]")
    WebElement confirm_password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement signIn_button_newPass;

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement login_usrname_field;

    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement login_passwd_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement login_button;


    CommonUtils commonUtils = new CommonUtils();

    public ForgotPasswordPage() {
        PageFactory.initElements(getDriver(), this);
    }

    public void Click_on_forgot_password_link() {
        forgot_pass_link.click();
    }

    public boolean Validate_forgot_pass_title() {
        boolean reset_pass_title = reset_pass_text.isDisplayed();
        return reset_pass_title;
    }

    public void User_enters_email_id_at_reset_password_page() {
        email_field.sendKeys(GetProperty.value("emailId_forgorPassword"));
    }

    public void Click_on_submit_button() {
        submit_button.click();
    }

    public void Click_on_okay_link_over_pop_up_message() {
        confirm_popup_message.click();
        commonUtils.sleep(2000);
    }

    public void Navigate_to_verify_email_link() {
        getDriver().navigate().to(GetProperty.value("mail_verify_url_forgotPass"));
        commonUtils.sleep(3000);

        inbox_mailId_searchBox.sendKeys(GetProperty.value("emailId_forgorPassword"));
        inbox_mailId_search_button.click();
        commonUtils.sleep(2000);

        mail_verify.click();
    }

    public void Click_on_the_reset_password_link_received_over_mail() {
        getDriver().switchTo().frame(iframe_element);
        commonUtils.sleep(2000);
        reset_pass_link_email_verify.click();
        commonUtils.sleep(2000);
        getDriver().switchTo().defaultContent();
    }


    public void Enter_new_password_under_confirm_password_and_click_on_SignIn_button(String new_password) {
        String parent = getDriver().getWindowHandle();

        System.out.println("parent window: " + parent);

        Set<String> allWinhandles = getDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(allWinhandles);

        getDriver().switchTo().window(tabs.get(1));

        System.out.println(getDriver().getCurrentUrl());
        commonUtils.sleep(3000);

        //validate username field at enter new password entry page
        String reset_pass_userName_value = reset_pass_username.getTagName();
        System.out.println("reset pass userName value: " +reset_pass_userName_value);
        Assert.assertTrue(reset_pass_username.isDisplayed());

        password_field.sendKeys(new_password);
        confirm_password_field.sendKeys(new_password);
        signIn_button_newPass.click();
        commonUtils.sleep(4000);
    }

    public void Enter_user_email_and_new_password_at_login_page_and_click_on_signin_button() {

        login_usrname_field.clear();
        login_usrname_field.sendKeys(GetProperty.value("emailId_forgorPassword"));
        login_passwd_field.clear();
        login_passwd_field.sendKeys(GetProperty.value("forgot_pass_new"));

        login_button.click();
        commonUtils.sleep(10000);

        String currURL = getDriver().getCurrentUrl();
        Assert.assertEquals("https://qa.assistive.site/settings-assistant", currURL);
    }

}