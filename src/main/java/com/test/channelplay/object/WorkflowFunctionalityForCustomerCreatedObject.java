package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class WorkflowFunctionalityForCustomerCreatedObject extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement email_field;

    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement SignIn_button;

    @FindBy(xpath = "//span[text()=\" Admin \"]")
    private WebElement Admin_Menu;

    @FindBy(xpath = "//span[text()=\" Workflows \"]")
    private WebElement workflow_subMenu;

    @FindBy(xpath = "//span[contains(text(),'Add New')]/parent::button")
    private WebElement addNew_button;

    @FindBy(xpath = "//label[text()=\"Name\"]/parent::div/following-sibling::div//input")
    private WebElement name_field;

    @FindBy(xpath = "//label[text()=\"Description\"]/parent::div/following-sibling::div//input")
    private WebElement description_field;

    @FindBy(xpath = "//label[text()=\"Entity\"]/parent::div/following-sibling::div//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement entity_dropdown;

    @FindBy(xpath = "//mat-option/span[contains(text(),'Customer')]")
    private WebElement customer_option;

    @FindBy(xpath = "//label[text()=\"Trigger\"]/parent::div/following-sibling::div//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement trigger_dropdown;

    @FindBy(xpath = "//mat-option/span[contains(text(),'Customer is Created')]")
    private WebElement customerIsCreated_option;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    private WebElement save_button;

    @FindBy(xpath = "//div[contains(text(),'Workflow is Inactive')]/following-sibling::div/kt-switch")
    private WebElement workflowActive_Button;

    @FindBy(xpath = "//input[@placeholder=\"Search\"]")
    private WebElement search_field;

    @FindBy(xpath = "//label[text()=\"Repeat\"]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement repeat_dropdown;

    @FindBy(xpath = "//button[@aria-label=\"Open calendar\"]")
    private WebElement openCalendar_button;

    @FindBy(xpath = "//img[@title=\"Workflow\"]")
    private WebElement workflowAction_Button;

    @FindBy(xpath = "//div[contains(text(),'Add an Action or Condition')]/parent::div//i")
    private WebElement addAnActionOrCondition_Button;

    @FindBy(xpath = "//div[text()='Condition ']/ancestor::div[3]/following-sibling::div/div[1]//div[text()='Add an Action or Condition']/ancestor::div[2]//i")
    private WebElement addAnActionOrCondition_ifNO_Button;

    @FindBy(xpath = "//div[text()='Condition ']/ancestor::div[3]/following-sibling::div/div[2]//div[text()='Add an Action or Condition']/ancestor::div[2]//i")
    private WebElement addAnActionOrCondition_ifYES_Button;


    @FindBy(xpath = "//div/h4[text()=\"Insert New Action\"]/parent::div/following-sibling::div[2]/button[text()=\" Save \"]")
    private WebElement InsertNewAction_SaveButton;

    @FindBy(xpath = "//div[contains(text(),'Send Email')]/img")
    private WebElement sentEmail_Action;

    @FindBy(xpath = "//button[contains(text(),'Next')]")
    private WebElement next_Button;

    @FindBy(xpath = "//label[contains(text(),'To')]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement to_Dropdown;

    @FindBy(xpath = "//label[contains(text(),'To')]/parent::div")
    private WebElement to_label;

    @FindBy(xpath = "//label[contains(text(),'Users')]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement users_Dropdown;

    @FindBy(xpath = "//label[contains(text(),'CC')]/ancestor::div[2]/following-sibling::div//label[contains(text(),'Users')]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement users_Dropdown_afterCC;

    @FindBy(xpath = "//label[contains(text(),'Users')]/parent::div")
    private WebElement users_label;

    @FindBy(xpath = "//label[contains(text(),'Email Ids')]/ancestor::div[2]//input")
    private WebElement emailIds_field;

    @FindBy(xpath = "//label[contains(text(),'CC')]/ancestor::div[2]/following-sibling::div//label[contains(text(),'Email Ids')]/ancestor::div[2]//input")
    private WebElement emailIds_field_afterCC;

    @FindBy(xpath = "//label[contains(text(),'CC')]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement CC_Dropdown;

    @FindBy(xpath = "//label[contains(text(),'CC')]/parent::div")
    private WebElement CC_label;

    @FindBy(xpath = "//label[contains(text(),'Subject')]/ancestor::div[2]//input")
    private WebElement subject_field;

    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]")
    private WebElement message_field;

    @FindBy(xpath = "//div[contains(text(),'Send SMS')]/img")
    private WebElement sendSMS_action;

    @FindBy(xpath = "//label[contains(text(),'Phone Numbers')]/ancestor::div[2]//input")
    private WebElement phoneNumbers_field;

    @FindBy(xpath = "//div[contains(text(),'Send App Notifications')]/img")
    private WebElement sendAppNotifications_action;

    @FindBy(xpath = "//label[contains(text(),'Title')]/ancestor::div[2]//input")
    private WebElement title_field;

    @FindBy(xpath = "//label[contains(text(),'Notification Action')]/ancestor::div[2]//mat-checkbox")
    private WebElement notificationAction_Button;

    @FindBy(xpath = "//div[contains(text(),'Create Activity')]/img")
    private WebElement CreateActivity_action;

    @FindBy(xpath = "//label[text()=\"Activity Name\"]/parent::div/following-sibling::div//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement activityName_dropdown;

    @FindBy(xpath = "//mat-option/span[contains(text(),\"Offsite Activity\")]")
    private WebElement offsiteActivity_Option;

    @FindBy(xpath = "//label[contains(text(),\"Comment\")]/ancestor::div[2]//input")
    private WebElement comment_field;

    @FindBy(xpath = "//label[contains(text(),\"Due Date\")]/ancestor::div[2]//input")
    private WebElement value_Field;

    @FindBy(xpath = "//label[contains(text(),\"Due Date\")]/ancestor::div[2]//mat-select")
    private WebElement duration_dropdown;

    @FindBy(xpath = "//mat-option/span[contains(text(),\"Days\")]")
    private WebElement days_option;

    @FindBy(xpath = "//div[contains(text(),'Delay Timer')]/img")
    private WebElement DelayTimer_action;

    @FindBy(xpath = "//label[text()=\"Due Date\"]/ancestor::div[2]//input")
    private WebElement dueDate_Value_field;

    @FindBy(xpath = "//label[text()=\"Due Date\"]/ancestor::div[2]//mat-select")
    private WebElement Duration_dropdown;

    @FindBy(xpath = "//mat-month-view//tbody/tr[1]/td[1]")
    private WebElement calenderMonth_text;

    @FindBy(xpath = "//button[@aria-label=\"Next month\"]")
    private WebElement calendarNextMonth_button;

    @FindBy(xpath = "//div[contains(text(),'New Condition')]/img")
    private WebElement newCondition_action;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"entity\"]/ancestor::div[3]")
    private WebElement selectEntity_dropdown;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"entityfield\"]/ancestor::div[3]")
    private WebElement selectEntityField_dropdown;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"operator\"]/ancestor::div[3]")
    private WebElement selectOperator_dropdown;

    @FindBy(xpath = "//input[@formcontrolname=\"value\"]")
    private WebElement selectValue_field;

    @FindBy(xpath = "//span[contains(text(),'Add more')]/parent::div//button")
    private WebElement addMore_button;

    @FindBy(xpath = "//span[contains(text(),'Add more')]/ancestor::div[4]//mat-select[@formcontrolname=\"entityfield\"]/ancestor::div[3]")
    private WebElement addMore_selectEntityField_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add more')]/ancestor::div[4]//mat-select[@formcontrolname=\"operator\"]/ancestor::div[3]")
    private WebElement addMore_selectOperator_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add more')]/ancestor::div[4]//input[@formcontrolname=\"value\"]")
    private WebElement addMore_selectValue_field;

    @FindBy(xpath = "//span[contains(text(),'Add more')]/ancestor::div[4]//mat-select[@formcontrolname=\"entity\"]/ancestor::div[3]")
    private WebElement addMore_selectEntity_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add Group')]/parent::div//button")
    private WebElement addGroup_button;

    @FindBy(xpath = "//span[contains(text(),'Add Group')]/ancestor::div[4]//mat-select[@formcontrolname=\"entity\"]/ancestor::div[3]")
    private WebElement addGroup_selectEntity_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add Group')]/ancestor::div[4]//mat-select[@formcontrolname=\"entityfield\"]/ancestor::div[3]")
    private WebElement addGroup_selectEntityField_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add Group')]/ancestor::div[4]//mat-select[@formcontrolname=\"operator\"]/ancestor::div[3]")
    private WebElement addGroup_selectOperator_dropdown;

    @FindBy(xpath = "//span[contains(text(),'Add Group')]/ancestor::div[4]//input[@formcontrolname=\"value\"]")
    private WebElement addGroup_selectValue_field;

    @FindBy(xpath = "//div[contains(text(),'Approval')]/img")
    private WebElement Approval_Action;

    @FindBy(xpath = "//label[text()='Name']/ancestor::div[2]//input")
    private WebElement ApprovalName_field;

    @FindBy(xpath = "//label[contains(text(),'Roles')]/ancestor::div[2]//div[@class=\"mat-form-field-wrapper\"]")
    private WebElement Roles_dropdown;

    @FindBy(xpath = "//label[text()=\"Enable Editing\"]/ancestor::div[2]//div[@class=\"mat-checkbox-inner-container\"]")
    private WebElement EnableEditing_checkbox;




    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[1]")
    private WebElement first_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[2]")
    private WebElement second_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[3]")
    private WebElement third_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[4]")
    private WebElement fourth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[5]")
    private WebElement fifth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[6]")
    private WebElement sixth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[1]")
    private WebElement seventh_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[2]")
    private WebElement eighth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[3]")
    private WebElement ninth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[4]")
    private WebElement tenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[5]")
    private WebElement eleventh_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ol/li[6]")
    private WebElement twelfth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[7]")
    private WebElement thirteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[1]")
    private WebElement fourteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[2]")
    private WebElement fifteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[3]")
    private WebElement sixteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[4]")
    private WebElement seventeenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[5]")
    private WebElement eighteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[6]")
    private WebElement nineteenth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[7]")
    private WebElement twentieth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/ul/li[8]")
    private WebElement twenty_first_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[7]")
    private WebElement twenty_second_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[8]")
    private WebElement twenty_third_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[9]")
    private WebElement twenty_fourth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[10]")
    private WebElement twenty_fifth_line;
    @FindBy(xpath = "//label[contains(text(),'Message')]/ancestor::div[2]//div[@data-placeholder=\"Insert message here ...\"]/div[11]")
    private WebElement twenty_sixth_line;
    @FindBy(xpath = "//span[@data-label=\"Placeholder\"]")
    private WebElement placeholder_dropdown;
    @FindBy(xpath = "//button[@class=\"ql-bold\"]")
    private WebElement boldStyle_button;
    @FindBy(xpath = "//button[@class=\"ql-bold ql-active\"]")
    private WebElement boldStyle_button_Active;
    @FindBy(xpath = "//button[@class=\"ql-italic\"]")
    private WebElement italicStyle_button;
    @FindBy(xpath = "//button[@class=\"ql-italic ql-active\"]")
    private WebElement italicStyle_button_Active;
    @FindBy(xpath = "//button[@class=\"ql-underline\"]")
    private WebElement underlineStyle_button;
    @FindBy(xpath = "//button[@class=\"ql-underline ql-active\"]")
    private WebElement underlineStyle_button_Active;
    @FindBy(xpath = "//button[@value=\"ordered\"]")
    private WebElement orderedListStyle_button;
    @FindBy(xpath = "//button[@value=\"bullet\"]")
    private WebElement bulletListStyle_button;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[4]")
    private WebElement lineMarginStyle_dropdown;
    @FindBy(xpath = "//span[@data-value=\"huge\"]")
    private WebElement margin_option_huge;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[5]")
    private WebElement textSize_dropdown;
    @FindBy(xpath = "//span[@data-value=\"5\"]")
    private WebElement heading5_option;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[6]/span[1]")
    private WebElement textColor_dropdown;
    @FindBy(xpath = "//span[@style=\"background-color: rgb(255, 255, 255);\"]")
    private WebElement textColor_white;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[6]/span[2]")
    private WebElement backgroundColor_dropdown;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[6]/span[2]//span[@style=\"background-color: rgb(102, 163, 224);\"]")
    private WebElement backgroundColor_blue;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[8]")
    private WebElement alignment_dropdown;
    @FindBy(xpath = "//span[@class=\"ql-picker-options\"]/span[@data-value=\"center\"]")
    private WebElement alignment_center;
    @FindBy(xpath = "//div[@class=\"ql-toolbar ql-snow\"]/span[7]")
    private WebElement textStyle_dropdown;
    @FindBy(xpath = "//span[@data-value=\"monospace\"]")
    private WebElement textStyle_monospace;
    @FindBy(xpath = "//button[@class=\"ql-link\"]")
    private WebElement link_button;
    @FindBy(xpath = "//div[@data-mode=\"link\"]//input")
    private WebElement link_field;
    @FindBy(xpath = "//div[@data-mode=\"link\"]/a[@class=\"ql-action\"]")
    private WebElement link_save_button;





    public WorkflowFunctionalityForCustomerCreatedObject(){
        PageFactory.initElements(getDriver(),this);
    }

    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    Actions actions = new Actions(getDriver());

    public String workflowName;
    public String emailSentSubject;
    public String SendSMSMessage;
    public String ActivityName;
    public String ApprovalName;
    public int SendSMSNodeSize = 0;
    public int SendNotificationNodeSize = 0;
    public int ConditionSize = 0;
    public String entityName;


    public void login(String username,String password){
        email_field.sendKeys(username);
        password_field.sendKeys(password);
        SignIn_button.click();
        commonUtils.sleep(10000);
    }

    public void AfterLoginToCRMUserWillBeOnTheWorkflowScreenUnderAdminMenu(){
        Admin_Menu.click();
        commonUtils.sleep(1000);
        workflow_subMenu.click();
        webDriverUtils.waitUntilVisible(getDriver(), addNew_button, Duration.ofSeconds(30));
    }

    public void UserSelectAddNewButtonUnderWorkflowScreen(){
        commonUtils.sleep(4000);
        addNew_button.click();
       getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void EnterNameAndDescriptionUnderAddNewWorkflowScreen(String workflow_name, String workflow_description){
        workflowName = workflow_name +" "+commonUtils.generateRandomString(3);
        name_field.sendKeys(workflowName);
        description_field.sendKeys(workflow_description);
    }

    public void SelectEntityAsCustomerUnderAddNewWorkflowScreen(String Entity){
        entity_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Entity+"')]")).click();
        commonUtils.sleep(2000);
    }

    public void SelectTriggerAsCustomerIsCreatedUnderAddNewWorkflowScreen(String trigger){
        trigger_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+trigger+"')]")).click();
        commonUtils.sleep(1000);
    }

    public void SelectSaveButtonUnderAddNewWorkflowScreen(){
        save_button.click();
        commonUtils.sleep(4000);
    }

    public void NewWorkflowWillBeCreated(){
        List<WebElement> workFlowHeading = getDriver().findElements(By.xpath("//h5[contains(text(),'"+workflowName+"')]"));
        Assert.assertTrue(workFlowHeading.size()>0);
    }

    public void ActiveTheWorkflow(){
        commonUtils.sleep(2000);
        workflowActive_Button.click();
    }

    public void GoToTheWorkflowScreen(){
        Admin_Menu.click();
        commonUtils.sleep(1000);
        workflow_subMenu.click();
        webDriverUtils.waitUntilVisible(getDriver(), addNew_button, Duration.ofSeconds(30));
    }

    public void NewWorkflowWillShowInTheWorkflowListUnderWorkflowScreen(){
        search_field.sendKeys(workflowName);
        commonUtils.sleep(4000);
        List<WebElement> workFlowHeading = getDriver().findElements(By.xpath("//div[contains(text(),'"+workflowName+"')]"));
        Assert.assertTrue(workFlowHeading.size()>0);
    }

    public void UserSelectOptionsFromDropdownList(String repeat){
        repeat_dropdown.click();
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+repeat+"')]")).click();
    }

    public void SelectStartDateUnderAddNewWorkflowScreen(String date){
        String[] monthArr = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        openCalendar_button.click();
        commonUtils.sleep(1000);
        String [] dateArr = date.split("/");
        int expectedMonth = Integer.parseInt(dateArr[0]);
        String currentDay = dateArr[1];
        String actualMonth = calenderMonth_text.getText();

        if(!actualMonth.contains(monthArr[expectedMonth-1])){
            for(int i=0; i<=monthArr.length; i++){
                calendarNextMonth_button.click();
                String givenMonth = calenderMonth_text.getText();
                if(givenMonth.contains(monthArr[expectedMonth-1])){
                    break;
                }
            }
        }
        getDriver().findElement(By.xpath("//mat-month-view//div[contains(text(), '" +currentDay+ "')]")).click();
    }

    public void UserWillSelectWorkflowActionForAnyItemFromTheWorkflowList(String workflowName){
        commonUtils.sleep(1000);
        search_field.sendKeys(workflowName);
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//div[contains(text(),'"+workflowName+"')]/parent::div//img[@title='Workflow']")).click();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void SelectAddAnActionOrConditionButton(String actionNodeConditionType, String ActionNodeName, int ActionNodePosition){
        commonUtils.sleep(3000);

        String[] entityNameArr = getDriver().findElement(By.xpath("//div[text()=\"Trigger \"]/following-sibling::div/span")).getText().split(" ");
        entityName = entityNameArr[0];

        List<WebElement> sendSMSNodes = getDriver().findElements(By.xpath("//div[text()=\"Send SMS \"]"));
        SendSMSNodeSize = sendSMSNodes.size();

        List<WebElement> sendNotificationNodes = getDriver().findElements(By.xpath("//div[text()=\"Send App Notifications \"]"));
        SendNotificationNodeSize = sendNotificationNodes.size();

        List<WebElement> conditionNodes = getDriver().findElements(By.xpath("//div[text()=\"Condition \"]"));
        ConditionSize = conditionNodes.size();


        switch (actionNodeConditionType) {

            case "InBetween":
                commonUtils.sleep(3000);
                getDriver().findElement(By.xpath("//descendant::div[contains(text(),'" +ActionNodeName+ "')][" +ActionNodePosition+ "]//parent::div/div[2]/i")).click();
                commonUtils.sleep(2000);
                break;

            case "YES":
                webDriverUtils.actionsToMoveToElement(getDriver(), addAnActionOrCondition_ifYES_Button);
                addAnActionOrCondition_ifYES_Button.click();
                break;

            case "NO":
                webDriverUtils.actionsToMoveToElement(getDriver(), addAnActionOrCondition_ifNO_Button);
                addAnActionOrCondition_ifNO_Button.click();
                break;

            default:
                webDriverUtils.actionsToMoveToElement(getDriver(), addAnActionOrCondition_Button);
                addAnActionOrCondition_Button.click();
                break;
        }
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void AfterSelectSendEmailActionUserSelectNextButtonUnderSentEmailSection(){
        commonUtils.sleep(8000);
        sentEmail_Action.click();
        next_Button.click();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void UserSelectToReceiverFromToDropdown(String to_receiver, String customEmailIds){
        String[] toReceiver_Arr = to_receiver.split(",");
        int toReceiver_Arr_size = toReceiver_Arr.length;
        to_Dropdown.click();
        commonUtils.sleep(2000);
        for(int i=0; i<=(toReceiver_Arr_size-1); i++){
            getDriver().findElement(By.xpath("//span[contains(text(),'"+toReceiver_Arr[i]+"')]/parent::mat-option")).click();
            commonUtils.sleep(1000);
        }
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option["+toReceiver_Arr_size+"]")).sendKeys(Keys.ESCAPE);
        commonUtils.sleep(2000);

        if(toReceiver_Arr_size>1 || Objects.equals(toReceiver_Arr[0], "Selected User")){
            users_Dropdown.click();
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
            getDriver().findElement(By.xpath("//mat-option[2]")).sendKeys(Keys.ESCAPE);
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }

        if (toReceiver_Arr_size>1 || Objects.equals(toReceiver_Arr[0], "Custom Email Id")) {
            emailIds_field.sendKeys(customEmailIds);
        }
    }

    public void UserSelectFromCCReceiverCCDropdown(String cc_receiver, String customEmailIds){
        String[] ccReceiver_Arr = cc_receiver.split(",");
        int ccReceiver_Arr_size = ccReceiver_Arr.length;
        CC_Dropdown.click();
        commonUtils.sleep(2000);
        for(int i=0; i<=(ccReceiver_Arr_size-1); i++){
            getDriver().findElement(By.xpath("//span[contains(text(),'"+ccReceiver_Arr[i]+"')]/parent::mat-option")).click();
            commonUtils.sleep(1000);
        }
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option["+ccReceiver_Arr_size+"]")).sendKeys(Keys.ESCAPE);
        commonUtils.sleep(2000);

        if(ccReceiver_Arr_size>1 || Objects.equals(ccReceiver_Arr[0], "Selected User")){
            users_Dropdown_afterCC.click();
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
            getDriver().findElement(By.xpath("//mat-option[2]")).sendKeys(Keys.ESCAPE);
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }

        if (ccReceiver_Arr_size>1 || Objects.equals(ccReceiver_Arr[0], "Custom Email Id")) {
            emailIds_field_afterCC.sendKeys(customEmailIds);
        }
    }

    public void EnterSubjectUnderSubjectField(String subject){
        emailSentSubject = subject;
        subject_field.sendKeys(emailSentSubject);
    }

    public void UserEnterEmailContentForCustomer(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Name\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below customer details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Customer Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Name\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Customer Type : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Type\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Address : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Address\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Country/State/City : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Country/State/City\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Phone : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Phone\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Gender : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Gender\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Time Zone : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Time zone\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Date\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Customer Email : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Email\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("Document : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Ducument\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("Video : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Video\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Photo : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Photo\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForContact(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Name\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Contact details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Contact Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Name\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Contact Type : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Contact Type\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Address : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Address\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Country/State/City : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Country/State/City\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Mobile : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Mobile\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Assign Customer : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Assign Customer\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Data List Groups : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Data List Groups\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Created Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Created Date\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Email : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Email\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("Document : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Company Registration Certificate Test\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("Video : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Company Location Video Test\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Data List Module : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Data List Module\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForOpportunity(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Name\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Opportunity details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Opportunity Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Opportunity Name\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Customer Type : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Type\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Address : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Address\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Country/State/City : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Country/State/City\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Phone : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Phone\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Win Probability : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Win probability\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Value : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Value\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Expected Closure Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Expected Closure Date\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Customer Email : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Customer Email\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("Document : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Document\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("Video : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Video\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Gender : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Gender\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForOffsiteActivity(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Offsite Activity details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Customer Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Select Opportunity : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Opportunity\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Title : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Title\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Update Opportunity Value : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Value\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Update Opportunity Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Status\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Update Exp Closure Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Exp Closure Date\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Update Win Probability : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Win Probability\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Contacts : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Contacts\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Image : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Image\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("State/Country ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"State/country\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("testActivity : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"testActivity\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Created Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Created Date\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForOnsiteActivity(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Onsite Activity details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Customer Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Select Opportunity : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Opportunity\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Title : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Title\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Update Opportunity Value : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Value\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Update Opportunity Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Status\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Update Exp Closure Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Exp Closure Date\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Update Win Probability : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Win Probability\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Contacts : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Contacts\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Image : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Image\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("State/Country ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"State/country\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("testActivity : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"testActivity\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Created Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Created Date\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForEstimateActivity(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Estimate Activity details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Customer Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Select Opportunity : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Opportunity\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Title : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Title\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Update Opportunity Value : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Value\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Update Opportunity Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Status\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Update Exp. Closure Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Exp. Closure Date\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Update Win Probability : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Win Probability\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Contacts : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Contacts\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Image : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Image\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("State/Country ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"State/country\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("testActivity : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"testActivity\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Created Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Created Date\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForOrderActivity(){
        commonUtils.sleep(1000);
        first_line.sendKeys("Dear ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        first_line.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        commonUtils.sleep(1000);
        boldStyle_button.click();
        first_line.click();


        first_line.sendKeys(Keys.ENTER);
        second_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        boldStyle_button_Active.click();
        italicStyle_button.click();
        third_line.sendKeys("You are successfully registered to Assistive.");
        italicStyle_button_Active.click();
        commonUtils.sleep(1000);


        third_line.sendKeys(Keys.ENTER);
        fourth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        underlineStyle_button.click();
        fifth_line.sendKeys("Find the below Order Activity details ");
        actions.doubleClick(fifth_line).build().perform();
        underlineStyle_button_Active.click();
        commonUtils.sleep(1000);


        fifth_line.sendKeys(Keys.ENTER);

        commonUtils.sleep(1000);
        sixth_line.sendKeys("Personal Details : " + Keys.ENTER);

        orderedListStyle_button.click();

        seventh_line.sendKeys("Customer Name : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Customer\"]")).click();
        orderedListStyle_button.click();
        seventh_line.sendKeys(Keys.ENTER);

        eighth_line.sendKeys("Select Opportunity : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Select Opportunity\"]")).click();
        orderedListStyle_button.click();
        eighth_line.sendKeys(Keys.ENTER);

        ninth_line.sendKeys("Title : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Title\"]")).click();
        orderedListStyle_button.click();
        ninth_line.sendKeys(Keys.ENTER);

        tenth_line.sendKeys("Update Opportunity Value : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Value\"]")).click();
        orderedListStyle_button.click();
        tenth_line.sendKeys(Keys.ENTER);

        eleventh_line.sendKeys("Update Opportunity Status : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Opportunity Status\"]")).click();
        orderedListStyle_button.click();
        eleventh_line.sendKeys(Keys.ENTER);

        twelfth_line.sendKeys("Update Exp. Closure Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Exp. Closure Date\"]")).click();
        orderedListStyle_button.click();
        twelfth_line.sendKeys(Keys.ENTER);

        orderedListStyle_button.click();
        commonUtils.sleep(2000);
        thirteenth_line.sendKeys("Other Details" + Keys.ENTER);

        bulletListStyle_button.click();

        fourteenth_line.sendKeys("Update Win Probability : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Update Win Probability\"]")).click();
        bulletListStyle_button.click();
        fourteenth_line.sendKeys(Keys.ENTER);

        fifteenth_line.sendKeys("Contacts : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Contacts\"]")).click();
        bulletListStyle_button.click();
        fifteenth_line.sendKeys(Keys.ENTER);

        sixteenth_line.sendKeys("Image : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Image\"]")).click();
        bulletListStyle_button.click();
        sixteenth_line.sendKeys(Keys.ENTER);

        seventeenth_line.sendKeys("State/Country ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"State/country\"]")).click();
        bulletListStyle_button.click();
        seventeenth_line.sendKeys(Keys.ENTER);

        eighteenth_line.sendKeys("testActivity : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"testActivity\"]")).click();
        bulletListStyle_button.click();
        eighteenth_line.sendKeys(Keys.ENTER);

        nineteenth_line.sendKeys("Created Date : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[text()=\"Created Date\"]")).click();
        bulletListStyle_button.click();
        nineteenth_line.sendKeys(Keys.ENTER);

        twentieth_line.sendKeys("Owner : ");
        placeholder_dropdown.click();
        commonUtils.sleep(2000);
        getDriver().findElement(By.xpath("//span[@class=\"ql-picker-options\"]/span[text()=\"Owner\"]")).click();
        bulletListStyle_button.click();
        twentieth_line.sendKeys(Keys.ENTER);

        twenty_first_line.sendKeys("Status : ");
//        placeholder_dropdown.click();
//        commonUtils.sleep(2000);
//        getDriver().findElement(By.xpath("//span[text()=\"Status\"]")).click();
        bulletListStyle_button.click();
        twenty_first_line.sendKeys(Keys.ENTER);

        bulletListStyle_button.click();
        commonUtils.sleep(1000);
        lineMarginStyle_dropdown.click();
        margin_option_huge.click();
        commonUtils.sleep(1000);
        textSize_dropdown.click();
        heading5_option.click();
        commonUtils.sleep(1000);
        textColor_dropdown.click();
        textColor_white.click();
        commonUtils.sleep(1000);
        backgroundColor_dropdown.click();
        backgroundColor_blue.click();
        commonUtils.sleep(1000);
        alignment_dropdown.click();
        alignment_center.click();
        twenty_second_line.sendKeys("Channel Play" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_third_line.sendKeys("TECHNOLOGY SOLUTIONS" + Keys.ENTER);

        textStyle_dropdown.click();
        textStyle_monospace.click();
        alignment_dropdown.click();
        alignment_center.click();
        twenty_fourth_line.sendKeys("CONSULTING & EXECUTION SERVICES" + Keys.ENTER);

        twenty_fifth_line.sendKeys("Thanks" + Keys.ENTER);

        twenty_sixth_line.sendKeys("Assistive");
        actions.doubleClick(twenty_sixth_line).build().perform();
        link_button.click();
        link_field.sendKeys(Keys.BACK_SPACE + "https://qa.assistive.site/");
        link_save_button.click();
        commonUtils.sleep(10000);
    }

    public void UserEnterEmailContentForDateTimeWorkflow() {
        commonUtils.sleep(2000);
        //first_line.click();
        first_line.sendKeys("DateTime workflow email body");
    }

    public void SelectSaveButtonUnderSentEmailSection(){
        save_button.click();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        List<WebElement> InsertNewAction = getDriver().findElements(By.xpath("//div/h4[text()=\"Insert New Action\"]"));
        System.out.println("check: " +InsertNewAction+"-------------------------");
        if (InsertNewAction.size()>0) {
            String InsertNewAction_moveAllActionRadioButton = ("//h4[text()=\"Insert New Action\"]/ancestor::div[2]/descendant::div[@class=\"mat-radio-container\"][2]");
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath(InsertNewAction_moveAllActionRadioButton)).click();
            commonUtils.sleep(1000);
            InsertNewAction_SaveButton.click();
        }
        commonUtils.sleep(2000);
    }

    public void SentEmailNodeWillShowUnderWorkflowSection(){
        commonUtils.sleep(4000);
        List<WebElement> sentEmailNode = getDriver().findElements(By.xpath("//span[contains(text(),'"+emailSentSubject+"')]"));
        Assert.assertTrue(sentEmailNode.size()>0);
    }

    public void AfterSelectSendSMSActionUserSelectNextButtonUnderSendSMSSection(){
        commonUtils.sleep(8000);
        sendSMS_action.click();
        next_Button.click();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void SelectToReceiverFromTheToDropdown(String to_receiver, String customPhoneNo){
        String[] toReceiver_Arr = to_receiver.split(",");
        int toReceiver_Arr_size = toReceiver_Arr.length;
        to_Dropdown.click();
        commonUtils.sleep(2000);
        for(int i=0; i<=(toReceiver_Arr_size-1); i++){
            getDriver().findElement(By.xpath("//span[contains(text(),'"+toReceiver_Arr[i]+"')]/parent::mat-option")).click();
            commonUtils.sleep(1000);
        }
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option["+toReceiver_Arr_size+"]")).sendKeys(Keys.ESCAPE);
        commonUtils.sleep(2000);

        if(toReceiver_Arr_size>1 || Objects.equals(toReceiver_Arr[0], "Selected User")){
            users_Dropdown.click();
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
            getDriver().findElement(By.xpath("//mat-option[2]")).sendKeys(Keys.ESCAPE);
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }

        if (toReceiver_Arr_size>1 || Objects.equals(toReceiver_Arr[0], "Custom Phone Number")) {
            phoneNumbers_field.sendKeys(customPhoneNo);
        }
    }

    public void EnterMessageUnderMessageField(String message){
        SendSMSMessage = message;
        message_field.sendKeys(message);
    }

    public void SendSMSNodeWillShowUnderWorkflowSection(){
        commonUtils.sleep(2000);
        List<WebElement> sendSMSNodes = getDriver().findElements(By.xpath("//div[text()=\"Send SMS \"]"));
        int TotalNodes = sendSMSNodes.size();
        Assert.assertTrue(TotalNodes>SendSMSNodeSize,TotalNodes + "---->" + SendSMSNodeSize);
    }

    public void AfterSelectSendAppNotificationActionUserSelectNextButtonUnderSendAppNotificationSection(){
        commonUtils.sleep(8000);
        sendAppNotifications_action.click();
        next_Button.click();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(4));
    }

    public void SelectToReceiverFromTheToDropdownUnderSendAppNotificationsScreen(String to_receiver){
        String[] toReceiver_Arr = to_receiver.split(",");
        int toReceiver_Arr_size = toReceiver_Arr.length;
        to_Dropdown.click();
        commonUtils.sleep(2000);
        for(int i=0; i<=(toReceiver_Arr_size-1); i++){
            getDriver().findElement(By.xpath("//span[contains(text(),'"+toReceiver_Arr[i]+"')]/parent::mat-option")).click();
            commonUtils.sleep(1000);
        }
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option["+toReceiver_Arr_size+"]")).sendKeys(Keys.ESCAPE);
        commonUtils.sleep(2000);

        if(toReceiver_Arr_size>1 || Objects.equals(toReceiver_Arr[0], "Selected User")){
            users_Dropdown.click();
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
            getDriver().findElement(By.xpath("//mat-option[2]")).sendKeys(Keys.ESCAPE);
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
    }

    public void EnterTitleInTheTitleField(String title){
        title_field.sendKeys(title);
    }

    public void EnterMessageUnderMessageFieldUnderSendAppNotificationsScreen(String message){
        message_field.sendKeys(message);
        notificationAction_Button.click();
    }

    public void SendAppNotificationNodeWillShowUnderWorkflowSection(){
        commonUtils.sleep(2000);
        List<WebElement> sendNotificationNodes = getDriver().findElements(By.xpath("//div[text()=\"Send App Notifications \"]"));
        int TotalNodes = sendNotificationNodes.size();
        Assert.assertTrue(TotalNodes>SendNotificationNodeSize, TotalNodes + "----->" + SendNotificationNodeSize);
    }

    public void AfterSelectCreateActivityActionUserSelectNextButtonUnderCreateActivitySection(){
        commonUtils.sleep(8000);
        CreateActivity_action.click();
        next_Button.click();
        commonUtils.sleep(2000);
    }

    public void SelectActivityNameFromTheActivityNameDropdown(String activityName){
        commonUtils.sleep(2000);
        ActivityName = activityName;
        activityName_dropdown.click();
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+activityName+"')]")).click();
    }

    public void FillAllTheFieldsWithProperDataUnderCreateActivityScreen(){
        comment_field.sendKeys("Add offsite activity for workflow");
        value_Field.sendKeys("2");
        duration_dropdown.click();
        commonUtils.sleep(1000);
        days_option.click();
    }

    public void CreateActivityNodeWillShowUnderWorkflowSection(){
        List<WebElement> activityActionNode = getDriver().findElements(By.xpath("//span[contains(text(),'"+ActivityName+"')]"));
        Assert.assertTrue(activityActionNode.size()>0);
    }

    public void AfterSelectDelayTimerActionUserSelectNextButtonUnderDelayTimerSection(){
        commonUtils.sleep(8000);
        DelayTimer_action.click();
        next_Button.click();
        commonUtils.sleep(2000);
    }

    public void EnterDurationValueAndSelectDurationMeasureFromTheDurationDropdown(String DurationValue, String DurationMeasure){
        commonUtils.sleep(2000);
        dueDate_Value_field.sendKeys(DurationValue);
        Duration_dropdown.click();
        Duration_dropdown.isSelected();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+DurationMeasure+"')]")).click();
        commonUtils.sleep(2000);
    }

    public void DelayTimerNodeWillShowUnderWorkflowSectionRespectToDurationValueAndDurationMeasure(String DurationValue, String DurationMeasure){
        List<WebElement> delayTimerNodes = getDriver().findElements(By.xpath("//div/span[contains(text(),'Wait for "+DurationValue+" "+DurationMeasure+"')]"));
        commonUtils.sleep(5000);
        Assert.assertTrue(delayTimerNodes.size()>0);
    }

    public void AfterSelectNewConditionUserSelectNextButton(){
        commonUtils.sleep(8000);
        newCondition_action.click();
        next_Button.click();
        commonUtils.sleep(2000);
    }

    public void SelectEntityFromTheSelectEntityDropdown(String Entity){
        commonUtils.sleep(2000);
        selectEntity_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Entity+"')]")).click();
    }

    public void SelectEntityFieldFromTheSelectEntityFieldDropdown(String EntityField){
        commonUtils.sleep(2000);
        selectEntityField_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+EntityField+"')]")).click();
    }

    public void SelectOperatorFromTheSelectOperatorDropdown(String Operator){
        commonUtils.sleep(2000);
        selectOperator_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Operator+"')]")).click();
    }

    public void EnterValueInTheSelectValueField(String Value){
        commonUtils.sleep(2000);
        selectValue_field.sendKeys(Value);
    }

    public void SelectAddMoreIfRequiredAdditionalConditionToBeAddedWithConditionType(String Condition_Type){
        addMore_button.click();
        commonUtils.sleep(3000);
        getDriver().findElement(By.xpath("//div[contains(text(),'"+Condition_Type+"')]")).click();
    }

    public void SelectEntityEntityFieldOperatorValueForAddMore(String Entity, String EntityField, String Operator, String Value){
        commonUtils.sleep(2000);
        addMore_selectEntity_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Entity+"')]")).click();

        commonUtils.sleep(2000);
        addMore_selectEntityField_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+EntityField+"')]")).click();

        commonUtils.sleep(2000);
        addMore_selectOperator_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Operator+"')]")).click();

        commonUtils.sleep(2000);
        addMore_selectValue_field.sendKeys(Value);
    }

    public void SelectAddGroupIfRequiredAdditionalConditionToBeAddedWithConditionType(String Condition_Type){
        addGroup_button.click();
        commonUtils.sleep(3000);
        getDriver().findElement(By.xpath("//span[text()=\"Remove Group\"]/ancestor::div[3]//div[contains(text(),'"+Condition_Type+"')]")).click();
    }

    public void selectEntityEntityFieldOperatorValueForAddGroup(String Entity, String EntityField, String Operator, String Value){
        commonUtils.sleep(2000);
        addGroup_selectEntity_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Entity+"')]")).click();

        commonUtils.sleep(6000);
        addGroup_selectEntityField_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+EntityField+"')]")).click();

        commonUtils.sleep(2000);
        addGroup_selectOperator_dropdown.click();
        getDriver().findElement(By.xpath("//mat-option/span[contains(text(),'"+Operator+"')]")).click();

        commonUtils.sleep(2000);
        addGroup_selectValue_field.sendKeys(Value);
    }

    public void NewConditionWillShowUnderWorkflowDashboard(){
        commonUtils.sleep(2000);
        List<WebElement> conditionNodes = getDriver().findElements(By.xpath("//div[text()=\"Condition \"]"));
        int TotalNodes = conditionNodes.size();
        Assert.assertTrue(TotalNodes>ConditionSize, TotalNodes + "----->" + ConditionSize);
    }

    public void AfterSelectApprovalUserSelectNextButton(){
        commonUtils.sleep(8000);
        Approval_Action.click();
        next_Button.click();
        commonUtils.sleep(2000);
    }

    public void UserEnterNameInTheNameField(String Name){
        ApprovalName = Name;
        ApprovalName_field.sendKeys(Name);
    }

    public void UserSelectAssignToFromAssignToDropdownAndSelectRolesIfNeeded(String AssignTo, String Roles){
        String[] assignTo_Arr = AssignTo.split(",");
        int assignTo_Arr_size = assignTo_Arr.length;
        to_Dropdown.click();
        commonUtils.sleep(2000);
        for(int i=0; i<=(assignTo_Arr_size-1); i++){
            getDriver().findElement(By.xpath("//span[contains(text(),'"+assignTo_Arr[i]+"')]/parent::mat-option")).click();
            commonUtils.sleep(1000);
        }
        commonUtils.sleep(1000);
        getDriver().findElement(By.xpath("//mat-option["+assignTo_Arr_size+"]")).sendKeys(Keys.ESCAPE);
        commonUtils.sleep(2000);

        String[] roles_Arr = Roles.split(",");
        int roles_Arr_size = roles_Arr.length;
        if(assignTo_Arr_size>1 || Objects.equals(assignTo_Arr[0], "Selected User Roles")){
            Roles_dropdown.click();
            commonUtils.sleep(2000);
            for(int i=0; i<=(roles_Arr_size-1); i++){
                getDriver().findElement(By.xpath("//span[contains(text(),'"+roles_Arr[i]+"')]/parent::mat-option")).click();
                commonUtils.sleep(1000);
            }
            commonUtils.sleep(1000);
            getDriver().findElement(By.xpath("//mat-option["+roles_Arr_size+"]")).sendKeys(Keys.ESCAPE);
            commonUtils.sleep(2000);
        }

        if(assignTo_Arr_size>1 || Objects.equals(assignTo_Arr[0], "Selected Users")){
            users_Dropdown.click();
            commonUtils.sleep(2000);
            getDriver().findElement(By.xpath("//mat-option[2]")).click();
            getDriver().findElement(By.xpath("//mat-option[2]")).sendKeys(Keys.ESCAPE);
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        }
    }

    public void SelectEnableEditingCheckboxIfNeeded(){
        EnableEditing_checkbox.click();
    }

    public void ApprovalNodeWillShowUnderWorkflowDashboard(){
        commonUtils.sleep(4000);
        List<WebElement> approvalNode = getDriver().findElements(By.xpath("//div[text()=\"Approval \"]/following-sibling::div/span[contains(text(),'"+ApprovalName+"')]"));
        Assert.assertTrue(approvalNode.size()>0);
    }

}
