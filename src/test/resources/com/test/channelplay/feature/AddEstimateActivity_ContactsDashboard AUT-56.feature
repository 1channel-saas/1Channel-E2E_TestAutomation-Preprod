@AUT56
Feature: Add new Estimate Activity from Contacts Dashboard page

  Background:
    Given user loggedIn to Assistive project under Contacts Estimate activity

  Scenario: Add Estimate from contacts dashboard
    When  user clicks on menu CRM and submenu Contacts
    Then user is on Contacts page

    And click on actions dashboard button of any Contact
    And click on plus button under Contacts Estimate
    And click on Estimate and landed on Add New Estimate page under Contacts Estimate
    And select Customer from dropdown under Contacts Estimate
#    And select opportunity from dropdown under Contacts Estimate
#    And enter update opportunity value under Contacts Estimate
#    And select update opportunity status under Contacts Estimate
#    And select update Exp. closure date from calendar under Contacts Estimate
#    And select update Win probability from dropdown under Contacts Estimate
    And select contact under Contacts Estimate
#    And fill data in Product Form table under Contacts Estimate
#    Then validate new Estimate is created under Contacts Estimate
#    Then validate new Estimate in Activities page under Contacts Estimate