@AUT34
Feature: Assistive CRM -> Approval

  Background:
    Given user logged in to Assistive project under approval
    When clicks on menu CRM and submenu Approval


  @Scenario1
  Scenario Outline: Perform Approval Actions for moving it from Pending to Completed section

    When clicks on Actions icon for any Approval "<Approval Name>" OR Entity name "<Entity Name>" from the list showing under Pending section
    And user is on Action page for the same Entity name "<Action Entity Name>" selected
    And edit the details as per requirement
    And click on Action dropdown and select Action type
    And click on Save button under approval
    Then validate row count reduced under Pending section
    And go to Completed section
    Then validate same Approval "<Approval Name>" is showing under Completed section

    Examples:
      | Approval Name  | Entity Name | Action Entity Name |
      | approval 05/04 | yellow      | yellow             |