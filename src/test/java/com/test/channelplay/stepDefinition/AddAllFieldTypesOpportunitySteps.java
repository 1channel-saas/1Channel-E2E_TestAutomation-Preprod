package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddAllFieldTypesOpportunityObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAllFieldTypesOpportunitySteps extends DriverBase {

    AddAllFieldTypesOpportunityObject addAllFieldTypesOpportunityObject = new AddAllFieldTypesOpportunityObject();
    CommonUtils commonUtils = new CommonUtils();

    @Given("after login to crm user will be on the Opportunity page under Settings")
    public void afterLoginToCrmUserWillBeOnTheContactPageUnderSettings() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addAllFieldTypesOpportunityObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select Add Field button under Opportunity")
    public void userSelectAddFieldButton() {
        addAllFieldTypesOpportunityObject.UserSelectAddFieldButton();
    }


    @When("user select numeric from the field type dropdown under Opportunity")
    public void userSelectNumericFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectNumericFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Numeric type under Opportunity")
    public void enterFieldNameForNumericType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForNumericType();
    }

    @And("select Save button under Opportunity")
    public void selectSaveButton() {
        addAllFieldTypesOpportunityObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under Opportunity")
    public void newFieldWillBeShownInTheList() {
        addAllFieldTypesOpportunityObject.NewFieldWillBeShownInTheList();
    }


    @When("user select Multi Select Dropdown from the field type dropdown under Opportunity")
    public void userSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectMultiSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Multi Select Dropdown type under Opportunity")
    public void enterFieldNameForMultiSelectDropdownType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForMultiSelectDropdownType();
    }

    @And("enter Options under Opportunity")
    public void enterOptions() {
        addAllFieldTypesOpportunityObject.enterOptions();
    }


    @When("user select Date from the field type dropdown under Opportunity")
    public void userSelectDateFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectDateFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Date type under Opportunity")
    public void enterFieldNameForDateType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForDateType();
    }

    @And("select Value Type as Custom under Opportunity")
    public void selectValueTypeAsCustom() {
        addAllFieldTypesOpportunityObject.SelectValueTypeAsCustom();
    }

    @When("user select Email from the field type dropdown under Opportunity")
    public void userSelectEmailFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectEmailFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Email type under Opportunity")
    public void enterFieldNameForEmailType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForEmailType();
    }

    @When("user select Document Upload from the field type dropdown under Opportunity")
    public void userSelectDocumentUploadFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectDocumentUploadFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Document Upload type under Opportunity")
    public void enterFieldNameForDocumentUploadType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForDocumentUploadType();
    }

    @When("user select Video from the field type dropdown under Opportunity")
    public void userSelectVideoFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectVideoFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Video type under Opportunity")
    public void enterFieldNameForVideoType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForVideoType();
    }

    @When("user select Reference Documents from the field type dropdown under Opportunity")
    public void userSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectReferenceDocumentsFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Reference Documents type under Opportunity")
    public void enterFieldNameForReferenceDocumentsType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForReferenceDocumentsType();
    }

    @And("upload a file under Upload File under Opportunity")
    public void uploadAFileUnderUploadFile() {
        addAllFieldTypesOpportunityObject.UploadAFileUnderUploadFile();
    }

    @When("user select Header Text from the field type dropdown under Opportunity")
    public void userSelectHeaderTextFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectHeaderTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Header Text type under Opportunity")
    public void enterFieldNameForHeaderTextType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForHeaderTextType();
    }

    @When("user select Normal Text from the field type dropdown under Opportunity")
    public void userSelectNormalTextFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectNormalTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Normal Text type under Opportunity")
    public void enterFieldNameForNormalTextType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForNormalTextType();
    }

    @When("user select Page Separator from the field type dropdown under Opportunity")
    public void userSelectPageSeparatorFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectPageSeparatorFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Page Separator type under Opportunity")
    public void enterFieldNameForPageSeparatorType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForPageSeparatorType();
    }

    @When("user select Data List from the field type dropdown under Opportunity")
    public void userSelectDataListFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectDataListFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Data List type under Opportunity")
    public void enterFieldNameForDataListType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameForDataListType();
    }

    @And("select List Name under Opportunity")
    public void selectListName() {
        addAllFieldTypesOpportunityObject.SelectListName();
    }

    @When("user select OTP Validation from the field type dropdown under Opportunity")
    public void userSelectOTPValidationFromTheFieldTypeDropdown() {
        addAllFieldTypesOpportunityObject.UserSelectOTPValidationFromTheFieldTypeDropdown();
    }

    @And("enter Field Name OTP Validation type under Opportunity")
    public void enterFieldNameOTPValidationType() {
        addAllFieldTypesOpportunityObject.EnterFieldNameOTPValidationType();
    }

    @And("enter Entity under Opportunity")
    public void enterEntity() {
        addAllFieldTypesOpportunityObject.EnterEntity();
    }

    @And("select Entity Field under Opportunity")
    public void selectEntityField() {
        addAllFieldTypesOpportunityObject.SelectEntityField();
    }

}
