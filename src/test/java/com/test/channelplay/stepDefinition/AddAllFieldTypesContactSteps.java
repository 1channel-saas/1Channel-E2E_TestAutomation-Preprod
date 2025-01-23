package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddAllFieldTypesContactObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddAllFieldTypesContactSteps extends DriverBase {

    AddAllFieldTypesContactObject addAllFieldTypesContactObject = new AddAllFieldTypesContactObject();
    CommonUtils commonUtils = new CommonUtils();

    @Given("after login to crm user will be on the Contact page under Settings")
    public void afterLoginToCrmUserWillBeOnTheContactPageUnderSettings() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addAllFieldTypesContactObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select Add Field button under Contact")
    public void userSelectAddFieldButton() {
        addAllFieldTypesContactObject.UserSelectAddFieldButton();
    }


    @When("user select numeric from the field type dropdown under Contact")
    public void userSelectNumericFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectNumericFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Numeric type under Contact")
    public void enterFieldNameForNumericType() {
        addAllFieldTypesContactObject.EnterFieldNameForNumericType();
    }

    @And("select Save button under Contact")
    public void selectSaveButton() {
        addAllFieldTypesContactObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under Contact")
    public void newFieldWillBeShownInTheList() {
        addAllFieldTypesContactObject.NewFieldWillBeShownInTheList();
    }


    @When("user select Multi Select Dropdown from the field type dropdown under Contact")
    public void userSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectMultiSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Multi Select Dropdown type under Contact")
    public void enterFieldNameForMultiSelectDropdownType() {
        addAllFieldTypesContactObject.EnterFieldNameForMultiSelectDropdownType();
    }

    @And("enter Options under Contact")
    public void enterOptions() {
        addAllFieldTypesContactObject.enterOptions();
    }


    @When("user select Date from the field type dropdown under Contact")
    public void userSelectDateFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectDateFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Date type under Contact")
    public void enterFieldNameForDateType() {
        addAllFieldTypesContactObject.EnterFieldNameForDateType();
    }

    @And("select Value Type as Custom under Contact")
    public void selectValueTypeAsCustom() {
        addAllFieldTypesContactObject.SelectValueTypeAsCustom();
    }

    @When("user select Email from the field type dropdown under Contact")
    public void userSelectEmailFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectEmailFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Email type under Contact")
    public void enterFieldNameForEmailType() {
        addAllFieldTypesContactObject.EnterFieldNameForEmailType();
    }

    @When("user select Document Upload from the field type dropdown under Contact")
    public void userSelectDocumentUploadFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectDocumentUploadFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Document Upload type under Contact")
    public void enterFieldNameForDocumentUploadType() {
        addAllFieldTypesContactObject.EnterFieldNameForDocumentUploadType();
    }

    @When("user select Video from the field type dropdown under Contact")
    public void userSelectVideoFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectVideoFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Video type under Contact")
    public void enterFieldNameForVideoType() {
        addAllFieldTypesContactObject.EnterFieldNameForVideoType();
    }

    @When("user select Reference Documents from the field type dropdown under Contact")
    public void userSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectReferenceDocumentsFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Reference Documents type under Contact")
    public void enterFieldNameForReferenceDocumentsType() {
        addAllFieldTypesContactObject.EnterFieldNameForReferenceDocumentsType();
    }

    @And("upload a file under Upload File under Contact")
    public void uploadAFileUnderUploadFile() {
        addAllFieldTypesContactObject.UploadAFileUnderUploadFile();
    }

    @When("user select Header Text from the field type dropdown under Contact")
    public void userSelectHeaderTextFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectHeaderTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Header Text type under Contact")
    public void enterFieldNameForHeaderTextType() {
        addAllFieldTypesContactObject.EnterFieldNameForHeaderTextType();
    }

    @When("user select Normal Text from the field type dropdown under Contact")
    public void userSelectNormalTextFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectNormalTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Normal Text type under Contact")
    public void enterFieldNameForNormalTextType() {
        addAllFieldTypesContactObject.EnterFieldNameForNormalTextType();
    }

    @When("user select Page Separator from the field type dropdown under Contact")
    public void userSelectPageSeparatorFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectPageSeparatorFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Page Separator type under Contact")
    public void enterFieldNameForPageSeparatorType() {
        addAllFieldTypesContactObject.EnterFieldNameForPageSeparatorType();
    }

    @When("user select Data List from the field type dropdown under Contact")
    public void userSelectDataListFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectDataListFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Data List type under Contact")
    public void enterFieldNameForDataListType() {
        addAllFieldTypesContactObject.EnterFieldNameForDataListType();
    }

    @And("select List Name under Contact")
    public void selectListName() {
        addAllFieldTypesContactObject.SelectListName();
    }

    @When("user select OTP Validation from the field type dropdown under Contact")
    public void userSelectOTPValidationFromTheFieldTypeDropdown() {
        addAllFieldTypesContactObject.UserSelectOTPValidationFromTheFieldTypeDropdown();
    }

    @And("enter Field Name OTP Validation type under Contact")
    public void enterFieldNameOTPValidationType() {
        addAllFieldTypesContactObject.EnterFieldNameOTPValidationType();
    }

    @And("enter Entity under Contact")
    public void enterEntity() {
        addAllFieldTypesContactObject.EnterEntity();
    }

    @And("select Entity Field under Contact")
    public void selectEntityField() {
        addAllFieldTypesContactObject.SelectEntityField();
    }

}
