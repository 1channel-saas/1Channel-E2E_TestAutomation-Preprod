package com.test.channelplay.stepDefinition_Mobile.activities;

import com.test.channelplay.mobile.screens.activities.AddActivityApp_testUserPage;
import com.test.channelplay.utils.MobileTestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.GetApiResponseObject;

public class AddActivityApp_testUserSteps extends MobileTestBase {

    String projectName, firstName;
    JsonPath json;
    AddActivityApp_testUserPage addActivityTestUserPage = new AddActivityApp_testUserPage();
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();

    public AddActivityApp_testUserSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }




    //  scenario - offsiteActivity

    //  # App UI

    @When("Clicks on Activities menu for offsiteActivity")
    public void clicksOnActivitiesMenuForOffsiteActivity() {
        addActivityTestUserPage.clicksOnActivitiesMenu();
    }

    @Then("Clicks on offsite activity option for offsiteActivity")
    public void clicksOnOffsiteActivityOptionForOffsiteActivity() {
        addActivityTestUserPage.clicksOnOffsiteActivityOption();
    }

    @Then("Clicks on Add button to add new offsite activity for offsiteActivity")
    public void clicksOnAddButtonToAddNewOffsiteActivityForOffsiteActivity() {
        addActivityTestUserPage.clicksOnAddButtonToAddNewOffsiteActivity();
    }

    @Then("select customer {string} from Select Customer dropdown for offsiteActivity")
    public void selectCustomerFromSelectCustomerDropdownForOffsiteActivity(String customerName) {
        addActivityTestUserPage.selectCustomerFromSelectCustomerDropdown(customerName);
    }

    @Then("Clicks on OK button on customer selection frame for offsiteActivity")
    public void clicksOnOKButtonOnCustomerSelectionFrameForOffsiteActivity() {
        addActivityTestUserPage.clicksOnOKButtonOnCustomerSelectionFrame();
    }

    @Then("Enter text into description box for offsiteActivity")
    public void enterTextIntoDescriptionBoxForOffsiteActivity() {
        addActivityTestUserPage.enterTextIntoDescriptionBox("Test activity description");
    }

    @Then("Enter name into title {string} field for offsiteActivity")
    public void enterNameIntoTitleFieldForOffsiteActivity(String title) {
        addActivityTestUserPage.enterNameIntoTitleField(title);
    }

    @Then("Select date {string} in perform Date field for offsiteActivity")
    public void selectDateInPerformDateFieldForOffsiteActivity(String performDate) {
        addActivityTestUserPage.selectDateInPerformDateField();
    }

    @And("add image in image field for offsiteActivity")
    public void addImageInImageFieldForOffsiteActivity() {
        addActivityTestUserPage.addImageInImageField();
    }

    @Then("click on Save to submit offsite activity")
    public void clickOnSaveToSubmitOffsiteActivity() {
        addActivityTestUserPage.clickOnSaveToSubmitOffsiteActivity();
    }

    @And("verify Activity is showing with {string} in list and fetch activity details for validation for offsiteActivity")
    public void verifyActivityIsShowingWithInListAndFetchActivityDetailsForValidationForOffsiteActivity(String customerName) {
        addActivityTestUserPage.verifyActivityIsShowingInListAndFetchActivityDetailsForValidation(customerName);
    }

}
