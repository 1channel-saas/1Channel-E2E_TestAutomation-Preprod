
Feature: Add all type fields under settings

  Background:
    Given after login to crm user will be on the Product page under Admin under Settings
    And user select Add Field button under Product


  Scenario: Add field type Text-multi-line
    When user select text-multi-line from the field type dropdown under Product
    And enter Field Name for text-multi-line type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type Text-single-line
    When user select text-single-line from the field type dropdown under Product
    And enter Field Name for text-single-line type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type Numeric
    When user select numeric from the field type dropdown under Product
    And enter Field Name for Numeric type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Single Select Dropdown
    When user select Single Select Dropdown from the field type dropdown under Product
    And enter Field Name for Single Select Dropdown type under Product
    And enter Options under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Multi Select Dropdown
    When user select Multi Select Dropdown from the field type dropdown under Product
    And enter Field Name for Multi Select Dropdown type under Product
    And enter Options under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Date
    When user select Date from the field type dropdown under Product
    And enter Field Name for Date type under Product
    And select Value Type as Custom under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Email
    When user select Email from the field type dropdown under Product
    And enter Field Name for Email type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Image
    When user select Image from the field type dropdown under Product
    And enter Field Name for Image type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Document Upload
    When user select Document Upload from the field type dropdown under Product
    And enter Field Name for Document Upload type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Video
    When user select Video from the field type dropdown under Product
    And enter Field Name for Video type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Reference Documents
    When user select Reference Documents from the field type dropdown under Product
    And enter Field Name for Reference Documents type under Product
    And upload a file under Upload File under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Header Text
    When user select Header Text from the field type dropdown under Product
    And enter Field Name for Header Text type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Normal Text
    When user select Normal Text from the field type dropdown under Product
    And enter Field Name for Normal Text type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Page Separator
    When user select Page Separator from the field type dropdown under Product
    And enter Field Name for Page Separator type under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Link from other entity
    When user select Link from other entity from the field type dropdown under Product
    And enter Field Name Link from other entity type under Product
    And enter Entity under Product
    And select Entity Field under Product
    And select Save button under Product
    Then new field will be shown in the list under Product


  Scenario: Add field type - Data List
    When user select Data List from the field type dropdown under Product
    And enter Field Name for Data List type under Product
    And select List Name under Product
    And select Save button under Product
    Then new field will be shown in the list under Product

  Scenario: Add field type - OTP Validation
    When user select OTP Validation from the field type dropdown under Product
    And enter Field Name OTP Validation type under Product
    And enter Entity under Product
    And select Entity Field under Product
    And select Save button under Product
    Then new field will be shown in the list under Product

