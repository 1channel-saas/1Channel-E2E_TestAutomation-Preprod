package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class DailyActivityReport_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" Analytics \"]")
    WebElement Analytics_menu;

    @FindBy(xpath = "//span[text()=\" Reports \"]")
    WebElement Reports_submenu;

    @FindBy(xpath = "//span[text()=\" Daily Activity Report \"]")
    WebElement DailyActReport_sub_submenu;

    @FindBy(xpath = "//span[text()=\"Export\"]")
    WebElement Export_button;

    @FindBy(xpath = "//h5[text()=\" Daily Activity Report \"]/parent::div/parent::div/following-sibling::div/div[2]/input")
    WebElement calendar_input;

    @FindBy(xpath = "//div[@id=\"kt_subheader\"]/div/div[2]/div[2]//child::button[text()=\"Apply\"]")
    //div/button[2][text()="Apply"]
    WebElement Apply_button;

    @FindBy(xpath = "//div[@ref=\"btNext\"]/span")
    WebElement Pagination_nextPage;

    @FindBy(xpath = "//span[@ref=\"lbTotal\"]")
    WebElement Last_page;



    CommonUtils commonUtils = new CommonUtils();
    WebDriverUtils webDriverUtils = new WebDriverUtils();

    public DailyActivityReport_Object() {
        PageFactory.initElements(getDriver(),this);
    }

    public void ClicksOnMenuAnalyticsAndSubmenuReportsThenDailyActivityReport() {
        Analytics_menu.click();
        Reports_submenu.click();
        commonUtils.sleep(2000);
        DailyActReport_sub_submenu.click();
        commonUtils.sleep(3000);
    }

    public void UserSelectsDateRangeFromCalendar(String startDate, String endDate) {
        calendar_input.clear();
        commonUtils.sleep(3000);
        calendar_input.sendKeys(startDate+" - "+endDate);
        commonUtils.sleep(4000);
        calendar_input.click();
        commonUtils.sleep(3000);
    }

    public void ClickOnApplyButton() {
        Apply_button.click();
        commonUtils.sleep(5000);
    }

    public int stringToIntegerConversion(String date){
        String[] dateArr = date.split("-");
        int newInteger = Integer.parseInt(dateArr[2] + dateArr[1] +dateArr[0]);
        return newInteger;
    }

    public void ReportsToBeShown() {

        int StartDate = stringToIntegerConversion(GetProperty.value("daily_calendar_startDate"));
        System.out.println("Start Date: " +StartDate);
        int EndDate = stringToIntegerConversion(GetProperty.value("daily_calendar_endDate"));
        System.out.println("End Date: " +EndDate);

        int count = 0;
        int total_pages = Integer.parseInt(Last_page.getText());
        commonUtils.sleep(3000);

        while (count<total_pages) {

            String rep_rows = ("//div[@ref=\"centerContainer\"]//div[@ref=\"eContainer\"]/div");
            List<WebElement> rep_ele_list = getDriver().findElements(By.xpath(rep_rows));

            for (int i=1; i<=rep_ele_list.size(); i++) {
                String dateList_data = getDriver().findElement(By.xpath("//div[@ref=\"centerContainer\"]//div[@ref=\"eContainer\"]/div[" + i + "]/div[@col-id=\"date\"]")).getText();
                String[] dateArr = dateList_data.split("-");
                int actualDate = Integer.parseInt(dateArr[0] + dateArr[1] +dateArr[2]);
                System.out.println("Actual date"+actualDate+"----------------------------->");
                Assert.assertTrue(StartDate<=actualDate && actualDate<=EndDate);
            }
            count++;

                if (total_pages>1) {
                    Pagination_nextPage.click();
                    commonUtils.sleep(3000);
                }
                System.out.println("total counter: " +count);
        }
    }

    public void ClickOnExportButtonExcelGetsDownloaded() {
        Export_button.click();
        commonUtils.sleep(3000);
    }

}
