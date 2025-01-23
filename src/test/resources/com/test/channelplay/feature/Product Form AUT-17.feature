@AUT17
Feature: Assistive Admin -> Product Form

  Background:
    Given user logged in to Assistive project
    When clicks on menu Admin and submenu Product Form


  Scenario: Add New Product Form with numeric type Fields and calculation logic type Field

    When clicks on Add New button opens Add Form window
    And user enters Form name
    And click on Save button
    Then click on back button to go to Product Form Home
    And validate new Form is present
    Then user clicks on Edit button of new Form
    And click on Add Field button from new Form page
    And Add new Form field Quantity of type numeric
    And click on Add Field button from new Form page
    And Add new Form field Tax% of type numeric
    And click on Add Field button from new Form page
    Then Add new Form field Total of type calculated field