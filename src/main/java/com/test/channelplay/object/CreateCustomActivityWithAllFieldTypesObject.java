package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CreateCustomActivityWithAllFieldTypesObject extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement username_field;

    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement submit_button;

    @FindBy(xpath = "//span[text()=\" Admin \"]")
    private WebElement admin_menu;

    @FindBy(xpath = "//span[text()=\" Settings \"]/ancestor::li//span[text()=\" Admin \"]")
    private WebElement Admin_dropdown;

    @FindBy(xpath = "//span[text()=\" Admin \"]/ancestor::li//span[text()=\" Activities \"]")
    private WebElement activities_menu;

    @FindBy(xpath = "//span[text()=\"Add\"]")
    private WebElement addActivities_button;

    @FindBy(xpath = "//label[text()=\"Activity Name\"]/parent::div/following-sibling::div//input")
    private WebElement activityName_field;

    @FindBy(xpath = "//span[text()=\" Customers \"]/ancestor::mat-checkbox")
    private WebElement customer_checkbox;

    @FindBy(xpath = "//span[text()=\" Opportunities \"]/ancestor::mat-checkbox")
    private WebElement opportunity_Checkbox;

    @FindBy(xpath = "//input[@placeholder=\"Search\"]")
    private WebElement search_Field;

    @FindBy(xpath = "//img[@title=\"Edit\"]")
    private WebElement action_button;

    @FindBy(xpath = "//a[@href=\"javascript:;\"]//span[text()=\" Settings \"]")
    private WebElement settings_menu;

    @FindBy(xpath = "//span[text()=\" Settings \"]/ancestor::li//span[text()=\" Users \"]")
    private WebElement Users_option;

    @FindBy(xpath = "//button/span[text()=\"Add Field\"]")
    private WebElement addField_button;

    @FindBy(xpath = "//h4[text()=\"Add Field\"]/parent::div/following-sibling::div//div[@class=\"mat-select-arrow-wrapper\"]")
    private WebElement fieldType_dropdown;

    @FindBy(xpath = "//span[text()=\" Text (single-line) \"]")
    private WebElement textSingleLine_option;

    @FindBy(xpath = "//span[text()=\" Text (multi-line) \"]")
    private WebElement textMultiLine_option;

    @FindBy(xpath = "//mat-option/span[text()=\" Numeric \"]")
    private WebElement numeric_option;

    @FindBy(xpath = "//span[text()=\" Single Select Dropdown \"]")
    private WebElement singleSelectDropdown_option;

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

    @FindBy(xpath = "//span[text()=\" Image \"]")
    private WebElement image_option;

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

    @FindBy(xpath = "//span[text()=\" Link from other entities \"]")
    private WebElement linkFromOtherEntities_option;

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

    @FindBy(xpath = "//img[@src=\"./assets/media/svg/icons/Navigation/Angle-double-left.svg\"]")
    private WebElement back_Button;

    @FindBy(xpath = "//span[text()=\" CRM \"]")
    private WebElement CRM_Menu;

    @FindBy(xpath = "//span[text()=\" CRM \"]/ancestor::li//span[text()=\" Customers \"]")
    private WebElement customers_Option;

    @FindBy(xpath = "//span[text()=\" CRM \"]/ancestor::li//span[text()=\" Opportunities \"]")
    private WebElement opportunity_Option;

    @FindBy(xpath = "//div[text()=\" Activities \"]/parent::div//i")
    private WebElement activitiesPlus_Button;

    @FindBy(xpath = "//div[text()=' Opportunities ']/parent::div//i")
    private WebElement addOpportunity_Button;

    @FindBy(xpath = "//label[text()=\"Opportunity Name\"]/parent::div/following-sibling::div//input")
    private WebElement opportunityName_field;

    @FindBy(xpath = "//label[text()=\"Value\"]/parent::div/following-sibling::div//input")
    private WebElement value_field;

    @FindBy(xpath = "//label[text()=\"Status\"]/parent::div/following-sibling::div/mat-form-field/div")
    private WebElement status_dropdown;

    @FindBy(xpath = "//label[text()=\"Win probability\"]/parent::div/following-sibling::div/mat-form-field/div")
    private WebElement WinProbability_dropdown;

    @FindBy(xpath = "//mat-option[2]")
    private WebElement option_one;

    @FindBy(xpath = "//label[text()=\"Select Opportunity\"]/parent::div/following-sibling::div/mat-form-field/div")
    private WebElement selectOpportunity_dropdown;

    @FindBy(xpath = "//label[text()=\"Title\"]/parent::div/following-sibling::div/mat-form-field//input")
    private WebElement title_field;

    @FindBy(xpath = "//label[text()=\"Registered Email\"]/parent::div/following-sibling::div//input")
    private WebElement registeredEmail_field;

    @FindBy(xpath = "//label[text()=\"Phone Number\"]/parent::div/following-sibling::div//input")
    private WebElement phoneNumber_field;

    public String NewFieldName;
    public String opportunityName;
    public String NewActivityName = "False";
    List<String> fields = new ArrayList<String>();
    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();


    public CreateCustomActivityWithAllFieldTypesObject(){
        PageFactory.initElements(getDriver(), this);
    }

    public void login(String username, String password) {
        username_field.sendKeys(username);
        password_field.sendKeys(password);
        submit_button.click();
        commonUtils.waitForPageToLoad();
        sleep(10000);
        webDriverUtils.waitUntilVisible(getDriver(), admin_menu, Duration.ofSeconds(2));
        admin_menu.click();
        activities_menu.click();
        sleep(8000);
    }

    public void UserSelectAddButtonUnderActivityScreen(){
        addActivities_button.click();
        sleep(2000);
    }

    public void UserEnterActivityName(){
        String random_text = commonUtils.generateRandomString(4);
        NewActivityName = "DCR Activity "+random_text;
        activityName_field.sendKeys(NewActivityName);
        customer_checkbox.click();
        opportunity_Checkbox.click();
        getDriver().findElement(By.xpath("//span[text()=\"Ok\"]")).click();
    }

    public void HitSaveButton(){
        save_button.click();
        sleep(4000);
    }

    public void NewActivityWillCreateAndWillShowInTheActivityList(){
        back_Button.click();
        sleep(4000);
        search_Field.sendKeys(NewActivityName);
        sleep(3000);
        Assert.assertTrue(getDriver().findElement(By.xpath("//div//span[contains(text(),'"+NewActivityName+"')]")).isDisplayed());
    }

    public void UserSelectActionButtonForNewCreatedActivity(){
        action_button.click();
        sleep(3000);
    }

    public void SelectAddFieldButtonUnderActivity(){
        addField_button.click();
        sleep(4000);
    }

    public void userSelectExpectedFromTheFieldTypeDropdownUnderActivity(String fieldType){
        sleep(5000);
        fieldType_dropdown.click();
        WebElement Dropdown_option = getDriver().findElement(By.xpath("//span[text()=' "+fieldType+" ']"));
        Dropdown_option.click();
        sleep(2000);
    }

    public void enterForUnderActivity(String fieldName){
        NewFieldName = fieldName;
        fields.add(fieldName);
        fieldName_field.sendKeys(fieldName);
    }

    public void SelectSaveButton() {
        save_button.click();
        sleep(4000);
    }

    public void NewFieldWillBeShownInTheList() {
        search_Field.clear();
        search_Field.sendKeys(NewFieldName);
        sleep(2000);
        Assert.assertTrue(getDriver().findElement(By.xpath("//div//span[contains(text(),'"+NewFieldName+"')]")).isDisplayed());
    }

    public void UserSelectCRMMenu(){
        CRM_Menu.click();
        sleep(1000);
    }

    public void UserSelectCustomers(){
        customers_Option.click();
        sleep(3000);
    }

    public void SelectDashboardButtonForAnyAnyCustomerFromTheList(){
        getDriver().findElement(By.xpath("//a/img[@title=\"Dashboard\"]")).click();
        sleep(5000);
    }

    public void SelectAddActivitiesButton(){
        sleep(2000);
        WebElement opportunity_Section = getDriver().findElement(By.xpath("//div[text()=' Opportunities ']/parent::div/following-sibling::div"));
        List<WebElement> opportunities = opportunity_Section.findElements(By.tagName("div"));
        System.out.println(opportunities.size()+"----------------------------------->");
        if(opportunities.size()<1){
            addOpportunity_Button.click();
            sleep(6000);
            String opportunityName = "Opp " + commonUtils.generateRandomString(4);
            opportunityName_field.sendKeys(opportunityName);
            value_field.sendKeys("50000");
            status_dropdown.click();
            sleep(1000);
            option_one.click();
            WinProbability_dropdown.click();
            sleep(1000);
            option_one.click();
            save_button.click();
            sleep(3000);
        }
        sleep(4000);
        activitiesPlus_Button.click();
        sleep(3000);
    }

    public void UserSelectNewCreatedActivity(){
        getDriver().findElement(By.xpath("//span[text()=' "+NewActivityName+" ']")).click();
        sleep(4000);
    }

    public void AllNewCreatedFieldsWillShowInTheForm(){
        for(int i=0; i<=fields.size()-1; i++){
            getDriver().findElement(By.xpath("//label[text()='"+fields.get(i)+"']")).isDisplayed();
            sleep(2000);
        }
    }

    public void UserSelectOpportunity(){
        opportunity_Option.click();
        sleep(3000);
    }

    public void SelectDashboardButtonForAnyOpportunityFromTheList(){
        getDriver().findElement(By.xpath("//a/img[@title=\"Dashboard\"]")).click();
        sleep(5000);
    }

    public void SelectOpportunity(){
        selectOpportunity_dropdown.click();
        sleep(1000);
        opportunityName = getDriver().findElement(By.xpath("//mat-option[2]/span")).getText();
        option_one.click();
    }

    public void EnterTitle(){
        String title = "title " + commonUtils.generateRandomString(3);
        title_field.sendKeys(title);
    }

    public void EnterRegisteredEmail(){
        registeredEmail_field.sendKeys("abcd@gmail.com");
    }

    public void  EnterPhoneNumber(){
        phoneNumber_field.sendKeys("8001695524");
    }

    public void SelectSaveButtonUnderActivityForm(){
        sleep(3000);
        save_button.click();
    }

    public void NewActivityWillShowInTheDashboard(){
        sleep(2000);
        WebElement createdActivity = getDriver().findElement(By.xpath("//div[contains(text(), '"+opportunityName+"')]"));
        Assert.assertTrue(createdActivity.isDisplayed());
    }

    public void SelectAddActivitiesButtonForOpportunity(){
        sleep(3000);
        activitiesPlus_Button.click();
        sleep(3000);
    }

    public void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
