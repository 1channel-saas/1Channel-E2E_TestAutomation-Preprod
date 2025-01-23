@AUT25
Feature: Assistive Analytics -> Reports -> Daily Activity Report

  Background:
    Given user logged in to Assistive
    When clicks on menu Analytics and submenu Reports then Daily Activity Report


  Scenario: Validate data of Daily Activity Report with specific date range selected from calendar
    When user selects date range from Calendar
    And click on apply button
    Then Reports to be shown
    Then click on Export button excel get downloaded