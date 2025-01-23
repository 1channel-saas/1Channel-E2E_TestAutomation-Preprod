@AUT33
Feature: workflow functionality for Opportunity entity and will trigger on Opportunity is Created

  Background:
    Given after login to CRM user will be on the Workflow screen under Admin Menu

  @Scenario1  # For creating Date Time workflow
  Scenario Outline:  user will add a new workflow with Date Time Entity
    When user select Add New button under workflow screen
    And enter Workflow Name "<Workflow Name>" and  Description "<Description>" under add new workflow screen
    And select Entity as "<Entity>" under add new workflow screen
    And select Start Date "<Start Date>" under add new workflow screen
    And user select "<Repeat>" options from dropdown list
    And select save button under add new workflow screen
    Then new workflow will be created

    And active the workflow
    And go to the workflow screen
    Then new workflow will show in the workflow list under workflow screen
    Examples:
      | Workflow Name          | Description              | Entity    | Start Date | Repeat |
      | Date Time Workflow 305 | Testing for Date Time WF | Date Time | 04/5/2023  | Never  |
#      | Date Time Workflow     | Testing for Date Time WF | Date Time | 03/16/2023 | Daily   |
#      | Date Time Workflow     | Testing for Date Time WF | Date Time | 03/16/2023 | Weekly  |
#      | Date Time Workflow     | Testing for Date Time WF | Date Time | 03/16/2023 | Monthly |
#      | Date Time Workflow     | Testing for Date Time WF | Date Time | 03/16/2023 | Yearly  |


  @Scenario2  # For adding Email
  Scenario Outline: user will open workflow dashboard of a item from the Workflows list and will add Send Email Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Send Email action user select Next button under Sent Email section
    And user select To Receiver "<To Receiver>" from To dropdown and enter "<Custom Email Id for To Receiver>" if needed
    And user select CC Receiver "<CC Receiver>" from CC dropdown and enter "<Custom Email Id for CC Receiver>" if needed
    And enter Subject "<Subject>" under Subject field
    And user enter email content
    And select save button under Sent Email section
    Then Sent Email node will show under workflow section

    Examples: For select email receiver for To
      | Workflow Name              | ActionNodeConditionType | To Receiver | Custom Email Id for To Receiver | CC Receiver | Custom Email Id for CC Receiver | Subject                          | ActionNode Name | ActionNode Position |
      | Date Time Workflow 305 zmf | InBetween               | Owner       |                                 |             |                                 | Date Time workflow Yes condition | Create Activity | 1                   |

