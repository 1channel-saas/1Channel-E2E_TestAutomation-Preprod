package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddAllFieldTypesCustomerObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



public class AddAllFieldTypesCustomerSteps extends DriverBase {

    AddAllFieldTypesCustomerObject addAllFieldTypesCustomerObject = new AddAllFieldTypesCustomerObject();
    CommonUtils commonUtils = new CommonUtils();

    @Given("after login to crm user will be on the Customer page under Settings")
    public void afterLoginToCrmUserWillBeOnTheCustomerPageUnderSettings() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addAllFieldTypesCustomerObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select Add Field button under Customer")
    public void userSelectAddFieldButton() {
        addAllFieldTypesCustomerObject.UserSelectAddFieldButton();
    }


    @When("user select numeric from the field type dropdown under Customer")
    public void userSelectNumericFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectNumericFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Numeric type under Customer")
    public void enterFieldNameForNumericType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForNumericType();
    }

    @And("select Save button under Customer")
    public void selectSaveButton() {
        addAllFieldTypesCustomerObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under Customer")
    public void newFieldWillBeShownInTheList() {
        addAllFieldTypesCustomerObject.NewFieldWillBeShownInTheList();
    }


    @When("user select Multi Select Dropdown from the field type dropdown under Customer")
    public void userSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectMultiSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Multi Select Dropdown type under Customer")
    public void enterFieldNameForMultiSelectDropdownType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForMultiSelectDropdownType();
    }

    @And("enter Options under Customer")
    public void enterOptions() {
        addAllFieldTypesCustomerObject.enterOptions();
    }


    @When("user select Date from the field type dropdown under Customer")
    public void userSelectDateFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectDateFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Date type under Customer")
    public void enterFieldNameForDateType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForDateType();
    }

    @And("select Value Type as Custom under Customer")
    public void selectValueTypeAsCustom() {
        addAllFieldTypesCustomerObject.SelectValueTypeAsCustom();
    }

    @When("user select Email from the field type dropdown under Customer")
    public void userSelectEmailFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectEmailFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Email type under Customer")
    public void enterFieldNameForEmailType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForEmailType();
    }

    @When("user select Document Upload from the field type dropdown under Customer")
    public void userSelectDocumentUploadFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectDocumentUploadFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Document Upload type under Customer")
    public void enterFieldNameForDocumentUploadType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForDocumentUploadType();
    }

    @When("user select Video from the field type dropdown under Customer")
    public void userSelectVideoFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectVideoFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Video type under Customer")
    public void enterFieldNameForVideoType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForVideoType();
    }

    @When("user select Reference Documents from the field type dropdown under Customer")
    public void userSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectReferenceDocumentsFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Reference Documents type under Customer")
    public void enterFieldNameForReferenceDocumentsType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForReferenceDocumentsType();
    }

    @And("upload a file under Upload File under Customer")
    public void uploadAFileUnderUploadFile() {
        addAllFieldTypesCustomerObject.UploadAFileUnderUploadFile();
    }

    @When("user select Header Text from the field type dropdown under Customer")
    public void userSelectHeaderTextFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectHeaderTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Header Text type under Customer")
    public void enterFieldNameForHeaderTextType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForHeaderTextType();
    }

    @When("user select Normal Text from the field type dropdown under Customer")
    public void userSelectNormalTextFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectNormalTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Normal Text type under Customer")
    public void enterFieldNameForNormalTextType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForNormalTextType();
    }

    @When("user select Page Separator from the field type dropdown under Customer")
    public void userSelectPageSeparatorFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectPageSeparatorFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Page Separator type under Customer")
    public void enterFieldNameForPageSeparatorType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForPageSeparatorType();
    }

    @When("user select Data List from the field type dropdown under Customer")
    public void userSelectDataListFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectDataListFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Data List type under Customer")
    public void enterFieldNameForDataListType() {
        addAllFieldTypesCustomerObject.EnterFieldNameForDataListType();
    }

    @And("select List Name under Customer")
    public void selectListName() {
        addAllFieldTypesCustomerObject.SelectListName();
    }

    @When("user select OTP Validation from the field type dropdown under Customer")
    public void userSelectOTPValidationFromTheFieldTypeDropdown() {
        addAllFieldTypesCustomerObject.UserSelectOTPValidationFromTheFieldTypeDropdown();
    }

    @And("enter Field Name OTP Validation type under Customer")
    public void enterFieldNameOTPValidationType() {
        addAllFieldTypesCustomerObject.EnterFieldNameOTPValidationType();
    }

    @And("enter Entity under Customer")
    public void enterEntity() {
        addAllFieldTypesCustomerObject.EnterEntity();
    }

    @And("select Entity Field under Customer")
    public void selectEntityField() {
        addAllFieldTypesCustomerObject.SelectEntityField();
    }

}
