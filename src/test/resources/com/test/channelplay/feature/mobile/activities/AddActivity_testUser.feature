@web @mobile @E2E
@AddActivity_testUser
Feature: Add Activity Test to verify user is able to add activity successfully from App and then verify in Portal,
          Create activity from API and verify in DB and then delete activity from API



  @offsiteActivity
  Scenario Outline: Add OffSide Activity Test

##    AppUI (submit activity from mobile)
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
    Then print template analytics report

##    PortalUI (verify created activity in portal)
    Given User launches 1Channel CRM portal
    When User logs in with test credentials
    Then User clicks on CRM menu and Activities submenu to validate offsiteActivity
    Then clicks on Filter and search with serialNo of offsiteActivity fetched from App
    Then clicks on Edit activity and validate data then delete the activity

##    API (submit activity from api)
    Given user submit "loginAPI" with "POST" request for login with testUser creds
    When create request for uploadFieldImage api for offsiteActivity
    Then submit "uploadFieldImage" api with "POST" request to fetch image path for offsiteActivity
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate "message" is "SUCCESS" in responseBody
    And fetch uploaded image name from responseBody to use in request of addEditActivity for offsiteActivity
    When create request for addEditActivity api for offsiteActivity
    Then submit "addEditActivity" api with "POST" request to add offsiteActivity
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate "message" is "SUCCESS" in responseBody
    Then verify activityId and serialKey is present in responseBody

##    DB
    Then verify whether created activity is present in database "channelplay_aurora.b2b_activity_master" table for offsiteActivity
    And validate created activity is Active and created date is today and trans unique key is generated in database for offsiteActivity

##    API (delete activity)
    Then submit "deleteActivityData" api with "DELETE" request to delete created offsite activity
    Then validate API call is success with status code 200 or handle error
    And validate "message" is "SUCCESS" in responseBody
    And validate "responseData" contains partialText "deleted successfully" in responseBody

    Examples:
      |Customer_Name|titleField|performDate|
      |rest@3       |Title     |           |