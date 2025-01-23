package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.AddProductObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddProductSteps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    AddProductObject addProductObject = new AddProductObject();

    @Given("After login the user will be on the Products page under Admin Menu")
    public void afterLoginTheUserWillBeOnTheProductsPageUnderAdminMenu() {
        getDriver().get(GetProperty.value("crmPortal"));
        commonUtils.validatePage("Assistive");
        addProductObject.login(GetProperty.value("username"),GetProperty.value("password"));
    }

    @And("User click on Add Button")
    public void userClickOnAddButton() {
        addProductObject.UserClickOnAddButton();
    }

    @When("user will enter a Product Name")
    public void userWillEnterAProductName() {
        addProductObject.UserWillEnterAProductName();
    }

    @And("user will enter a Product Code")
    public void userWillEnterAProductCode() {
        addProductObject.UserWillEnterAProductCode();
    }

    @And("user will enter Description")
    public void userWillEnterDescription() {
        addProductObject.UserWillEnterDescription();
    }

    @And("user will enter Price")
    public void userWillEnterPrice() {
        addProductObject.UserWillEnterPrice();
    }

    @And("user will click on Save Button")
    public void userWillClickOnSaveButton() {
        addProductObject.UserWillClickOnSaveButton();
    }

    @Then("Product will be added Successfully and will show in the list")
    public void productWillBeAddedSuccessfullyAndWillShowInTheList() {
        addProductObject.ProductWillBeAddedSuccessfullyAndWillShowInTheList();
    }
}
