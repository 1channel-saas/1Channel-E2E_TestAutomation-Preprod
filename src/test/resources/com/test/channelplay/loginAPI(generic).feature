@loginAPI
Feature: This is generic Login api feature which can be used for all other APIs to retrieve token


  Scenario: submit login api for portal
    When user submit "loginAPI" with "POST" request for login


  Scenario: submit login api for portal with test creds
    When user submit "loginAPI" with "POST" request for login with testUser creds


  Scenario: submit login api for loyalty
    When user submit "loginAPI" with "POST" request for loyalty


  Scenario: submit login api for mobile App with request_login_otp
    When user submit "request_login_otp" with "POST" request for App-login with request_login_otp


  Scenario: submit login api for mobileApp with requestOTP
    When user submit "requestOTP" with "POST" request for App-login with requestOTP