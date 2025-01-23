package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.CustomerTransObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class CustomerTransStep extends DriverBase {

    CustomerTransObject customerTransObject = new CustomerTransObject();

    CommonUtils commonUtils = new CommonUtils();
    @Given("after login to crm user will be on the Customer page under CRM")
    public void afterLoginToCrmUserWillBeOnTheCustomerPageUnderCRM() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        customerTransObject.loginCRM(GetProperty.value("username"), GetProperty.value("password"));
    }


    @And("user select CRM and Click on Customer")
    public void userSelectCRMAndClickOnCustomer() {
        customerTransObject.Customer();
    }

    @When("User Click on Add button")
    public void userClickOnAddButton() {
        customerTransObject.UserClickOnAddButton();

    }

    @And("user Enter Customer Name")
    public void userEnterCustomerName() {
        customerTransObject.addCus();
    }

    @And("user select CustomerType dropdown and click first option")
    public void userSelectCustomerTypeDropdownAndClickFirstOption() {
        customerTransObject.setCustomerType_dropdown();

    }

    @And("user enter address")
    public void userEnterAddress() {
        customerTransObject.setAddress_Field();
    }

       @And("user select Country")
    public void userSelectCountry() {
        customerTransObject.setCountry_Dropdown();
        customerTransObject.setIndia_Option();
    }

    @And("user select State")
    public void userSelectState() {
        customerTransObject.setState_Dropdown();
        customerTransObject.setAndhraPradesh_Option();
    }

    @And("user select City")
    public void userSelectCity() {
        customerTransObject.setCity_Dropdown();
        customerTransObject.setAdilabad_Option();
    }

    @And("user upload photo")
    public void userUploadPhoto() {
        customerTransObject.setPhoto_upload();
    }

    @And("user enter phone number")
    public void userEnterPhoneNumber() {
        customerTransObject.setPhone_Field();
    }

    @And("user select timezone dropdown and click on first option")
    public void userSelectTimezoneDropdownAndClickOnFirstOption() {
        customerTransObject.setTimeZone_Dropdown();
    }

    @And("user click calender button and click select current date")
    public void userClickCalenderButtonAndClickSelectCurrentDate() {
        customerTransObject.setCalender_button();
    }

    @And("user enter email")
    public void userEnterEmail() {
        customerTransObject.setEmail_Field();
    }

    @And("user upload document docs,pdf,ppt.xlsx")
    public void userUploadDocumentDocsPdfPptXlsx() {
        customerTransObject.setDocument_upload();
    }

    @And("user upload video mpfour,mpg")
    public void userUploadVideoMpfourMpg() {
        customerTransObject.setVideo_upload();
    }

    @And("user select select gender and click first option")
    public void userSelectSelectGenderAndClickFirstOption() {
        customerTransObject.setGender_Dropdown();
    }

    @Then("user click save button")

    public void userClickSaveButton() {
        customerTransObject.setSave_button();
    }


    @Then("customer will added successfully with active status")
    public void customerWillAddedSuccessfullyWithActiveStatus() {
        customerTransObject.setSearch_bar();
      }
}
