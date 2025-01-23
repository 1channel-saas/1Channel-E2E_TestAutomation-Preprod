package com.test.channelplay.stepDefinition;

import com.test.channelplay.object.Assistive_Login;
import com.test.channelplay.object.DailyActivityReport_Object;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DailyActivityReport_Steps extends DriverBase {

    CommonUtils commonUtils = new CommonUtils();
    Assistive_Login login = new Assistive_Login();
    DailyActivityReport_Object dailyactivityreports = new DailyActivityReport_Object();


    @Given("user logged in to Assistive")
    public void user_logged_in_to_Assistive() {
        getDriver().get(GetProperty.value("appUrl"));
        commonUtils.validatePage("Assistive");
        login.loginToCRM(GetProperty.value("username"),GetProperty.value("password"));
    }

    @When("clicks on menu Analytics and submenu Reports then Daily Activity Report")
    public void clicksOnMenuAnalyticsAndSubmenuReportsThenDailyActivityReport() {
        dailyactivityreports.ClicksOnMenuAnalyticsAndSubmenuReportsThenDailyActivityReport();
    }

    @When("user selects date range from Calendar")
    public void userSelectsDateRangeFromCalendar() {
        dailyactivityreports.UserSelectsDateRangeFromCalendar(GetProperty.value("daily_calendar_startDate"), GetProperty.value("daily_calendar_endDate"));
    }

    @And("click on apply button")
    public void clickOnApplyButton() {
        dailyactivityreports.ClickOnApplyButton();
    }

    @Then("Reports to be shown")
    public void reportsToBeShown() {
        dailyactivityreports.ReportsToBeShown();
    }

    @Then("click on Export button excel get downloaded")
    public void clickOnExportButtonExcelGetsDownloaded() {
        dailyactivityreports.ClickOnExportButtonExcelGetsDownloaded();
    }

}
