@AUT31
Feature: workflow functionality for customer entity and will trigger Customer is Created

  Background:
    Given after login to CRM user will be on the Workflow screen under Admin Menu

  @Scenario1 #for creating any workflow except Date-Time
  Scenario Outline:  user will add a new workflow with CRM Transaction Entity and Trigger will be CRM Transaction
    When user select Add New button under workflow screen
    And enter Workflow Name "<Workflow Name>" and  Description "<Description>" under add new workflow screen
    And select Entity as "<Entity>" under add new workflow screen
    And select Trigger as "<Trigger>" under add new workflow screen
    And select save button under add new workflow screen
    Then new workflow will be created

    And active the workflow
    And go to the workflow screen
    Then new workflow will show in the workflow list under workflow screen

    Examples:
      | Workflow Name               | Description                               | Entity                    | Trigger                |
      | Customer Created WF         | Testing for Customer Created WF           | Customer                  | Customer is Created    |
      | Customer Updated WF         | Testing for Customer Updated WF           | Customer                  | Customer is Updated    |
      | Contacts Created WF         | Testing for Contacts Created WF           | Contact                   | Contact is Created     |
      | Contacts Updated WF         | Testing for Contacts Updated WF           | Contact                   | Contact is Updated     |
      | Opportunity Created WF      | Testing for Opportunity Created WF        | Opportunity               | Opportunity is Created |
      | Opportunity Updated WF      | Testing for Opportunity Updated WF        | Opportunity               | Opportunity is Updated |
      | Offsite Activity Submitted  | Testing for Offsite Activity Submitted WF | Offsite Activity          | Activity is Submitted  |
      | Offsite Activity Updated    | Testing for Offsite Activity Updated WF   | Offsite Activity          | Activity is Updated    |
      | Onsite Activity Submitted   | Testing for Onsite Activity Submitted WF  | Onsite Activity           | Activity is Submitted  |
      | Onsite Activity Updated     | Testing for Onsite Activity Updated WF    | Onsite Activity           | Activity is Updated    |
      | Order Activity Submitted    | Testing for Order Activity Submitted WF   | Order                     | Activity is Submitted  |
      | Order Activity Updated      | Testing for Order Activity Updated WF     | Order                     | Activity is Updated    |
      | Estimate Activity Submitted | Testing for Custom Activity Submitted WF  | Estimate                  | Activity is Submitted  |
      | Estimate Activity Updated   | Testing for Custom Activity Updated WF    | Estimate Activity         | Activity is Updated    |
      | Custom Activity Submitted   | Testing for Custom Activity Submitted WF  | Custom Activity Submitted | Activity is Submitted  |
      | Custom Activity Updated     | Testing for Custom Activity Updated WF    | Custom Activity Updated   | Activity is Updated    |


  @Scenario2 #for creating Date-Time workflow
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
      | Workflow Name      | Description              | Entity    | Start Date | Repeat  |
      | Date Time Workflow | Testing for Date Time WF | Date Time | 08/23/2023 | Never   |
      | Date Time Workflow | Testing for Date Time WF | Date Time | 03/16/2023 | Daily   |
      | Date Time Workflow | Testing for Date Time WF | Date Time | 03/16/2023 | Weekly  |
      | Date Time Workflow | Testing for Date Time WF | Date Time | 03/16/2023 | Monthly |
      | Date Time Workflow | Testing for Date Time WF | Date Time | 03/16/2023 | Yearly  |


  @Scenario3  # For adding Email
  Scenario Outline: user will open workflow dashboard of a item from the Workflows list and will add Send Email Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Send Email action user select Next button under Sent Email section
    And user select To Receiver "<To Receiver>" from To dropdown and enter "<Custom Email Id for To Receiver>" if needed
    And user select CC Receiver "<CC Receiver>" from CC dropdown and enter "<Custom Email Id for CC Receiver>" if needed
    And enter Subject "<Subject>" under Subject field
    And user enter email content
    And select save button under Sent Email section
    Then Sent Email node will show under workflow section

    Examples: For select email receiver for To
      | Workflow Name               | ActionNodeConditionType | To Receiver     | Custom Email Id for To Receiver               | CC Receiver | Custom Email Id for CC Receiver | Subject                        | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      | Contacts Created WF 101 eof |                         | Custom Email Id | pritamatta12345@gmail.com,schat0427@gmail.com | Owner       |                                 | Testing for Contact Created WF |                                  |                 |                    |

