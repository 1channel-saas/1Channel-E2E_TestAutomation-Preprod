package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.object.CreateCustomActivityWithAllFieldTypesObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.*;
import io.cucumber.java.en.And;

public class CreateCustomActivityWithAllFieldTypesSteps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    CreateCustomActivityWithAllFieldTypesObject createCustomActivityWithAllFieldTypesObject = new CreateCustomActivityWithAllFieldTypesObject();
    Assistive_Login login = new Assistive_Login();

    @Given("after login to CRM user will be on the Activities screen under Admin menu")
    public void afterLoginToCRMUserWillBeOnTheActivitiesScreenUnderAdminMenu() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        createCustomActivityWithAllFieldTypesObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @When("user select Add button under Activity screen")
    public void userSelectAddButtonUnderActivityScreen() {
        createCustomActivityWithAllFieldTypesObject.UserSelectAddButtonUnderActivityScreen();
    }

    @And("user enter Activity Name and select Customer and Opportunity")
    public void userEnterActivityName() {
        createCustomActivityWithAllFieldTypesObject.UserEnterActivityName();
    }

    @And("hit Save button")
    public void hitSaveButton() {
        createCustomActivityWithAllFieldTypesObject.HitSaveButton();
    }

    @Then("new activity will create and will show in the activity list")
    public void newActivityWillCreateAndWillShowInTheActivityList() {
        createCustomActivityWithAllFieldTypesObject.NewActivityWillCreateAndWillShowInTheActivityList();
    }

    @And("user select Action button for new created Activity")
    public void userSelectActionButtonForNewCreatedActivity() {
        createCustomActivityWithAllFieldTypesObject.UserSelectActionButtonForNewCreatedActivity();
    }

    @And("select Add Field button under Activity")
    public void selectAddFieldButtonUnderActivity() {
        createCustomActivityWithAllFieldTypesObject.SelectAddFieldButtonUnderActivity();
    }

    @And("user select Email field type from the field type dropdown under Activity")
    public void userSelectEmailFieldTypeFromTheFieldTypeDropdownUnderActivity() {
        String field_type = "Numeric";
        createCustomActivityWithAllFieldTypesObject.userSelectExpectedFromTheFieldTypeDropdownUnderActivity(field_type);
    }

    @And("enter field name for Email under Activity")
    public void enterFieldNameForEmailUnderActivity() {
        String field_name = "Phone Number";
        createCustomActivityWithAllFieldTypesObject.enterForUnderActivity(field_name);
    }

    @And("select Save button under Activity")
    public void selectSaveButtonUnderActivity() {
        createCustomActivityWithAllFieldTypesObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under Activity")
    public void newFieldWillBeShownInTheList() {
        createCustomActivityWithAllFieldTypesObject.NewFieldWillBeShownInTheList();
    }

    @And("user select Numeric field type from the field type dropdown under Activity")
    public void userSelectNumericFieldTypeFromTheFieldTypeDropdownUnderActivity() {
        String field_type = "Email";
        createCustomActivityWithAllFieldTypesObject.userSelectExpectedFromTheFieldTypeDropdownUnderActivity(field_type);
    }

    @And("enter field name for Numeric under Activity")
    public void enterFieldNameForNumericUnderActivity() {
        String field_name = "Registered Email";
        createCustomActivityWithAllFieldTypesObject.enterForUnderActivity(field_name);
    }

    @And("user select Normal Text from the field type dropdown under Activity")
    public void userSelectNormalTextFromTheFieldTypeDropdownUnderActivity() {
        String field_type = "Normal Text";
        createCustomActivityWithAllFieldTypesObject.userSelectExpectedFromTheFieldTypeDropdownUnderActivity(field_type);
    }

    @And("enter field name for Normal Text under Activity")
    public void enterFieldNameForNormalTextUnderActivity() {
        String field_name = "Remarks";
        createCustomActivityWithAllFieldTypesObject.enterForUnderActivity(field_name);
    }


    @And("user select CRM menu")
    public void userSelectCRMMenu() {
        createCustomActivityWithAllFieldTypesObject.UserSelectCRMMenu();
    }

    @And("user select Customers")
    public void userSelectCustomers() {
        createCustomActivityWithAllFieldTypesObject.UserSelectCustomers();
    }

    @And("select Dashboard Button for any customer from the list")
    public void selectDashboardButtonForAnyCustomerFromTheList() {
        createCustomActivityWithAllFieldTypesObject.SelectDashboardButtonForAnyAnyCustomerFromTheList();
    }

    @And("select Add activities button")
    public void selectAddActivitiesButton() {
        createCustomActivityWithAllFieldTypesObject.SelectAddActivitiesButton();
    }

    @And("user select new created Activity")
    public void userSelectNewCreatedActivity() {
        createCustomActivityWithAllFieldTypesObject.UserSelectNewCreatedActivity();
    }

    @Then("All new created fields will show in the form")
    public void allNewCreatedFieldsWillShowInTheForm() {
        createCustomActivityWithAllFieldTypesObject.AllNewCreatedFieldsWillShowInTheForm();
    }

    @And("user select Opportunity")
    public void userSelectOpportunity() {
        createCustomActivityWithAllFieldTypesObject.UserSelectOpportunity();
    }

    @And("select Dashboard Button for any opportunity from the list")
    public void selectDashboardButtonForAnyOpportunityFromTheList() {
        createCustomActivityWithAllFieldTypesObject.SelectDashboardButtonForAnyOpportunityFromTheList();
    }

    @And("select opportunity")
    public void selectOpportunity() {
        createCustomActivityWithAllFieldTypesObject.SelectOpportunity();
    }

    @And("enter title")
    public void enterTitle() {
        createCustomActivityWithAllFieldTypesObject.EnterTitle();
    }

    @And("enter Registered Email")
    public void enterRegisteredEmail() {
        createCustomActivityWithAllFieldTypesObject.EnterRegisteredEmail();
    }

    @And("enter Phone Number")
    public void enterPhoneNumber() {
        createCustomActivityWithAllFieldTypesObject.EnterPhoneNumber();
    }

    @And("select save button under Activity form")
    public void selectSaveButtonUnderActivityForm() {
        createCustomActivityWithAllFieldTypesObject.SelectSaveButtonUnderActivityForm();
    }

    @Then("new activity will show in the dashboard")
    public void newActivityWillShowInTheDashboard() {
        createCustomActivityWithAllFieldTypesObject.NewActivityWillShowInTheDashboard();
    }

    @And("select Add activities button for opportunity")
    public void selectAddActivitiesButtonForOpportunity() {
        createCustomActivityWithAllFieldTypesObject.SelectAddActivitiesButtonForOpportunity();
    }
}
