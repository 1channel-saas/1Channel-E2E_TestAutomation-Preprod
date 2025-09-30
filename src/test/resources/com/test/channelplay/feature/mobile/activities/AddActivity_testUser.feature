@web @mobile @E2E
@AddActivity_testUser
Feature: Add Activity Test to verify user is able to add activity successfully


  @offsiteActivity
  Scenario Outline: Add OffSide Activity Test

##    AppUI
    Given User logIn to CRM mobile App with testUser creds
    When Clicks on Activities menu for offsiteActivity
    Then Clicks on offsite activity option for offsiteActivity
    Then Clicks on Add button to add new offsite activity for offsiteActivity
    Then select customer "<Customer_Name>" from Select Customer dropdown for offsiteActivity
    Then Clicks on OK button on customer selection frame for offsiteActivity
    Then Enter text into description box for offsiteActivity
    Then Enter name into title "<titleField>" field for offsiteActivity
    Then Select date "<performDate>" in perform Date field for offsiteActivity
    And add image in image field for offsiteActivity
    Then click on Save to submit offsite activity
    And verify Activity is showing with "<Customer_Name>" in list and fetch activity details for validation for offsiteActivity

##    PortalUI
    Given User launches 1Channel CRM portal
    When User logs in with test credentials
    Then User clicks on CRM menu and Activities submenu to validate offsiteActivity
    Then clicks on Filter and search with serialNo of offsiteActivity fetched from App
    Then clicks on Edit activity and validate data then delete the activity

##    API
#    Then create request for getActivityLookupView api with "<projectId>" of offsiteActivity
#    And submit "getActivityLookupView" with "GET" request to fetch activity details for offsiteActivity
#    Then validate API call is success with status code 200 or handle error
#    Then validate ApiResponse execution time

    Examples:
      |Customer_Name|titleField|performDate|
      |rest@3       |Title     |           |