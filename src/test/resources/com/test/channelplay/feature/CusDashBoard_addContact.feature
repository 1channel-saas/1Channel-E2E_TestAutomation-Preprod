@currentFile
Feature: Add a new Contact from customer Dashboard

  Background:
    Given after login to crm user will be on the Customer page under CRM menu
    And user select CRM and Click on Customer submenu

  Scenario: Add a new contact with all fields from customer dashboard
    When user Click on dashboard button
    And user click on plussign contact button
    And Contact_user Enter contact Name
    And select ContactType dropdown and click first option
    And enter designation
    And enter mobile number under contact under customer dashboard
    And enter contact email
    And enter contact address
    And select contact Country
    And select contact State
    And select contact City
    And select data list group
    And select company test from dropdown list
    And enter company phone number
    And select date
    And upload certificate
    And upload video
    And select Data List Module from dropdown
    And enter Company Registered Email Test
    Then click contact save button
#    And user click CRM menu and Click on contact menu
#    Then user check contact will added successfully with active status or not
#    And then close












