@AUT26
Feature: Assistive Analytics -> Reports -> Detailed Activity Report

  Background:
    Given user LoggedIn to Assistive To Test Detailed Activity Report
    And clicks on menu Analytics and submenu Reports then Detailed Activity Report


  Scenario: Create detailed activity report with date range,activity and performedBy And Validate report data using Filters of Date Range
    When Filters interface opens under detailed activity report
    And click on Cancel button of Filter interface
    Then user is on Detailed Activity Report Page
    When click on Filter icon under detailed activity report
    And enter start date under Date Range field
    And enter end date under Date Range field
    And select value from Activity dropdown
    And select value from Performed By dropdown
    And click on apply button under Filters interface of detailed activity report
    Then validate actual result with expected results