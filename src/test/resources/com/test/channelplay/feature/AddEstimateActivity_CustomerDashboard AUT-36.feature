@AUT36
Feature: Add new Estimate Activity from Customer Dashboard page

  Background:
    Given user loggedIn to Assistive project under Estimate activity

  Scenario: Add Estimate from customer dashboard
    When  user clicks on menu CRM and submenu Customers
    Then user is on Customers page

    And click on Actions Dashboard button of any customer
    And click on plus button
    And click on Estimate and landed on Add New Estimate page
    And select Customer from dropdown under Estimate
    And select opportunity from dropdown under Estimate
    And enter update opportunity value under Estimate
    And select update opportunity status under Estimate
    And select update Exp. closure date from calendar under Estimate
    And select update Win probability from dropdown under Estimate
    And select contact under Estimate
    And fill data in Product Form table
    Then validate new Estimate is created under Estimate
    Then validate new Estimate in Activities page under Estimate