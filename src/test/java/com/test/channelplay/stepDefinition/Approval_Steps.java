package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.Approval_Object;
import com.test.channelplay.object.AssistiveLogin;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Approval_Steps extends DriverBase {
    Approval_Object approval = new Approval_Object();
    AssistiveLogin login = new AssistiveLogin();
    CommonUtils commonUtils = new CommonUtils();

    @Given("user logged in to Assistive project under approval")
    public void userLoggedInToAssistiveProjectUnderApproval() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("clicks on menu CRM and submenu Approval")
    public void clicksOnMenuCRMAndSubmenuApproval() {
        approval.ClicksOnMenuCRMAndSubmenuApproval();
    }

    @When("clicks on Actions icon for any Approval {string} OR Entity name {string} from the list showing under Pending section")
    public void clicksOnActionsIconForAnyApprovalOrEntityNameFromTheListShowingUnderPendingSection(String Approval_name, String Entity_name) {
        approval.ClicksOnActionsIconForAnyApprovalOrEntityNameFromTheListShowingUnderPendingSection(Approval_name, Entity_name);
    }

    @And("user is on Action page for the same Entity name {string} selected")
    public void userIsOnActionPageForTheSameEntityNameSelected(String ActionEntityName) {
        approval.UserIsOnActionPageForTheSameEntityNameSelected(ActionEntityName);
    }

    @And("edit the details as per requirement")
    public void editTheDetailsAsPerRequirement() {
        approval.EditTheDetailsAsPerRequirement();
    }

    @And("click on Action dropdown and select Action type")
    public void clickOnActionDropdownAndSelectActionType() {
        approval.ClickOnActionDropdownAndSelectActionType();
    }

    @And("click on Save button under approval")
    public void clickOnSaveButtonUnderApproval() {
        approval.ClickOnSaveButtonUnderApproval();
    }
    @Then("validate row count reduced under Pending section")
    public void validateRowCountReducedUnderPendingSection() {
        approval.ValidateRowCountReducedUnderPendingSection();
    }

    @And("go to Completed section")
    public void goToCompletedSection() {
        approval.GoToCompletedSection();
    }

    @Then("validate same Approval {string} is showing under Completed section")
    public void validateSameEntityIsShowingUnderCompletedSection(String Approval_name) {
        approval.ValidateSameEntityIsShowingUnderCompletedSection(Approval_name);
    }

}
