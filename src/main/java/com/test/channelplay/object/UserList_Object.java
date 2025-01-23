package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import com.test.channelplay.utils.GetProperty;
//import org.apache.tools.ant.util.LeadPipeInputStream;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class UserList_Object extends DriverBase {

    CommonUtils commonutils = new CommonUtils();


    @FindBy(xpath = "//ul[@class=\"menu-nav\"]//following-sibling::li/a//descendant::span[text()=\" Admin \"]")
    WebElement Admin_menu;

    @FindBy(xpath = "//li/a[@href=\"/users\"]/span[text()=\" Users \"]")
    WebElement Users_subMenu;

    @FindBy(xpath = "//div/h5[text()=\" Users \"]")
    WebElement users_title;

    @FindBy(xpath = "//div/button/span[text()=\"Add\"]")
    WebElement Add_button;

    @FindBy(xpath = "//div/h4[@class=\"modal-title\" and text()=\"Add New User\"]")
    WebElement addNewUser_title;

    @FindBy(xpath = "//div/label[text()=\"First Name\"]/parent::div/following-sibling::div//child::input")
    WebElement firstName_field;

    @FindBy(xpath = "//div/label[text()=\"Last Name\"]/parent::div/following-sibling::div//child::input")
    WebElement lastName_field;

    @FindBy(xpath = "//div/label[text()=\"Work Email Address\"]/parent::div/following-sibling::div//child::input")
    WebElement work_email_field;

    @FindBy(xpath = "//div/label[text()=\"Mobile Number\"]/parent::div/following-sibling::div//child::input")
    WebElement mobile_no_field;

    @FindBy(xpath = "//div/label[text()=\"User Role\"]/parent::div/following-sibling::div//child::mat-select/div/div")
    WebElement User_Role_dropdown;

    @FindBy(xpath = "//mat-option/span[@class=\"mat-option-text\" and text()=\" Admin \"]")
    WebElement User_Role_dropdown_admin;

    @FindBy(xpath = "//mat-option/span[@class=\"mat-option-text\" and text()=\" User \"]")
    WebElement User_Role_dropdown_user;

    @FindBy(xpath = "//div/label[text()=\"Reports To\"]/parent::div/following-sibling::div//child::mat-select/div/div[@class=\"mat-select-value\"]")
    WebElement Reports_To_dropdown;

    @FindBy(xpath = "//div[@class=\"cdk-overlay-pane\"]/div/div//following-sibling::mat-option")
    WebElement Reports_To_dropdown_options;

    @FindBy(xpath = "//label[@class=\"mat-checkbox-layout\"]/div")
    WebElement Set_password_checkbox;

    @FindBy(xpath = "//div/label[text()=\"Password\"]/parent::div/following-sibling::div/mat-form-field//child::input")
    WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    private WebElement save_button;

    @FindBy (xpath = "//div/input[@placeholder=\"Search\"]")
    WebElement search_field;



    public String dataPicker;
    public String userFirstName, userLastName, user_email;

    public UserList_Object() {
        PageFactory.initElements(getDriver(), this);
    }

    public void User_clicks_on_menu_admin_and_submenu_users() {
        Admin_menu.click();
        Users_subMenu.click();
    }

    public boolean User_is_on_users_page() {
        boolean usersTitle = users_title.isDisplayed();
        return usersTitle;
    }

    public void Clicks_on_add_button_opens_add_new_user_page() {
        Add_button.click();
        addNewUser_title.isDisplayed();
        commonutils.sleep(2000);
    }

    public void Fill_data_into_first_name_and_last_name() {
        dataPicker = commonutils.generateRandomString(5);
        userFirstName = "FirstName" + dataPicker;
        userLastName = "LastName" + dataPicker;

        firstName_field.sendKeys(userFirstName);
        lastName_field.sendKeys(userLastName);
    }

    public void Enter_email_id_in_email_field() {
        dataPicker = commonutils.generateRandomString(5);
        user_email = "email" + dataPicker + "@crm.com";
        work_email_field.sendKeys(user_email);
    }

    public void Enter_mobile_number(String mob_no) {
        mobile_no_field.sendKeys(mob_no);
    }

    public void Select_user_role_from_dropdown() {
        User_Role_dropdown.click();
        User_Role_dropdown_user.click();
        commonutils.sleep(3000);
    }

    public void Select_reports_to_from_dropdown() {
        Reports_To_dropdown.click();
        List<WebElement> repTo_dropdown_ele = getDriver().findElements(By.xpath("//div[@class=\"cdk-overlay-pane\"]/div/div//following-sibling::mat-option['\" +i+ \"']/span"));

        for (WebElement repTo_dropdown_ele_list : repTo_dropdown_ele) {

            String repToDropdown_values = repTo_dropdown_ele_list.getText();

            if (repToDropdown_values.contentEquals("david - david@boranora.com")) {
                repTo_dropdown_ele_list.click();
                commonutils.sleep(2000);
                System.out.println("repTo dropdown values inside loop: " +repToDropdown_values+"------------");
                break;
            }
            System.out.println("repTo dropdown values outside loop: " +repToDropdown_values+"------------");
        }
    }

    public void Click_on_checkbox_of_set_password() {
        Set_password_checkbox.click();
        commonutils.sleep(2000);
    }

    public void Enter_password_in_password_checkbox() {
        password_field.sendKeys("1");
        commonutils.sleep(2000);
    }

    public void Clicks_on_save_button() {
        save_button.click();
        commonutils.sleep(6000);
    }

    public void NewlyCreatedUsernameShouldShowInTheList() {
        String firstName = userFirstName;
        String lastName = userLastName;
        String userFullNameStr = firstName + " " + lastName;
        search_field.sendKeys(userFullNameStr);
        commonutils.sleep(2000);
        WebElement Search_rowsName = getDriver().findElement(By.xpath("//div[@ref=\"eViewport\" and @role=\"presentation\"]/div/div['\" +i+ \"']//child::div[@col-id=\"1\"]"));
        boolean Search_rowsName_isDisplayed = Search_rowsName.isDisplayed();
        Assert.assertTrue(Search_rowsName_isDisplayed);
        System.out.println("New user name: " +userFullNameStr+"---------");
    }

}
