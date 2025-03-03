package com.test.channelplay.stepDefinition.INB;

import StepDefinitions_API.INB.CustomerBulkUpload_APISteps;
import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.object.INB.CustomerBulkUpload_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utilities_API.ExcelFileReader;

import static org.junit.Assert.assertEquals;

public class customerBulkUpload_Steps extends DriverBase {
    CustomerBulkUpload_Object custBulkUpload = new CustomerBulkUpload_Object();
    AssistiveLogin login = new AssistiveLogin();
    CommonUtils commonUtils = new CommonUtils();
    CustomerBulkUpload_APISteps custBulkUploadAPI = new CustomerBulkUpload_APISteps();


    @Given("user loggedIn to 1Channel project under Admin User for INB")
    public void userLoggedInTo1ChannelProjectUnderAdminUserForINB() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("user clicks on menu CRM and submenu Customers for INB")
    public void userClicksOnMenuCRMAndSubmenuCustomersForINB() {
        custBulkUpload.userClicksOnMenuCRMAndSubmenuCustomersForINB();
    }

    @Then("user is on Customers page for INB")
    public void userIsOnCustomersPageForINB() {
        boolean custPageTitle = custBulkUpload.userIsOnCustomersPageForINB();
        Assert.assertTrue(custPageTitle);
    }

    @And("clicks on dropdown under Add button and then click on Bulk Upload option for INB")
    public void clicksOnDropdownUnderAddButtonAndThenClickOnBulkUploadOptionForINB() {
        custBulkUpload.clicksOnDropdownUnderAddButtonAndThenClickOnBulkUploadOptionForINB();
    }

    @And("upload the excel file and validate MapFields and ValidateData pages and upload validated records for INB")
    public void uploadTheExcelFileAndValidateMapFieldsAndValidateDataPagesAndUploadValidatedRecordsForINB() {
        custBulkUpload.uploadTheExcelFileAndValidateMapFieldsAndValidateDataPagesAndUploadValidatedRecordsForINB();

        //  get total excel row count after UI upload
        int excelRowCountAfterUpload = custBulkUpload.getTotalRowCount();
        System.out.println("Total Excel rows count after upload: " + excelRowCountAfterUpload);

        //  get total excel rows count from FIle directory before UI upload
        int excelRowCountBeforeUpload = ExcelFileReader.getTotalRowCount();
        System.out.println("Total Excel rows count before upload was: " + excelRowCountBeforeUpload);

        //  assert that total excel rows count before and after upload are same
        assertEquals(excelRowCountBeforeUpload, excelRowCountAfterUpload);
    }

    @Then("verify email received for successful bulk upload with attachment for INB")
    public void verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB() {
        custBulkUpload.verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB();
    }

    @And("validate success_failure status of the upload from email attachments for INB")
    public void validateSuccess_FailureStatusOfTheUploadFromEmailAttachmentsForINB() {
        custBulkUpload.validateSuccess_FailureStatusOfTheUploadFromEmailAttachmentsForINB();
    }

    @And("call the assertions of bulk upload Success-Failure email status at the end for INB")
    public void callTheAssertionsOfBulkUploadSuccessFailureEmailStatusAtTheEndForINB() {
    /*    //  assertion on excel upload success-failure status
        custBulkUpload.getSoftAssert().assertAll();
        //  assertion on data entry in master DB
        custBulkUploadAPI.getAPISoftAssert().assertAll();
        //custBulkUploadAPI.getAPISoftAssert2();*/
    }
}
