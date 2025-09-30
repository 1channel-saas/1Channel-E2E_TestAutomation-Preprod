@CRMApp
@mobile
@E2E
@appLaunch
Feature: Mobile App Launch Test to verify 1Channel CRM app launches successfully


  Scenario: Verify 1Channel CRM app launches successfully

    Given I launch the 1Channel CRM application
    Then the app should launch successfully
    And I wait for 5 seconds
    Then I should see the app is running




  Scenario: Verify correct app package is installed

    Given I launch the 1Channel CRM application
    Then the app package should be "com.onechannelcrm.assistive"
    And the app should be in foreground