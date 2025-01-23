package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddEstimateActivity_OpportunityDashboard_Object;
import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddEstimateActivity_OpportunityDashboard_Steps extends DriverBase {

    Assistive_Login login = new Assistive_Login();
    CommonUtils commonUtils = new CommonUtils();
    AddEstimateActivity_OpportunityDashboard_Object estimate = new AddEstimateActivity_OpportunityDashboard_Object();

    @Given("user loggedIn to Assistive project under opportunity Estimate activity")
    public void user_loggedIn_to_Assistive_project_under_opportunity_Estimate_activity() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("user clicks on menu CRM and submenu Opportunities")
    public void user_clicks_on_menu_CRM_and_submenu_Opportunities() {
        estimate.User_clicks_on_menu_CRM_and_submenu_Opportunities();
    }

    @Then("user is on Opportunities page")
    public void user_is_on_Opportunities_page() {
        estimate.User_is_on_Opportunities_page();
    }

    @And("click on actions dashboard button of any Opportunity")
    public void click_on_actions_dashboard_button_of_any_Opportunity() {
        estimate.Click_on_actions_dashboard_button_of_any_Opportunity();
    }

    @And("click on plus button under opportunity Estimate")
    public void click_on_plus_button_under_opportunity_Estimate() {
        estimate.Click_on_plus_button_under_opportunity_Estimate();
    }

    @And("click on Estimate and landed on Add New Estimate page under opportunity Estimate")
    public void click_on_estimate_and_landed_on_add_new_estimate_page_under_opportunity_Estimate() {
        estimate.Click_on_estimate_and_landed_on_add_new_estimate_page_under_opportunity_Estimate();
    }

    @And("check select customer is locked and customer name is displayed")
    public void check_select_customer_is_locked_and_customer_name_is_displayed() {
        estimate.Check_select_customer_is_locked_and_customer_name_is_displayed();
    }

    @And("check select opportunity is locked and opportunity name is displayed")
    public void check_select_opportunity_is_locked_and_opportunity_name_is_displayed() {
        estimate.Check_select_opportunity_is_locked_and_opportunity_name_is_displayed();
    }

    @And("enter update opportunity value under opportunity Estimate")
    public void enter_update_opportunity_value_under_opportunity_estimate() {
        estimate.Enter_update_opportunity_value_under_opportunity_estimate();
    }

    @And("select update opportunity status under opportunity Estimate")
    public void select_update_opportunity_status_under_opportunity_estimate() {
        estimate.Select_update_opportunity_status_under_opportunity_estimate();
    }

    @And("select update Exp. closure date from calendar under opportunity Estimate")
    public void select_update_exp_closure_date_from_calendar_under_opportunity_estimate() {
        estimate.Select_update_exp_closure_date_from_calendar_under_opportunity_estimate("30");
    }

    @And("select update Win probability from dropdown under opportunity Estimate")
    public void select_update_win_probability_from_dropdown_under_opportunity_estimate() {
        estimate.Select_update_win_probability_from_dropdown_under_opportunity_estimate();
    }

    @And("select contact under opportunity Estimate")
    public void select_contact_under_opportunity_estimate() {
        estimate.Select_contact_under_opportunity_estimate();
    }

    @And("fill data in Product Form table under opportunity Estimate")
    public void fill_data_in_product_form_table_under_opportunity_Estimate() {
        estimate.Fill_data_in_product_form_table_under_opportunity_Estimate();
    }

    @Then("validate new Estimate is created under opportunity Estimate")
    public void validate_new_Estimate_is_created_under_opportunity_Estimate() {
        estimate.Validate_new_Estimate_is_created_under_opportunity_Estimate();
    }

    @Then("validate new Estimate in Activities page under opportunity Estimate")
    public void validate_new_Estimate_in_Activities_page_under_opportunity_Estimate() {
        estimate.Validate_new_Estimate_in_Activities_page_under_opportunity_Estimate();
    }

}
