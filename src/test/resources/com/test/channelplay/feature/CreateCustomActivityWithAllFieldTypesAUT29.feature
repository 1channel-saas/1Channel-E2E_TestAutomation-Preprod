
Feature: Create custom activity and all all field types for this new activity

  Background:
    Given after login to CRM user will be on the Activities screen under Admin menu

  Scenario: User will successfully add a new Activity
    When user select Add button under Activity screen
    And user enter Activity Name and select Customer and Opportunity
    And hit Save button
    Then new activity will create and will show in the activity list

    When user select Action button for new created Activity
    And select Add Field button under Activity
    And user select Numeric field type from the field type dropdown under Activity
    And enter field name for Numeric under Activity
    And select Save button under Activity
    Then new field will be shown in the list under Activity

    And select Add Field button under Activity
    And user select Email field type from the field type dropdown under Activity
    And enter field name for Email under Activity
    And select Save button under Activity
    Then new field will be shown in the list under Activity

    And select Add Field button under Activity
    And user select Normal Text from the field type dropdown under Activity
    And enter field name for Normal Text under Activity
    And select Save button under Activity
    Then new field will be shown in the list under Activity

    And user select CRM menu
    And user select Customers
    And select Dashboard Button for any customer from the list
    And select Add activities button
    And user select new created Activity

    And select opportunity
    And enter title
    And enter Registered Email
    And enter Phone Number
    And select save button under Activity form
    Then new activity will show in the dashboard

    And user select CRM menu
    And user select Opportunity
    And select Dashboard Button for any opportunity from the list
    And select Add activities button for opportunity
    And user select new created Activity

    And select opportunity
    And enter title
    And enter Registered Email
    And enter Phone Number
    And select save button under Activity form
    Then new activity will show in the dashboard