#    Data Table:
#      | Choose To Receiver Data                               | Choose CC Receiver Data                               |            Choose Subject Data            |    Choose ActionNodeConditionType   | choose ActionNodeConditionTypeInBetween  | choose ActionNode Name | choose ActionNode message         |
#      | Owner,Reporting Manager,Selected User,Custom Email Id | Owner,Reporting Manager,Selected User,Custom Email Id | Testing for Customer Created WF           | YES                                 |  InBetween                               |    Send Email          | manual entered mail subject       |
#      | Owner                                                 | Owner                                                 | Testing for Customer Updated WF           | NO                                  |                                          |    Send SMS            | manual entered SMS subject        |
#      | Reporting Manager                                     | Reporting Manager                                     | Testing for Contacts Created WF           |                                     |                                          | Send App Notifications | manual entered notification title |
#      | Selected User                                         | Selected User                                         | Testing for Contacts Updated WF           |                                     |                                          |  Create Activity       | Onsite Activity     |
#      | Custom Email Id                                       | Custom Email Id                                       | Testing for Opportunity Created WF        |                                     |                                          |  Create Activity       | Offsite Activity    |
#                                                                                                                      | Testing for Opportunity Updated WF        |                                     |                                          |  Create Activity       | Estimate            |
#                                                                                                                      | Testing for Offsite Activity Submitted WF |                                     |                                          |  Create Activity       | Order               |
#                                                                                                                      | Testing for Offsite Activity Updated WF   |                                     |                                          |  Approval              | manual entered approval title     |
#                                                                                                                      | Testing for Onsite Activity Submitted WF  |                                     |                                          |  Delay Timer           | Wait for manual entered "Duration Value" and "Duration Measure" |
#                                                                                                                      | Testing for Onsite Activity Updated WF    |
#                                                                                                                      | Testing for Order Activity Submitted WF   |
#                                                                                                                      | Testing for Order Activity Updated WF     |
#                                                                                                                      | Testing for Custom Activity Submitted WF  |
#                                                                                                                      | Testing for Custom Activity Updated WF    |
#                                                                                                                      | Testing for Custom Activity Submitted WF  |
#                                                                                                                      | Testing for Custom Activity Updated WF    |



  @Scenario4   # for adding Send SMS
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Send SMS Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Send SMS action user select Next button under Send SMS section
    And select To Receiver "<To Receiver>" from the To dropdown and add Custom Phone Numbers "<Custom Phone Numbers>" if needed
    And enter Message "<Message>" under Message field
    And select save button under Send SMS section
    Then Send SMS node will show under workflow section

    Examples:
      | To Receiver       | Message                                                                      | Custom Phone Numbers | Workflow Name | ActionNodeConditionType | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      | Reporting Manager | Authentication code for resetting your 1Channel password is Condition Passed | *****                |               |                         |                                  |                 |                    |

#  Data Table:
#  |               Choose To Receiver Data                     |  Choose ActionNodeConditionType |  choose ActionNode Name |  choose ActionNode message|
#  | Owner,Reporting Manager,Selected User,Custom Phone Number |  YES                            |  Send SMS               |
#  | Owner                                                     |  NO                             |
#  | Reporting Manager                                         |  InBetween
#  | Selected User                                             |
#  | Custom Phone Number                                       |



  @Scenario5     # for adding Send App Notification
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Send App Notification Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Send App Notification action user select Next button under Send App Notification section
    And select To Receiver "<To Receiver>" from the To dropdown under Send App Notifications Screen
    And enter Title "<Title>" in the Title field
    And enter Message "<Message>" under Message field under Send App Notifications Screen
    And select save button under Send App Notification section
    Then Send App Notification node will show under workflow section

    Examples:
      | To Receiver | Title | Message                 | Workflow Name | ActionNodeConditionType | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      | Owner       | Grips | Can not create customer |               |                         |                                  |                 |                    |

#    Data Table:
#    |       Choose To Receiver Data         |  Choose ActionNodeConditionType |  choose ActionNode Name |
#    | Owner,Reporting Manager,Selected User |  YES                            |  Send App Notifications |
#    | Owner                                 |  NO                             |
#    | Reporting Manager                     |  InBetween
#    | Selected User                         |



  @Scenario6      # for adding Create Activity
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Create Activity Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Create Activity action user select Next button under Create Activity section
    And select Activity Name "<Activity Name>" from the Activity Name dropdown
    And fill all the fields with proper data under Create Activity screen
    And select save button under Create Activity section
    Then Create Activity node will show under workflow section

    Examples:
      | Activity Name    | Workflow Name | ActionNodeConditionType | ActionNode Name | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      | Offsite Activity |               |                         |                 |                                  |                 |                    |

#    Data Table:
#      | Choose Activity Name | Choose ActionNodeConditionType |
#      | Offsite Activity     | YES                            |
#      | Onsite Activity      | NO                             |
#      | Estimate             |
#      | Order                |


  @Scenario7    # for adding Delay Timer
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Delay Timer Action
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Delay Timer action user select Next button under Delay Timer section
    And enter Duration Value "<Duration Value>" and select Duration Measure "<Duration Measure>" from the Duration dropdown
    And select save button under Delay Timer section
    Then Delay Timer node will show under workflow section respect to Duration Value "<Duration Value>" and Duration Measure "<Duration Measure>"

    Examples:
      | Duration Value | Duration Measure | Workflow Name                  | ActionNodeConditionType | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message                                                          |
      | 2              | Minutes          | Opportunity Created WF 301 ayq |                         | InBetween                        | Send SMS        | Authentication code for resetting your 1Channel password is ConditionPassed |

