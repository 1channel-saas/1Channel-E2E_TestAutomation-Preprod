package com.test.channelplay.stepDefinition.userAuthentication;

import com.test.channelplay.object.userAuthentication.LoginPage;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;

public class LoginSteps extends DriverBase {

    LoginPage CRMLoginPage = new LoginPage();


    //  scenario - loginAuth

    @When("User enters email {string}")
    public void userEntersEmail(String arg0) {
        CRMLoginPage.userEntersEmail(arg0);
    }

    @And("User enters password {string}")
    public void userEntersPassword(String arg0) {
        CRMLoginPage.userEntersPassword(arg0);
    }

    @And("User clicks on SignIn button")
    public void userClicksOnSignInButton() {
        CRMLoginPage.userClicksOnSignInButton();
    }

    @Then("User should be loggedIn successfully and redirected to assistant page")
    public void userShouldBeLoggedInSuccessfullyAndRedirectedToAssistantPage() {
        CRMLoginPage.userShouldBeLoggedInSuccessfullyAndRedirectedToAssistantPage();
    }

    @And("User profile should be displayed")
    public void userProfileShouldBeDisplayed() {
        CRMLoginPage.userProfileShouldBeDisplayed();
    }




    //  scenario - loginFieldValidation

    @And("Clicks outside the Username field")
    public void clicksOutsideTheUsernameField() {
        CRMLoginPage.clicksOutsideTheUsernameField();
    }

    @Then("Username field validation {string} should be triggered")
    public void usernameFieldValidationShouldBeTriggered(String validation_result) {
        CRMLoginPage.usernameFieldValidationShouldBeTriggered(validation_result);
    }




    //  scenario - loginNegativeTest

    @Then("User should remain on login page")
    public void userShouldRemainOnLoginPage() {
        CRMLoginPage.userShouldRemainOnLoginPage();
    }

    @And("Error message {string} should be displayed and validate with {string}")
    public void errorMessageShouldBeDisplayedAndValidateWith(String err_message, String test_description) {
        CRMLoginPage.errorMessageShouldBeDisplayedAndValidateWith(err_message, test_description);
    }




    //  scenario - loginPasswordMasking

    @When("User enters password in the password field")
    public void userEntersPasswordInThePasswordField() {
        CRMLoginPage.userEntersPasswordInThePasswordField();
    }

    @Then("Password should be masked with asterisks")
    public void passwordShouldBeMaskedWithAsterisks() {
        CRMLoginPage.passwordShouldBeMaskedWithAsterisks();

    }

    @And("ShowOrHide password toggle should be available")
    public void showorhidePasswordToggleShouldBeAvailable() {
        CRMLoginPage.showorhidePasswordToggleShouldBeAvailable();
    }

}
