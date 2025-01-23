
Feature: Login to CRM portal

  Background:
    Given User is on the login page


    Scenario Outline: User can Successfully login on CRM Portal with different users
      When User enter email "<username>"
      And User enter password "<password>"
      And User click on SignIn button
      Then User will be on the homepage

      Examples:
        |username|password|
        |cowork@mailinator.com|12345678|
        |pritamatta5524@gmail.com|12345678|
        |taufik@1channel.ai|test1234|



  Scenario Outline: Login with valid and Invalid Credentials - valid username and invalid password ,
                    invalid username and valid password , invalid username and invalid password

    When User enters valid "<username>" and invalid "<password>"
    And  User click on SignIn button
    Then User should not logged in and Validation message should be shown as "Username or Password is incorrect."

    Examples:
      |username|password|
      |cowork@mailinator.com|1234678|
      |cowok@mailinator.com|12345678|
      |cowok@mailinator.com|1245678|