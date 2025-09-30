package com.test.channelplay.object.activities;

import com.test.channelplay.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddActivity_testUserPage extends DriverBase {

    private static final Logger log = LoggerFactory.getLogger(AddActivity_testUserPage.class);


    @FindBy(xpath = Constants.CRM_menu)
    WebElement CRM_menu_ele;
    @FindBy(xpath = Constants.CRMActivities_subMenu)
    WebElement CRMActivities_subMenu_ele;
    @FindBy(xpath = "//h5[text()=' Activities ']")
    WebElement CRMActivities_PageHeader;
    @FindBy(xpath = "//div[contains(@class, '-active')]/div[text()='Performed Activities']")
    WebElement PerformedActivities_tab_Selected;
    @FindBy(xpath = "//*[text()=' Filters ']")
    WebElement Filters_button;
    @FindBy(xpath = "//h4[text()='Filters']")
    WebElement FilterTextHeader;
    @FindBy(xpath = "//*[@formcontrolname='activityType']")
    WebElement activityType_dropdown;
    @FindBy(xpath = "//*[@formcontrolname='entityfield']")
    WebElement entityField_dropdown;
    @FindBy(xpath = "//*[@formcontrolname='operator']")
    WebElement operator_dropdown;
    @FindBy(xpath = "//*[@formcontrolname='value']")
    WebElement valueEntry_textbox;
    @FindBy(xpath = "//button[text()=' Apply ']")
    WebElement Apply_button;
    @FindBy(xpath = "(//span[text()=' Offsite Activity '])[2]")
    WebElement activityType_offsiteActivity_option;
    @FindBy(xpath = "//span[text()=' Serial Key ']")
    WebElement entityField_serialKey_option;
    @FindBy(xpath = "//span[text()=' Equals to ']")
    WebElement operator_equalsTo_option;
    @FindBy(xpath = "//label[text()='Serial No.']/parent::div//following-sibling::div//input")
    WebElement editAct_SerialNoText;
    @FindBy(xpath = "//label[text()='Title ']/parent::div//following-sibling::div//input")
    WebElement editAct_TitleText;
    @FindBy(xpath = "//label[text()='Description ']/parent::div//following-sibling::div//textarea")
    WebElement editAct_DescriptionText;
    @FindBy(xpath = "//label[text()='Perform date ']/parent::div//following-sibling::div//input")
    WebElement editAct_PerformDateText;
    @FindBy(xpath = "//button[text()='Delete']")
    WebElement activity_DeleteButton;
    @FindBy(xpath = "//span[text() = 'Activities updated']")
    WebElement activity_UpdatedToast;




    String SerialNo_OffsiteAct = SharedTestData.getCurrent_SerialNo_OffsiteAct();
    String Title_OffsiteAct = SharedTestData.getCurrent_Title_OffsiteAct();
    CommonUtils commonUtils = new CommonUtils();
    SoftAssert softAssert = new SoftAssert();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

    public AddActivity_testUserPage() {
        PageFactory.initElements(getDriver(), this);
    }




    //  scenario - offsiteActivity

    public void userClicksOnCRMMenuAndActivitiesSubmenu() {
        webDriverUtils.until(ExpectedConditions.visibilityOf(CRM_menu_ele));
        CRM_menu_ele.click();
        webDriverUtils.until(ExpectedConditions.visibilityOf(CRMActivities_subMenu_ele));
        CRMActivities_subMenu_ele.click();
        commonUtils.sleep(1000);
        Assert.assertTrue(CRMActivities_PageHeader.isDisplayed());
        Assert.assertTrue(PerformedActivities_tab_Selected.isDisplayed());
    }

    public void clicksOnFilterAndSearchWithSerialNoOfOffsiteActivityFetchedFromApp() {
        Filters_button.click();
        webDriverUtils.until(ExpectedConditions.visibilityOf(FilterTextHeader));
        Assert.assertTrue(FilterTextHeader.isDisplayed());
        commonUtils.sleep(1000);
        //  Filtering with fetched offsite activity serial no
        webDriverUtils.until(ExpectedConditions.elementToBeClickable(entityField_dropdown));
        entityField_dropdown.click();
        webDriverUtils.until(ExpectedConditions.visibilityOf(entityField_serialKey_option));
        commonUtils.sleep(1000);
        entityField_serialKey_option.click();

        webDriverUtils.until((ExpectedConditions.elementToBeClickable(activityType_dropdown)));
        commonUtils.sleep(1000);
        activityType_dropdown.click();
        webDriverUtils.until(ExpectedConditions.visibilityOf(activityType_offsiteActivity_option));
        commonUtils.sleep(1000);
        activityType_offsiteActivity_option.click();

        operator_dropdown.click();
        operator_equalsTo_option.click();
        commonUtils.sleep(1000);
        valueEntry_textbox.sendKeys(SerialNo_OffsiteAct);
        ScreenshotHelper.captureScreenshot("Filter criteria");
        Apply_button.click();
        commonUtils.sleep(2000);

        //  Verify searched activity is displayed in list
        WebElement searchedActivity = getDriver().findElement(By.xpath
                ("((//span[text()='Serial No.']/ancestor::div[@role='presentation'])[1]/following-sibling::" +
                        "div[div[@role='presentation']])[2]/descendant::div[text()='" +SerialNo_OffsiteAct+ "']"));
        Assert.assertTrue(searchedActivity.isDisplayed());
        ScreenshotHelper.captureScreenshot("activity displayed list");
    }

    public void clicksOnEditActivityAndValidateDataThenDelete() {
        WebElement searchedActivityEditButton = getDriver().findElement(By.xpath
                ("//div[text()='" + SerialNo_OffsiteAct + "']/following-sibling::div/descendant::img"));
        searchedActivityEditButton.click();
        commonUtils.sleep(1000);
        webDriverUtils.until(ExpectedConditions.visibilityOf(editAct_SerialNoText));

        //  verify fields and values are properly visible on portal
        softAssert.assertEquals(SerialNo_OffsiteAct, editAct_SerialNoText.getAttribute("value"));
        softAssert.assertEquals(Title_OffsiteAct, editAct_TitleText.getAttribute("value"));
        softAssert.assertTrue(editAct_DescriptionText.isDisplayed());
        //  Get today's date in M/d/yyyy format
        String TodayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
        System.out.println(editAct_PerformDateText.getAttribute("value"));
        softAssert.assertEquals(editAct_PerformDateText.getAttribute("value"), TodayDate);

        try {
            softAssert.assertAll();
            commonUtils.sleep(1000);
            // if no exception → all assertions passed → safe to delete
            activity_DeleteButton.click();
            webDriverUtils.until(ExpectedConditions.visibilityOf(activity_UpdatedToast));
            ScreenshotHelper.captureScreenshot("Activity deleted");
            webDriverUtils.until(ExpectedConditions.visibilityOf(CRMActivities_PageHeader));
            log.info("Delete action completed successfully. Redirected to Activities page.");
        } catch (AssertionError e) {
            log.info("Assertions failed. Delete action skipped.");
            throw e;
        }

        //  reset shared test data
        SharedTestData.reset();
    }

}
