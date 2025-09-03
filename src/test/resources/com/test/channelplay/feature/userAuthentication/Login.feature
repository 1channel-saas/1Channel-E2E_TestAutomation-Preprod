@CRMPortal
@userAuthentication
Feature: CRM Portal User Authentication


  Background:
    Given User launches 1Channel CRM


  @loginAuth
  Scenario Outline: Successful login with valid credentials

    When User enters email "<username>"
    And User enters password "<password>"
    And User clicks on SignIn button
    Then User should be loggedIn successfully and redirected to assistant page
    And User profile should be displayed
    Then User logs Out from portal

    Examples:
    |username|password|
    |prodTest@mailinator.com|12345678|




  @loginFieldValidation
  Scenario Outline: Login field validation

    When User enters email "<username>"
    And Clicks outside the Username field
    Then Username field validation "<validation_result>" should be triggered

    Examples:
      |username         |validation_result |
      |invalidemail     |Field is not valid|
      |user@            |Field is not valid|
      |@domain.com      |Field is not valid|
      |user @domain.com |Field is not valid|
      |test..@domain.com|Field is not valid|
      |prdTe@@.com      |Field is not valid|




  @loginNegativeTest
  Scenario Outline: Failed login with invalid credentials

    When User enters email "<username>"
    And User enters password "<password>"
    And User clicks on SignIn button
    Then User should remain on login page
    And Error message "<err_message>" should be displayed and validate with "<test_description>"

    Examples:
      |username               |password|err_message                                |test_description                 |
      |testemailqa@1channel.co|wrongPwd|Incorrect email address or password entered|Valid user and invalid password  |
      |prdTst@mailinator.com  |12345678|Invalid username or password.              |Invalid user and valid password  |
      |prdTst@mailinator.com  |1234    |Invalid username or password.              |Invalid user and invalid password|
      |                       |1245678 |Required field                             |Empty username                   |
      |prodTest@mailinator.com|        |Required field                             |Empty password                   |
      |                       |        |Required field                             |Empty credentials                |




  @loginPasswordMasking
  Scenario: Password field security

    When User enters password in the password field
    Then Password should be masked with asterisks
    And ShowOrHide password toggle should be available




  @API
  @portalLogin
  Scenario Outline: submit login api and verify whether login is successful

    Given add loginAPI payload with "<username>" and "<password>"
    When user submit "loginAPI" with "POST" request for loginPortal
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    Then validate token is generated
    Then projectId "<projectId>" is validated
    Then validate count of assigned projects and display project_name "<project_name>"

    Examples:
      |projectId|project_name    |username                      |password|
      |   436   |restAssured Test|restAssuredtest@mailinator.com|12345678|




#  @skip
  @API
  @confirmLogin
  Scenario: submit confirmLogin api and verify whether login is successful

    When submit "confirmLogin" api with "POST" request for confirmLogin
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time