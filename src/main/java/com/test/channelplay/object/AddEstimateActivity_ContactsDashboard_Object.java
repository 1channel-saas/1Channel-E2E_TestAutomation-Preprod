package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class AddEstimateActivity_ContactsDashboard_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" CRM \"]")
    WebElement CRM_menu;

    @FindBy(xpath = "//div/ul/li/a[@href=\"/contacts\"]")
    WebElement Contacts_Submenu;

    @FindBy(xpath = "//div/h5[text()=\" Contacts \"]")
    WebElement contactsPage_headerText;

    @FindBy(xpath = "//div//a/img[@src=\"./assets/media/svg/icons/General/dashboard.svg\" and @title=\"Dashboard\"]")
    WebElement Action_Dashboard_button;

    @FindBy(xpath = "//div[text()=\" Activities \"]")
    WebElement Dashboard_Activities_textHeader;

    @FindBy(xpath = "//div[@title=\"Add Activity\"]/a")
    WebElement activity_Plus_button;

    @FindBy(xpath = "//div/h4[text()=\"Add New Estimate\"]")
    WebElement addNewEstimate_textHeader;

    @FindBy(xpath = "//div/label[text()=\"Select Customer\"]/parent::div/following-sibling::div//child::div//child::mat-select/div/div[1]")
    WebElement selectCustomer_dropdown;

    @FindBy(xpath = "//mat-option[2]")
    WebElement selectCustomer_dropdown_FirstValue;

    @FindBy(xpath = "//div/label[text()=\"Select Opportunity\"]/parent::div/following-sibling::div//child::mat-select/div/div[1]")
    WebElement selectOpportunity_dropdown;

    @FindBy(xpath = "//div/mat-option[1]")
    WebElement selectOpportunity_dropdown_SearchValue;

    @FindBy(xpath = "//div/mat-option[2]")
    WebElement selectOpportunity_dropdown_FirstValue;

    @FindBy(xpath = "//div/label[text()=\"Update Opportunity Value\"]/parent::div/following-sibling::div//child::input")
    WebElement updateOpportunityValue_field;

    @FindBy(xpath = "//div/label[text()=\"Update Opportunity Status\"]/parent::div/following-sibling::div//child::mat-select/div/div")
    WebElement updateOpportunityStatus_dropdown;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]//child::mat-option[2]/span[text()=\" In Progress \"]")
    WebElement updateOpportunityStatus_value_inProgress;

    @FindBy(xpath = "//button[@aria-label=\"Open calendar\"]")
    WebElement updateExpClosureDate_Calender_button;

    @FindBy(xpath = "//div[@class=\"mat-calendar-body-cell-content mat-calendar-body-today\"]")
    private WebElement updateExpClosureDate_Calender_CurrentDate;

    @FindBy(xpath = "//div/label[text()=\"Update Win Probability\"]/parent::div/following-sibling::div//child::mat-select/div/div")
    WebElement updateWinProbability_dropdown;

    @FindBy(xpath = "//div/label[text()=\"Contacts\"]/parent::div/following-sibling::div//child::mat-select//child::span")
    WebElement Contacts_dropdown;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]//child::mat-option[2]")
    WebElement Contacts_dropdown_FirstValue;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]//child::mat-option[2]")
    WebElement Contacts_dropdown_FirstValue_checkbox;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]/div//child::mat-option[1]")
    WebElement Contacts_dropdown_SearchValue;

    @FindBy(xpath = "//table/tr/th[text()=\"Product\"]/parent::tr/following-sibling::tr//child::mat-select//child::span")
    WebElement ProductForm_ProductDropdown;

    @FindBy(xpath = "//table/tr/th[text()=\"Quantity\"]/parent::tr/following-sibling::tr[1]/td[2]/mat-form-field//child::input")
    WebElement Quantity_field;

    @FindBy(xpath = "//table/tr/th[text()=\"Tax %\"]/parent::tr/following-sibling::tr[1]/td[4]/mat-form-field//child::input")
    WebElement Tax_field;

    @FindBy(xpath = "//table/tr/th[text()=\"Total\"]/parent::tr/following-sibling::tr[1]/td[5]/mat-form-field//child::input")
    WebElement Total_field;

    @FindBy(xpath = "//div/label[text()=\"Total\"]/parent::div/parent::td/following-sibling::td/input")
    WebElement TotalCalculatedValue;

    @FindBy(xpath = "//div/button[text()=\"Save\"]")
    WebElement Save_button;

    @FindBy(xpath = "//span[text()=\" Activities \"]")
    WebElement Activities_submenu;




    CommonUtils commonUtils = new CommonUtils();
    public String DashboardTimelineValue;

    public AddEstimateActivity_ContactsDashboard_Object() {
        PageFactory.initElements(getDriver(), this);
    }


    public void User_clicks_on_menu_CRM_and_submenu_Contacts() {
        CRM_menu.click();
        commonUtils.sleep(2000);
        Contacts_Submenu.click();
        commonUtils.sleep(5000);
    }

    public void User_is_on_Contacts_page() {
        boolean contacts_textHeaderisDisplayed = contactsPage_headerText.isDisplayed();
        Assert.assertTrue(contacts_textHeaderisDisplayed);
    }

    public void Click_on_actions_dashboard_button_of_any_Contact() {
        Action_Dashboard_button.click();
        commonUtils.sleep(10000);
        boolean dashboard_textHeaderisDispplayed = Dashboard_Activities_textHeader.isDisplayed();
        Assert.assertTrue(dashboard_textHeaderisDispplayed);
    }

    public void Click_on_plus_button_under_Contacts_Estimate() {
        activity_Plus_button.click();
        commonUtils.sleep(2000);
    }

    public void Click_on_estimate_and_landed_on_add_new_estimate_page_under_Contacts_Estimate() {
        String Activities_lisrArea = ("//div/h4[text()=\"Add Activity\"]/parent::div/following-sibling::div/div/ul/li['\" +i+ \"']/a/span");
        List<WebElement> activity_list = getDriver().findElements(By.xpath(Activities_lisrArea));

        for (WebElement activity_select : activity_list) {
            String activityText = activity_select.getText();

            if (activityText.contentEquals("Estimate")) {
                activity_select.click();
                commonUtils.sleep(10000);
            }
        }
        boolean addNewEstimateTextHeader = addNewEstimate_textHeader.isDisplayed();
        Assert.assertTrue(addNewEstimateTextHeader);
    }

    public void Select_Customer_from_dropdown_under_Contacts_Estimate() {
        selectCustomer_dropdown.click();
        selectCustomer_dropdown_FirstValue.click();
        commonUtils.sleep(5000);
    }

    public void Select_opportunity_from_dropdown_under_Contacts_Estimate() {
        selectOpportunity_dropdown.click();
        String selectOpportunity_listArea = ("//div/mat-option/parent::div/mat-option");
        List<WebElement> selectOpportunity_values = getDriver().findElements(By.xpath(selectOpportunity_listArea));

        if (selectOpportunity_values.size()>1) {
            selectOpportunity_dropdown_FirstValue.click();
        }
        else {
            selectOpportunity_dropdown_SearchValue.sendKeys(Keys.ESCAPE);
        }
        commonUtils.sleep(4000);
    }

    public void Enter_update_opportunity_value_under_Contacts_estimate() {
        updateOpportunityValue_field.clear();
        updateOpportunityValue_field.sendKeys("500");
        commonUtils.sleep(2000);
    }

    public void Select_update_opportunity_status_under_Contacts_estimate() {
        updateOpportunityStatus_dropdown.click();
        updateOpportunityStatus_value_inProgress.click();
        commonUtils.sleep(2000);
    }

    public void Select_update_exp_closure_date_from_calendar_under_Contacts_estimate(String date) {
        updateExpClosureDate_Calender_button.click();
        commonUtils.sleep(2000);
        WebElement expClosureDatePick = getDriver().findElement(By.xpath("//table[@class=\"mat-calendar-table\"]/tbody/tr/td/div[contains(text(), '" +date+ "')]"));
        expClosureDatePick.click();
        commonUtils.sleep(2000);
    }

    public void Select_update_win_probability_from_dropdown_under_Contacts_estimate() {
        updateWinProbability_dropdown.click();
        commonUtils.sleep(1000);

        String Win_probability_listArea = ("//div[@class=\"cdk-overlay-pane\"]//child::mat-option");
        List<WebElement> Win_probability_list = getDriver().findElements(By.xpath(Win_probability_listArea));

        for (WebElement Win_probability_select : Win_probability_list) {
            String Win_probability_Text = Win_probability_select.getText();

            if (Win_probability_Text.contentEquals("80")) {
                Win_probability_select.click();
                break;
            }
        }
        commonUtils.sleep(2000);
    }

    public void Select_contact_under_Contacts_estimate() {
        Contacts_dropdown.click();
        commonUtils.sleep(3000);
        System.out.println("checkbox state: " +Contacts_dropdown_FirstValue_checkbox.getAttribute("aria-selected")+"-------------------->");

        List<WebElement> contact_list = getDriver().findElements(By.xpath("//div[@class=\"cdk-overlay-pane\"]/div//child::mat-option"));

        if (contact_list.size()>=1) {
                Contacts_dropdown_FirstValue.click();
                commonUtils.sleep(2000);
                Contacts_dropdown_FirstValue.click();
                Contacts_dropdown_SearchValue.sendKeys(Keys.ESCAPE);
        }
        else {
            Contacts_dropdown_SearchValue.sendKeys(Keys.ESCAPE);
        }
        commonUtils.sleep(2000);
    }

    public void Fill_data_in_product_form_table_under_Contacts_Estimate() {
        ProductForm_ProductDropdown.click();
        List<WebElement> ProductDropdown_values = getDriver().findElements(By.xpath("//div[@class=\"cdk-overlay-pane\"]//child::mat-option/span"));

        for (WebElement productDropdownValue : ProductDropdown_values) {
            String ProductDropdown_selectValue_text = productDropdownValue.getText();

            if (ProductDropdown_selectValue_text.equals("Samsung")) {
                productDropdownValue.click();
            }
        }
        Quantity_field.sendKeys("10");
        Tax_field.sendKeys("10");
        Total_field.click();
        commonUtils.sleep(2000);
        Save_button.click();
        commonUtils.sleep(6000);
    }

    public void Validate_new_Estimate_is_created_under_Contacts_Estimate() {
        //test Estimate calculated amount
        String EstimateTotal_onDashboard = getDriver().findElement(By.xpath("//div[@class=\"timeline-items\"]/div[1]//child::tr[2]//child::div[1]")).getText();
        Assert.assertEquals(EstimateTotal_onDashboard, "1100.00");
        System.out.println("Total Estimate calc:" +EstimateTotal_onDashboard+"-------------");

        //test created Estimate dashboard timeline date
        String dashResultEstimate = ("//div[@class=\"timeline-items\"]/div[1]//child::tr[1]/td[1]/div[2]");
        DashboardTimelineValue = getDriver().findElement(By.xpath(dashResultEstimate)).getText();
        System.out.println("server Date----------" +DashboardTimelineValue+"--------------");

        //validate dashboard timeline date with Assert
        String[] Dash_DateArr = DashboardTimelineValue.split("-");
        String Estimate_ActualDate = Dash_DateArr[0] +"-"+ Dash_DateArr[1] +"-"+ Dash_DateArr[2];
        System.out.println("actual date---------: " +Estimate_ActualDate+"----------");
        Assert.assertEquals(DashboardTimelineValue, Estimate_ActualDate);

        //validate dashboard timeline Estimate name
    /*    String Estimate_name = getDriver().findElement(By.xpath("//div[@class=\"timeline-items\"]/div[1]//child::tr[1]/td[1]/div[1]")).getText();
        System.out.println("server Estimate name---------: " +Estimate_name+"--------------");
        String[]EstNameArr = Estimate_name.split("");
        String EstActualName = EstNameArr[0] + EstNameArr[1] + EstNameArr[2] + EstNameArr[3];
        System.out.println("actual Estimate name--------:" +EstActualName+"------------");
        Assert.assertEquals(EstActualName, ": #E");
        commonUtils.sleep(2000);

     */
    }

    public void Validate_new_Estimate_in_Activities_page_under_Contacts_Estimate() {
        CRM_menu.click();
        commonUtils.sleep(1000);
        Activities_submenu.click();
        commonUtils.sleep(3000);
        String Activities_FirstRow = ("//div[@ref=\"eContainer\" and @role=\"rowgroup\"]/div[1]");
        String Activities_FirstRow_ActivityName = getDriver().findElement(By.xpath(Activities_FirstRow+"//child::div[1]")).getText();
        String Activities_FirstRow_Activity_DateTime = getDriver().findElement(By.xpath(Activities_FirstRow+"//child::div[5]")).getText();

        //validate Activity name should Estimate
        Assert.assertEquals(Activities_FirstRow_ActivityName, "Estimate");
        commonUtils.sleep(2000);

        //validate Estimate creation date same as dashboard timeline
        String[] monthArr = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] ActivitiesDateTimeArr = Activities_FirstRow_Activity_DateTime.split(" ");
        String ActivitiesDate = ActivitiesDateTimeArr[0];
        String[] ActivitiesDateArr = ActivitiesDate.split("-");
        String Actual_ActivitiesDate = ActivitiesDateArr[2] + "-" + monthArr[2] + "-" + ActivitiesDateArr[0];
        System.out.println("Activities Estimate date: " +Actual_ActivitiesDate+"-------------->");

        String[] estimateDashboard_dateTime = DashboardTimelineValue.split(" ");
        String estimateDashboard_date = estimateDashboard_dateTime[0];
        System.out.println("Activities Estimate dash date: " +estimateDashboard_date+"-------------------->");
        Assert.assertEquals(estimateDashboard_date, Actual_ActivitiesDate);
    }

}
