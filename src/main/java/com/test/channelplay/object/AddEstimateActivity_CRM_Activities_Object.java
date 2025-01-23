package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import io.cucumber.java.sl.In;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class AddEstimateActivity_CRM_Activities_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" CRM \"]")
    WebElement CRM_menu;

    @FindBy(xpath = "//div/ul/li/a[@href=\"/crm_activities\"]")
    WebElement Activities_submenu;

    @FindBy(xpath = "//h5[text()=\" Activities \"]/parent::div/parent::div/following-sibling::div/div[1]/input")
    WebElement calendar_input;

    @FindBy(xpath = "//div/h5[text()=\" Activities \"]")
    WebElement ActivitiesPage_headerText;

    @FindBy(xpath = "//span/span[@ref=\"lbTotal\"]")
    WebElement pagination_totalPages;

    @FindBy(xpath = "//span/div[@ref=\"btNext\"]/span")
    WebElement pagination_nextPage;

    @FindBy(xpath = "//span/span[@ref=\"lbRecordCount\"]")
    WebElement total_activityRows;

    @FindBy(xpath = "//span/span[@ref=\"lbLastRowOnPage\"]")
    WebElement total_activityRows_onPage;

    @FindBy(xpath = "//button/span[text()=\"Add Activity\"]")
    WebElement AddActivity_button;

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

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]//child::mat-option[2]//child::div")
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

    @FindBy(xpath = "//div/button[text()=\"Save\"]")
    WebElement Save_button;






    CommonUtils commonUtils = new CommonUtils();
    public int Total_no_of_activityRows_before;
    public int Total_no_of_activityRows_after;

    public AddEstimateActivity_CRM_Activities_Object() {
        PageFactory.initElements(getDriver(), this);
    }

    public void User_clicks_on_menu_crm_and_submenu_activities() {
        CRM_menu.click();
        commonUtils.sleep(2000);
        Activities_submenu.click();
        commonUtils.sleep(5000);
    }

    public void User_is_on_activities_page(){
        boolean activities_textHeaderisDisplayed = ActivitiesPage_headerText.isDisplayed();
        Assert.assertTrue(activities_textHeaderisDisplayed);
    }

    public void User_select_date_range_from_calendar_and_click_on_save_button_under_crm_activities(String startDate, String endDate) {
        calendar_input.clear();
        commonUtils.sleep(2000);
        calendar_input.sendKeys(startDate+" - "+endDate);
        commonUtils.sleep(2000);
        calendar_input.click();
        commonUtils.sleep(2000);
    }

    public void Check_total_number_of_records_from_the_activity_list_under_crm_activities() {
        int Total_activity_Rows_eachPage = 0;
        int count=0;
        int total_pages = Integer.parseInt(pagination_totalPages.getText());
        int totalActivityRows = Integer.parseInt(total_activityRows.getText());
        Total_no_of_activityRows_before = 0;

        while (count < total_pages) {
            List<WebElement> activityRows_list = getDriver().findElements(By.xpath("//div[@ref=\"eContainer\"]/div"));
            Total_activity_Rows_eachPage = activityRows_list.size();
            System.out.println("Total act rows EachPage: " + Total_activity_Rows_eachPage + "---------------->");
            Total_no_of_activityRows_before = Total_activity_Rows_eachPage + Total_no_of_activityRows_before;     //assign total row count in each page
            pagination_nextPage.click();
            count++;
        }
        System.out.println("Total No of Activity Rows before: " +Total_no_of_activityRows_before+"----------------->");
        Assert.assertEquals(Total_no_of_activityRows_before, totalActivityRows);       //validate total no. of rows found, match with actual count on webpage
        commonUtils.sleep(2000);
    }

    public void Click_on_add_activity_button_under_crm_activities() {
        AddActivity_button.click();
        commonUtils.sleep(2000);
    }

    public void Click_on_estimate_and_landed_on_Add_New_Estimate_page_under_crm_activities() {
        String CRM_Activities_lisrArea = ("//button/span[text()=\"Add Activity\"]/parent::button/parent::a/following-sibling::div//child::div/ul");
        List<WebElement> CRM_activity_list = getDriver().findElements(By.xpath(CRM_Activities_lisrArea));

        for (WebElement CRM_activity_select : CRM_activity_list) {
            String activityText = CRM_activity_select.getText();

            if (activityText.contentEquals("Estimate")) {
                CRM_activity_select.click();
                commonUtils.sleep(6000);
            }
        }
        boolean addNewEstimateTextHeader = addNewEstimate_textHeader.isDisplayed();
        Assert.assertTrue(addNewEstimateTextHeader);
    }

    public void Select_customer_from_dropdown_under_crm_activities() {
        selectCustomer_dropdown.click();
        selectCustomer_dropdown_FirstValue.click();
        commonUtils.sleep(5000);
    }

    public void Select_opportunity_from_dropdown_under_crm_activities() {
        selectOpportunity_dropdown.click();
        String selectOpportunity_listArea = ("//div/mat-option/parent::div/mat-option");
        List<WebElement> selectOpportunity_values = getDriver().findElements(By.xpath(selectOpportunity_listArea));

        if (selectOpportunity_values.size()>1) {
            selectOpportunity_dropdown_FirstValue.click();
        }
        else {
            selectOpportunity_dropdown_SearchValue.sendKeys(Keys.ESCAPE);
        }
        commonUtils.sleep(5000);
    }

    public void Enter_update_opportunity_value_under_crm_activities() {
        updateOpportunityValue_field.clear();
        updateOpportunityValue_field.sendKeys("1000");
        commonUtils.sleep(2000);
    }

    public void Select_update_opportunity_status_under_crm_activities() {
        updateOpportunityStatus_dropdown.click();
        updateOpportunityStatus_value_inProgress.click();
        commonUtils.sleep(2000);
    }

    public void Select_update_exp_closure_date_from_calendar_under_crm_activities(String date) {
        updateExpClosureDate_Calender_button.click();
        commonUtils.sleep(2000);
        WebElement expClosureDatePick = getDriver().findElement(By.xpath("//table[@class=\"mat-calendar-table\"]/tbody/tr/td/div[contains(text(), '" +date+ "')]"));
        expClosureDatePick.click();
        commonUtils.sleep(2000);
    }

    public void Select_update_win_probability_from_dropdown_under_crm_activities() {
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

    public void Select_contact_under_crm_activities() {
        Contacts_dropdown.click();
        commonUtils.sleep(3000);
        System.out.println("checkbox state: " +Contacts_dropdown_FirstValue_checkbox.isSelected()+"-------------------->");

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

    public void Fill_data_in_product_form_table_and_click_on_save_button_under_crm_activities() {
        ProductForm_ProductDropdown.click();
        commonUtils.sleep(1000);
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

    public void Validate_new_created_activity_in_list() {
        int Total_activity_Rows_eachPage = 0;
        int count=0;
        int total_pages = Integer.parseInt(pagination_totalPages.getText());
        int totalActivityRows = Integer.parseInt(total_activityRows.getText());
        Total_no_of_activityRows_after = 0;

        String Activities_FirstRow = ("//div[@ref=\"eContainer\" and @role=\"rowgroup\"]/div[1]");
        String Activities_FirstRow_ActivityName = getDriver().findElement(By.xpath(Activities_FirstRow+"//child::div[1]")).getText();

        //validate Activity name should Estimate
        Assert.assertEquals(Activities_FirstRow_ActivityName, "Estimate");
        commonUtils.sleep(3000);

        //validate total activity row count increased by one from previous count
        while (count < total_pages) {
            List<WebElement> activityRows_list = getDriver().findElements(By.xpath("//div[@ref=\"eContainer\"]/div"));
            Total_activity_Rows_eachPage = activityRows_list.size();
            System.out.println("Total act rows EachPage: " + Total_activity_Rows_eachPage + "---------------->");
            Total_no_of_activityRows_after = Total_activity_Rows_eachPage + Total_no_of_activityRows_after;     //assign total row count in each page
            pagination_nextPage.click();
            count++;
        }
        commonUtils.sleep(3000);
        System.out.println("Total No of Activity Rows after: " +Total_no_of_activityRows_after+"----------------->");
        Assert.assertEquals(Total_no_of_activityRows_after, totalActivityRows);       //validate total no. of rows found, match with actual count on webpage

        //validate no. of rows after is greater than before
        Assert.assertTrue(Total_no_of_activityRows_after > Total_no_of_activityRows_before);
    }


}
