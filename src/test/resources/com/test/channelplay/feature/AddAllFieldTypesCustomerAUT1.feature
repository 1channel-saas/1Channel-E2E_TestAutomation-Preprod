
Feature: Add all type fields under settings

  Background:
    Given after login to crm user will be on the Customer page under Settings
    And user select Add Field button under Customer

    Scenario: Add field type Numeric
      When user select numeric from the field type dropdown under Customer
      And enter Field Name for Numeric type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Multi Select Dropdown
      When user select Multi Select Dropdown from the field type dropdown under Customer
      And enter Field Name for Multi Select Dropdown type under Customer
      And enter Options under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Date
      When user select Date from the field type dropdown under Customer
      And enter Field Name for Date type under Customer
      And select Value Type as Custom under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Email
      When user select Email from the field type dropdown under Customer
      And enter Field Name for Email type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Document Upload
      When user select Document Upload from the field type dropdown under Customer
      And enter Field Name for Document Upload type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Video
      When user select Video from the field type dropdown under Customer
      And enter Field Name for Video type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - Reference Documents
      When user select Reference Documents from the field type dropdown under Customer
      And enter Field Name for Reference Documents type under Customer
      And upload a file under Upload File under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer


    Scenario: Add field type - Header Text
      When user select Header Text from the field type dropdown under Customer
      And enter Field Name for Header Text type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer


    Scenario: Add field type - Normal Text
      When user select Normal Text from the field type dropdown under Customer
      And enter Field Name for Normal Text type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer


    Scenario: Add field type - Page Separator
      When user select Page Separator from the field type dropdown under Customer
      And enter Field Name for Page Separator type under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer


    Scenario: Add field type - Data List
      When user select Data List from the field type dropdown under Customer
      And enter Field Name for Data List type under Customer
      And select List Name under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

    Scenario: Add field type - OTP Validation
      When user select OTP Validation from the field type dropdown under Customer
      And enter Field Name OTP Validation type under Customer
      And enter Entity under Customer
      And select Entity Field under Customer
      And select Save button under Customer
      Then new field will be shown in the list under Customer

