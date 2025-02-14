package com.test.channelplay.stepDefinition.INB;

import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.object.INB.CustomerBulkUpload_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class customerBulkUpload_Steps extends DriverBase {
    CustomerBulkUpload_Object custBulkUpload = new CustomerBulkUpload_Object();
    AssistiveLogin login = new AssistiveLogin();
    CommonUtils commonUtils = new CommonUtils();


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
    }

    @Then("verify email received for successful bulk upload with attachment for INB")
    public void verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB() {
        custBulkUpload.verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB();
    }
}
