package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.CRMloginObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CRMloginSteps extends DriverBase {
    CommonUtils commonUtils = new CommonUtils();
    CRMloginObject crMloginObject = new CRMloginObject();
    @Given("User is on the login page")
    public void userIsOnTheLoginPage() {
        getDriver().get(GetProperty.value("crmPortal"));
        commonUtils.validatePage("Assistive");
    }

    @When("User enter email {string}")
    public void userEnterEmail(String arg0) {
        crMloginObject.UserEnterEmail(arg0);
    }

    @And("User enter password {string}")
    public void userEnterPassword(String arg0) {
        crMloginObject.UserEnterPassword(arg0);
    }

    @And("User click on SignIn button")
    public void userClickOnSignInButton() {
        crMloginObject.UserClickSignin();
    }

    @Then("User will be on the homepage")
    public void userWillBeOnTheHomepage() {
        crMloginObject.UserWillBeOnTheHomepage();

    }


    @When("User enters valid {string} and invalid {string}")
    public void userEntersValidAndInvalid(String arg0 , String arg1) {
        crMloginObject.UserEntersValidAndInvalid(arg0 , arg1);
    }


    @Then("User should not logged in and Validation message should be shown as {string}")
    public void userShouldNotLoggedInAndValidationMessageShouldBeShownAs(String arg0) {
        crMloginObject.UserShouldNotLoggedInAndValidationMessageShouldBeShownAs();
    }



}
