#@currentFile
Feature: Add a new Contact

  Background:
    Given after login to crm user will be on the Contact page under CRM
    And user select CRM and Click on Contact

  Scenario: Add contact with all fields
   When User Click on Add contact button
    And user Enter contact Name
    And user select ContactType dropdown and click first option
    And user select assign customer
    And user enter designation
    And user enter mobile number
    And user enter contact email
    And user enter contact address
    And user select contact Country
    And user select contact State
    And user select contact City
    And user select data list group
    And user select company test from dropdown list
    And user enter company phone number
    And user select date
    And user upload certificate
    And user upload video
    And user select Data List Module from dropdown
    And user enter Company Registered Email Test
    And user click contact save button
    Then check contact will added successfully with active status












