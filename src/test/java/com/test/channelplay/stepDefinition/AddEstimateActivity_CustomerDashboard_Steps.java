package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddEstimateActivity_CustomerDashboard_Object;
import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddEstimateActivity_CustomerDashboard_Steps extends DriverBase {

    AssistiveLogin login = new AssistiveLogin();
    CommonUtils commonUtils = new CommonUtils();
    AddEstimateActivity_CustomerDashboard_Object estimate = new AddEstimateActivity_CustomerDashboard_Object();

    @Given("user loggedIn to Assistive project under Estimate activity")
    public void user_logged_in_to_assistive_project_under_estimate_activity() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("user clicks on menu CRM and submenu Customers")
    public void user_clicks_on_menu_crm_and_submenu_customers() {
        estimate.User_clicks_on_menu_crm_and_submenu_customers();
    }

    @Then("user is on Customers page")
    public void user_is_on_customers_page() {
        estimate.User_is_on_customers_page();
    }

    @And("click on Actions Dashboard button of any customer")
    public void click_on_actions_dashboard_button_of_any_customer() {
        estimate.Click_on_actions_dashboard_button_of_any_customer();
    }

    @And("click on plus button")
    public void click_on_plus_button() {
        estimate.Click_on_plus_button();
    }

    @And("click on Estimate and landed on Add New Estimate page")
    public void click_on_estimate_and_landed_on_add_new_estimate_page() {
        estimate.Click_on_estimate_and_landed_on_add_new_estimate_page();
    }

    @And("select Customer from dropdown under Estimate")
    public void select_customer_from_dropdown_under_estimate() {
        estimate.Select_customer_from_dropdown_under_estimate();
    }

    @And("select opportunity from dropdown under Estimate")
    public void select_opportunity_from_dropdown_under_estimate() {
        estimate.Select_opportunity_from_dropdown_under_estimate();
    }

    @And("enter update opportunity value under Estimate")
    public void enter_update_opportunity_value_under_estimate() {
        estimate.Enter_update_opportunity_value_under_estimate();
    }

    @And("select update opportunity status under Estimate")
    public void select_update_opportunity_status_under_estimate() {
        estimate.Select_update_opportunity_status_under_estimate();
    }

    @And("select update Exp. closure date from calendar under Estimate")
    public void select_update_exp_closure_date_from_calendar_under_estimate() {
        estimate.Select_update_exp_closure_date_from_calendar_under_estimate("30");
    }

    @And("select update Win probability from dropdown under Estimate")
    public void select_update_win_probability_from_dropdown_under_estimate() {
        estimate.Select_update_win_probability_from_dropdown_under_estimate();
    }

    @And("select contact under Estimate")
    public void select_contact_under_estimate() {
        estimate.Select_contact_under_estimate();
    }

    @And("fill data in Product Form table")
    public void fill_data_in_product_form_table() {
        estimate.Fill_data_in_product_form_table();
    }

    @Then("validate new Estimate is created under Estimate")
    public void validate_new_Estimate_is_created_under_Estimate() {
        estimate.Validate_new_Estimate_is_created_under_Estimate();
    }

    @Then("validate new Estimate in Activities page under Estimate")
    public void validate_new_Estimate_in_Activities_page_under_Estimate() {
        estimate.Validate_new_Estimate_in_Activities_page_under_Estimate();
    }

}
