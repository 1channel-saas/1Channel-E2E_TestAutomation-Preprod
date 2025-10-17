package com.test.channelplay.stepDefinition.activities;

import com.test.channelplay.object.activities.AddActivity_testUserPage;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.en.Then;

public class AddActivity_testUserSteps extends DriverBase {

    AddActivity_testUserPage addActivity = new AddActivity_testUserPage();




    //  scenario - offsiteActivity

    // # Portal UI

    @Then("User clicks on CRM menu and Activities submenu to validate offsiteActivity")
    public void userClicksOnCRMMenuAndActivitiesSubmenuToValidateOffsiteActivity() {
        addActivity.userClicksOnCRMMenuAndActivitiesSubmenu();
    }

    @Then("clicks on Filter and search with serialNo of offsiteActivity fetched from App")
    public void clicksOnFilterAndSearchWithSerialNoOfOffsiteActivityFetchedFromApp() {
        addActivity.clicksOnFilterAndSearchWithSerialNoOfOffsiteActivityFetchedFromApp();
    }

    @Then("clicks on Edit activity and validate data then delete the activity")
    public void clicksOnEditActivityAndValidateDataThenDeleteTheActivity() {
        addActivity.clicksOnEditActivityAndValidateDataThenDelete();
    }

}
