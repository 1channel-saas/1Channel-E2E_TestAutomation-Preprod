package com.test.channelplay.object.INB;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import utilities_API.ExcelFileReader;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerBulkUpload_Object extends DriverBase {
    private static final Logger log = LoggerFactory.getLogger(CustomerBulkUpload_Object.class);
    CommonUtils commonutils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    private SoftAssert softAssert = new SoftAssert();


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
    @FindBy(xpath = "//a[contains(@class, 'btn') and @data-bi-ecn='Sign in' and @data-bi-cn='signin' and contains(@href, 'LinkID=2125442')]/span[text()='Sign in']")
    WebElement mailer_HomeSignIn_button;
    @FindBy(xpath = "//input[@id='i0116' and @type='email']")
    WebElement mailer_EnterEmailId;
    @FindBy(xpath = "//button[@type='submit' and @id='idSIButton9' and text()='Next']")
    WebElement mailer_EnterEmailId_Next_button;
    @FindBy(xpath = "//input[@type='password' and @id='i0118' and @name='passwd']")
    WebElement mailer_EnterPassword;
    @FindBy(xpath = "//input[@type='submit' and @id='idSIButton9' and @value='Sign in']")
    WebElement mailer_SignIn_button;
    @FindBy(xpath = "//input[@type='submit' and @id='idSIButton9' and @value='Yes']")
    WebElement staySignedIn_Yes_button;
    @FindBy(xpath = "//img[@id = 'O365_MainLink_TenantLogoImg']")
    WebElement mailer_Logo;
    @FindBy(xpath = "//span[text()='Inbox']")
    WebElement mailer_Inbox;
    @FindBy(xpath = "//div[@class='DrnCK l8Tnu']/descendant::div[@class='AL_OM l8Tnu I1wdR']")
    WebElement mailer_Inbox_email_dateTime;
    @FindBy(xpath = "//span[@title='Customers Upload Status']/ancestor::div[@class='NTPm6 idxFD HynGd WWy1F']/following-sibling::div/descendant::div[contains(text(), '_upload Customer.xlsx')]")
    WebElement mailer_inbox_CustomerUploadStatus_email_Attachment;
    @FindBy(xpath = "//button/descendant::span[text()='Open in Excel']")
    WebElement mailer_inbox_email_Attachment_OpenInExcel_button;
    @FindBy(xpath = "//button[@name='Download']")
    WebElement mailer_inbox_email_attachment_Download_button;
    @FindBy(xpath = "//button/descendant::span[text()='Home']/ancestor::div[@class='VvU3M']/preceding-sibling::div/button")
    WebElement mailer_Show_Navigation_button;
    @FindBy(xpath = "//button[@title='Close']")
    WebElement mailer_Attachement_Close_button;
    @FindBy(xpath = "//div[@class='p4pwT bZBXb']/div/descendant::button")
    WebElement mailer_inbox_Select_button;
    @FindBy(xpath = "(//i[@data-icon-name='CheckMark'])[1]")
    WebElement mailer_inbox_Select_All_checkbox;
    @FindBy(xpath = "(//span[@role='presentation' and @class='FLwLv']/i)[5]")
    WebElement mailer_inbox_DeleteAll_button;



    public CustomerBulkUpload_Object() {
        PageFactory.initElements(getDriver(), this);
    }
    private static String totalRecords_value, totalRecordsValCounterStr, ErrorFound_value, testStartTime;
    private int totalExcelRowCount = 0;
    static int totalRecordsVal;
    static int totalRecordsValCounter = 0;
    int filesToMove;


    public void userClicksOnMenuCRMAndSubmenuCustomersForINB() {
        CRM_menu.click();
        Customers_subMenu.click();
        webDriverUtils.waitUntilVisible(getDriver(), SerialNo_column_text, Duration.ofSeconds(20));
        commonutils.sleep(2000);

        //  get the test start time for future use
        testStartTime();
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
        String directoryFilePath = Paths.get("Data_Files").toAbsolutePath().toString();
        //  Get all .xlsx files in the directory
        List<File> testFiles = CommonUtils.getAllXlsxFiles(directoryFilePath);
        if (testFiles.isEmpty()) {
            log.info("No files found for upload.");
            return;
        }
        for (int i = 0; i < testFiles.size(); i++) {
            File file = testFiles.get(i);
            log.info("Uploading file for bulkUpload: {}", file.getName());

            //  Read row count from excel before uploading
            int fileRowCount = ExcelFileReader.readExcelFile(file).size();
            totalExcelRowCount += fileRowCount;
            log.info("Rows in {}: {} | Total Accumulated Rows: {}", file.getName(), fileRowCount, totalExcelRowCount);

            //  Iterate and upload each file
            uploadFile(file.getAbsolutePath());
            commonutils.sleep(2000);

            //  Process the file one-by-one over UI
            processUploadedFile();

            //  Reset the page before next upload
            if (i < testFiles.size() - 1) {
                resetUploadPage();
            } else {
                log.info("Last file uploaded, skipping page refresh.");
            }
        }

        //  for uploading single xlsx file during bulk upload
    /*    String filePath = Paths.get(GetProperty.value("custBulkUpload_xlsx")).toAbsolutePath().toString();
        fileUpload_input.sendKeys(filePath);
        commonutils.sleep(2000);
        Upload_button.click();
        commonutils.sleep(1000);

        processUploadedFile();
    */
    }

    //  method to get the test start time
    public static void testStartTime() {
        testStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        setTestStartTime(testStartTime);
    }

    //  Getter Setter method to extract TestStartTime at time time of file upload over UI
    public static void setTestStartTime(String testStart_time) {
        testStartTime = testStart_time;
    }
    public String getTestStartTime() {
        return testStartTime;
    }

    //  upload multiple xlsx files
    public void uploadFile(String filePath) {
        fileUpload_input.sendKeys(filePath);
        commonutils.sleep(2000);
        Upload_button.click();
        commonutils.sleep(1000);
    }

    //  Process the uploaded file through UI steps
    private void processUploadedFile() {
        //  Map Fields window
        Assert.assertTrue(MapFields_pageHeader_text.isDisplayed());
        MapFields_Next_button.click();
        commonutils.sleep(1000);

        //  Validate Data window
        Assert.assertTrue(ValidateData_pageHeader_text.isDisplayed());
        //  calling static method extractRecordsAndErrors() to extract the total records and errors
        extractRecordsAndErrors();
        log.info("Total Records from Excel on UI: {}", totalRecords_value);
        log.info("Error Found during validation on UI: {}", ErrorFound_value);
        commonutils.sleep(1000);

        UploadValidatedRecords_button.click();

        //  Upload Validated Records
        webDriverUtils.waitUntilVisible(getDriver(), Upload_Success_message, Duration.ofSeconds(2));
        if (!getDriver().findElements(By.xpath("//span[contains(text(), \"Upload started. Status will be sent\")]")).isEmpty()) {
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

    //  Method to reset UI before next upload
    private void resetUploadPage() {
        try {
            // Refresh page to clear previous uploads
            getDriver().navigate().refresh();
            webDriverUtils.waitUntilVisible(getDriver(), SerialNo_column_text, Duration.ofSeconds(20));
            userIsOnCustomersPageForINB();
            clicksOnDropdownUnderAddButtonAndThenClickOnBulkUploadOptionForINB();
            webDriverUtils.waitUntilVisible(getDriver(), fileUpload_input, Duration.ofSeconds(5));
            log.info("Upload page refreshed successfully.");
        } catch (Exception e) {
            log.error("Error while refreshing upload page: {}", e.getMessage());
        }
    }

    //  creating static method extractRecordsAndErrors() which helps to get values of Total Records and use in another class
    public static void extractRecordsAndErrors() {
        //  extract the text from div, and extracts the value after the colon
        totalRecords_value = TotalRecords.getText().split(":")[1].trim();
        totalRecordsVal = Integer.parseInt(totalRecords_value);
        totalRecordsValCounter += totalRecordsVal;

        totalRecordsValCounterStr = String.valueOf(totalRecordsValCounter);
        setTotalRecordsValue(totalRecordsValCounterStr);

        ErrorFound_value = ErrorFound.getText().split(":")[1].trim();
    }

    //  Getter Setter method to extract totalRecords_value during validation
    public static void setTotalRecordsValue(String totalRecordsVal) {
        totalRecordsValCounterStr = totalRecordsVal;
    }
    public String getTotalRecordsValue() {
        return totalRecordsValCounterStr;
    }

    //  method to Get the Total Row Count for future use
    public int getTotalRowCount() {
        return totalExcelRowCount;
    }

    public void verifyEmailReceivedForSuccessfulBulkUploadWithAttachmentForINB() {
        String parentWindow = getDriver().getWindowHandle();
        String oneChannelWindowURL = getDriver().getCurrentUrl();

        String url = GetProperty.value("mailVerify_bulkUpload");
        getDriver().navigate().to(url);
        commonutils.sleep(1000);

        mailer_HomeSignIn_button.click();
        commonutils.sleep(1000);
        // Switch to new window
        Set<String> handles =  getDriver().getWindowHandles();
        for(String windowHandle  : handles)
        {
            if(!windowHandle.equals(parentWindow))
            {
                getDriver().switchTo().window(windowHandle);
                commonutils.sleep(1000);
                System.out.println("Entered into new window");

                //  perform emailer operations on new window
                performEmailerTask();
                System.out.println("Entered into new performEmailerTask method");

                //  close the child window
                commonutils.sleep(1000);
                getDriver().close();
                getDriver().switchTo().window(parentWindow);
                System.out.println("Switched back to parent window");
                getDriver().navigate().to(oneChannelWindowURL);
            }
        }
    }

    public void performEmailerTask() {
        //  Convert testStartTime to LocalDateTime
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter trimmedFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        //  call getTestStartTime() to get the test start time
        String UITestStartTime = getTestStartTime();

        LocalDateTime fullTestStartDateTime = LocalDateTime.parse(testStartTime, fullFormatter);
        String trimmedtestStartDateTime = fullTestStartDateTime.format(trimmedFormatter);
        LocalDateTime UITestStartDateTime = LocalDateTime.parse(trimmedtestStartDateTime, trimmedFormatter);
        System.out.println("Test Start Date-Time: " + UITestStartDateTime);

        webDriverUtils.waitUntilVisible(getDriver(), mailer_EnterEmailId, Duration.ofSeconds(5));
        mailer_EnterEmailId.sendKeys(GetProperty.value("testEmailId"));
        mailer_EnterEmailId_Next_button.click();

        webDriverUtils.waitUntilVisible(getDriver(), mailer_EnterPassword, Duration.ofSeconds(5));
        mailer_EnterPassword.sendKeys(GetProperty.value("testEmailPassword"));
        mailer_SignIn_button.click();

        webDriverUtils.waitUntilVisible(getDriver(), staySignedIn_Yes_button, Duration.ofSeconds(5));
        staySignedIn_Yes_button.click();
        webDriverUtils.waitUntilVisible(getDriver(), mailer_Logo, Duration.ofSeconds(10));

        String currMailerUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currMailerUrl.contains("https://outlook.office365.com/mail/"));

        if (!(mailer_Inbox.isDisplayed())) {
            mailer_Show_Navigation_button.click();
            mailer_Inbox.click();
        } else {
            mailer_Inbox.click();
        }
        commonutils.sleep(1000);

        //  calling wait for emil method after clicking inbox
        waitForEmailToReceive(2, 3);

        List<WebElement> CustomerUploadStatus_emailList = getDriver().findElements(By.xpath("//span[text()='Customers Upload Status']"));

        //  Ensure only the last 10 emails are processed
        int emailCount = CustomerUploadStatus_emailList.size();
        if (emailCount > 10) {
            CustomerUploadStatus_emailList = CustomerUploadStatus_emailList.subList(emailCount - 10, emailCount);
        }
        commonutils.sleep(1000);

        for (WebElement CustomerUploadStatus_email : CustomerUploadStatus_emailList) {
            CustomerUploadStatus_email.click();
            commonutils.sleep(1000);
            String email_datetime = mailer_Inbox_email_dateTime.getText();
            System.out.println("Email Date and Time on UI: " + email_datetime);
            //  Convert email timestamp to LocalDateTime
            LocalDateTime emailTestStartDateTime = convertEmailDateTime(email_datetime);
            System.out.println("formatted Email Date and Time: " + emailTestStartDateTime);
            commonutils.sleep(1000);

            if (emailTestStartDateTime.isAfter(UITestStartDateTime) || emailTestStartDateTime.isEqual(UITestStartDateTime)) {
                log.info("Email received at {} after test start time {}", emailTestStartDateTime, UITestStartDateTime);
                mailer_inbox_CustomerUploadStatus_email_Attachment.click();
                webDriverUtils.waitUntilVisible(getDriver(), mailer_inbox_email_Attachment_OpenInExcel_button, Duration.ofSeconds(5));
                mailer_inbox_email_attachment_Download_button.click();
                log.info("Downloading attachment...");
                mailer_Attachement_Close_button.click();
                commonutils.sleep(2000);
            } else {
                log.info("Email was received before the test started. Skipping download.");
            }
        }
        commonutils.sleep(2000);

        //  deleting all emails from inbox at the end of emailer operations
        mailer_inbox_Select_button.click();
        webDriverUtils.waitUntilVisible(getDriver(), mailer_inbox_Select_All_checkbox, Duration.ofSeconds(5));
        mailer_inbox_Select_All_checkbox.click();
        webDriverUtils.waitUntilVisible(getDriver(), mailer_inbox_DeleteAll_button, Duration.ofSeconds(5));
        mailer_inbox_DeleteAll_button.click();
        commonutils.sleep(1000);


        //  moving file to bulkUpload_Downloaded_Files dir
        moveLatestUploadedFiles();
    }

    //  method for move files from downloads to respective directory
    public void moveLatestUploadedFiles() {
        //  max file count limit in the target directory
        int maxFileCount = 10;

        //  Get count of all files in the upload directory
        String fileUploadDir = System.getProperty("user.dir") + File.separator + "Data_Files";
        List<File> uploadFiles = CommonUtils.getAllXlsxFiles(fileUploadDir);
        int uploadCount = uploadFiles.size();

        if (uploadCount == 0) {
            log.error("No files found in the upload directory. Nothing to move");
            return;
        }

        //  getting source and target dir for moving files
        String sourceDir = System.getProperty("user.dir") + File.separator + "downloads";
        String targetDir = System.getProperty("user.dir") + File.separator + "bulkUpload_Downloaded_Files";

        //  Manage file count in target folder
        File targetFolder = new File(targetDir);
        commonutils.enforceFileCountLimit(targetFolder, maxFileCount);

        File sourceFolder = new File(sourceDir);
        //  filter files with specific name
        File[] sourceFiles = sourceFolder.listFiles((dir, name) -> name.contains("_upload Customer.xlsx"));

        if (sourceFiles == null || sourceFiles.length == 0) {
            log.error("No matching files found in {} directory", sourceDir);
            return;
        }

        //  sort by latest modified date
        Arrays.sort(sourceFiles, Comparator.comparingLong(File::lastModified).reversed());

        //  Move only the latest `uploadCount` files
        filesToMove = Math.min(uploadCount, sourceFiles.length);
        log.info("Moving {} latest files from downloads to bulkUpload_Downloaded_Files.", filesToMove);

        //  moving the file to target directory
        for (int i = 0; i < filesToMove; i++) {
            File fileToMove = sourceFiles[i];
            commonutils.moveFiles(sourceDir, fileToMove.getName(), targetDir);
            log.info("Moved file: {}", fileToMove.getName());
        }
    }

    //  method to convert email date-Time to LocalDateTime
    private LocalDateTime convertEmailDateTime(String emailDateTimeStr) {
        try {
            DateTimeFormatter emailFormatter = DateTimeFormatter.ofPattern("EEE M/d/yyyy h:mm a", Locale.ENGLISH);
            return LocalDateTime.parse(emailDateTimeStr, emailFormatter);
        } catch (Exception e) {
            log.error("Error parsing email date: {}", emailDateTimeStr);
            return null;
        }
    }

    //  method for wait for email to arrive after upload
    public void waitForEmailToReceive(int maxWaitTimeMinutes, int pollIntervalSeconds) {
        int elapsedTime = 0;
        boolean emailFound = false;

        while (elapsedTime < maxWaitTimeMinutes * 60) {
            List<WebElement> emailList = getDriver().findElements(By.xpath("//span[text()='Customers Upload Status']"));

            if (!emailList.isEmpty()) {
                log.info("Email received! Processing...");
                emailFound = true;
                break;
            }

            log.info("Email not found yet. Retrying in {} seconds...", pollIntervalSeconds);
            commonutils.sleep(pollIntervalSeconds * 1000);
            elapsedTime += pollIntervalSeconds;
        }

        if (!emailFound) {
            log.error("Timed out waiting for email. No matching email received within {} minutes.", maxWaitTimeMinutes);
        }
    }

    public void validateSuccess_FailureStatusOfTheUploadFromEmailAttachmentsForINB() {
        String downloadDirFilePath = Paths.get("bulkUpload_Downloaded_Files").toAbsolutePath().toString();
        List<File> testFiles = CommonUtils.getAllXlsxFiles(downloadDirFilePath);

        if (testFiles.isEmpty()) {
            log.info("No downloaded files found for validation.");
            return;
        }

        //  Sort files by last modified date (latest first)
        testFiles.sort(Comparator.comparingLong(File::lastModified).reversed());

        //  Process only the latest 'filesToMove' files
        List<File> latestFiles = testFiles.stream().limit(filesToMove).collect(Collectors.toList());

        for (File file : latestFiles) {
            log.info("File for Success_Failure status validation: {}", file.getName());

            //  Define the columns to validate
            List<String> columnNames = Arrays.asList("ACCT_NO", "Upload Status", "Reason For Failure");

            //  Get the values as a list of row-wise data
            List<Map<String, String>> rowDataList = ExcelFileReader.getColumnValues(file, columnNames);

            //  Counters and lists for different conditions
            int successBlankReasonCount = 0;
            int successWithCommentsCount = 0;
            int failedWithCommentsCount = 0;
            int failedBlankReasonCount = 0;

            List<String> successBlankReasonAccounts = new ArrayList<>();
            List<String> successWithCommentsAccounts = new ArrayList<>();
            List<String> failedWithCommentsAccounts = new ArrayList<>();
            List<String> failedBlankReasonAccounts = new ArrayList<>();

            //  Iterate through the data to classify records
            for (Map<String, String> rowData : rowDataList) {
                log.info("Acc No: {}, Upload Status: {}, Reason For Failure: {}", rowData.get("ACCT_NO"), rowData.get("Upload Status"), rowData.get("Reason For Failure"));
                System.out.println("<--------------------------------------------->");

                String acctNo = rowData.get("ACCT_NO");
                String uploadStatus = rowData.get("Upload Status");
                String reasonForFailure = rowData.get("Reason For Failure");

                if ("SUCCESS".equalsIgnoreCase(uploadStatus) && (reasonForFailure == null || reasonForFailure.trim().isEmpty())) {
                    successBlankReasonCount++;
                    successBlankReasonAccounts.add(acctNo);
                    softAssert.assertTrue(uploadStatus.equalsIgnoreCase("SUCCESS"), "Upload Status should be 'SUCCESS'");

                } else if ("SUCCESS".equalsIgnoreCase(uploadStatus.split(";")[0]) && uploadStatus.contains(";") &&
                        (reasonForFailure == null || reasonForFailure.trim().isEmpty())) {
                    successWithCommentsCount++;
                    successWithCommentsAccounts.add(acctNo);
                    softAssert.assertTrue(uploadStatus.startsWith("SUCCESS;"), "Upload Status should contain 'SUCCESS;' followed by comments");

                } else if ("FAILED".equalsIgnoreCase(uploadStatus) && reasonForFailure != null && !reasonForFailure.trim().isEmpty()) {
                    failedWithCommentsCount++;
                    failedWithCommentsAccounts.add(acctNo);
                    softAssert.fail("Upload Status is - " + uploadStatus + ". with Reason For Failure: " + reasonForFailure + "");

                } else if ("FAILED".equalsIgnoreCase(uploadStatus) && (reasonForFailure == null || reasonForFailure.trim().isEmpty())) {
                    failedBlankReasonCount++;
                    failedBlankReasonAccounts.add(acctNo);
                    softAssert.fail("Since upload Status is - " + uploadStatus + ". required - Reason For Failure");
                }
            }

            // Log results
            log.info("Count of ACC_NO with Upload Status SUCCESS: {}", successBlankReasonCount);
            log.info("Accounts: {}", successBlankReasonAccounts);

            log.warn("Count of ACC_NO with Upload Status: SUCCESS with COMMENTS having no failure reason: {}", successWithCommentsCount);
            log.warn("Accounts: {}", successWithCommentsAccounts);

            log.warn("Count of ACC_NO with Upload Status: FAILED with proper Reason For Failure: {}", failedWithCommentsCount);
            log.warn("Accounts: {}", failedWithCommentsAccounts);

            log.error("Count of ACC_NO with Upload Status: FAILED but no Reason For Failure found: {}", failedBlankReasonCount);
            log.error("Accounts: {}", failedBlankReasonAccounts);
        }
        softAssert.assertAll();
    }

    //  method to get all assertion for these to call at the end of whole test execution
    /*public SoftAssert getSoftAssert() {
        return softAssert;
    }*/


}