#    Data Table:
#      |               Choose To Receiver Data                 |                Choose CC Receiver Data                |            Choose Subject Data            |  Choose ActionNodeConditionType |
#      | Owner,Reporting Manager,Selected User,Custom Email Id | Owner,Reporting Manager,Selected User,Custom Email Id | Testing for Customer Created WF           |  YES                            |
#      | Owner                                                 | Owner                                                 | Testing for Customer Updated WF           |  NO                             |
#      | Reporting Manager                                     | Reporting Manager                                     | Testing for Contacts Created WF           |
#      | Selected User                                         | Selected User                                         | Testing for Contacts Updated WF           |
#      | Custom Email Id                                       | Custom Email Id                                       | Testing for Opportunity Created WF        |
#                                                                                                                      | Testing for Opportunity Updated WF        |
#                                                                                                                      | Testing for Offsite Activity Submitted WF |
#                                                                                                                      | Testing for Offsite Activity Updated WF   |
#                                                                                                                      | Testing for Onsite Activity Submitted WF  |
#                                                                                                                      | Testing for Onsite Activity Updated WF    |
#                                                                                                                      | Testing for Order Activity Submitted WF   |
#                                                                                                                      | Testing for Order Activity Updated WF     |
#                                                                                                                      | Testing for Custom Activity Submitted WF  |
#                                                                                                                      | Testing for Custom Activity Updated WF    |
#                                                                                                                      | Testing for Custom Activity Submitted WF  |
#                                                                                                                      | Testing for Custom Activity Updated WF    |


  @Scenario3  # For adding Send SMS
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Send SMS Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Send SMS action user select Next button under Send SMS section
    And select To Receiver "<To Receiver>" from the To dropdown and add Custom Phone Numbers "<Custom Phone Numbers>" if needed
    And enter Message "<Message>" under Message field
    And select save button under Send SMS section
    Then Send SMS node will show under workflow section

    Examples:
      | To Receiver                                               | Message                                                                      | Custom Phone Numbers | Workflow Name                  | ActionNodeConditionType | ActionNode Name | ActionNode Position |
      | Owner,Reporting Manager,Custom Phone Number,Selected User | Authentication code for resetting your 1Channel password is Condition Passed | +91-9873674841       | Opportunity Created WF 302 qvh | YES                     | Send SMS        | 0                   |


  @Scenario4  # For Adding Condition
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Condition
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select New Condition user select Next button
    And select Entity "<Entity>" from the Select Entity dropdown
    And select Entity Field "<Entity Field>" from the Select Entity Field dropdown
    And select Operator "<Operator>" from the Select Operator dropdown
    And enter Value "<Value>" in the Select Value field
    And select Add More "<Add More>" if required additional condition to be added with Condition Type "<Condition Type>"
    And select Entity "<Entity2>", Entity Field "<Entity Field2>", Operator "<Operator2>", Value "<Value2>" for Add More "<Add More>"
    And select Add Group "<Add Group>" if required additional condition to be added with Condition Type "<Condition Type2>"
    And select Entity "<Entity3>", Entity Field "<Entity Field3>", Operator "<Operator3>", Value "<Value3>" for Add Group "<Add Group>"
    And select save button under New Condition section
    Then New Condition will show under workflow dashboard

    Examples:
      | Workflow Name              | ActionNodeConditionType | Entity          | Entity Field | Operator | Value | Add More | Condition Type | Entity2 | Entity Field2 | Operator2 | Value2 | Add Group | Condition Type2 | Entity3 | Entity Field3 | Operator3 | Value3 | ActionNode Name | ActionNode Position |
      | Date Time Workflow 305 zmf |                         | Onsite Activity | Title        | Contains | On    |          |                |         |               |           |        |           |                 |         |               |           |        |                 | 0                   |


  @Scenario5  # For adding Approval
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Approval
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Approval user select Next button
    And user enter Name "<Name>" in the Name field
    And user select AssignTo "<Assign To>" from AssignTo dropdown and select Roles "<Role>" if needed
    And select Enable Editing "<Enable Editing>" checkbox if needed
    And select save button under Approval section
    Then Approval node will show under workflow dashboard

    Examples:
      | Workflow Name                  | ActionNodeConditionType | Name                  | Assign To                                            | Enable Editing | Role       | ActionNode Name | ActionNode Position |
      | Opportunity Created WF 302 qvh | NO                      | Workflow 302 Approval | Reporting Manager,Selected User Roles,Selected Users | YES            | Admin,User |                 | 0                   |


  @Scenario6  # For adding Delay Timer
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Delay Timer Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Delay Timer action user select Next button under Delay Timer section
    And enter Duration Value "<Duration Value>" and select Duration Measure "<Duration Measure>" from the Duration dropdown
    And select save button under Delay Timer section
    Then Delay Timer node will show under workflow section respect to Duration Value "<Duration Value>" and Duration Measure "<Duration Measure>"

    Examples:
      | Duration Value | Duration Measure | Workflow Name              | ActionNodeConditionType | ActionNode Name | ActionNode Position |
      | 5              | Minutes          | Date Time Workflow 305 zmf | InBetween               | Create Activity | 2                   |

#  Data Table:
#  | Choose Duration Measure |  Choose ActionNodeConditionType |  choose ActionNode Name   |  choose ActionNode message                                        |
#  | Minutes                 |  YES                            |  Send Email               |  manual entered mail subject                                      |
#  | Hours                   |  NO                             |  Send SMS                 |  manual entered SMS subject                                       |
#  | Days                    |  InBetween                      |  Send App Notifications   |  manual entered notification title                                |
#  | Weeks                   |                                 |  Create Activity          |  Onsite Activity                                                  |
#  | Months                  |                                 |  Create Activity          |  Offsite Activity                                                 |
#  | Years                   |                                 |  Create Activity          |  Estimate                                                         |
#                                                              |  Create Activity          |  Order                                                            |
#                                                              |  Approval                 |  manual entered approval title                                    |
#                                                              |  Delay Timer              |  Wait for manual entered "Duration Value" and "Duration Measure"  |


  @Scenario7  # For adding Create Activity
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Create Activity Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Create Activity action user select Next button under Create Activity section
    And select Activity Name "<Activity Name>" from the Activity Name dropdown
    And fill all the fields with proper data under Create Activity screen
    And select save button under Create Activity section
    Then Create Activity node will show under workflow section

    Examples:
      | Activity Name    | Workflow Name              | ActionNodeConditionType | ActionNode Name | ActionNode Position |
      | Offsite Activity | Date Time Workflow 305 zmf | YES                     |                 | 0                   |


  @Scenario8  # For adding Send App Notification
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Send App Notification Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button before Node contains ActionNode Name "<ActionNode Name>" and ActionNode position <ActionNode Position> if needed
    And after select Send App Notification action user select Next button under Send App Notification section
    And select To Receiver "<To Receiver>" from the To dropdown under Send App Notifications Screen
    And enter Title "<Title>" in the Title field
    And enter Message "<Message>" under Message field under Send App Notifications Screen
    And select save button under Send App Notification section
    Then Send App Notification node will show under workflow section

    Examples:
      | To Receiver | Title                 | Message                                                                     | Workflow Name                  | ActionNodeConditionType | ActionNode Name | ActionNode Position |
      | Owner       | 1Channel notification | This is to notify tou that new Opportunity is created for Assistive project | Opportunity Created WF 302 qvh | YES                     |                 | 0                   |

