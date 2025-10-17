package com.test.channelplay.object.activitySetting.useCase_Scenarios;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.Constants;
import com.test.channelplay.utils.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.test.channelplay.utils.SharedTestData;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class LinkedField_DateObject extends DriverBase {

    private static final Logger log = LoggerFactory.getLogger(LinkedField_DateObject.class);


    @FindBy(xpath = Constants.Admin_menu)
    WebElement Admin_menu_ele;
    @FindBy(xpath = Constants.AdminActivities_subMenu)
    WebElement Activities_subMenu_ele;
    @FindBy(xpath = "//h5[text()=' Activities ']")
    WebElement activitiesPageHeader;
    @FindBy(xpath = "//span[text()='Add Field']/parent::button")
    WebElement addFieldButton;
    @FindBy(xpath = "//div/label[text()='Field Type ']/parent::div/following-sibling::div//child::mat-select/div/div")
    WebElement FieldType_dropdown;
    @FindBy(xpath = "//span[text()=' Link from other entities ']")
    WebElement FieldType_dropdown_LinkFromOtherEntitiesOption;
    @FindBy(xpath = "//h4[text()='Add Field']")
    WebElement addField_popupHeader;
    @FindBy(xpath = "//label[text()='Field Name ']/parent::div/following-sibling::div//child::input")
    WebElement FieldName_input;
    @FindBy(xpath = "//label[text()='Entity ']/parent::div/following-sibling::div//child::span")
    WebElement Entity_dropdown;
    @FindBy(xpath = "//label[text()='Entity Field ']/parent::div/following-sibling::div//child::mat-select")
    WebElement Entity_Field_dropdown;
    @FindBy(xpath = "//span[text()=' Date of Creation ']")
    WebElement Entity_Field_DateOption;
    @FindBy(xpath = "//button[text()='Save']")
    WebElement saveButton;
    @FindBy(xpath = "//img[contains(@src, 'Navigation/Angle-double-left')]")
    WebElement customActivityPageBackButton;
    @FindBy(xpath = "//button[text() = ' Yes ']")
    WebElement confirmDeleteActivity_YesButton;
    @FindBy(xpath = "//input[@placeholder='Search']")
    WebElement searchActivity_input;


    String singularName, customActivity;
    CommonUtils commonUtils = new CommonUtils();
    SoftAssert softAssert = new SoftAssert();
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

    public LinkedField_DateObject() {
        PageFactory.initElements(getDriver(), this);
    }




    //  scenario - useCase_Scenario_b2b-2943

    public void userClicksOnAdminMenuAndActivitiesSubmenuAndNavigatesToActivitiesPage() {
        wait.until(ExpectedConditions.visibilityOf(Admin_menu_ele));
        Admin_menu_ele.click();
        wait.until(ExpectedConditions.visibilityOf(Activities_subMenu_ele));
        Activities_subMenu_ele.click();
        commonUtils.sleep(1000);
        Assert.assertTrue(activitiesPageHeader.isDisplayed());
    }

    public void enterIntoCustomActivityPageAndClicksOnAddFieldButtonToCreateANewDateTypeLinkedField() {
        //  get custom activity name from API response
        customActivity = SharedTestData.getCurrentActivityName();
        System.out.println("Custom Activity name at UI: " + customActivity);
        By customActEditButton = By.xpath("(//div[contains(.,'" + customActivity + "')]/following-sibling::div//img[@title='Edit'])[1]");
        commonUtils.clickWithRetry(customActEditButton, 5);
        commonUtils.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(addFieldButton));
        addFieldButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(FieldType_dropdown));
        FieldType_dropdown.click();
        commonUtils.sleep(1000);
        FieldType_dropdown_LinkFromOtherEntitiesOption.click();
        Assert.assertTrue(addField_popupHeader.isDisplayed());
        String fieldName = "testLinkedDateField__";
            FieldName_input.sendKeys(fieldName);

        //  get singular name from SharedTestData
        singularName = SharedTestData.getSingularNameJson();
        System.out.println("Singular Name found at UI class: " + singularName);
        commonUtils.sleep(1000);
        Entity_dropdown.click();
        WebElement Entity_dropdown_CompanyOption = getDriver().findElement(By.xpath("//mat-option[@role='option']/span[text()=' " + singularName + " ']"));
        Entity_dropdown_CompanyOption.click();
        commonUtils.sleep(1000);
        Entity_Field_dropdown.click();
        Entity_Field_DateOption.click();
        commonUtils.sleep(1000);
        saveButton.click();
        commonUtils.sleep(3000);

        //  verify created linked field is present under field list on activity Fields page
        System.out.println("Created field present for verification: " + fieldName);
        WebElement customFieldCreated = getDriver().findElement(By.xpath("//span[text()='" + fieldName + "']"));
        wait.until(ExpectedConditions.visibilityOf(customFieldCreated));
        Assert.assertTrue(customFieldCreated.isDisplayed());

        WebElement customFieldType = getDriver().findElement(By.xpath("//span[text()='" + fieldName + "']/ancestor::div[@role='row']//child::span[contains(text(), 'Link from')]"));
        assertEquals("Link from other entities", customFieldType.getText());
        commonUtils.sleep(1000);
    }

    public void deleteTheCustomActivityAfterValidationIsCompleteForBB() {
        //  click back from custom activity page to activities page
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(customActivityPageBackButton)));
        customActivityPageBackButton.click();
        Assert.assertTrue(activitiesPageHeader.isDisplayed());
        commonUtils.waitForPageToLoad();
        //  verify created custom activity is present in activities list
        By createdCustomActivity = By.xpath("//div/span[contains(text(), '" + customActivity + "')]");
        commonUtils.isDisplayedWithRetry(createdCustomActivity, 5);
        //  delete the created custom activity
        By deleteCustomActivityButton = By.xpath("//div/span[contains(text(), '" + customActivity + "')]/parent::div/parent::div/following-sibling::div/descendant::span/img[contains(@src, 'General/delete')]");
        commonUtils.clickWithRetry(deleteCustomActivityButton, 3);
        confirmDeleteActivity_YesButton.click();
        commonUtils.sleep(2000);
        //  verify the custom activity is deleted successfully
        commonUtils.waitForPageToLoad();
        searchActivity_input.click();
        searchActivity_input.sendKeys(customActivity);
        commonUtils.sleep(1000);

        List<WebElement> searchResults = getDriver().findElements(By.xpath("//span[contains(text(), '" + customActivity + "')]/parent::div/parent::div/parent::div"));

        softAssert.assertEquals(searchResults.size(), 0);
        softAssert.assertTrue(searchResults.isEmpty(), "Activity should be deleted but still found in search results");
        softAssert.assertAll();
        log.info("Activity '{}' deleted successfully - verified not found in search results", customActivity);
    }

}
