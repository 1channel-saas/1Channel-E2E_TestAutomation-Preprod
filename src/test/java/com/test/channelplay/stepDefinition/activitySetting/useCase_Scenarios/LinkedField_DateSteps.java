package com.test.channelplay.stepDefinition.activitySetting.useCase_Scenarios;

import com.test.channelplay.object.activitySetting.useCase_Scenarios.LinkedField_DateObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class LinkedField_DateSteps extends DriverBase {

    LinkedField_DateObject linkedFieldDateObject = new LinkedField_DateObject();
    CommonUtils commonUtils = new CommonUtils();




    //  scenario - useCase_Scenario_b2b-2943

    @Then("User clicks on Admin menu and Activities submenu and navigates to Activities page")
    public void userClicksOnAdminMenuAndActivitiesSubmenuAndNavigatesToActivitiesPage() {
        linkedFieldDateObject.userClicksOnAdminMenuAndActivitiesSubmenuAndNavigatesToActivitiesPage();
    }

    @And("Enter into custom Activity {string} page and clicks on Add Field button to create a new date type linked field")
    public void enterIntoCustomActivityPageAndClicksOnAddFieldButtonToCreateANewDateTypeLinkedField(String customAct) {
        linkedFieldDateObject.enterIntoCustomActivityPageAndClicksOnAddFieldButtonToCreateANewDateTypeLinkedField();
    }

    @Then("delete the custom activity after validation is complete for b2b-2943")
    public void deleteTheCustomActivityAfterValidationIsCompleteForBB() {
        linkedFieldDateObject.deleteTheCustomActivityAfterValidationIsCompleteForBB();
    }

}
