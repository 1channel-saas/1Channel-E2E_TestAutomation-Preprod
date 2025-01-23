
Feature: Add all type fields under settings

  Background:
    Given after login to crm user will be on the Contact page under Settings
    And user select Add Field button under Contact

  Scenario: Add field type Numeric
    When user select numeric from the field type dropdown under Contact
    And enter Field Name for Numeric type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Multi Select Dropdown
    When user select Multi Select Dropdown from the field type dropdown under Contact
    And enter Field Name for Multi Select Dropdown type under Contact
    And enter Options under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Date
    When user select Date from the field type dropdown under Contact
    And enter Field Name for Date type under Contact
    And select Value Type as Custom under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Email
    When user select Email from the field type dropdown under Contact
    And enter Field Name for Email type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Document Upload
    When user select Document Upload from the field type dropdown under Contact
    And enter Field Name for Document Upload type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Video
    When user select Video from the field type dropdown under Contact
    And enter Field Name for Video type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - Reference Documents
    When user select Reference Documents from the field type dropdown under Contact
    And enter Field Name for Reference Documents type under Contact
    And upload a file under Upload File under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact


  Scenario: Add field type - Header Text
    When user select Header Text from the field type dropdown under Contact
    And enter Field Name for Header Text type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact


  Scenario: Add field type - Normal Text
    When user select Normal Text from the field type dropdown under Contact
    And enter Field Name for Normal Text type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact


  Scenario: Add field type - Page Separator
    When user select Page Separator from the field type dropdown under Contact
    And enter Field Name for Page Separator type under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact


  Scenario: Add field type - Data List
    When user select Data List from the field type dropdown under Contact
    And enter Field Name for Data List type under Contact
    And select List Name under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

  Scenario: Add field type - OTP Validation
    When user select OTP Validation from the field type dropdown under Contact
    And enter Field Name OTP Validation type under Contact
    And enter Entity under Contact
    And select Entity Field under Contact
    And select Save button under Contact
    Then new field will be shown in the list under Contact

