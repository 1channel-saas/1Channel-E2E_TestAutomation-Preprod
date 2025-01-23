package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.WorkflowFunctionalityForCustomerCreatedObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Objects;

public class WorkflowFunctionalityForCustomerCreatedSteps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    WorkflowFunctionalityForCustomerCreatedObject workflowFunctionalityForCustomerCreatedObject = new WorkflowFunctionalityForCustomerCreatedObject();

    @Given("after login to CRM user will be on the Workflow screen under Admin Menu")
    public void afterLoginToCRMUserWillBeOnTheWorkflowScreenUnderAdminMenu() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        workflowFunctionalityForCustomerCreatedObject.login(GetProperty.value("username"),GetProperty.value("password"));
        workflowFunctionalityForCustomerCreatedObject.AfterLoginToCRMUserWillBeOnTheWorkflowScreenUnderAdminMenu();
    }

    @When("user select Add New button under workflow screen")
    public void userSelectAddNewButtonUnderWorkflowScreen() {
        workflowFunctionalityForCustomerCreatedObject.UserSelectAddNewButtonUnderWorkflowScreen();
    }

    @And("enter Workflow Name {string} and  Description {string} under add new workflow screen")
    public void enterAndUnderAddNewWorkflowScreen(String workflow_name, String workflow_description) {
        workflowFunctionalityForCustomerCreatedObject.EnterNameAndDescriptionUnderAddNewWorkflowScreen(workflow_name, workflow_description);
    }
    

    @And("select Entity as {string} under add new workflow screen")
    public void selectEntityAsUnderAddNewWorkflowScreen(String Entity) {
        workflowFunctionalityForCustomerCreatedObject.SelectEntityAsCustomerUnderAddNewWorkflowScreen(Entity);
    }

    @And("select Trigger as {string} under add new workflow screen")
    public void selectTriggerAsUnderAddNewWorkflowScreen(String trigger) {
        workflowFunctionalityForCustomerCreatedObject.SelectTriggerAsCustomerIsCreatedUnderAddNewWorkflowScreen(trigger);
    }


    @And("select save button under add new workflow screen")
    public void selectSaveButtonUnderAddNewWorkflowScreen() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderAddNewWorkflowScreen();
    }

    @Then("new workflow will be created")
    public void newWorkflowWillBeCreated() {
        workflowFunctionalityForCustomerCreatedObject.NewWorkflowWillBeCreated();
    }

    @And("active the workflow")
    public void activeTheWorkflow() {
        workflowFunctionalityForCustomerCreatedObject.ActiveTheWorkflow();
    }

    @And("go to the workflow screen")
    public void goToTheWorkflowScreen() {
        workflowFunctionalityForCustomerCreatedObject.GoToTheWorkflowScreen();
    }

    @Then("new workflow will show in the workflow list under workflow screen")
    public void newWorkflowWillShowInTheWorkflowListUnderWorkflowScreen() {
        workflowFunctionalityForCustomerCreatedObject.NewWorkflowWillShowInTheWorkflowListUnderWorkflowScreen();
    }

    @And("user select {string} options from dropdown list")
    public void userSelectOptionsFromDropdownList(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.UserSelectOptionsFromDropdownList(arg0);
    }

    @And("select Start Date {string} under add new workflow screen")
    public void selectStartDateUnderAddNewWorkflowScreen(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectStartDateUnderAddNewWorkflowScreen(arg0);
    }

    @When("user will select Workflow action for any item {string} from the workflow list")
    public void userWillSelectWorkflowActionForAnyItemFromTheWorkflowList(String workflowName) {
        workflowFunctionalityForCustomerCreatedObject.UserWillSelectWorkflowActionForAnyItemFromTheWorkflowList(workflowName);
    }

    @And("select Add an Action or Condition button {string} OR select plus button before Node contains ActionNode Name {string} and ActionNode position {int} if needed")
    public void selectAddAnActionOrConditionButtonORSelectPlusButtonBeforeNodeContainsActionNodeNameIfNeeded(String actionNodeConditionType, String ActionNodeName, int ActionNodePosition) {
        workflowFunctionalityForCustomerCreatedObject.SelectAddAnActionOrConditionButton(actionNodeConditionType, ActionNodeName, ActionNodePosition);
    }

    @And("after select Send Email action user select Next button under Sent Email section")
    public void afterSelectSendEmailActionUserSelectNextButtonUnderSentEmailSection() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectSendEmailActionUserSelectNextButtonUnderSentEmailSection();
    }

    @And("user select To Receiver {string} from To dropdown and enter {string} if needed")
    public void userSelectToReceiverFromToDropdown(String ToReceiverName, String customEmailIds) {
        workflowFunctionalityForCustomerCreatedObject.UserSelectToReceiverFromToDropdown(ToReceiverName, customEmailIds);
    }

    @And("user select CC Receiver {string} from CC dropdown and enter {string} if needed")
    public void userSelectFromCCReceiverCCDropdown(String CCReceiverName, String customEmailIds) {
        workflowFunctionalityForCustomerCreatedObject.UserSelectFromCCReceiverCCDropdown(CCReceiverName, customEmailIds);
    }

    @And("enter Subject {string} under Subject field")
    public void enterSubjectUnderSubjectField(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.EnterSubjectUnderSubjectField(arg0);
    }

    @And("user enter email content")
    public void userEnterEmailContent() {
        String EntityName = workflowFunctionalityForCustomerCreatedObject.entityName;
        switch (EntityName) {
            case "Customer":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForCustomer();
                break;
            case "Contact":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForContact();
                break;
            case "Opportunity":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForOpportunity();
                break;
            case "Offsite":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForOffsiteActivity();
                break;
            case "Onsite":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForOnsiteActivity();
                break;
            case "Estimate":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForEstimateActivity();
                break;
            case "Order":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForOrderActivity();
                break;
            case "Date":
                workflowFunctionalityForCustomerCreatedObject.UserEnterEmailContentForDateTimeWorkflow();
                break;
        }
    }

    @And("select save button under Sent Email section")
    public void selectSaveButtonUnderSentEmailSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Sent Email node will show under workflow section")
    public void sentEmailNodeWillShowUnderWorkflowSection() {
        workflowFunctionalityForCustomerCreatedObject.SentEmailNodeWillShowUnderWorkflowSection();
    }

    @And("after select Send SMS action user select Next button under Send SMS section")
    public void afterSelectSendSMSActionUserSelectNextButtonUnderSendSMSSection() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectSendSMSActionUserSelectNextButtonUnderSendSMSSection();
    }

    @And("select To Receiver {string} from the To dropdown and add Custom Phone Numbers {string} if needed")
    public void selectToReceiverFromTheToDropdownAndAddCustomPhoneNumbers(String arg0, String arg1) {
        workflowFunctionalityForCustomerCreatedObject.SelectToReceiverFromTheToDropdown(arg0, arg1);
    }

    @And("enter Message {string} under Message field")
    public void enterMessageUnderMessageField(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.EnterMessageUnderMessageField(arg0);
    }

    @And("select save button under Send SMS section")
    public void selectSaveButtonUnderSendSMSSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Send SMS node will show under workflow section")
    public void sendSMSNodeWillShowUnderWorkflowSection() {
        workflowFunctionalityForCustomerCreatedObject.SendSMSNodeWillShowUnderWorkflowSection();
    }

    @And("after select Send App Notification action user select Next button under Send App Notification section")
    public void afterSelectSendAppNotificationActionUserSelectNextButtonUnderSendAppNotificationSection() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectSendAppNotificationActionUserSelectNextButtonUnderSendAppNotificationSection();
    }

    @And("select To Receiver {string} from the To dropdown under Send App Notifications Screen")
    public void selectToReceiverFromTheToDropdownUnderSendAppNotificationsScreen(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectToReceiverFromTheToDropdownUnderSendAppNotificationsScreen(arg0);
    }

    @And("enter Title {string} in the Title field")
    public void enterTitleInTheTitleField(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.EnterTitleInTheTitleField(arg0);
    }

    @And("enter Message {string} under Message field under Send App Notifications Screen")
    public void enterMessageUnderMessageFieldUnderSendAppNotificationsScreen(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.EnterMessageUnderMessageFieldUnderSendAppNotificationsScreen(arg0);
    }

    @And("select save button under Send App Notification section")
    public void selectSaveButtonUnderSendAppNotificationSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Send App Notification node will show under workflow section")
    public void sendAppNotificationNodeWillShowUnderWorkflowSection() {
        workflowFunctionalityForCustomerCreatedObject.SendAppNotificationNodeWillShowUnderWorkflowSection();
    }

    @And("after select Create Activity action user select Next button under Create Activity section")
    public void afterSelectCreateActivityActionUserSelectNextButtonUnderCreateActivitySection() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectCreateActivityActionUserSelectNextButtonUnderCreateActivitySection();
    }

    @And("select Activity Name {string} from the Activity Name dropdown")
    public void selectActivityNameFromTheActivityNameDropdown(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectActivityNameFromTheActivityNameDropdown(arg0);
    }

    @And("fill all the fields with proper data under Create Activity screen")
    public void fillAllTheFieldsWithProperDataUnderCreateActivityScreen() {
        workflowFunctionalityForCustomerCreatedObject.FillAllTheFieldsWithProperDataUnderCreateActivityScreen();
    }

    @And("select save button under Create Activity section")
    public void selectSaveButtonUnderCreateActivitySection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Create Activity node will show under workflow section")
    public void createActivityNodeWillShowUnderWorkflowSection() {
        workflowFunctionalityForCustomerCreatedObject.CreateActivityNodeWillShowUnderWorkflowSection();
    }

    @And("after select Delay Timer action user select Next button under Delay Timer section")
    public void afterSelectDelayTimerActionUserSelectNextButtonUnderDelayTimerSection() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectDelayTimerActionUserSelectNextButtonUnderDelayTimerSection();
    }

    @And("enter Duration Value {string} and select Duration Measure {string} from the Duration dropdown")
    public void enterDurationValueAndSelectDurationMeasureFromTheDurationDropdown(String arg0, String arg1) {
        workflowFunctionalityForCustomerCreatedObject.EnterDurationValueAndSelectDurationMeasureFromTheDurationDropdown(arg0, arg1);
    }

    @And("select save button under Delay Timer section")
    public void selectSaveButtonUnderDelayTimerSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Delay Timer node will show under workflow section respect to Duration Value {string} and Duration Measure {string}")
    public void delayTimerNodeWillShowUnderWorkflowSectionRespectToDurationValueAndDurationMeasure(String arg0, String arg1) {
        workflowFunctionalityForCustomerCreatedObject.DelayTimerNodeWillShowUnderWorkflowSectionRespectToDurationValueAndDurationMeasure(arg0, arg1);
    }

    @And("after select New Condition user select Next button")
    public void afterSelectNewConditionUserSelectNextButton() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectNewConditionUserSelectNextButton();
    }

    @And("select Entity {string} from the Select Entity dropdown")
    public void selectEntityFromTheSelectEntityDropdown(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectEntityFromTheSelectEntityDropdown(arg0);
    }

    @And("select Entity Field {string} from the Select Entity Field dropdown")
    public void selectEntityFieldFromTheSelectEntityFieldDropdown(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectEntityFieldFromTheSelectEntityFieldDropdown(arg0);
    }

    @And("select Operator {string} from the Select Operator dropdown")
    public void selectOperatorFromTheSelectOperatorDropdown(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.SelectOperatorFromTheSelectOperatorDropdown(arg0);
    }

    @And("enter Value {string} in the Select Value field")
    public void enterValueInTheSelectValueField(String arg0) {
        workflowFunctionalityForCustomerCreatedObject.EnterValueInTheSelectValueField(arg0);
    }

    @And("select Add More {string} if required additional condition to be added with Condition Type {string}")
    public void selectAddMoreIfRequiredAdditionalConditionToBeAddedWithConditionType(String AddMore, String ConditionType) {
        if(AddMore.equals("YES")){
            workflowFunctionalityForCustomerCreatedObject.SelectAddMoreIfRequiredAdditionalConditionToBeAddedWithConditionType(ConditionType);
        }
    }

    @And("select Entity {string}, Entity Field {string}, Operator {string}, Value {string} for Add More {string}")
    public void selectEntityEntityFieldOperatorValueForAddMore(String Entity, String EntityField, String Operator, String Value, String AddMore) {
        if(AddMore.equals("YES")){
            workflowFunctionalityForCustomerCreatedObject.SelectEntityEntityFieldOperatorValueForAddMore(Entity, EntityField, Operator, Value);
        }
    }

    @And("select Add Group {string} if required additional condition to be added with Condition Type {string}")
    public void selectAddGroupIfRequiredAdditionalConditionToBeAddedWithConditionType(String AddGroup, String ConditionType) {
        if(AddGroup.equals("YES")){
            workflowFunctionalityForCustomerCreatedObject.SelectAddGroupIfRequiredAdditionalConditionToBeAddedWithConditionType(ConditionType);
        }
    }

    @And("select Entity {string}, Entity Field {string}, Operator {string}, Value {string} for Add Group {string}")
    public void selectEntityEntityFieldOperatorValueForAddGroup(String Entity, String EntityField, String Operator, String Value, String AddGroup) {
        if(AddGroup.equals("YES")){
            workflowFunctionalityForCustomerCreatedObject.selectEntityEntityFieldOperatorValueForAddGroup(Entity, EntityField, Operator, Value);
        }
    }

    @And("select save button under New Condition section")
    public void selectSaveButtonUnderNewConditionSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("New Condition will show under workflow dashboard")
    public void newConditionWillShowUnderWorkflowDashboard() {
        workflowFunctionalityForCustomerCreatedObject.NewConditionWillShowUnderWorkflowDashboard();
    }


    @And("after select Approval user select Next button")
    public void afterSelectApprovalUserSelectNextButton() {
        workflowFunctionalityForCustomerCreatedObject.AfterSelectApprovalUserSelectNextButton();
    }

    @And("user enter Name {string} in the Name field")
    public void userEnterNameInTheNameField(String Name) {
        workflowFunctionalityForCustomerCreatedObject.UserEnterNameInTheNameField(Name);
    }

    @And("user select AssignTo {string} from AssignTo dropdown and select Roles {string} if needed")
    public void userSelectAssignToFromAssignToDropdownAndSelectRolesIfNeeded(String AssignTo, String Roles) {
        workflowFunctionalityForCustomerCreatedObject.UserSelectAssignToFromAssignToDropdownAndSelectRolesIfNeeded(AssignTo, Roles);
    }

    @And("select Enable Editing {string} checkbox if needed")
    public void selectEnableEditingCheckboxIfNeeded(String EnableEditing) {
        if(EnableEditing.equals("YES")){
            workflowFunctionalityForCustomerCreatedObject.SelectEnableEditingCheckboxIfNeeded();
        }
    }

    @And("select save button under Approval section")
    public void selectSaveButtonUnderApprovalSection() {
        workflowFunctionalityForCustomerCreatedObject.SelectSaveButtonUnderSentEmailSection();
    }

    @Then("Approval node will show under workflow dashboard")
    public void approvalNodeWillShowUnderWorkflowDashboard() {
        workflowFunctionalityForCustomerCreatedObject.ApprovalNodeWillShowUnderWorkflowDashboard();
    }
}
