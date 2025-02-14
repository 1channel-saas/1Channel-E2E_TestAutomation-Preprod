package com.test.channelplay.object.INB;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.nio.file.Paths;
import java.time.Duration;

public class CustomerBulkUpload_Object extends DriverBase {
    private static final Logger log = LoggerFactory.getLogger(CustomerBulkUpload_Object.class);
    CommonUtils commonutils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();


    @FindBy(xpath = "//ul[@class=\"menu-nav\"]//following-sibling::li/a//descendant::span[text()=\" CRM \"]")
    WebElement CRM_menu;
    @FindBy(xpath = "//li/a[@href=\"/customers\"]/span[text()=\" Customers \"]")
    WebElement Customers_subMenu;
    @FindBy(xpath = "//div/h5[text()=\" Customers \"]")
    WebElement CustomersPage_title;
    @FindBy(xpath = "//span[text()=\"Serial No.\"]")
    WebElement SerialNo_column_text;
    @FindBy(xpath = "//button[text()=\"Add\"]/following-sibling::button[contains(@class, 'mat-menu-trigger')]//mat-icon[text()='arrow_drop_down']")
    WebElement Add_button_dropdown;
    @FindBy(xpath = "//button[text()=\"Bulk Upload\"]")
    WebElement BulkUpload_dropdown_option;
    @FindBy(xpath = "//div[@class='file-drop-zone']//input[@type='file']")
    WebElement fileUpload_input;
    @FindBy(xpath = "//button[text() = \"Upload\"]")
    WebElement Upload_button;
    @FindBy(xpath = "//h4[text()=\"Map Fields\"]")
    WebElement MapFields_pageHeader_text;
    @FindBy(xpath = "//button[text()=\"Next\"]")
    WebElement MapFields_Next_button;
    @FindBy(xpath = "//h4[text()=\"Validate Data\"]")
    WebElement ValidateData_pageHeader_text;
    @FindBy(xpath = "//div[@class='validation-record']//div[label[text()='Total Records:']]")
    static WebElement TotalRecords;
    @FindBy(xpath = "//div[@class='validation-record']//div[label[text()='Error Found:']]")
    static WebElement ErrorFound;
    @FindBy(xpath = "//button[text() = \"Upload Validated Records\"]")
    WebElement UploadValidatedRecords_button;
    @FindBy(xpath = "//span[contains(text(), \"Upload started. Status will be sent to your email id after completion.\")]")
    WebElement Upload_Success_message;
    @FindBy(xpath = "//div/p[contains(text(), \"This will only upload the validated record\")]")
    WebElement ImportantUpload_alertText;
    @FindBy(xpath = "//button[text() = \"Confirm\"]")
    WebElement ImportantUpload_alertText_Confirm_button;

    //  Emailer xpath expressions
    @FindBy(xpath = "//input[@id='i0116' and @type='email']")
    WebElement mailer_EnterEmailId;
    @FindBy(xpath = "//input[@type='submit' and @id='idSIButton9' and @value='Next']")
    WebElement mailer_EnterEmailId_Next_button;
    @FindBy(xpath = "//input[@type='password' and @id='i0118' and @name='passwd']")
    WebElement mailer_EnterPassword;
    @FindBy(xpath = "//input[@type='submit' and @id='idSIButton9' and @value='Sign in']")
    WebElement mailer_SignIn_button;



    public CustomerBulkUpload_Object() {
        PageFactory.initElements(getDriver(), this);
    }
    private static String totalRecords_value, ErrorFound_value;


    public void userClicksOnMenuCRMAndSubmenuCustomersForINB() {
        CRM_menu.click();
        Customers_subMenu.click();
        webDriverUtils.waitUntilVisible(getDriver(), SerialNo_column_text, Duration.ofSeconds(20));
    }

    public boolean userIsOnCustomersPageForINB() {
        boolean customerPageTitle = CustomersPage_title.isDisplayed();
        commonutils.sleep(2000);
        return customerPageTitle;
    }

    public void clicksOnDropdownUnderAddButtonAndThenClickOnBulkUploadOptionForINB() {
        Add_button_dropdown.click();
        commonutils.sleep(2000);
        BulkUpload_dropdown_option.click();
        commonutils.sleep(2000);
    }

    public void uploadTheExcelFileAndValidateMapFieldsAndValidateDataPagesAndUploadValidatedRecordsForINB() {
        String filePath = Paths.get(GetProperty.value("custBulkUpload_xlsx")).toAbsolutePath().toString();
        fileUpload_input.sendKeys(filePath);
        commonutils.sleep(2000);
        Upload_button.click();
        commonutils.sleep(1000);

        //  Map Fields window
        Assert.assertTrue(MapFields_pageHeader_text.isDisplayed());
        MapFields_Next_button.click();
        commonutils.sleep(1000);

        //  Validate Data window
        Assert.assertTrue(ValidateData_pageHeader_text.isDisplayed());
        //  calling static method extractRecordsAndErrors() to extract the total records and errors
        extractRecordsAndErrors();
        log.info("Total Records from Excel: {}", totalRecords_value);
        log.info("Error Found during UI validation: {}", ErrorFound_value);
        commonutils.sleep(1000);

        UploadValidatedRecords_button.click();

        //  Upload Validated Records
        webDriverUtils.waitUntilVisible(getDriver(), Upload_Success_message, Duration.ofSeconds(2));
        if (!getDriver().findElements(By.xpath("//span[contains(text(), \"Upload started. Status will be sent\")]")).isEmpty())
        {
            if (Upload_Success_message.isDisplayed()) {
                log.info("Bulk Upload Successful");
            }
        //  Only upload the validated records not records with errors
        } else if (ImportantUpload_alertText.isDisplayed()) {
            System.out.println("inside important alert window");
            ImportantUpload_alertText_Confirm_button.click();
            System.out.println("clicked alert window confirm button");

            webDriverUtils.waitUntilVisible(getDriver(), Upload_Success_message, Duration.ofSeconds(2));
            if (Upload_Success_message.isDisplayed()) {
                log.info("Bulk Upload Successful for validated records only");
            }
        } else {
            log.info("Bulk Upload Failed");
        }
        commonutils.sleep(2000);
        webDriverUtils.waitUntilVisible(getDriver(), SerialNo_column_text, Duration.ofSeconds(20));
    }

    //  creating static method extractRecordsAndErrors() which helps to get values of Total Records and use in another class
    public static void extractRecordsAndErrors() {
        //  extract the text from div, and extracts the value after the colon
        totalRecords_value = TotalRecords.getText().split(":")[1].trim();
        setTotalRecordsValue(totalRecords_value);
        ErrorFound_value = ErrorFound.getText().split(":")[1].trim();
    }

    //  Getter Setter method to extract totalRecords_value during validation
    public static void setTotalRecordsValue(String totalRecordsVal) {
        totalRecords_value = totalRecordsVal;
    }
    public String getTotalRecordsValue() {
        return totalRecords_value;
    }

    public void verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB() {
        getDriver().navigate().to(GetProperty.value("mailVerify_bulkUpload"));
        commonutils.sleep(3000);
        String mail_search = "soumya@1channel.co";
        mailer_EnterEmailId.sendKeys(mail_search);
        mailer_EnterEmailId_Next_button.click();
        commonutils.sleep(1000);
        mailer_EnterPassword.sendKeys("{c06SWEETHOME06@");
        commonutils.sleep(1000);
        mailer_SignIn_button.click();
    }

}
