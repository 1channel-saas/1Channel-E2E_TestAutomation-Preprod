package com.test.channelplay.stepDefinition;

import com.beust.jcommander.JCommander;
import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.object.DetailedActivityReport_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DetailedActivityReport_Steps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    Assistive_Login login = new Assistive_Login();
    DetailedActivityReport_Object detailedActivityReport = new DetailedActivityReport_Object();


    @Given("user LoggedIn to Assistive To Test Detailed Activity Report")
    public void userLoggedIntoAssistiveToTestDetailedActivityReport() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @And("clicks on menu Analytics and submenu Reports then Detailed Activity Report")
    public void clicks_on_menu_analytics_and_submenu_reports_then_detailed_activity_report() {
        detailedActivityReport.Clicks_on_menu_analytics_and_submenu_reports_then_detailed_activity_report();
    }

    @When("Filters interface opens under detailed activity report")
    public void filters_interface_opens_under_detailed_activity_report() {
        detailedActivityReport.Filters_interface_opens_under_detailed_activity_report();
    }


    @And("click on Cancel button of Filter interface")
    public void click_on_cancel_button_of_filter_interface() {
        detailedActivityReport.Click_on_cancel_button_of_filter_interface();
    }


    @Then("user is on Detailed Activity Report Page")
    public void user_is_on_detailed_activity_report_page() {
        detailedActivityReport.User_is_on_detailed_activity_report_page();
    }


    @When("click on Filter icon under detailed activity report")
    public void click_on_filter_icon_under_detailed_activity_report() {
        detailedActivityReport.Click_on_filter_icon_under_detailed_activity_report();
    }


    @And("enter start date under Date Range field")
    public void enter_start_date_under_date_range_field() {
        detailedActivityReport.Enter_start_date_under_date_range_field(GetProperty.value("detailed_calendar_startDay"), (GetProperty.value("detailed_calendar_startMonth")), GetProperty.value("detailed_calendar_startYear"));
    }

    @And("enter end date under Date Range field")
    public void enter_end_date_under_Date_Range_field() {
        detailedActivityReport.Enter_end_date_under_Date_Range_field(GetProperty.value("detailed_calendar_endDay"), GetProperty.value("detailed_calendar_endMonth"), GetProperty.value("detailed_calendar_endYear"));
    }

    @And("select value from Activity dropdown")
    public void select_value_from_activity_dropdown() {
        detailedActivityReport.Select_value_from_activity_dropdown("Customer Created");
    }


    @And("select value from Performed By dropdown")
    public void select_value_from_performed_by_dropdown() {
        detailedActivityReport.Select_value_from_performed_by_dropdown(" david marthon ");
    }


    @And("click on apply button under Filters interface of detailed activity report")
    public void click_on_apply_button_under_filters_interface_of_detailed_activity_report() {
        detailedActivityReport.Click_on_apply_button_under_filters_interface_of_detailed_activity_report();
    }

    @Then("validate actual result with expected results")
    public void validate_actual_result_with_expected_results() {
        detailedActivityReport.Validate_actual_result_with_expected_results();
    }

}