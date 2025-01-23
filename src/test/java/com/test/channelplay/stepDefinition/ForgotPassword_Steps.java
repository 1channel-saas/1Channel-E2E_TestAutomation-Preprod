package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.ForgotPassword_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class ForgotPassword_Steps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    ForgotPassword_Object forgot_Pass = new ForgotPassword_Object();

    @Given("user is on Assistive login page")
    public void user_is_on_assistive_login_page() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
    }

    @When("click on Forgot Password link")
    public void click_on_forgot_password_link() {
        forgot_Pass.Click_on_forgot_password_link();
        boolean reset_pass_Text = forgot_Pass.Validate_forgot_pass_title();
        Assert.assertTrue(reset_pass_Text);
    }

    @When("user enters email_id at Reset Password page")
    public void user_enters_email_id_at_reset_password_page() {
        forgot_Pass.User_enters_email_id_at_reset_password_page();
    }

    @When("click on Submit button")
    public void click_on_submit_button() {
        forgot_Pass.Click_on_submit_button();
    }

    @When("click on okay link over pop up message")
    public void click_on_okay_link_over_pop_up_message() {
        forgot_Pass.Click_on_okay_link_over_pop_up_message();
    }

    @Then("navigate to verify email link")
    public void navigate_to_verify_email_link() {
        forgot_Pass.Navigate_to_verify_email_link();
    }

    @Then("click on the reset password link received over mail")
    public void click_on_the_reset_password_link_received_over_mail() {
        forgot_Pass.Click_on_the_reset_password_link_received_over_mail();
    }

    @Then("enter new password under confirm password and click on SignIn button")
    public void enter_new_password_under_confirm_password_and_click_on_SignIn_button() {
        forgot_Pass.Enter_new_password_under_confirm_password_and_click_on_SignIn_button(GetProperty.value("forgot_pass_new"));
    }

    @Then("enter user email and new password at login page and click on Signin button")
    public void enter_user_email_and_new_password_at_login_page_and_click_on_signin_button() {
        forgot_Pass.Enter_user_email_and_new_password_at_login_page_and_click_on_signin_button();
    }

}
