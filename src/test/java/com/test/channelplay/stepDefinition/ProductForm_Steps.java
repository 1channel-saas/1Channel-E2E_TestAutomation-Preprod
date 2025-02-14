package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.object.ProductForm_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ProductForm_Steps extends DriverBase {

    ProductForm_Object ProductForm = new ProductForm_Object();
    CommonUtils commonUtils = new CommonUtils();
    AssistiveLogin login = new AssistiveLogin();


    @Given("user logged in to Assistive project")
    public void user_logged_in_to_Assistive_Project() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("clicks on menu Admin and submenu Product Form")
    public void clicks_on_menu_Admin_and_submenu_Product_Form() {
     ProductForm.Clicks_on_menu_Admin_and_submenu_Product_Form();
    }

   @When ("clicks on Add New button opens Add Form window")
    public void clicks_on_Add_New_button_opens_Add_Form_window() {
        ProductForm.Clicks_on_Add_New_button_opens_Add_Form_window();
   }

    @And("user enters Form name")
    public void user_enters_form_name() {
        ProductForm.User_enters_form_name();
    }

    @And("click on Save button")
    public void click_on_save_button() {
        ProductForm.Click_on_save_button();
    }

    @Then("click on back button to go to Product Form Home")
    public void click_on_back_button_to_go_to_product_form_home(){
        ProductForm.Click_on_back_button_to_go_to_product_form_home();
    }

    @And("validate new Form is present")
    public void validate_new_form_is_present() {
        ProductForm.Validate_new_form_is_present();
    }

    @Then("user clicks on Edit button of new Form")
    public void user_clicks_on_edit_button_of_new_form() {
        ProductForm.User_clicks_on_edit_button_of_new_form();
    }

    @And("click on Add Field button from new Form page")
    public void click_on_add_field_button_from_new_form_page() {
        ProductForm.Click_on_add_field_button_from_new_form_page();
    }

    @And("Add new Form field Quantity of type numeric")
    public void addNewFormFieldQuantityOfTypeNumeric() {
        ProductForm.AddNewFormFieldQuantityOfTypeNumeric("Quantity");
    }

    @And("Add new Form field Tax% of type numeric")
    public void addNewFormFieldTaxOfTypeNumeric() {
        ProductForm.AddNewFormFieldTaxOfTypeNumeric("Tax%");
    }

    @Then("Add new Form field Total of type calculated field")
    public void addNewFormFieldTotalOfTypeCalculatedField() {
        ProductForm.AddNewFormFieldTotalOfTypeCalculatedField("Total");
    }
}
