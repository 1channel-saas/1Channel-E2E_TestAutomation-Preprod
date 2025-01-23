package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class DetailedActivityReport_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" Analytics \"]")
    WebElement Analytics_menu;

    @FindBy(xpath = "//span[text()=\" Reports \"]")
    WebElement Reports_submenu;

    @FindBy(xpath = "//span[text()=\" Detailed Activity Report \"]")
    WebElement DetailedActivityReport_sub_submenu;

    @FindBy(xpath = "//h5[text()=\" Detailed Activity Report \"]")
    WebElement Detailed_Activity_Report_headerText;

    @FindBy(xpath = "//h4[text()=\"Select Filters\"]")
    WebElement filterInterface_SelectFilters_text;

    @FindBy(xpath = "//button[text()=\" Cancel \"]")
    WebElement filterInterface_Cancel_button;

    @FindBy(xpath = "//button[text()=\" Apply \"]")
    WebElement filterInterface_Apply_button;

    @FindBy(xpath = "//span/img[@src=\"/assets/media/svg/icons/Text/Filter.svg\"]")
    WebElement Filter_icon;

    @FindBy(xpath = "//div/label[text()=\"Date Range\"]/parent::div/following-sibling::div//child::button")
    WebElement calendar_icon;

    @FindBy(xpath = "//mat-calendar//child::button/span[contains(text(), ' 20')]")
    WebElement month_year_val;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-container\"]")
    WebElement calendar_overlay;

    @FindBy(xpath = "//mat-calendar//child::button[2]")
    WebElement calendar_back_button;

    @FindBy(xpath = "//mat-calendar//child::button[3]")
    WebElement calendar_next_button;

    @FindBy(xpath = "//div/label[text()=\"Activity\"]/parent::div/following-sibling::div/mat-form-field//child::mat-select/div/div")
    WebElement calendar_Activity_dropdown;

    @FindBy(xpath = "//div/label[text()=\"Performed by\"]/parent::div/following-sibling::div/mat-form-field//child::mat-select/div/div")
    WebElement calendar_performedBy_dropdown;

    @FindBy(xpath = "//span[@ref=\"lbTotal\"]")
    WebElement last_page;

    @FindBy(xpath = "//div[@ref=\"btNext\"]/span")
    WebElement Pagination_nextPage;



    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();


    public DetailedActivityReport_Object() {
        PageFactory.initElements(getDriver(), this);
    }

    public void Clicks_on_menu_analytics_and_submenu_reports_then_detailed_activity_report() {
        Analytics_menu.click();
        Reports_submenu.click();
        commonUtils.sleep(2000);
        DetailedActivityReport_sub_submenu.click();
        commonUtils.sleep(3000);
    }

    public void Filters_interface_opens_under_detailed_activity_report() {
        boolean filter_interface_open = filterInterface_SelectFilters_text.isDisplayed();
        Assert.assertTrue(filter_interface_open);
    }

    public void Click_on_cancel_button_of_filter_interface() {
        filterInterface_Cancel_button.click();
        commonUtils.sleep(2000);
    }

    public void User_is_on_detailed_activity_report_page() {
        boolean Detailed_Act_Report_TextVisible = Detailed_Activity_Report_headerText.isDisplayed();
        Assert.assertTrue(Detailed_Act_Report_TextVisible);
    }

    public void Click_on_filter_icon_under_detailed_activity_report() {
        Filter_icon.click();
        commonUtils.sleep(5000);
    }

    public String[] getMonthYear(String monthYearVal) {
        return monthYearVal.split(" ");
    }

    public void Enter_start_date_under_date_range_field(String exp_day, String exp_month, String exp_year) {

        int arrIndex_exp_startMonth = Integer.parseInt(exp_month);
        String[] MonthArray = {"select month", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        calendar_icon.click();
        commonUtils.sleep(2000);

        String monthYearVal = month_year_val.getText();
        System.out.println("date start: " +monthYearVal);

        while (!(getMonthYear(monthYearVal)[0].equals(MonthArray[arrIndex_exp_startMonth]) && getMonthYear(monthYearVal)[1].equals(exp_year))) {
            calendar_back_button.click();
            monthYearVal = month_year_val.getText();
            System.out.println("new date start: " +monthYearVal);
        }

        WebElement calendar_startDatePick = getDriver().findElement(By.xpath("//table[@class=\"mat-calendar-table\"]/tbody/tr/td/div[contains(text(), '" +exp_day+ "')]"));
        calendar_startDatePick.click();
        commonUtils.sleep(3000);
    }

    public void Enter_end_date_under_Date_Range_field(String exp_day, String exp_month, String exp_year) {

        int arrIndex_exp_endMonth = Integer.parseInt(exp_month);
        String[] MonthArray = {"select month", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        String monthYearVal = month_year_val.getText();
        System.out.println("date end: "+monthYearVal);

        while (!(getMonthYear(monthYearVal)[0].equals(MonthArray[arrIndex_exp_endMonth]) && getMonthYear(monthYearVal)[1].equals(exp_year))) {
            calendar_next_button.click();
            monthYearVal = month_year_val.getText();
            System.out.println("new date end: " +monthYearVal);
        }

        WebElement calendar_endDatePick = getDriver().findElement(By.xpath("//table[@class=\"mat-calendar-table\"]/tbody/tr/td/div[contains(text(), '" +exp_day+ "')]"));
        calendar_endDatePick.click();
        commonUtils.sleep(2000);
        calendar_overlay.click();
    }

    public void Select_value_from_activity_dropdown(String Act_list) {
        calendar_Activity_dropdown.click();
        commonUtils.sleep(2000);
        List<WebElement> Activity_values_list = getDriver().findElements(By.xpath("//div[@class=\"cdk-overlay-pane\"]//child::mat-option/span[contains(text(), '" +Act_list+ "')]"));
        for (WebElement Activity_values : Activity_values_list) {
            Activity_values.click();
            System.out.println("selected activity: " +Activity_values.getText());
        }
        commonUtils.sleep(2000);
    }

    public void Select_value_from_performed_by_dropdown(String performer_list){
        calendar_performedBy_dropdown.click();
        commonUtils.sleep(2000);
        List<WebElement> performedBy_values_list = getDriver().findElements(By.xpath("//div[@class=\"cdk-overlay-pane\"]//child::mat-option/span[contains(text(), '" +performer_list+ "')]"));
        for (WebElement performedBy_values : performedBy_values_list) {
            performedBy_values.click();
            System.out.println("selected performedBy: " +performedBy_values.getText());
        }
        calendar_overlay.click();
        commonUtils.sleep(2000);
    }

    public void Click_on_apply_button_under_filters_interface_of_detailed_activity_report() {
        filterInterface_Apply_button.click();
        commonUtils.sleep(6000);
    }

    public String concatenateDatesStrings(String day, String month, String year) {

        String calendar_day;

        int calendar_day_int = Integer.parseInt(day);
        if (calendar_day_int<10) {
            calendar_day = "0" +Integer.toString(calendar_day_int);
        }
        else {
            calendar_day = Integer.toString(calendar_day_int);
        }

        String fullDateStr = calendar_day + "-" +month + "-" +year;
        return fullDateStr;
    }

    public int stringToIntegerConversion(String date){
        String[] dateArr = date.split("-");
        int newInteger = Integer.parseInt(dateArr[2] + dateArr[1] +dateArr[0]);
        return newInteger;
    }

    public void Validate_actual_result_with_expected_results() {

        String start_dateStr = concatenateDatesStrings(GetProperty.value("detailed_calendar_startDay"), GetProperty.value("detailed_calendar_startMonth"), GetProperty.value("detailed_calendar_startYear"));;
        System.out.println("start date string: " +start_dateStr);

        String end_dateStr = concatenateDatesStrings(GetProperty.value("detailed_calendar_endDay"), GetProperty.value("detailed_calendar_endMonth"), GetProperty.value("detailed_calendar_endYear"));
        System.out.println("end date string: " +end_dateStr);

        int StartDate = stringToIntegerConversion(start_dateStr);
        System.out.println("Start Date: " +StartDate);

        int EndDate = stringToIntegerConversion(end_dateStr);
        System.out.println("End Date: "+EndDate);

        int count = 0;
        int total_pages = Integer.parseInt(last_page.getText());

        while (count<total_pages) {
            String report_rows = ("//div[@ref=\"eContainer\"]/div");
            List <WebElement> report_rows_list = getDriver().findElements(By.xpath(report_rows));
            System.out.println("total row:" +report_rows_list.size());

            for (int i=1; i<report_rows_list.size(); i++) {
                String date_result = getDriver().findElement(By.xpath("//div[@ref=\"eContainer\"]/div['" +i+ "']/div[@col-id=\"1\"]")).getText();
                System.out.println("date from row: " +date_result);
                String[] actual_dateArr = date_result.split("-");
                int actual_date = Integer.parseInt(actual_dateArr[2] + actual_dateArr[1] + actual_dateArr[0]);
                System.out.println("Actual date: " +actual_date);
                Assert.assertTrue(StartDate<=actual_date && actual_date<=EndDate);
            }
            count++;

            if (total_pages>1) {
                Pagination_nextPage.click();
                commonUtils.sleep(3000);
            }
            System.out.println("Total counter: " +count);
        }
    }

}