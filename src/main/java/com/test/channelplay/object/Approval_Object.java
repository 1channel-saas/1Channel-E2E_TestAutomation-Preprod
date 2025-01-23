package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class Approval_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" CRM \"]")
    WebElement CRM_menu;

    @FindBy(xpath = "//span[text()=\" Approval \"]")
    WebElement Approval_subMenu;

    @FindBy(xpath = "//div/h5[text()=\" Approval \"]")
    WebElement Approval_headerText;

    @FindBy(xpath = "//div[text()=\"Pending\"]")
    WebElement Pending_moduleMenu;

    @FindBy(xpath = "//div[text()=\"Completed\"]")
    WebElement Completed_moduleMenu;

    @FindBy(xpath = "//img[@src=\"./assets/media/svg/icons/General/dashboard.svg\" and @title=\"actions\"]")
    WebElement Actions_icon;

    @FindBy(xpath = "//div/h4[text()=\"Action\"]")
    WebElement Action_editPage_textHeader;

    @FindBy(xpath = "//label[text()=\"Customer Name\"]//parent::div/following-sibling::div//input")
    WebElement Action_editPage_CustomerName_field;

    @FindBy(xpath = "//label[text()=\"Opportunity Name\"]//parent::div/following-sibling::div//input")
    WebElement Action_editPage_OpportunityName_field;

    @FindBy(xpath = "//label[text()=\"Value\"]//parent::div/following-sibling::div//input")
    WebElement Action_editPage_Value_field;

    @FindBy(xpath = "//label[text()=\"Action\"]//parent::div/following-sibling::div//mat-select")
    WebElement Action_editPage_ActionDropdown;

    @FindBy(xpath = "//span[text()=\" In Progress \"]")
    WebElement Action_editPage_ActionDropdown_InProgress;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    WebElement Action_editPage_Save_button;

    @FindBy(xpath = "//div/input[@placeholder=\"Search\"]")
    WebElement Searchbar_field;


    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();
    int PendingRowCount_before, PendingRowCount_after = 0;
    int CompletedRowCount_before, CompletedRowCount_after = 0;

    Actions action = new Actions(getDriver());

    public Approval_Object() {
        PageFactory.initElements(getDriver(), this);
    }

    public void ClicksOnMenuCRMAndSubmenuApproval() {
        CRM_menu.click();
        commonUtils.sleep(1000);
        Approval_subMenu.click();
        commonUtils.sleep(2000);
        boolean Approval_headerText_isDisplayed = Approval_headerText.isDisplayed();
        Assert.assertTrue(Approval_headerText_isDisplayed);
        commonUtils.sleep(8000);

        String RowCount = ("//div[@ref=\"eViewport\" and @role=\"presentation\"]/div/div");
        List<WebElement> PendingRowCount_list = getDriver().findElements(By.xpath(RowCount));
        PendingRowCount_before = PendingRowCount_list.size();
        commonUtils.sleep(2000);

        // Taking row count of Completed approval list before perform Action
        Completed_moduleMenu.click();
        commonUtils.sleep(5000);
        List<WebElement> CompletedRowCount_list = getDriver().findElements(By.xpath(RowCount));
        CompletedRowCount_before = CompletedRowCount_list.size();

        Pending_moduleMenu.click();
    }

    public void ClicksOnActionsIconForAnyApprovalOrEntityNameFromTheListShowingUnderPendingSection(String Approval_name, String Entity_name) {
        commonUtils.sleep(4000);
        getDriver().findElement(By.xpath("//div[text()='" +Entity_name+ "']/following-sibling::div[text()='" +Approval_name+ "']//parent::div//img")).click();
        commonUtils.sleep(10000);
    }

    public void UserIsOnActionPageForTheSameEntityNameSelected(String ActionEntityName) {

        boolean ActionEditPageTextHeader_isDisplayed = Action_editPage_textHeader.isDisplayed();
        Assert.assertTrue(ActionEditPageTextHeader_isDisplayed);
        commonUtils.sleep(2000);

        String Action_opportunityName = Action_editPage_OpportunityName_field.getAttribute("value");
        Assert.assertEquals(Action_opportunityName, ActionEntityName);
        commonUtils.sleep(2000);
    }

    public void EditTheDetailsAsPerRequirement() {
        Action_editPage_Value_field.clear();
        commonUtils.sleep(1000);
        Action_editPage_Value_field.sendKeys("2000");
    }

    public void ClickOnActionDropdownAndSelectActionType() {
        commonUtils.sleep(2000);
        webDriverUtils.actionsToMoveToElement(getDriver(), Action_editPage_ActionDropdown);
        Action_editPage_ActionDropdown.click();
        Action_editPage_ActionDropdown_InProgress.click();
    }

    public void ClickOnSaveButtonUnderApproval() {
        commonUtils.sleep(2000);
        Action_editPage_Save_button.click();
        commonUtils.sleep(6000);
    }

    public void ValidateRowCountReducedUnderPendingSection() {
        String RowCount = ("//div[@ref=\"eViewport\" and @role=\"presentation\"]/div/div");
        List<WebElement> PendingRowCount_list = getDriver().findElements(By.xpath(RowCount));
        PendingRowCount_after = PendingRowCount_list.size();

        Assert.assertTrue(PendingRowCount_after<PendingRowCount_before);
        commonUtils.sleep(2000);
    }

    public void GoToCompletedSection() {
        commonUtils.sleep(2000);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,-350)", "");
        Completed_moduleMenu.click();
    }

    public void ValidateSameEntityIsShowingUnderCompletedSection(String Approval_name) {
        commonUtils.sleep(3000);
        String RowCount = ("//div[@ref=\"eViewport\" and @role=\"presentation\"]/div/div");
        List<WebElement> CompletedRowCount_list = getDriver().findElements(By.xpath(RowCount));
        CompletedRowCount_after = CompletedRowCount_list.size();

        Assert.assertTrue(CompletedRowCount_after>CompletedRowCount_before);

        Searchbar_field.sendKeys(Approval_name);
        Assert.assertTrue(CompletedRowCount_after>0);
    }

}
