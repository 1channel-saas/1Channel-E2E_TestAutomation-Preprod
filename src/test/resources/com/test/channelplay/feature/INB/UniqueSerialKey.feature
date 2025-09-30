@INB
@DB
@E2E
@useCase_Scenarios
Feature: Serial Nos for all Customers and Users should be unique in DB - validation for INB


  @uniqueSerialNo_INB
  Scenario Outline: Validate serial key uniqueness in DB for customers and users

##    DB
    When validate all serial_Nos are unique for "<project_id>" for users under "channelplay_aurora.b2b_user_prj_role_owner_assign" table for INB
    Then validate all serial_Nos are unique for "<project_id>" for customers under "channelplay_aurora.b2b_company_master" table for INB

    Examples:
      |project_id|
      |426       |
      |433       |