@web @E2E
@userAuthentication
@forgotPassword
Feature: 1Channel Forgot Password feature test for both UI and API with both via email and mobile. Also validates DB changes where applicable.

  Background:
    Given User launches 1Channel CRM portal


  @forgotPasswordUI
  Scenario Outline: Forgot and reset Password module test over UI

##    UI
    When click on Forgot Password link
    Then navigate to emailer and perform password reset operation with selecting "<resetIdentifier>"
    Then enter user email and new password at login page and click on Signin button

    Examples:
      |resetIdentifier|
      |Email          |
      |Mobile         |




  @forgotPasswordAPI_Email
  Scenario: Forgot Password feature test via API with user email. api name: resetPassword, updatePassword

##    API
    Given user submit "resetPassword" with "POST" request for forgotPasswordAPI_Email
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate "responseData" is "password reset email sent successfully." in responseBody
#    When user submit "passValidateToken" with "POST" request for passValidateToken
#    Then validate API call is success with status code 200 or handle error
#    Then validate ApiResponse execution time
#    And validate response data for passValidateToken
    Then add request for updatePassword
    When user submit "updatePassword" with "POST" request for updatePassword
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate response data for updatePassword




    @forgotPasswordAPI_Mobile
    Scenario: Forgot Password feature test via API with user mobile. api name: requestOTPV2, validateOTPV2

##    API
    Given user submit "requestOTPV2" with "POST" request for forgotPasswordAPI_Mobile
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate "message" is "OTP Sent Successfully" in responseBody
    Then fetch otp from DB table "channelplay_aurora.b2b_otp_reset_password" for forgotPasswordAPI_Mobile
    Then user submit "validateOTPV2" with "POST" request to validate otp for forgotPasswordAPI_Mobile
    Then validate API call is success with status code 200 or handle error
    Then validate ApiResponse execution time
    And validate "message" is "OTP Validated Successfully" in responseBody