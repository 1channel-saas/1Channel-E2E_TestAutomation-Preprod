@AUT3
Feature: Assistive Forgot Password

  Background:
    Given user is on Assistive login page

  Scenario: Forgot Password module at Login Page
    When click on Forgot Password link
    And user enters email_id at Reset Password page
    And click on Submit button
    And click on okay link over pop up message
    Then navigate to verify email link
    And click on the reset password link received over mail
    And enter new password under confirm password and click on SignIn button
    Then enter user email and new password at login page and click on Signin button