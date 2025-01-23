package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.time.Duration;

public class CustomerTransObject extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement username_field;

    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement submit_button;

    @FindBy(xpath = "//span[text()=\" CRM \"]")
    private WebElement CRM_menu;

    @FindBy(xpath = "//div[@id='kt_header_menu']/ul/li[2]/div/ul/li/a/span[2]")
    private WebElement Customer_menu;

    @FindBy(xpath = "//div[@id='kt_content']/div/div/kt-company-management/kt-portlet/div/kt-portlet-body/kt-companies-list/div/div/div/div[3]/button[2]/span")
    private WebElement Add_button;

    @FindBy(xpath = "//label[text()=\"Customer Name\"]/parent::div/following-sibling::div//input")
    private WebElement CustomerName_field;

    @FindBy(xpath = "//label[text()=\"Customer Type\"]/parent::div/following-sibling::div/mat-form-field/div")
    private WebElement CustomerType_dropdown;

    @FindBy(xpath = "//mat-option[2]")
    private WebElement First_Option;

    @FindBy(xpath = "//label[text()=\"Address\"]/parent::div/following-sibling::div//textarea")
    private WebElement Address_Field;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"countryName\"]")
    private WebElement Country_Dropdown;

    @FindBy(xpath = "//span[text()=\" India \"]")
    private WebElement India_Option;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"stateName\"]")
    private WebElement State_Dropdown;

    @FindBy(xpath = "//span[text()=\" Andhra Pradesh \"]")
    public WebElement AndhraPradesh_Option;

    @FindBy(xpath = "//mat-select[@formcontrolname=\"cityName\"]")
    private WebElement City_Dropdown;

    @FindBy(xpath = "//span[text()=\" Adilabad \"]")
    private WebElement Adilabad_Option;

    @FindBy(xpath = "//label[text()=\"Photo\"]/parent::div/following-sibling::div//input")
    private WebElement Photo_upload;

    @FindBy(xpath = "//label[text()=\"Phone\" ]/parent::div/following-sibling::div//input")
    private WebElement Phone_Field;

    @FindBy(xpath = "//label[text()=\"Time zone\"]/parent::div//following-sibling::div/mat-form-field/div")
    private WebElement TimeZone_Dropdown;

    @FindBy(xpath = "//button[@aria-label=\"Open calendar\"]")
    private WebElement Calender_button;

    @FindBy(xpath = "//div[@class=\"mat-calendar-body-cell-content mat-calendar-body-today\"]\n")
    private WebElement CurrentDate;

    @FindBy(xpath = "//label[text()=\"Customer Email\"]/parent::div/following-sibling::div//input")
    private WebElement Email_Field;

    @FindBy(xpath = "//label[text()=\"Ducument\"]/parent::div/following-sibling::div//input")
    private WebElement Document_upload;

    @FindBy(xpath = "//label[text()=\"Video\"]/parent::div/following-sibling::div//input")
    private WebElement Video_upload;

    @FindBy(xpath = "//label[text()=\"Gender\"]/parent::div//following-sibling::div/mat-form-field/div")
    private WebElement Gender_Dropdown;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    private WebElement Save_button;

    @FindBy(xpath = "//input[@placeholder=\"Search\"]")
    private WebElement Search_bar;

    //Global Code
    CommonUtils commonUtils = new CommonUtils();
    Actions actions = new Actions(getDriver());
    public String customerName;
    public String dataPicker;
    public CustomerTransObject() {
        PageFactory.initElements(getDriver(), this);
    }
    public void loginCRM(String username, String password) {
        username_field.sendKeys(username);
        password_field.sendKeys(password);
        submit_button.click();
        sleep(10000);

    }

    public void Customer() {
        sleep(3000);
        CRM_menu.click();
        sleep(2000);
        Customer_menu.click();
        sleep(5000);
    }

    public void UserClickOnAddButton() {

        Add_button.click();
        sleep(3000);
    }

    public void addCus() {
        dataPicker = commonUtils.generateRandomString(5);
        customerName = "user " + dataPicker;
        CustomerName_field.sendKeys(customerName);
    }

    public void setCustomerType_dropdown() {
        CustomerType_dropdown.click();
        First_Option.click();

    }

    public void setAddress_Field() {
        Address_Field.sendKeys("Kolkata new bazaar");
        sleep(2000);
    }

    public void setCountry_Dropdown() {
        Country_Dropdown.click();
        sleep(3000);

    }

    public void setIndia_Option() {
        India_Option.click();


    }

    public void setState_Dropdown() {
        State_Dropdown.click();
    }

    public void setAndhraPradesh_Option() {
        AndhraPradesh_Option.click();


    }

    public void setCity_Dropdown() {
        City_Dropdown.click();
    }

    public void setAdilabad_Option() {
        Adilabad_Option.click();
        sleep(3000);
    }

    public void setPhoto_upload() {
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath + "....................>");
        sleep(3000);
        Photo_upload.sendKeys(rootPath + "/src/main/resources/Files/1640076584197.jpg");
        sleep(3000);
    }

    public void setPhone_Field() {
        Phone_Field.sendKeys("9875678767");
        sleep(1000);
    }

    public void setTimeZone_Dropdown() {
        TimeZone_Dropdown.click();
        sleep(1000);
        First_Option.click();
        sleep(1000);
        WebElement freeSpace = getDriver().findElement(By.xpath("//label[text()=\"Date\"]"));
        actions.moveToElement(freeSpace).click().perform();

    }

    public void setCalender_button() {
        sleep(2000);
        Calender_button.click();
        sleep(2000);
        CurrentDate.click();
        sleep(2000);
    }

    public void setEmail_Field() {
        Email_Field.sendKeys("testuser@test.com");
        sleep(1000);
    }

    public void setDocument_upload() {
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath + "....................>");
        Document_upload.sendKeys(rootPath + "/src/main/resources/Files/samplePDF.pdf");
        sleep(3000);
    }

    public void setVideo_upload() {
        String rootPath = System.getProperty("user.dir");
        System.out.println(rootPath + "....................>");
        Video_upload.sendKeys(rootPath + "/src/main/resources/Files/dummymp4.mp4");
        sleep(5000);
    }

    public void setGender_Dropdown() {
        Gender_Dropdown.click();
        sleep(1000);
        First_Option.click();
        sleep(2000);
    }

    public void setSave_button() {
        Save_button.click();
        sleep(10000);

    }

    public void setSearch_bar() {
        sleep(2000);
        Search_bar.click();
        Search_bar.sendKeys(customerName);
        sleep(6000);
        String isCustomerName_xpath = ("//div[text()='" + customerName + "']");
        WebElement isCustomerName = getDriver().findElement(By.xpath(isCustomerName_xpath));
        String isStatusActive_xpath = ("//div[text()='" + customerName + "']//following-sibling::div[5]//button[text()='Active']");
        WebElement isStatusActive = getDriver().findElement(By.xpath(isStatusActive_xpath));
        Assert.assertTrue(isCustomerName.isDisplayed() && isStatusActive.isDisplayed());


    }

    public void sleep(long s) {
        try {
            Thread.sleep(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}







