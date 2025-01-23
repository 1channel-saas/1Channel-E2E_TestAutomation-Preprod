package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AddAllFieldTypesOpportunityObject extends DriverBase {

    public String NewFieldName;
    CommonUtils commonUtils = new CommonUtils();
    Actions action = new Actions(getDriver());

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement username_field;
    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;
    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement submit_button;
    @FindBy(xpath = "//a[@href=\"javascript:;\"]//span[text()=\" Settings \"]")
    private WebElement settings_menu;
    @FindBy(xpath = "//span[text()=\" Settings \"]/ancestor::li//span[text()=\" CRM \"]")
    private WebElement CRM_dropdown;
    @FindBy(xpath = "//span[text()=\" Opportunity \"]")
    private WebElement opportunity_option;
    @FindBy(xpath = "//button/span[text()=\"Add Field\"]")
    private WebElement addField_button;
    @FindBy(xpath = "//h4[text()=\"Add Field\"]/parent::div/following-sibling::div//div[@class=\"mat-select-arrow-wrapper\"]")
    private WebElement fieldType_dropdown;
    @FindBy(xpath = "//mat-option/span[text()=\" Numeric \"]")
    private WebElement numeric_option;
    @FindBy(xpath = "//label[text()=\"Field Name\"]/parent::div/following-sibling::div//input")
    private WebElement fieldName_field;
    @FindBy(xpath = "//mat-option/span[text()=\" Multi Select Dropdown \"]")
    private WebElement multiSelectDropdown_option;
    @FindBy(xpath = "//label[text()=\"Options\"]/parent::div/following-sibling::div//input")
    private WebElement options_field;
    @FindBy(xpath = "//mat-option/span[text()=\" Date \"]")
    private WebElement date_option;
    @FindBy(xpath = "//label[text()=\"Value Type\"]/parent::div/following-sibling::div//mat-radio-button[@id=\"mat-radio-3\"]")
    private WebElement CustomValueType_radioButton;
    @FindBy(xpath = "//mat-option/span[text()=\" Email \"]")
    private WebElement email_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Document Upload \"]")
    private WebElement documentUpload_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Video \"]")
    private WebElement video_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Reference Documents \"]")
    private WebElement referenceDocuments_option;
    @FindBy(xpath = "//input[@id=\"fileInput\"]")
    private WebElement uploadFile_field;
    @FindBy(xpath = "//mat-option/span[text()=\" Header Text \"]")
    private WebElement headerText_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Normal Text \"]")
    private WebElement normalText_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Page Separator \"]")
    private WebElement pageSeparator_option;
    @FindBy(xpath = "//mat-option/span[text()=\" Data List \"]")
    private WebElement dataList_option;
    @FindBy(xpath = "//label[text()=\"List Name\"]/parent::div/following-sibling::div/mat-form-field/div")
    private WebElement listName_dropdown;
    @FindBy(xpath = "//h4[text()=\"Add Field\"]/parent::div/following-sibling::div[2]/button[text()=\"Cancel\"]")
    private WebElement cancel_button;
    @FindBy(xpath = "//mat-option/span[text()=\" OTP Validation \"]")
    private WebElement OTPValidation_option;
    @FindBy(xpath = "//label[text()=\"Entity\"]/parent::div/following-sibling::div/mat-form-field")
    private WebElement entity_dropdown;
    @FindBy(xpath = "//label[text()=\"Entity Field\"]/parent::div/following-sibling::div/mat-form-field")
    private WebElement entityField_dropdown;
    @FindBy(xpath = "//button[text()=\"Save\"]")
    private WebElement save_button;
    @FindBy(xpath = "//span[text()=\"Field added.\"]")
    private WebElement success_message;

    public AddAllFieldTypesOpportunityObject() {
        PageFactory.initElements(getDriver(), this);
    }

    public void login(String username, String password) {
        username_field.sendKeys(username);
        password_field.sendKeys(password);
        submit_button.click();
        commonUtils.waitForPageToLoad();
        sleep(10000);
        settings_menu.click();
        CRM_dropdown.click();
        opportunity_option.click();
    }

    public void UserSelectAddFieldButton() {
        addField_button.click();
        sleep(3000);
    }

    public void UserSelectNumericFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        numeric_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForNumericType() {
        NewFieldName = "Company Phone Number Test";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void SelectSaveButton() {
        save_button.click();
        sleep(4000);
    }

    public void NewFieldWillBeShownInTheList() {
        Assert.assertTrue(getDriver().findElement(By.xpath("//div//span[text()='" + NewFieldName + "']")).isDisplayed());
    }

    public void UserSelectMultiSelectDropdownFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        multiSelectDropdown_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForMultiSelectDropdownType() {
        NewFieldName = "Follow Up";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void enterOptions() {
        options_field.sendKeys("By Week");
    }

    public void UserSelectDateFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        date_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForDateType() {
        NewFieldName = "Visit Date";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void SelectValueTypeAsCustom() {
        CustomValueType_radioButton.click();
    }

    public void UserSelectEmailFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        email_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForEmailType() {
        NewFieldName = "Store Email";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectDocumentUploadFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        documentUpload_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForDocumentUploadType() {
        NewFieldName = "Plan Document";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectVideoFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        video_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForVideoType() {
        NewFieldName = "Customer Testimonial";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectReferenceDocumentsFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        referenceDocuments_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForReferenceDocumentsType() {
        NewFieldName = "Self Reference";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UploadAFileUnderUploadFile() {
        String path = System.getProperty("user.dir");
        uploadFile_field.sendKeys(path + "/src/main/resources/Files/samplePDF.pdf");

        action.moveToElement(save_button);
        sleep(2000);
    }

    public void UserSelectHeaderTextFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        headerText_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForHeaderTextType() {
        NewFieldName = "Store Details";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectNormalTextFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        normalText_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForNormalTextType() {
        NewFieldName = "Store Type";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectPageSeparatorFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        pageSeparator_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForPageSeparatorType() {
        NewFieldName = "Another Details";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void UserSelectDataListFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        dataList_option.click();
        sleep(2000);
    }

    public void EnterFieldNameForDataListType() {
        NewFieldName = "Products";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void SelectListName() {
        listName_dropdown.click();
        sleep(2000);

        WebElement option = getDriver().findElement(By.xpath("//input[@aria-label=\"dropdown search\"]/ancestor::div[2]"));
        List<WebElement> options = option.findElements(By.tagName("mat-option"));

        if (options.size() > 1) {
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
        } else {
            sleep(2000);
            action.moveToElement(cancel_button).click().perform();
            sleep(1000);
            cancel_button.click();
            sleep(1000);
            settings_menu.click();
            sleep(1000);
            getDriver().findElement(By.xpath("//span[text()=\" System \"]")).click();
            sleep(1000);
            getDriver().findElement(By.xpath("//span[text()=\" Data List \"]")).click();
            sleep(2000);
            getDriver().findElement(By.xpath("//span[text()=\"Add\"]")).click();
            sleep(2000);
            getDriver().findElement(By.xpath("//label[text()=\"List Name\"]/parent::div/following-sibling::div//input")).sendKeys("Mobile");
            getDriver().findElement(By.xpath("//label[text()=\"Value\"]/parent::div/following-sibling::div//input")).sendKeys("Samsung");
            save_button.click();
            sleep(3000);
            settings_menu.click();
            CRM_dropdown.click();
            opportunity_option.click();
            addField_button.click();
            sleep(3000);
            UserSelectDataListFromTheFieldTypeDropdown();
            EnterFieldNameForDataListType();
            listName_dropdown.click();
            sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
        }
    }

    public void UserSelectOTPValidationFromTheFieldTypeDropdown() {
        fieldType_dropdown.click();
        OTPValidation_option.click();
        sleep(2000);
    }

    public void EnterFieldNameOTPValidationType() {
        NewFieldName = "Enter OTP";
        fieldName_field.sendKeys(NewFieldName);
    }

    public void EnterEntity() {
        entity_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option[2]")).click();
        sleep(3000);
    }

    public void SelectEntityField() {
        entityField_dropdown.click();
        sleep(3000);
        getDriver().findElement(By.xpath("//mat-option[2]")).click();
        sleep(2000);

    }

    public void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
