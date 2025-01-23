Feature: Add product to CRM Portal

  Background:
   Given After login the user will be on the Products page under Admin Menu
    And User click on Add Button

    Scenario: User will add a new Product
      When user will enter a Product Name
      And user will enter a Product Code
      And user will enter Description
      And user will enter Price
      And user will click on Save Button
      Then Product will be added Successfully and will show in the list


