package com.test.channelplay.object;

import com.test.channelplay.utils.DriverBase;
import io.cucumber.java8.Pa;
//import org.apache.tools.ant.taskdefs.Sleep;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class AddProductObject extends DriverBase {

    @FindBy(xpath = "//input[@formcontrolname=\"email\"]")
    private WebElement email_field;

    @FindBy(xpath = "//input[@formcontrolname=\"password\"]")
    private WebElement password_field;

    @FindBy(xpath = "//button[text()=\"Sign In\"]")
    private WebElement SignIn_button;

    @FindBy(xpath = "//li//span[text()=\" Admin \"]")
    private WebElement Admin_Menu;

    @FindBy(xpath = "//span[text()=\" Admin \"]/ancestor::li//span[text()=\" Products \"]")
    private WebElement Products_Menu;

    @FindBy(xpath = "//span[text()=\"Add\"]")
    private  WebElement Add_Button;

    @FindBy(xpath = "//label[text()=\"Product Name\"]/parent::div/following-sibling::div//input")
    private WebElement ProductName_Field;

    @FindBy(xpath = "//label[text()=\"Product Code\"]/parent::div/following-sibling::div//input")
    private WebElement ProductCode_Field;

    @FindBy(xpath = "//label[text()=\"Description\"]/parent::div/following-sibling::div//textarea")
    private WebElement Decription_Field;

    @FindBy(xpath = "//label[text()=\"Price\"]/parent::div/following-sibling::div//input")
    private WebElement Price_Field;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    private WebElement Save_Button;

    @FindBy(xpath = "//input[@placeholder=\"Search\"]")
    private WebElement Search_Field;

    public AddProductObject(){
        PageFactory.initElements(getDriver(),this);
    }

    public String ProductValueHolder;


    public void login(String username,String password){
        email_field.sendKeys(username);
        password_field.sendKeys(password);
        SignIn_button.click();
        sleep(8000);
        Admin_Menu.click();
        Products_Menu.click();
    }

    public void UserClickOnAddButton() {
        Add_Button.click();
        sleep(4000);
    }

    public void UserWillEnterAProductName(){
        ProductValueHolder = "Apple";
        ProductName_Field.sendKeys(ProductValueHolder);
    }
    public void UserWillEnterAProductCode(){
        ProductCode_Field.sendKeys("p-101");
    }
    public void UserWillEnterDescription(){
        Decription_Field.sendKeys("hi there how are you?");
    }
    public void UserWillEnterPrice(){
        Price_Field.sendKeys("60");
    }
    public void UserWillClickOnSaveButton(){
        Save_Button.click();
        sleep(6000);
    }
    public void ProductWillBeAddedSuccessfullyAndWillShowInTheList(){
        Search_Field.sendKeys(ProductValueHolder);
        sleep(3000);
        String ActualProductName = getDriver().findElement(By.xpath("//div[text()='"+ProductValueHolder+"']")).getText();
        Assert.assertEquals(ProductValueHolder,ActualProductName);
    }

    public void sleep(long s){
        try{
            Thread.sleep(s);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
