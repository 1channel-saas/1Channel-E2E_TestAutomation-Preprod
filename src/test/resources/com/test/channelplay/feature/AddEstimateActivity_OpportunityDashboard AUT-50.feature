@AUT50
Feature: Add new Estimate Activity from Opportunity Dashboard page

  Background:
    Given user loggedIn to Assistive project under opportunity Estimate activity

  Scenario: Add Estimate from opportunity dashboard
    When  user clicks on menu CRM and submenu Opportunities
    Then user is on Opportunities page

    And click on actions dashboard button of any Opportunity
    And click on plus button under opportunity Estimate
    And click on Estimate and landed on Add New Estimate page under opportunity Estimate
    And check select customer is locked and customer name is displayed
    And check select opportunity is locked and opportunity name is displayed
    And enter update opportunity value under opportunity Estimate
    And select update opportunity status under opportunity Estimate
    And select update Exp. closure date from calendar under opportunity Estimate
    And select update Win probability from dropdown under opportunity Estimate
    And select contact under opportunity Estimate
    And fill data in Product Form table under opportunity Estimate
    Then validate new Estimate is created under opportunity Estimate
    Then validate new Estimate in Activities page under opportunity Estimate