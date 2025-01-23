package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddContfromCusDashObject;
import com.test.channelplay.object.ContactTransObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddContfromCusDashSteps extends DriverBase {

    AddContfromCusDashObject addContfromCusDashObject = new AddContfromCusDashObject();
    CommonUtils commonUtils = new CommonUtils();
    @Given("after login to crm user will be on the Customer page under CRM menu")
    public void afterLoginToCrmUserWillBeOnTheContactPageUnderCRM() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        addContfromCusDashObject.loginCRM(GetProperty.value("username"), GetProperty.value("password"));
    }

    @And("user select CRM and Click on Customer submenu")
    public void userSelectCRMAndClickOnCustomerSubmenu() {
        addContfromCusDashObject.setCRM_menu();

    }

    @When("user Click on dashboard button")
    public void userClickOnDashboardButton() {
        addContfromCusDashObject.setDashboard_button();
    }

    @And("user click on plussign contact button")
    public void userClickOnPlussignContactButton() {
        addContfromCusDashObject.setAddContactPlus_button();
    }

    @And("Contact_user Enter contact Name")
    public void contact_userEnterContactName() {
        addContfromCusDashObject.setContactName();
    }

    @And("select ContactType dropdown and click first option")
    public void selectContactTypeDropdownAndClickFirstOption() {
        addContfromCusDashObject.setContactType_dropdown();
    }

    @And("enter designation")
    public void enterDesignation() {
        addContfromCusDashObject.setDesignation_field();
    }

    @And("enter mobile number under contact under customer dashboard")
    public void enterMobileNumber() {
        addContfromCusDashObject.setMobileNumber_Field();
    }

    @And("enter contact email")
    public void enterContactEmail() {
        addContfromCusDashObject.setEmail_Field();
    }

    @And("enter contact address")
    public void enterContactAddress() {
        addContfromCusDashObject.setAddress_Field();
    }

    @And("select contact Country")
    public void selectContactCountry() {
        addContfromCusDashObject.setCountry_Dropdown();
        addContfromCusDashObject.setIndia_Option();
    }

    @And("select contact State")
    public void selectContactState() {
        addContfromCusDashObject.setState_Dropdown();
        addContfromCusDashObject.setAndhraPradesh_Option();
    }

    @And("select contact City")
    public void selectContactCity() {
        addContfromCusDashObject.setCity_Dropdown();
        addContfromCusDashObject.setAdilabad_Option();
    }

    @And("select data list group")
    public void selectDataListGroup() {
        addContfromCusDashObject.setDataListGroups_dropdown();
    }

    @And("select company test from dropdown list")
    public void selectCompanyTestFromDropdownList() {
        addContfromCusDashObject.setCompanyTest_Dropdown();
    }

    @And("enter company phone number")
    public void enterCompanyPhoneNumber() {
        addContfromCusDashObject.setCompanyPhoneNumber_Field();
    }

    @And("select date")
    public void selectDate() {
        addContfromCusDashObject.setStartedDateCalender_button();
    }

    @And("upload certificate")
    public void uploadCertificate() {
        addContfromCusDashObject.setCertificate_upload();
    }

    @And("upload video")
    public void uploadVideo() {
        addContfromCusDashObject.setLocationVideo_upload();
    }

    @And("select Data List Module from dropdown")
    public void selectDataListModuleFromDropdown() {
        addContfromCusDashObject.setDataListModule_Dropdown();
    }

    @And("enter Company Registered Email Test")
    public void enterCompanyRegisteredEmailTest() {
        addContfromCusDashObject.setCompanyRegistredEmail_field();
    }

    @Then("click contact save button")
    public void clickContactSaveButton() {
        addContfromCusDashObject.setContactSave_button();
    }
}