#  Data Table:
#  | Choose Duration Measure |  Choose ActionNodeConditionType |  choose ActionNodeConditionTypeInBetween |  choose ActionNode Name   |  choose ActionNode message                                       |
#  | Minutes                 |  YES                            | InBetween                                |    Send Email             |  manual entered mail subject                                     |
#  | Hours                   |  NO                             |                                          |    Send SMS               |  manual entered SMS subject                                      |
#  | Days                    |  InBetween                      |                                          | Send App Notifications    |   manual entered notification title                              |
#  | Weeks                   |                                 |                                          |   Create Activity         |  Onsite Activity     |
#  | Months                  |                                 |                                          |   Create Activity         |  Offsite Activity    |
#  | Years                   |                                 |                                          |   Create Activity         |  Estimate            |
#                                                              |                                          |   Create Activity         |  Order               |
#                                                              |                                          |   Approval                |  manual entered approval title                                   |
#                                                              |                                          |  Delay Timer              |  Wait for manual entered "Duration Value" and "Duration Measure" |

  @Scenario8  # For Adding Condition
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Condition
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
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
      | Workflow Name | ActionNodeConditionType | Entity   | Entity Field    | Operator  | Value  | Add More | Add Group | Condition Type | Entity2     | Entity Field2   | Operator2 | Value2 | Entity3 | Entity Field3 | Operator3 | Value3 | Condition Type2 | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      | som wf1       |                         | Estimate | Select Customer | Equals to | soumya | NO       | NO        | OR             | Opportunity | Win probability | Equals to | 100    | Contact | Name          | Contains  | Taufik | AND             |                                  |                 |                    |

#    Data Table:
#      | Choose Entity | Choose Entity Field for Customer | Choose Entity Field for Opportunity | Choose Entity Field for Contact | Choose Operator | Choose Condition Type | Choose Add More/Add Group | Choose ActionNodeConditionType | 
#      | Customer      | Customer Name                    | Customer Name                       | Name                            | Contains        | AND                   | YES                       | YES                            | 
#      | Opportunity   | Customer Type                    | Opportunity Name                    | Contact Type                    | Equals to       | OR                    | NO                        | NO                             | 
#      | Contact       | Address                          | Win probability                     | Assign Customer                 |                 |                       |                           |
#      |               | Country/State/City               | Value                               | Designation                     |                 |                       |                           |
#      |               | Owner                            | Status                              | Mobile                          |                 |                       |                           |
#      |               | Status                           | Expected Closure Date               | Email                           |                 |                       |                           |
#      |               | Phone                            | Description                         | Address                         |                 |                       |                           |
#      |               | Time zone                        | Contacts                            | Country/State/City              |                 |                       |                           |
#      |               | Date                             | Address                             | Owner                           |                 |                       |                           |
#      |               | Customer Email                   | Country/State/City                  | Status                          |                 |                       |                           |
#      |               | Gender                           | About                               | Created Date                    |                 |                       |                           |


  @Scenario9     # for adding Approval
  Scenario Outline: user will open workflow section of a item from the Workflows list and will add Approval
    When user will select Workflow action for any item "<Workflow Name>" from the workflow list
    And select Add an Action or Condition button "<ActionNodeConditionType>" OR select plus button "<ActionNodeConditionTypeInBetween>" before node contains ActionNode Name "<ActionNode Name>" and ActionNode Message "<ActionNode message>" if needed
    And after select Approval user select Next button
    And user enter Name "<Name>" in the Name field
    And user select AssignTo "<Assign To>" from AssignTo dropdown and select Roles "<Role>" if needed
    And select Enable Editing "<Enable Editing>" checkbox if needed
    And select save button under Approval section
    Then Approval node will show under workflow dashboard

    Examples:
      | Workflow Name | ActionNodeConditionType | Name | Assign To                                            | Enable Editing | Role  | ActionNodeConditionTypeInBetween | ActionNode Name | ActionNode message |
      |               | NO                      | DDRS | Reporting Manager,Selected User Roles,Selected Users | YES            | Admin |                                  |                 |                    |

#    Data Table:
#      | Choose ActionNodeConditionType | Choose Assign To                                     | Choose Enable Editing | Choose Roles |
#      | YES                            | Reporting Manager,Selected User Roles,Selected Users | YES                   | Admin        |
#      | NO                             | Reporting Manager                                    | NO                    | User         |
#      |                                | Selected User Roles                                  |                       |              |
#      |                                | Selected Users                                       |                       |              |




