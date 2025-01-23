package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddAllFieldTypesUserObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



public class AddAllTypeFieldsUserSteps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    AddAllFieldTypesUserObject addAllFieldTypesUserObject = new AddAllFieldTypesUserObject();

    @Given("after login to crm user will be on the User page under Admin under Settings")
    public void afterLoginToCrmUserWillBeOnTheUserPageUnderAdminUnderSettings() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addAllFieldTypesUserObject.login(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select Add Field button under User")
    public void userSelectAddFieldButton() {
        addAllFieldTypesUserObject.UserSelectAddFieldButton();
    }

    @When("user select text-multi-line from the field type dropdown under User")
    public void userSelectTextMultiLineFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectTextMultiLineFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for text-multi-line type under User")
    public void enterFieldNameForTextMultiLineType() {
        addAllFieldTypesUserObject.EnterFieldNameForTextMultiLineType();
    }

    @And("select Save button under User")
    public void selectSaveButton() {
        addAllFieldTypesUserObject.SelectSaveButton();
    }

    @Then("new field will be shown in the list under User")
    public void newFieldWillBeShownInTheList() {
        addAllFieldTypesUserObject.NewFieldWillBeShownInTheList();
    }

    @When("user select text-single-line from the field type dropdown under User")
    public void userSelectTextSingleLineFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectTextSingleLineFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for text-single-line type under User")
    public void enterFieldNameForTextSingleLineType() {
        addAllFieldTypesUserObject.EnterFieldNameForTextSingleLineType();
    }

    @When("user select numeric from the field type dropdown under User")
    public void userSelectNumericFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectNumericFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Numeric type under User")
    public void enterFieldNameForNumericType() {
        addAllFieldTypesUserObject.EnterFieldNameForNumericType();
    }

    @When("user select Single Select Dropdown from the field type dropdown under User")
    public void userSelectSingleSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectSingleSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Single Select Dropdown type under User")
    public void enterFieldNameForSingleSelectDropdownType() {
        addAllFieldTypesUserObject.EnterFieldNameForSingleSelectDropdownType();
    }

    @When("user select Multi Select Dropdown from the field type dropdown under User")
    public void userSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectMultiSelectDropdownFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Multi Select Dropdown type under User")
    public void enterFieldNameForMultiSelectDropdownType() {
        addAllFieldTypesUserObject.EnterFieldNameForMultiSelectDropdownType();
    }

    @And("enter Options under User")
    public void enterOptions() {
        addAllFieldTypesUserObject.enterOptions();
    }

    @When("user select Date from the field type dropdown under User")
    public void userSelectDateFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectDateFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Date type under User")
    public void enterFieldNameForDateType() {
        addAllFieldTypesUserObject.EnterFieldNameForDateType();
    }

    @And("select Value Type as Custom under User")
    public void selectValueTypeAsCustom() {
        addAllFieldTypesUserObject.SelectValueTypeAsCustom();
    }

    @When("user select Email from the field type dropdown under User")
    public void userSelectEmailFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectEmailFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Email type under User")
    public void enterFieldNameForEmailType() {
        addAllFieldTypesUserObject.EnterFieldNameForEmailType();
    }

    @When("user select Image from the field type dropdown under User")
    public void userSelectImageFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectImageFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Image type under User")
    public void enterFieldNameForImageType() {
        addAllFieldTypesUserObject.EnterFieldNameForImageType();
    }

    @When("user select Document Upload from the field type dropdown under User")
    public void userSelectDocumentUploadFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectDocumentUploadFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Document Upload type under User")
    public void enterFieldNameForDocumentUploadType() {
        addAllFieldTypesUserObject.EnterFieldNameForDocumentUploadType();
    }

    @When("user select Video from the field type dropdown under User")
    public void userSelectVideoFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectVideoFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Video type under User")
    public void enterFieldNameForVideoType() {
        addAllFieldTypesUserObject.EnterFieldNameForVideoType();
    }

    @When("user select Reference Documents from the field type dropdown under User")
    public void userSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectReferenceDocumentsFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Reference Documents type under User")
    public void enterFieldNameForReferenceDocumentsType() {
        addAllFieldTypesUserObject.EnterFieldNameForReferenceDocumentsType();
    }

    @And("upload a file under Upload File under User")
    public void uploadAFileUnderUploadFile() {
        addAllFieldTypesUserObject.UploadAFileUnderUploadFile();
    }

    @When("user select Header Text from the field type dropdown under User")
    public void userSelectHeaderTextFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectHeaderTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Header Text type under User")
    public void enterFieldNameForHeaderTextType() {
        addAllFieldTypesUserObject.EnterFieldNameForHeaderTextType();
    }

    @When("user select Normal Text from the field type dropdown under User")
    public void userSelectNormalTextFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectNormalTextFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Normal Text type under User")
    public void enterFieldNameForNormalTextType() {
        addAllFieldTypesUserObject.EnterFieldNameForNormalTextType();
    }

    @When("user select Page Separator from the field type dropdown under User")
    public void userSelectPageSeparatorFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectPageSeparatorFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Page Separator type under User")
    public void enterFieldNameForPageSeparatorType() {
        addAllFieldTypesUserObject.EnterFieldNameForPageSeparatorType();
    }

    @When("user select Link from other entity from the field type dropdown under User")
    public void userSelectLinkFromOtherEntityFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectLinkFromOtherEntityFromTheFieldTypeDropdown();
    }

    @And("enter Field Name Link from other entity type under User")
    public void enterFieldNameLinkFromOtherEntityType() {
        addAllFieldTypesUserObject.EnterFieldNameLinkFromOtherEntityType();
    }

    @When("user select Data List from the field type dropdown under User")
    public void userSelectDataListFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectDataListFromTheFieldTypeDropdown();
    }

    @And("enter Field Name for Data List type under User")
    public void enterFieldNameForDataListType() {
        addAllFieldTypesUserObject.EnterFieldNameForDataListType();
    }

    @And("select List Name under User")
    public void selectListName() {
        addAllFieldTypesUserObject.SelectListName();
    }

    @When("user select OTP Validation from the field type dropdown under User")
    public void userSelectOTPValidationFromTheFieldTypeDropdown() {
        addAllFieldTypesUserObject.UserSelectOTPValidationFromTheFieldTypeDropdown();
    }

    @And("enter Field Name OTP Validation type under User")
    public void enterFieldNameOTPValidationType() {
        addAllFieldTypesUserObject.EnterFieldNameOTPValidationType();
    }

    @And("enter Entity under User")
    public void enterEntity() {
        addAllFieldTypesUserObject.EnterEntity();
    }

    @And("select Entity Field under User")
    public void selectEntityField() {
        addAllFieldTypesUserObject.SelectEntityField();
    }


}
