@AUT61
Feature: Add new Estimate Activity from CRM Activities page

  Background:
    Given user loggedIn to Assistive project under CRM Activities

  Scenario: Add and validate Estimate from CRM Activities page with date range
    When  user clicks on menu CRM and submenu Activities
    Then user is on Activities page

    And user select date range from calendar and click on save button under CRM Activities
    And check total number of records from the activity list under CRM Activities
    And click on Add Activity button under CRM Activities
    And click on Estimate and landed on Add New Estimate page under CRM Activities
    And select customer from dropdown under CRM Activities
    And select opportunity from dropdown under CRM Activities
    And enter update opportunity value under CRM Activities
    And select update opportunity status under CRM Activities
    And select update Exp. closure date from calendar under CRM Activities
    And select update Win probability from dropdown under CRM Activities
    And select contact under CRM Activities
    And fill data in Product Form table and click on Save button under CRM Activities
    Then validate new created activity in list