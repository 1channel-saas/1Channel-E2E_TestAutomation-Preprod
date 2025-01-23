package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.SignUp_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class SignUp_Steps extends DriverBase {
    CommonUtils commonUtils = new CommonUtils();
    SignUp_Object signup = new SignUp_Object();

    @Given("user landed on assistive login page")
    public void userLandedOnAssistiveLoginPage() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        commonUtils.sleep(2000);
    }


    @When("click on Sign Up at login page")
    public void click_on_sign_up_at_login_page() {
        signup.Click_on_sign_up_at_login_page();
    }

    @And("user enters firstname")
    public void user_enters_firstname() {
        signup.User_enters_firstname();
    }

    @And("user enters lastname")
    public void user_enters_lastname() {
        signup.User_enters_lastname();
    }

    @And("user enters email address")
    public void user_enters_email_address() {
        signup.User_enters_email_address();
    }

    @And("click on terms & conditions checkbox")
    public void click_on_terms_conditions_checkbox() {
        signup.Click_on_terms_conditions_checkbox();
    }

    @And("click on sign Up button under signUp")
    public void click_on_sign_up_button_under_signUp() {
        signup.Click_on_sign_up_button_under_signUp();
    }

    @And("click on okay link over pop up message under signUp")
    public void click_on_okay_link_over_pop_up_message_under_signUp() {
        signup.Click_on_okay_link_over_pop_up_message_under_signUp();
    }

    @And("navigate to verify email link under signUp")
    public void navigate_to_verify_email_link_undersignUp() {
        signup.Navigate_to_verify_email_link_undersignUp();
    }

    @And("click on the create password link received over mail under signUp")
    public void click_on_the_create_password_link_received_over_mail_under_signUp() {
        signup.Click_on_the_create_password_link_received_over_mail_under_signUp();
    }

    @And("create new password and click on SignIn button")
    public void create_new_password_and_click_on_SignIn_button(){
        signup.Create_new_password_and_click_on_SignIn_button(GetProperty.value("signUp_newPassword"));
    }

    @Then("validate login successful with new signUp")
    public void validate_login_successful_with_new_signUp() {
        signup.Validate_login_successful_with_new_signUp();
    }

}
