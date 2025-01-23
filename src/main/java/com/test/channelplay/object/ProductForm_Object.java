package com.test.channelplay.object;

import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.DriverBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class    ProductForm_Object extends DriverBase {

    @FindBy(xpath = "//span[text()=\" Admin \"]")
    WebElement Admin_menu;

    @FindBy(xpath = "//span[text()=\" Product Form \"]")
    WebElement ProductForm_submenu;

    @FindBy(xpath = "//span[text()=\"Add New\"]")
    WebElement AddNew_button;

    @FindBy(xpath = "//label[text()=\"Form Name\"]/parent::div/following-sibling::div/descendant::input")
    WebElement AddFormName_field;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    WebElement Save_button;

    @FindBy(xpath = "//div/a/img[@src=\"./assets/media/svg/icons/Navigation/Angle-double-left.svg\"]")
    WebElement Back_button;

    @FindBy(xpath = "//input[@placeholder=\"Search\"]")
    WebElement Search_field;

    @FindBy(xpath = "//a/img[@title=\"Edit\"]")
    WebElement edit_newForm;

    @FindBy(xpath = "//span[text()=\"Add Field\"]")
    WebElement AddField_button;

    @FindBy(xpath = "//label[text()=\"Field Name\"]/parent::div/following-sibling::div/descendant::input")
    WebElement FieldName_field;

    @FindBy(xpath = "//label[text()=\"Field Instructions\"]")
    WebElement FieldInstruction_Field;

    @FindBy(xpath = "//label[text()=\"Field Type\"]/parent::div/following-sibling::div/descendant::div[@class=\"mat-select-value\"]")
    WebElement FieldType_dropdown;

    @FindBy(xpath = "//span[text()=\" Numeric \"]")
    WebElement FieldType_dropdown_Numeric;

    @FindBy(xpath = "//span[text()=\" Calculated Field \"]")
    WebElement FieldType_dropdown_CalculatedField;

    @FindBy(xpath = "//label[text()=\"Mandatory\"]/parent::div/following-sibling::div//child::label/div")
    WebElement Mandatory_checkbox;

    @FindBy(xpath = "//button[text()=\"Save\"]")
    WebElement Save_Formfield_button;

    @FindBy(xpath = "//label[text()=\"Calculation logic\"]/parent::div/following-sibling::div/descendant::input")
    WebElement CalculationLogic_Field;

    @FindBy(xpath = "//span[text()=\" Quantity \"]")
    WebElement cal_a;

    @FindBy(xpath = "//span[text()=\" List Price \"]")
    WebElement cal_b;

    @FindBy(xpath = "//span[text()=\" Tax% \"]")
    WebElement cal_c;

    @FindBy(xpath = "//span[text()=\" * \"]")
    WebElement cal_d;

    @FindBy(xpath = "//span[text()=\" - \"]")
    WebElement cal_e;

    @FindBy(xpath = "//span[text()=\" + \"]")
    WebElement cal_f;

    @FindBy(xpath = "//span[text()=\" / \"]")
    WebElement cal_g;

    @FindBy(xpath = "//span[text()=\" ( \"]")
    WebElement cal_h;

    @FindBy(xpath = "//span[text()=\" ) \"]")
    WebElement cal_i;

    @FindBy(xpath = "//span[text()=\"Test Data\"]")
    WebElement TestData_button;

    @FindBy(xpath = "//label[text()=\"Quantity\"]/parent::div/following-sibling::div/descendant::input")
    WebElement TestData_Quantity_Field;

    @FindBy(xpath = "//label[text()=\"List Price\"]/parent::div/following-sibling::div/descendant::input")
    WebElement TestData_ListPrice_Field;

    @FindBy(xpath = "//label[text()=\"Tax%\"]/parent::div/following-sibling::div/descendant::input")
    WebElement TestData_Tax_Field;

    @FindBy(xpath = "//button[text()=\"Calculate\"]")
    WebElement TestData_Calculate_button;

    @FindBy(xpath = "//label[text()=\"Total\"]/parent::div/following-sibling::div/descendant::input")
    WebElement Total_Field;

    @FindBy(xpath = "//button[text()=\"Calculate\"]/preceding-sibling::button")
    WebElement Cancel_button;



    CommonUtils commonutils = new CommonUtils();
    public String ProductFormName;
    public String dataPicker;

    public ProductForm_Object() {
        PageFactory.initElements(getDriver(), this);
    }

    public void Clicks_on_menu_Admin_and_submenu_Product_Form() {
        commonutils.sleep(3000);
        Admin_menu.click();
        ProductForm_submenu.click();
    }

    public void Clicks_on_Add_New_button_opens_Add_Form_window() {
        AddNew_button.click();
    }

    public void User_enters_form_name() {
        dataPicker = commonutils.generateRandomString(5);
        ProductFormName = "ProductForm " + dataPicker;
        AddFormName_field.sendKeys(ProductFormName);
        commonutils.sleep(2000);
    }

    public void Click_on_save_button() {
        Save_button.click();
        commonutils.sleep(3000);
    }

    public void Click_on_back_button_to_go_to_product_form_home() {
        Back_button.click();
        commonutils.sleep(3000);
    }

    public void Validate_new_form_is_present(){
        Search_field.click();
        commonutils.sleep(2000);
        Search_field.sendKeys(ProductFormName);
        commonutils.sleep(3000);
        String isProductFormName_xpath = ("//div/span[contains(text(), '" + ProductFormName + "')]");
        WebElement isProductFormName = getDriver().findElement(By.xpath(isProductFormName_xpath));
        Assert.assertTrue(isProductFormName.isDisplayed());
        Search_field.clear();
        commonutils.sleep(2000);
    }

    public void User_clicks_on_edit_button_of_new_form() {
        edit_newForm.click();
        commonutils.sleep(3000);

/*        String edit_Field_newForm_xpath = ("//div/span[contains(text(), '" + ProductFormName + "')//parent::div/parent::div/following-sibling::div[2]//child::img[@title='Edit']");
        WebElement edit_Field_newForm = getDriver().findElement(By.xpath(edit_Field_newForm_xpath));
        edit_Field_newForm.click();
        commonutils.sleep(3000); */
    }

    public void Click_on_add_field_button_from_new_form_page() {
        AddField_button.click();
        commonutils.sleep(2000);
    }

    public void AddNewFormFieldQuantityOfTypeNumeric(String Field_name) {
        FieldName_field.sendKeys(Field_name);
        FieldType_dropdown.click();
        FieldType_dropdown_Numeric.click();
        Mandatory_checkbox.click();
        Save_Formfield_button.click();
        commonutils.sleep(3000);
        boolean verifyFieldName_Quantity = getDriver().findElement(By.xpath("//span[text()=\"Quantity\"]")).isDisplayed();
        Assert.assertTrue(verifyFieldName_Quantity);
        commonutils.sleep(3000);
    }

    public void AddNewFormFieldTaxOfTypeNumeric(String Field_name) {
        FieldName_field.sendKeys(Field_name);
        FieldType_dropdown.click();
        FieldType_dropdown_Numeric.click();
        Mandatory_checkbox.click();
        Save_Formfield_button.click();
        commonutils.sleep(3000);
        boolean verifyFieldName_Tax = getDriver().findElement(By.xpath("//span[text()=\"Tax%\"]")).isDisplayed();
        Assert.assertTrue(verifyFieldName_Tax);
        commonutils.sleep(3000);
    }

    public void AddNewFormFieldTotalOfTypeCalculatedField(String Field_name) {
        FieldName_field.sendKeys(Field_name);
        FieldType_dropdown.click();
        FieldType_dropdown_CalculatedField.click();

        //Enter values in Calculation Logic expression

        CalculationLogic_Field.click();
        CalculationLogic_Field.sendKeys("(");
        cal_h.click();
        cal_a.click();
        CalculationLogic_Field.sendKeys("*");
        cal_d.click();
        CalculationLogic_Field.sendKeys("L");
        cal_b.click();
        CalculationLogic_Field.sendKeys(")");
        cal_i.click();
        CalculationLogic_Field.sendKeys("*");
        cal_d.click();
        CalculationLogic_Field.sendKeys("(");
        cal_h.click();
        CalculationLogic_Field.sendKeys("1");
        FieldInstruction_Field.click();                         // we just need to click anywhere else for system to accept "+" string within logic expression
        CalculationLogic_Field.sendKeys("+");
        cal_f.click();
        CalculationLogic_Field.sendKeys("T");
        cal_c.click();
        CalculationLogic_Field.sendKeys("/");
        cal_g.click();
        CalculationLogic_Field.sendKeys("100");
        FieldInstruction_Field.click();
        CalculationLogic_Field.sendKeys(")");
        cal_i.click();
        FieldInstruction_Field.click();
        commonutils.sleep(2000);


        //Test Data validation

        TestData_button.click();
        commonutils.sleep(2000);
        TestData_Quantity_Field.sendKeys("10");
        TestData_ListPrice_Field.sendKeys("1000");
        TestData_Tax_Field.sendKeys("10");
        TestData_Calculate_button.click();
        commonutils.sleep(2000);
        boolean testData_total = Total_Field.isDisplayed();
        Assert.assertTrue(testData_total);
        commonutils.sleep(2000);
        Cancel_button.click();
        commonutils.sleep(2000);
        Mandatory_checkbox.click();
        Save_Formfield_button.click();
        commonutils.sleep(3000);
        boolean verifyFieldName_Total = getDriver().findElement(By.xpath("//span[text()=\"Total\"]")).isDisplayed();
        Assert.assertTrue(verifyFieldName_Total);
        commonutils.sleep(3000);
    }

}
