package com.test.channelplay.utils;


public class Constants {

    private Constants() {}
    
    
    public static final long TIMINGS_EXPLICIT_TIMEOUT = 20;
    public static final long TIMINGS_IMPLICIT_TIMEOUT = 10;


    //  ## Common Xpath

    //  # Menu
    public static final String Analytics_menu = "//span[text()=' Analytics ']";
    public static final String Admin_menu = "(//span[text()=' Admin '])[1]";
    public static final String CRM_menu = "(//span[text()=' CRM '])[1]";
    public static final String Settings_menu = "//span[text()=\" Settings \"]";


    //  # Settings SubMenu
    public static final String Settings_System_submenu = "//span[text()=' System ']";
    public static final String Settisngs_System_AccountSetup_submenu = "//span[text()=' Account Setup ']";


    //  # Admin SubMenu
    public static final String AdminActivities_subMenu = "(//span[text()=' Activities '])[2]";


    //  # CRM SubMenu
    public static final String CRMCustomers_submenu = "(//span[contains(text(), 'Customers') or contains(text(), 'Retailers') or contains(text(), 'Leads')])[1]";
    public static final String CRMActivities_subMenu = "(//span[text()=' Activities '])[1]";




    //  Emailer xpath
    public static final String mailer_HomeSignIn_button = "//div[@class='dropdown']/following-sibling::div[@class='button-group']//span[contains(text(), 'Sign in')]";
    public static final String mailer_EnterEmailId = "//input[@id='i0116' and @type='email']";
    public static final String mailer_EnterEmailId_Next_button = "//input[@type='submit' and @id='idSIButton9']";
    public static final String mailer_EnterPassword = "//input[@type='password' and @id='i0118' and @name='passwd']";
    public static final String mailer_SignIn_button = "//input[@type='submit' and @id='idSIButton9' and @value='Sign in']";
    public static final String staySignedIn_Yes_button = "//input[@type='submit' and @id='idSIButton9' and @value='Yes']";
    public static final String mailer_Logo = "//img[@id = 'O365_MainLink_TenantLogoImg']";
    public static final String mailer_Inbox = "//span[text()='Inbox']";

}


	    

