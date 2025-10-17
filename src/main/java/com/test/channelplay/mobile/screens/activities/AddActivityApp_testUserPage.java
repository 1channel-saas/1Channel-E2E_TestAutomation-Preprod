package com.test.channelplay.mobile.screens.activities;

import com.test.channelplay.mobile.config_Helper.*;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.MobileTestBase;
import com.test.channelplay.utils.SharedTestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class AddActivityApp_testUserPage extends MobileTestBase {

    private static final Logger log = LoggerFactory.getLogger(AddActivityApp_testUserPage.class);


    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Activities\"]")
    WebElement ActivitiesMenu;
    @FindBy(xpath = "(//android.view.View//android.widget.ImageView[1])[1]")
    WebElement ActivitiesPageHeader;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Activity\n" +
            "Tab 2 of 3\"]")
    WebElement AddActivityPlusButton_ActivitiesScreen;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Offsite Activity\"]")
    WebElement offsiteActivity_button;
    @FindBy(xpath = "//android.view.View//android.widget.ImageView[1]")
    WebElement AddOffsiteActivityPageHeader;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Add\"]")
    WebElement activityADD_button;
    String SelectCustomer_dropdown = "//android.widget.ScrollView//android.widget.ImageView";
    @FindBy(xpath = "//android.widget.EditText")
    WebElement SelectCustomerFrameHeader;
    String selectCustomerFrameList_xpath = "//android.view.View[@content-desc]";
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"OK\"]")
    WebElement SelectCustomer_OK_button;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Title\"]")
    WebElement titleAsHeader;
    String titleField_xpath = "//android.view.View[@content-desc=\"Title\"]";
    String descriptionField_xpath1 = "//android.widget.EditText[contains(@hint,'descriptioneer')]";
    String descriptionField_xpath2 = "(//android.widget.EditText)[200]";
    @FindBy(xpath = "//android.view.View[@content-desc=\"Perform date\"]")
    WebElement performDateAsHeader;
    String performDate_xpath = "//android.view.View[@content-desc=\"Perform date\"]";
    @FindBy(xpath = "//android.view.View[contains(@content-desc,'Select date')]")
    WebElement performDate_field_CalendarHeader;
    String todayDate_xpath1 = "//android.widget.Button[contains(@content-desc,'Today')]";
    String todayDate_xpath2 = "//android.widget.Button[@selected='true']";
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"OK\"]")
    WebElement calendar_OK_button;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc=\"Save\"]")
    WebElement saveButton;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Offsite Activity\"]")
    WebElement viewOffsiteActivityPageHeader;
    @FindBy(xpath = "//android.view.View[contains(@content-desc,'OFF')]")
    WebElement serialNo_offsiteActivity;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Image\"]")
    WebElement imgFieldHeaderText;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Take Image\"]")
    WebElement takeImageButton;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Choose Image\"]")
    WebElement chooseImageButton;
    @FindBy(xpath = "//android.view.View[@content-desc=\"Image Edit\"]")
    WebElement imageEditHeader;
    @FindBy(xpath = "//android.widget.Button[@content-desc=\"No\"]")
    WebElement imageEdit_No_button;




    CommonUtils commonUtils = new CommonUtils();
    FlutterXPathHelper xpathHelper;
    BaseDropdownHelper dropdownHelper;
    ValidationStrategy validator;
    CalendarHelper calendarHelper;
    WebDriverWait wait;
    WebElement offsiteActivityName;
    String SerialNo_OffsiteAct, Title_OffsiteAct;


    private boolean initialized = false;
    private void setupPageElements() {
        if (!initialized && getDriver() != null) {
            driver = getDriver();
            PageFactory.initElements(driver, this);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            xpathHelper = new FlutterXPathHelper(driver);
            xpathHelper.enableAutoTemplates(true);
            validator = new ValidationStrategy(xpathHelper);
            calendarHelper = new CalendarHelper(driver, validator, xpathHelper);
            initialized = true;
            log.debug("setupPageElements initialized with driver, auto templates, and calendar helper");
        }
    }




    //  scenario - offsiteActivity

    public void clicksOnActivitiesMenu() {
        setupPageElements();
        wait.until(ExpectedConditions.elementToBeClickable(ActivitiesMenu));
        ActivitiesMenu.click();
        commonUtils.sleep(1000);
        Assert.assertTrue(ActivitiesPageHeader.isDisplayed());
    }

    public void clicksOnOffsiteActivityOption() {

        setupPageElements();
        wait.until(ExpectedConditions.visibilityOf(AddActivityPlusButton_ActivitiesScreen));
        wait.until(ExpectedConditions.elementToBeClickable(AddActivityPlusButton_ActivitiesScreen));
        AddActivityPlusButton_ActivitiesScreen.click();
        commonUtils.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(offsiteActivity_button));
        offsiteActivity_button.click();
        commonUtils.sleep(1000);
    }

    public void clicksOnAddButtonToAddNewOffsiteActivity() {
        setupPageElements();
        activityADD_button.click();
        commonUtils.sleep(2000);
        Assert.assertTrue(AddOffsiteActivityPageHeader.isDisplayed());
        System.out.println("Add Offsite Activity page displayed" + AddOffsiteActivityPageHeader.getText());
    }

    public void selectCustomerFromSelectCustomerDropdown(String customerName) {
        setupPageElements();
        openCustomerDropdown();

        dropdownHelper = new BaseDropdownHelper(driver, "Select Customer");
        List <WebElement> customerList = dropdownHelper.getDropdownList(selectCustomerFrameList_xpath);

        if (customerList.isEmpty()) {
            log.warn("No customers found in the Select Customer dropdown");
            return;
        }

        dropdownHelper.selectFromDropdown(customerList, customerName);
        commonUtils.sleep(1000);
    }

    private void openCustomerDropdown() {
        String[] dropdownXpaths = {SelectCustomer_dropdown};
        xpathHelper.smartFindElementWithAI(
                "offsiteActivity_SelectCustomer",
                dropdownXpaths,
                "templates/manual_captured_images/offsiteActivity_selectCustomer.png",
                "Select Customer",
                "click",
                null
        );
        commonUtils.sleep(1000);
        Assert.assertTrue(SelectCustomerFrameHeader.isDisplayed());
    }

    public void clicksOnOKButtonOnCustomerSelectionFrame() {
        setupPageElements();
        wait.until(ExpectedConditions.elementToBeClickable(SelectCustomer_OK_button));
        SelectCustomer_OK_button.click();
        commonUtils.sleep(2000);
        System.out.println("Clicked on OK button at Select Customer frame");
    }
    
    public void enterTextIntoDescriptionBox(String description) {
        setupPageElements();
        try {
            // Description field specific XPath strategies
            String[] descriptionXPaths = {descriptionField_xpath1, descriptionField_xpath2};

            // Use generic method with description-specific parameters
            WebElement descField = xpathHelper.smartFindElementWithAI(
                "offsiteActivity_description_field",
                descriptionXPaths,
                "templates/manual_captured_images/offsiteActivity_description_field.png",
                "Description",
                "focus",  // Action: focus and send text
                description  // Text to send
            );
            commonUtils.sleep(1000);

            // Use ValidationStrategy for validation
            String foundText = validator.validateTextEntry(descField, description, "description");

            System.out.println("SUCCESS: Description entered and verified: '" + foundText + "'");

        } catch (Exception e) {
            System.out.println("FAILED to enter description: " + e.getMessage());
            xpathHelper.saveScreenshotToFolder("description_error.png", "screenshots/validation_errors");
            throw new RuntimeException("Failed to enter description: " + e.getMessage());
        }
        commonUtils.sleep(2000);
    }

    public void enterNameIntoTitleField(String title) {
        setupPageElements();
        String randomStr = commonUtils.generateRandomString(3);
        String titleText = "offAct" + randomStr;
        Assert.assertTrue(titleAsHeader.isDisplayed());

        try {
            String[] titleXPaths = {titleField_xpath};

            WebElement titileField = xpathHelper.smartFindElementWithAI(
                    "offsiteActivity_title_field",
                     titleXPaths,
                    "templates/manual_captured_images/offsiteActivity_title_field.png",
                     title,
                    "focus",
                     titleText
            );
            commonUtils.sleep(1000);

            // Use ValidationStrategy for validation
            String foundText = validator.validateTextEntry(titileField, "offAct", title, 3);

            log.info("SUCCESS: Title entered and verified: {}", foundText);
            Title_OffsiteAct = foundText;

        } catch (Exception e) {
            log.warn("FAILED to enter title: {}", e.getMessage());
            xpathHelper.saveScreenshotToFolder("offsiteActivity_title_field_error.png", "screenshots/validation_errors");
            throw new RuntimeException("Failed to enter title: " + e.getMessage());
        }
        commonUtils.sleep(2000);
        //  store in SharedTestData class for access from portal UI classes
        SharedTestData.setCurrent_Title_OffsiteAct(Title_OffsiteAct);
    }

    public void selectDateInPerformDateField() {
        setupPageElements();
        xpathHelper.scrollToElement(performDateAsHeader, 5);
        Assert.assertTrue(performDateAsHeader.isDisplayed());
        commonUtils.sleep(1000);

        try {
            String[] performDateXpaths = {performDate_xpath};

            WebElement performDateField = xpathHelper.smartFindElementWithAI(
                    "offsiteActivity_performDate_field",
                     performDateXpaths,
                    "templates/manual_captured_images/offsiteActivity_performDate_field.png",
                    "Perform date",
                    "click",
                    null
            );
            commonUtils.sleep(1000);

            //  Use ValidationStrategy for validation
            boolean clickPerformedDate = validator.validateButtonClick(performDateField, "offsiteActivity_performDate_field");
            log.info("SUCCESS: clicked on perform_date_field: {}", clickPerformedDate);
            commonUtils.sleep(2000);
            Assert.assertTrue(performDate_field_CalendarHeader.isDisplayed(), "Calendar not opened");

            //  Click on today's date (current date)
            String selectedDate = selectTodayDate();

            commonUtils.sleep(1000);
            calendar_OK_button.click();
            log.info("Date selection completed - Selected date: {}", selectedDate);

        } catch (Exception e) {
            log.warn("FAILED to select Date: {}", e.getMessage());
            xpathHelper.saveScreenshotToFolder("offsiteActivity_performDate_field_error.png", "screenshots/validation_errors");
            throw new RuntimeException("Failed to select date: " + e.getMessage());
        }
        commonUtils.sleep(2000);
    }

    private String selectTodayDate() {
        setupPageElements();

        // Use the comprehensive calendar helper
        CalendarActionResult result = calendarHelper.selectTodayDate("performDate_calendar_today_date");

        // Validate and return result
        result.validateSuccess(); // Throws exception if failed

        log.info("Calendar date selection completed: {}", result);
        return result.getSelectedDate();
    }

    public void addImageInImageField() {
        setupPageElements();
        xpathHelper.scrollToElement(performDateAsHeader, 5);
        wait.until(ExpectedConditions.visibilityOf(imgFieldHeaderText));
        Assert.assertTrue(imgFieldHeaderText.isDisplayed());
        commonUtils.sleep(1000);

        //  click on image icon to add image
        String[] imgFieldXpaths = {};
        WebElement imgField = xpathHelper.smartFindElementWithAI(
                "offsiteActivity_image_field",
                 imgFieldXpaths,
                "templates/manual_captured_images/offsiteActivity_image_field.png",
                "Image",
                "click",
                null
        );
        commonUtils.sleep(1000);

        //  Use ValidationStrategy for validation
        boolean clickImgField = validator.validateButtonClick(imgField, "offsiteActivity_image_field");
        log.info("SUCCESS: clicked on image_field: {}", clickImgField);
        commonUtils.sleep(1000);
        //  click on take image button
        wait.until(ExpectedConditions.elementToBeClickable(takeImageButton));
        takeImageButton.click();
        commonUtils.sleep(2000);

        //  click camera shutter button on device using new shutter_click action
        String[] cameraShutterXpaths = {};  // No XPath needed for camera shutter
        WebElement cameraShutterButton = xpathHelper.smartFindElementWithAI(
                "offsiteActivity_camera_shutter_button",
                 cameraShutterXpaths,
                null,  // No template needed - using coordinate-based approach
                 null,  // No OCR text needed
                 "shutter_click",
                null
        );

        boolean clickShutter = (cameraShutterButton != null);
        log.info("Camera shutter click result: {}", clickShutter ? "SUCCESS" : "FAILED");
        commonUtils.sleep(2000);

        try {
            if (imageEditHeader.isDisplayed()) {
                commonUtils.sleep(1000);
                imageEdit_No_button.click();
                System.out.println("Clicked on Image Edit screen");
            }
        } catch (Exception e) {
            log.info("Image Edit screen not displayed, proceeding");
        }
        //  Assert in both cases
        commonUtils.sleep(1000);
        Assert.assertTrue(AddOffsiteActivityPageHeader.isDisplayed());

        log.info("Image capture completed and returned to Add Offsite Activity page");
        commonUtils.sleep(2000);
    }

    public void clickOnSaveToSubmitOffsiteActivity() {
        saveButton.click();
        System.out.println("click on Save completed");
        commonUtils.sleep(2000);
    }

    public void verifyActivityIsShowingInListAndFetchActivityDetailsForValidation(String customerName) {
        setupPageElements();
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(ActivitiesPageHeader)));
        commonUtils.sleep(1000);

        try {
            // Get all offsite activities from the list
            commonUtils.waitForFlutterStability();
            List<WebElement> offsiteActNameList = driver.findElements(By.xpath("//android.view.View[contains(@content-desc, 'Offsite Activity')]"));
            System.out.println("Found! " + offsiteActNameList.size() + " offsite activities in the list");

            WebElement lastMatchingActivity = null;
            int lastMatchIndex = -1;
            int matchCount = 0;

            // Process each activity found and store the last matching one
            for (int i = 0; i < offsiteActNameList.size(); i++) {
                WebElement activityElement = offsiteActNameList.get(i);
                String activityName = activityElement.getAttribute("content-desc");

                log.debug("Activity {}: {}", (i + 1), activityName);

                // Check if this activity matches the expected customer
                if (activityName != null && activityName.contains(customerName)) {
                    matchCount++;
                    lastMatchingActivity = activityElement;
                    lastMatchIndex = i + 1;
                    // Continue loop to find the last matching offsite activity
                }
            }

            // Click the last matching offsite activity if found
            if (lastMatchingActivity != null) {
                System.out.println("Match found: Offsite Activity for customer '" + customerName + "', Match count: " + matchCount);
                log.info("CLICKING LATEST MATCH: Offsite Activity for customer '{}' at position {} (latest occurrence)", customerName, lastMatchIndex);
                offsiteActivityName = lastMatchingActivity;

                fetchActivityDetails();

            } else {
                log.error("No offsite activities found in the list matching Customer: {}", customerName);
                xpathHelper.saveScreenshotToFolder("no_activities_found.png", "screenshots/validation_errors");
            }
        } catch (Exception e) {
            log.error("Error during activity verification: {}", e.getMessage());
            xpathHelper.saveScreenshotToFolder("activity_verification_error.png", "screenshots/validation_errors");
            commonUtils.sleep(2000);
        }
    }

    //  click on Activity to verify activity name
    private void fetchActivityDetails() {
        offsiteActivityName.click();
        commonUtils.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(viewOffsiteActivityPageHeader));
        Assert.assertTrue(serialNo_offsiteActivity.isDisplayed());

        // Get the full content-desc value
        String fullContentDesc = serialNo_offsiteActivity.getAttribute("content-desc");

        // Extract only the serial number (OFF followed by numbers)
        String serialNo_offAct = "";
        if (fullContentDesc != null && fullContentDesc.contains("OFF")) {
            // Use regex to extract OFF followed by numbers
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("OFF\\d+");
            java.util.regex.Matcher matcher = pattern.matcher(fullContentDesc);
            if (matcher.find()) {
                serialNo_offAct = matcher.group();
                log.info("Extracted serial number: {}", serialNo_offAct);
            }
        }
        System.out.println("Offsite Activity Serial Number: " + serialNo_offAct);

        //  assign to global variable
        SerialNo_OffsiteAct = serialNo_offAct;
        //  store in current_SerialNo_OffsiteAct for access from portal UI classes
        SharedTestData.setCurrent_SerialNo_OffsiteAct(SerialNo_OffsiteAct);
    }

}
