package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddAllFieldTypesProductObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



public class AddAllTypeFieldsProductSteps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    AddAllFieldTypesProductObject addAllFieldTypesProductObject = new AddAllFieldTypesProductObject();

    @Given("after login to crm user will be on the Product page under Admin under Settings")
    public void afterLoginToCrmUserWillBeOnTheUserPageUnderAdminUnderSettings() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addAllFieldTypesProductObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select Add Field button under Product")
    public void userSelectAddFieldButton() {
        addAllFieldTypesProductObject.UserSelectAddFieldButton();
    }

    @When("user select text-multi-line from the field type dropdown under Product")
    public void userSelectTextMultiLineFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectTextMultiLineFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for text-multi-line type under Product")
    public void enterFieldNameForTextMultiLineType() {
        addAllFieldTypesProductObject.EnterFieldNameForTextMultiLineType();
    }

    @And("select Save button under Product")
    public void selectSaveButton() {
        addAllFieldTypesProductObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under Product")
    public void newFieldWillBeShownInTheList() {
        addAllFieldTypesProductObject.NewFieldWillBeShownInTheList();
    }

    @When("user select text-single-line from the field type dropdown under Product")
    public void userSelectTextSingleLineFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectTextSingleLineFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for text-single-line type under Product")
    public void enterFieldNameForTextSingleLineType() {
        addAllFieldTypesProductObject.EnterFieldNameForTextSingleLineType();
    }

    @When("user select numeric from the field type dropdown under Product")
    public void userSelectNumericFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectNumericFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Numeric type under Product")
    public void enterFieldNameForNumericType() {
        addAllFieldTypesProductObject.EnterFieldNameForNumericType();
    }

    @When("user select Single Select Dropdown from the field type dropdown under Product")
    public void userSelectSingleSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectSingleSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Single Select Dropdown type under Product")
    public void enterFieldNameForSingleSelectDropdownType() {
        addAllFieldTypesProductObject.EnterFieldNameForSingleSelectDropdownType();
    }

    @When("user select Multi Select Dropdown from the field type dropdown under Product")
    public void userSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectMultiSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Multi Select Dropdown type under Product")
    public void enterFieldNameForMultiSelectDropdownType() {
        addAllFieldTypesProductObject.EnterFieldNameForMultiSelectDropdownType();
    }

    @And("enter Options under Product")
    public void enterOptions() {
        addAllFieldTypesProductObject.enterOptions();
    }

    @When("user select Date from the field type dropdown under Product")
    public void userSelectDateFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectDateFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Date type under Product")
    public void enterFieldNameForDateType() {
        addAllFieldTypesProductObject.EnterFieldNameForDateType();
    }

    @And("select Value Type as Custom under Product")
    public void selectValueTypeAsCustom() {
        addAllFieldTypesProductObject.SelectValueTypeAsCustom();
    }

    @When("user select Email from the field type dropdown under Product")
    public void userSelectEmailFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectEmailFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Email type under Product")
    public void enterFieldNameForEmailType() {
        addAllFieldTypesProductObject.EnterFieldNameForEmailType();
    }

    @When("user select Image from the field type dropdown under Product")
    public void userSelectImageFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectImageFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Image type under Product")
    public void enterFieldNameForImageType() {
        addAllFieldTypesProductObject.EnterFieldNameForImageType();
    }

    @When("user select Document Upload from the field type dropdown under Product")
    public void userSelectDocumentUploadFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectDocumentUploadFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Document Upload type under Product")
    public void enterFieldNameForDocumentUploadType() {
        addAllFieldTypesProductObject.EnterFieldNameForDocumentUploadType();
    }

    @When("user select Video from the field type dropdown under Product")
    public void userSelectVideoFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectVideoFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Video type under Product")
    public void enterFieldNameForVideoType() {
        addAllFieldTypesProductObject.EnterFieldNameForVideoType();
    }

    @When("user select Reference Documents from the field type dropdown under Product")
    public void userSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectReferenceDocumentsFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Reference Documents type under Product")
    public void enterFieldNameForReferenceDocumentsType() {
        addAllFieldTypesProductObject.EnterFieldNameForReferenceDocumentsType();
    }

    @And("upload a file under Upload File under Product")
    public void uploadAFileUnderUploadFile() {
        addAllFieldTypesProductObject.UploadAFileUnderUploadFile();
    }

    @When("user select Header Text from the field type dropdown under Product")
    public void userSelectHeaderTextFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectHeaderTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Header Text type under Product")
    public void enterFieldNameForHeaderTextType() {
        addAllFieldTypesProductObject.EnterFieldNameForHeaderTextType();
    }

    @When("user select Normal Text from the field type dropdown under Product")
    public void userSelectNormalTextFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectNormalTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Normal Text type under Product")
    public void enterFieldNameForNormalTextType() {
        addAllFieldTypesProductObject.EnterFieldNameForNormalTextType();
    }

    @When("user select Page Separator from the field type dropdown under Product")
    public void userSelectPageSeparatorFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectPageSeparatorFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Page Separator type under Product")
    public void enterFieldNameForPageSeparatorType() {
        addAllFieldTypesProductObject.EnterFieldNameForPageSeparatorType();
    }

    @When("user select Link from other entity from the field type dropdown under Product")
    public void userSelectLinkFromOtherEntityFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectLinkFromOtherEntityFromTheFieldTypeDropdown();
    }

    @And("enter Field Name Link from other entity type under Product")
    public void enterFieldNameLinkFromOtherEntityType() {
        addAllFieldTypesProductObject.EnterFieldNameLinkFromOtherEntityType();
    }

    @When("user select Data List from the field type dropdown under Product")
    public void userSelectDataListFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectDataListFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Data List type under Product")
    public void enterFieldNameForDataListType() {
        addAllFieldTypesProductObject.EnterFieldNameForDataListType();
    }

    @And("select List Name under Product")
    public void selectListName() {
        addAllFieldTypesProductObject.SelectListName();
    }

    @When("user select OTP Validation from the field type dropdown under Product")
    public void userSelectOTPValidationFromTheFieldTypeDropdown() {
        addAllFieldTypesProductObject.UserSelectOTPValidationFromTheFieldTypeDropdown();
    }

    @And("enter Field Name OTP Validation type under Product")
    public void enterFieldNameOTPValidationType() {
        addAllFieldTypesProductObject.EnterFieldNameOTPValidationType();
    }

    @And("enter Entity under Product")
    public void enterEntity() {
        addAllFieldTypesProductObject.EnterEntity();
    }

    @And("select Entity Field under Product")
    public void selectEntityField() {
        addAllFieldTypesProductObject.SelectEntityField();
    }


}
