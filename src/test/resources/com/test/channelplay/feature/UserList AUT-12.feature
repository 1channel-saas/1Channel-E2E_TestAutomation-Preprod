@AUT12
Feature: Assistive Admin -> Users

  Background:
    Given user loggedIn to Assistive project under Admin User

  Scenario: Add Users
    When  user clicks on menu Admin and submenu Users
    Then user is on Users page

    And clicks on Add button opens Add new user page
    And  fill data into FirstName and LastName
    And  enter email id in email field
    And  enter mobile number
    And  select User Role from dropdown
    And  select reports to from dropdown
    And  click on checkbox of set password
    And  enter password in password checkbox
    And  clicks on Save button
    Then newly created user name should show in the list