package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddEstimateActivity_CRM_Activities_Object;
import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddEstimateActivity_CRM_Activities_Steps extends DriverBase {

    AddEstimateActivity_CRM_Activities_Object estimate = new AddEstimateActivity_CRM_Activities_Object();
    AssistiveLogin login = new AssistiveLogin();
    CommonUtils commonUtils = new CommonUtils();


    @Given("user loggedIn to Assistive project under CRM Activities")
    public void user_logged_in_to_assistive_project_under_crm_activities() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("user clicks on menu CRM and submenu Activities")
    public void user_clicks_on_menu_crm_and_submenu_activities() {
        estimate.User_clicks_on_menu_crm_and_submenu_activities();
    }

    @Then("user is on Activities page")
    public void user_is_on_activities_page() {
        estimate.User_is_on_activities_page();
    }

    @And("user select date range from calendar and click on save button under CRM Activities")
    public void user_select_date_range_from_calendar_and_click_on_save_button_under_crm_activities() {
        estimate.User_select_date_range_from_calendar_and_click_on_save_button_under_crm_activities(GetProperty.value("CRM_activities_startDate"), GetProperty.value("CRM_activities_endDate"));
    }

    @And("check total number of records from the activity list under CRM Activities")
    public void check_total_number_of_records_from_the_activity_list_under_crm_activities() {
        estimate.Check_total_number_of_records_from_the_activity_list_under_crm_activities();
    }

    @And("click on Add Activity button under CRM Activities")
    public void click_on_add_activity_button_under_crm_activities() {
        estimate.Click_on_add_activity_button_under_crm_activities();
    }

    @And("click on Estimate and landed on Add New Estimate page under CRM Activities")
    public void click_on_estimate_and_landed_on_Add_New_Estimate_page_under_crm_activities() {
        estimate.Click_on_estimate_and_landed_on_Add_New_Estimate_page_under_crm_activities();
    }

    @And("select customer from dropdown under CRM Activities")
    public void select_customer_from_dropdown_under_crm_activities() {
        estimate.Select_customer_from_dropdown_under_crm_activities();
    }

    @And("select opportunity from dropdown under CRM Activities")
    public void select_opportunity_from_dropdown_under_crm_activities() {
        estimate.Select_opportunity_from_dropdown_under_crm_activities();
    }

    @And("enter update opportunity value under CRM Activities")
    public void enter_update_opportunity_value_under_crm_activities() {
        estimate.Enter_update_opportunity_value_under_crm_activities();
    }

    @And("select update opportunity status under CRM Activities")
    public void select_update_opportunity_status_under_crm_activities() {
        estimate.Select_update_opportunity_status_under_crm_activities();
    }

    @And("select update Exp. closure date from calendar under CRM Activities")
    public void select_update_exp_closure_date_from_calendar_under_crm_activities() {
        estimate.Select_update_exp_closure_date_from_calendar_under_crm_activities("30");
    }

    @And("select update Win probability from dropdown under CRM Activities")
    public void select_update_win_probability_from_dropdown_under_crm_activities() {
        estimate.Select_update_win_probability_from_dropdown_under_crm_activities();
    }

    @And("select contact under CRM Activities")
    public void select_contact_under_crm_activities() {
        estimate.Select_contact_under_crm_activities();
    }

    @And("fill data in Product Form table and click on Save button under CRM Activities")
    public void fill_data_in_product_form_table_and_click_on_save_button_under_crm_activities() {
        estimate.Fill_data_in_product_form_table_and_click_on_save_button_under_crm_activities();
    }

    @Then("validate new created activity in list")
    public void validate_new_created_activity_in_list() {
        estimate.Validate_new_created_activity_in_list();
    }

}
