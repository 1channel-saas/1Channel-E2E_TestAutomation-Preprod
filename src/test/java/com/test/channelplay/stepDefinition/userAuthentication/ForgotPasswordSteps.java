package com.test.channelplay.stepDefinition.userAuthentication;

import com.test.channelplay.object.userAuthentication.ForgotPasswordPage;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class ForgotPasswordSteps extends DriverBase {

        ForgotPasswordPage forgot_Pass = new ForgotPasswordPage();




    //  scenario - forgotPassword

    @When("click on Forgot Password link")
    public void click_on_forgot_password_link() {
        forgot_Pass.Click_on_forgot_password_link();
        boolean identifierPageTitle = forgot_Pass.Validate_identifierPageTitle();
        Assert.assertTrue(identifierPageTitle);
    }

    @Then("navigate to emailer and perform password reset operation with selecting {string}")
    public void navigateToEmailerAndPerformPasswordResetOperationWithSelecting(String arg) {
        forgot_Pass.navigateToEmailerAndPerformPasswordResetOperationWithSelecting(arg);
    }

    @Then("enter user email and new password at login page and click on Signin button")
    public void enterUserEmailAndNewPasswordAtLoginPageAndClickOnSigninButton() {
        forgot_Pass.enterUserEmailAndNewPasswordAtLoginPageAndClickOnSigninButton();
    }

}
