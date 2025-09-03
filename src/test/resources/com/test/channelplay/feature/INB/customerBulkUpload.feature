@INB
@customerBulkUpload
Feature: Upload customers from excel from 'Bulk Upload' option under Add button dropdown for CRM -> Customers for INB


  @E2E
  Scenario Outline: Add Customer through Bulk Upload for project IGSSL Collection (projectId - 433)

##    API
    Given user submit "loginAPI" with "POST" request with "<username>" and "<password>" for INB
    And validate whether session already active. If yes then submit "confirmLogin" with "POST" request with same "<username>" and "<password>" for INB
    Then validate API call is success with status code 200 or handle error
    Then fetch projectId from login response for INB
##    UI
    Given user loggedIn to 1Channel project for INB
    When user clicks on menu CRM and submenu Customers for INB
    Then user is on Customers page for INB
    And clicks on dropdown under Add button and then click on Bulk Upload option for INB
##    API
    Then add request for checkPrimaryAttribute with "<projectIdInUse>" for INB
    Then submit "checkPrimaryAttribute" with "POST" request for customerBulkUpload for INB
    Then validate API call is success with status code 200 or handle error
    And validate ApiResponse execution time
    Then submit "getFieldsInSetting" with "GET" request to fetch attributeId of PrimaryAttribute for customerBulkUpload for "<projectIdInUse>" for INB
    And validate response data for checkPrimaryAttribute for INB
##    UI
    And upload the excel file and validate MapFields and ValidateData pages and upload validated records for INB
    Then verify email received for successful bulk upload with attachment for INB
    And validate success_failure status of the upload from email attachments for INB
##    DB
    Then validate all data actually uploaded by "<assignedUserId>" through bulk Upload to "channelplay_aurora.b2b_company_master_stage" table with "<uploadDate>" for INB
    And validate whether Is_moved flag turned to 1 in "channelplay_aurora.b2b_company_master_stage" table and for "<assignedUserId>" and "<uploadDate>" for INB
    Then validate whether bulk upload performed by "<assignedUserId>" saved under "channelplay_aurora.b2b_company_master_stage" table with testAccounts "<AccNo1>" "<AccNo2>" "<AccNo3>" "<AccNo4>" "<AccNo5>" and "<uploadDateTime>" "<uploadDate>" for INB
    Then validate whether bulk upload performed by "<assignedUserId>" saved under "channelplay_aurora.b2b_company_master" table from staging table with testAccounts "<AccNo1>" "<AccNo2>" "<AccNo3>" "<AccNo4>" "<AccNo5>" and "<uploadDateTime>" "<uploadDate>" for INB
    Then validate all data actually uploaded by "<assignedUserId>" saved under "channelplay_aurora.b2b_company_master" table with "<uploadDate>" for INB
##    UI
    And call the assertions of bulk upload Success-Failure email status at the end for INB

    Examples:
      |username               |password        |projectIdInUse|assignedUserId|AccNo1    |AccNo2    |AccNo3    |AccNo4    |AccNo5    |uploadDateTime     |uploadDate|
      |testemailqa@1channel.co|K(460848703994az|433           |7709          |91102001  |91102003  |7189786   |7189788   |7189790   |2025-02-12 02:53:08|20250401  |
