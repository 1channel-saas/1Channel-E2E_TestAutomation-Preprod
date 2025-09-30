@web @E2E
@activity
@useCase_Scenarios
Feature: Create Linked field with field type - Date on activity module


  Background:
    Given User launches 1Channel CRM portal


  @useCase_Scenario_b2b-2943
  Scenario: To validate whether user able to create linked field of Date type in Activity module

#  API (to fetch singularName of Company field)
  When user submit "loginAPI" with "POST" request for login with testUser creds
  Then add request for getCompanySettings API
  Then submit "getCompanySettings_AccSetUp" with "POST" request for b2b-2943
  Then validate API call is success with status code 200 or handle error
  And validate ApiResponse execution time
  And validate response data to get singularName

#  API (to create new test activity)
  Given user submit "loginAPI" with "POST" request for login with testUser creds
  When add request for createActivitiesSettings
  Then user submit "createActivitiesSettings" with "POST" request for createActivitiesSettings
  Then validate API call is success with status code 200 or handle error
  Then validate createActivitiesSettings response

#  UI
  When User logs in with test credentials
  Then User clicks on Admin menu and Activities submenu and navigates to Activities page
  And Enter into "custom Activity" page and clicks on Add Field button to create a new date type linked field