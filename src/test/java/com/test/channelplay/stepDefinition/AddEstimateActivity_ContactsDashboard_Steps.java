package com.test.channelplay.stepDefinition;


import com.test.channelplay.object.AddEstimateActivity_ContactsDashboard_Object;
import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddEstimateActivity_ContactsDashboard_Steps extends DriverBase {

    Assistive_Login login = new Assistive_Login();
    CommonUtils commonUtils = new CommonUtils();
    AddEstimateActivity_ContactsDashboard_Object estimate = new AddEstimateActivity_ContactsDashboard_Object();

    @Given("user loggedIn to Assistive project under Contacts Estimate activity")
    public void user_loggedIn_to_Assistive_project_under_Contacts_Estimate_activity() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("user clicks on menu CRM and submenu Contacts")
    public void user_clicks_on_menu_CRM_and_submenu_Contacts() {
        estimate.User_clicks_on_menu_CRM_and_submenu_Contacts();
    }

    @Then("user is on Contacts page")
    public void user_is_on_Contacts_page() {
        estimate.User_is_on_Contacts_page();
    }

    @And("click on actions dashboard button of any Contact")
    public void click_on_actions_dashboard_button_of_any_Contact() {
        estimate.Click_on_actions_dashboard_button_of_any_Contact();
    }

    @And("click on plus button under Contacts Estimate")
    public void click_on_plus_button_under_Contacts_Estimate() {
        estimate.Click_on_plus_button_under_Contacts_Estimate();
    }

    @And("click on Estimate and landed on Add New Estimate page under Contacts Estimate")
    public void click_on_estimate_and_landed_on_add_new_estimate_page_under_Contacts_Estimate() {
        estimate.Click_on_estimate_and_landed_on_add_new_estimate_page_under_Contacts_Estimate();
    }

    @And("select Customer from dropdown under Contacts Estimate")
    public void select_Customer_from_dropdown_under_Contacts_Estimate() {
        estimate.Select_Customer_from_dropdown_under_Contacts_Estimate();
    }

    @And("select opportunity from dropdown under Contacts Estimate")
    public void select_opportunity_from_dropdown_under_Contacts_Estimate() {
        estimate.Select_opportunity_from_dropdown_under_Contacts_Estimate();
    }

    @And("enter update opportunity value under Contacts Estimate")
    public void enter_update_opportunity_value_under_Contacts_estimate() {
        estimate.Enter_update_opportunity_value_under_Contacts_estimate();
    }

    @And("select update opportunity status under Contacts Estimate")
    public void select_update_opportunity_status_under_Contacts_estimate() {
        estimate.Select_update_opportunity_status_under_Contacts_estimate();
    }

    @And("select update Exp. closure date from calendar under Contacts Estimate")
    public void select_update_exp_closure_date_from_calendar_under_Contacts_estimate() {
        estimate.Select_update_exp_closure_date_from_calendar_under_Contacts_estimate("30");
    }

    @And("select update Win probability from dropdown under Contacts Estimate")
    public void select_update_win_probability_from_dropdown_under_Contacts_estimate() {
        estimate.Select_update_win_probability_from_dropdown_under_Contacts_estimate();
    }

    @And("select contact under Contacts Estimate")
    public void select_contact_under_Contacts_estimate() {
        estimate.Select_contact_under_Contacts_estimate();
    }

    @And("fill data in Product Form table under Contacts Estimate")
    public void fill_data_in_product_form_table_under_Contacts_Estimate() {
        estimate.Fill_data_in_product_form_table_under_Contacts_Estimate();
    }

    @Then("validate new Estimate is created under Contacts Estimate")
    public void validate_new_Estimate_is_created_under_Contacts_Estimate() {
        estimate.Validate_new_Estimate_is_created_under_Contacts_Estimate();
    }

    @Then("validate new Estimate in Activities page under Contacts Estimate")
    public void validate_new_Estimate_in_Activities_page_under_Contacts_Estimate() {
        estimate.Validate_new_Estimate_in_Activities_page_under_Contacts_Estimate();
    }

}
