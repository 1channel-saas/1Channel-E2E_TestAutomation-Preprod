package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.object.UserList_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class UserList_Steps extends DriverBase {

    UserList_Object userlist = new UserList_Object();
    Assistive_Login login = new Assistive_Login();
    CommonUtils commonUtils = new CommonUtils();

    @Given("user loggedIn to Assistive project under Admin User")
    public void user_logged_in_to_assistive_project_under_admin_user() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }


    @When("user clicks on menu Admin and submenu Users")
    public void user_clicks_on_menu_admin_and_submenu_users() {
        userlist.User_clicks_on_menu_admin_and_submenu_users();
    }


    @Then("user is on Users page")
    public void user_is_on_users_page() {
        boolean user_Title = userlist.User_is_on_users_page();
        Assert.assertTrue(user_Title);
    }


    @And("clicks on Add button opens Add new user page")
    public void clicks_on_add_button_opens_add_new_user_page() {
        userlist.Clicks_on_add_button_opens_add_new_user_page();
    }


    @Then("fill data into FirstName and LastName")
    public void fill_data_into_first_name_and_last_name() {
        userlist.Fill_data_into_first_name_and_last_name();
    }


    @Then("enter email id in email field")
    public void enter_email_id_in_email_field() {
        userlist.Enter_email_id_in_email_field();
    }


    @Then("enter mobile number")
    public void enter_mobile_number() {
        userlist.Enter_mobile_number("9876543210");
    }


    @Then("select User Role from dropdown")
    public void select_user_role_from_dropdown() {
        userlist.Select_user_role_from_dropdown();
    }


    @Then("select reports to from dropdown")
    public void select_reports_to_from_dropdown() {
        userlist.Select_reports_to_from_dropdown();
    }


    @Then("click on checkbox of set password")
    public void click_on_checkbox_of_set_password() {
        userlist.Click_on_checkbox_of_set_password();
    }


    @Then("enter password in password checkbox")
    public void enter_password_in_password_checkbox() {
        userlist.Enter_password_in_password_checkbox();
    }


    @Then("clicks on Save button")
    public void clicks_on_save_button() {
        userlist.Clicks_on_save_button();
    }

    @Then("newly created user name should show in the list")
    public void newlyCreatedUsernameShouldShowInTheList() {
        userlist.NewlyCreatedUsernameShouldShowInTheList();
    }

}
