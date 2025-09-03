package com.test.channelplay.object.userAuthentication;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Thread.sleep;

public class SignUpPage extends DriverBase {

    @FindBy(xpath = "//div/a[text()=\"Sign Up\"]")
    WebElement signUp_link;

    @FindBy(xpath = "//input[@formcontrolname=\"firstName\"]")
    WebElement firstname_field;

    @FindBy(xpath = "//input[@formcontrolname=\"lastName\"]")
    WebElement lastname_field;

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement work_email_field;

    @FindBy(xpath = "//span/a[text()=\"terms & conditions\"]/parent::span/preceding-sibling::div")
    WebElement termsCheck_checkbox;

    @FindBy(xpath = "//div/button[text()=\"Sign Up\"]")
    WebElement signUp_button;

    @FindBy(xpath = "//div/h3[text()=\"Sign Up\"]")
    WebElement signUp_text;

    @FindBy(xpath = "//div/button/span[@class=\"mat-button-wrapper\" and text()=\"Okay\"]")
    WebElement confirm_popup_message;

    @FindBy(xpath = "//div/input[@class=\"primary-input\" and @id=\"inbox_field\"]")
    WebElement inbox_mailId_searchBox;

    @FindBy(xpath = "//button[@class=\"primary-btn\" and contains(text(), \"GO\")]")
    WebElement inbox_mailId_search_button;

    @FindBy(xpath = "//div/table[@class=\"table-striped jambo_table\"]/tbody/tr/td[contains(text(), 'Welcome to Assistive')]")
    WebElement mail_verify;

    @FindBy(xpath = "//div[@class=\"tab-pane fade m-b-20 show active\"]/iframe[@id=\"html_msg_body\"]")
    WebElement iframe_element;

    @FindBy(xpath = "//a[text()='Create Password' and @target='_other']")
    WebElement createPassword_link_email_verify;

    @FindBy(xpath = "//div/input[@placeholder=\"Password\"]")
    WebElement create_password;

    @FindBy(xpath = "//div/input[@placeholder=\"Confirm Password\"]")
    WebElement create_confPassword;

    @FindBy(xpath = "//div/button[text()=\"Sign In\"]")
    WebElement signIn_new_button;

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    WebElement login_username;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    WebElement login_password;
    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    WebElement SignIn;



    CommonUtils commonUtils = new CommonUtils();
    public String first_Name, last_Name, email_id;
    public String dataPicker;
    public String new_password;

    public SignUpPage() {
        PageFactory.initElements(getDriver(),this);
    }

    public void Click_on_sign_up_at_login_page() {
        signUp_link.click();
        boolean signUpText_isVisible = signUp_text.isDisplayed();
        Assert.assertTrue(signUpText_isVisible);
        commonUtils.sleep(2000);
    }

    public void User_enters_firstname() {
        dataPicker = commonUtils.generateRandomString(5);
        first_Name = "newUser" + dataPicker;
        firstname_field.sendKeys(first_Name);
    }

    public void User_enters_lastname() {
        last_Name = dataPicker;
        lastname_field.sendKeys(last_Name);
    }

    public void User_enters_email_address() {
        email_id = "Email" + dataPicker + "@mailinator.com";
        work_email_field.sendKeys(email_id);
    }



/*    public void User_enters_email_address() {
        email_id = "1h@h.com";
        work_email_field.sendKeys(email_id);
        //commonUtils.validateEmailFormat(email_id);
        //System.out.println("email id format is: " +commonUtils.validateEmailFormat(email_id)+"<-------------------------->");

        Assert.assertTrue(commonUtils.emailPatternMatches(email_id));
        System.out.println("email address: " +commonUtils.emailPatternMatches(email_id)+"<----------------------------------------->");
    }

 */

    public void Click_on_terms_conditions_checkbox() {
        termsCheck_checkbox.click();
    }

    public void Click_on_sign_up_button_under_signUp() {
        signUp_button.click();
        commonUtils.sleep(2000);
    }

    public void Click_on_okay_link_over_pop_up_message_under_signUp() {
        confirm_popup_message.click();
        commonUtils.sleep(3000);
    }

    public void Navigate_to_verify_email_link_undersignUp() {
        getDriver().navigate().to(GetProperty.value("mail_verify_url_signUp"));
        commonUtils.sleep(3000);
        String mail_search = email_id;
        inbox_mailId_searchBox.sendKeys(mail_search);
        inbox_mailId_search_button.click();
        commonUtils.sleep(2000);
        mail_verify.click();
    }

    public void Click_on_the_create_password_link_received_over_mail_under_signUp() {
        getDriver().switchTo().frame(iframe_element);
        commonUtils.sleep(2000);
        createPassword_link_email_verify.click();
        commonUtils.sleep(2000);
        getDriver().switchTo().defaultContent();
    }

    public void Create_new_password_and_click_on_SignIn_button(String new_password) {
        String parent_window = getDriver().getWindowHandle();
        System.out.println("Parent Window: " +parent_window);

        Set<String> allWinHandles = getDriver().getWindowHandles();
        List<String> tabs = new ArrayList<String>(allWinHandles);

        getDriver().switchTo().window(tabs.get(1));
        System.out.println("Current Window: " +getDriver().getCurrentUrl());
        commonUtils.sleep(2000);

        String newUser_visibleText = ("//div/p[text()=\"Hello \"]/b[text()='" + first_Name + "']");
        WebElement signUpUser_text = getDriver().findElement(By.xpath(newUser_visibleText));
        boolean signUpUser_text_isVisible = signUpUser_text.isDisplayed();
        Assert.assertTrue(signUpUser_text_isVisible);

        create_password.sendKeys(new_password);
        create_confPassword.sendKeys(new_password);
        signIn_new_button.click();
        commonUtils.sleep(6000);
    }

    public void Validate_login_successful_with_new_signUp() {
        login_username.sendKeys(email_id);
        login_password.sendKeys(GetProperty.value("signUp_newPassword"));
        SignIn.click();
        commonUtils.sleep(10000);

        String currURL = getDriver().getCurrentUrl();
        Assert.assertEquals("https://qa.assistive.site/settings-assistant", currURL);
    }

}
