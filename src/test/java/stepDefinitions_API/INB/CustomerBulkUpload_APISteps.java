package stepDefinitions_API.INB;

import com.test.channelplay.object.INB.CustomerBulkUpload_Object;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;
import pojo_API.responsePojo.Login.LoginResponse_pojo;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;
import utilities_API.DBConnection;
import utilities_API.ExcelFileReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class CustomerBulkUpload_APISteps extends CommonUtils_API {
    private static final Logger log = LoggerFactory.getLogger(CustomerBulkUpload_APISteps.class);
    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response, checkPrimaryAttributeResponse, getFieldsInSettingResp_INB;
    Endpoints endpoints;
    JsonPath json;
    String bearerToken, checkPrimaryAttributeJsonContent, INBProjectId, is_active, trans_created_on, serial_no, trans_updated_int,
            Is_moved, trans_created_on_stage, dynamicUploadDate, AccNO_Master, serial_no_custMaster, INBProjectId_custMaster, is_active_custMaster,
            created_by_custMaster, updated_by_custMaster, trans_created_on_custMaster, trans_updated_on_custMaster,
            trans_created_int_custMaster,trans_updated_int_custMaster;
    int defaultProjectId, projectId_IGSSL, projectId_Collection, currentProjectId, primaryAttributeId, currentPrimaryAttributeId,
            recordCountStage = 0, dataUploadedInExcel, missingSerialCount = 0;
    boolean missingCounter = false;
    LoginResponse_pojo loginResponsePojo;
    private final GetApiResponseObject getApiResponseObject;
    private Connection preprodConnection;
    ResultSet resultSet;
    SoftAssert softAssert = new SoftAssert();
    List<String> stageAccountNos = new ArrayList<>();    // stores serial numbers from staging table AddActivity_testUserSteps future step utility
    CustomerBulkUpload_Object custBulkUpload = new CustomerBulkUpload_Object();
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();

    public CustomerBulkUpload_APISteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }


//    scenario - customerBulkUpload

    @Given("user submit {string} with {string} request with {string} and {string} for INB")
    public void userSubmitWithRequestForINB(String endpoint, String httpMethod, String username, String password) {
        String loginPayload = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);
        reqspec = given().spec(commonUtils.requestSpec("loginAPI_INB")).body(loginPayload);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        loginResponsePojo = response.as(LoginResponse_pojo.class);
        getApiResponseObject.setResponse(response);
    }

    @And("validate whether session already active. If yes then submit {string} with {string} request with same {string} and {string} for INB")
    public void validateWhetherSessionAlreadyActiveIfYesThenSubmitWithRequestWithSameAndForINB(String endpoint, String POST, String username, String password) {
        endpoints = Endpoints.valueOf(endpoint);
        if (response.getStatusCode() == 409) {
            String confirmLoginPayload = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);
            reqspec = given().spec(commonUtils.requestSpec("confirm-login")).body(confirmLoginPayload);
            resspec = responseSpec();

            response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

            loginResponsePojo = response.as(LoginResponse_pojo.class);
            getApiResponseObject.setResponse(response);
        }
    }

    @Then("fetch projectId from login response for INB")
    public void fetchProjectIdFromLoginResponseForINB() {
        defaultProjectId = loginResponsePojo.getUser().getDefaultProjectId();
        System.out.println("Default ProjectId of INB: " + defaultProjectId);

        //  fetch projectId in case of multiple project with same userId
        List<LoginResponse_pojo.User.UserProject> projectList = loginResponsePojo.getUser().getUserProject();
        for (LoginResponse_pojo.User.UserProject projectIds : projectList) {
            if (projectIds.getProject().getProjectName().equalsIgnoreCase("IGSSL")) {
                projectId_IGSSL = projectIds.getProject().getProjectId();
                System.out.println("Project Id of IGSSL: " + projectId_IGSSL);
            }
            if (projectIds.getProject().getProjectName().equalsIgnoreCase("IGSSL - Collection")) {
                projectId_Collection = projectIds.getProject().getProjectId();
                System.out.println("Project Id of IGSSL Collection: " + projectId_Collection);
            }
        }
    }

    @Then("add request for checkPrimaryAttribute with {string} for INB")
    public void addRequestForCheckPrimaryAttributeForINB(String projectIdInUse) {
        response = getApiResponseObject.getResponse();
        //bearerToken = getToken(response);
        bearerToken = getTokenFromUI();
        //bearerToken = assistiveLogin.UIAuthToken;
        //  dynamically fetch projectId in json payload
        if (Integer.parseInt(projectIdInUse) == projectId_IGSSL) {
            currentProjectId = projectId_IGSSL;
        } else if (Integer.parseInt(projectIdInUse) == projectId_Collection) {
            currentProjectId = projectId_Collection;
        }

        checkPrimaryAttributeJsonContent = readJsonFile("src/test/java/resources_API/Payload_API/json_Files/INB/checkPrimaryAttribute.json")
                .replace("\"project_id\"", "\"" + currentProjectId + "\"");

        reqspec = given().spec(commonUtils.requestSpec("checkPrimaryAttribute")).body(checkPrimaryAttributeJsonContent);
        resspec = responseSpec();
    }

    @Then("submit {string} with {string} request for customerBulkUpload for INB")
    public void submitWithRequestForCustomerBulkUploadForINB(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);

        checkPrimaryAttributeResponse = reqspec.when().header("Authorization", "Bearer " + bearerToken)
                .post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(checkPrimaryAttributeResponse);
    }

    @Then("submit {string} with {string} request to fetch attributeId of PrimaryAttribute for customerBulkUpload for {string} for INB")
    public void submitWithRequestToFetchAttributeIdOfPrimaryAttributeForCustomerBulkUploadForForINB(String endpoint, String httpMethod, String projectIdInUse) {
        reqspec = given().spec(commonUtils.requestSpec("getFieldsInSetting_INB"))
                .queryParam("projectId", Integer.parseInt(projectIdInUse))
                .queryParam("moduleType", 2);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        getFieldsInSettingResp_INB = reqspec.when().header("Authorization", "Bearer " + bearerToken)
                .get(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        //  fetch primaryAttributeId from getFieldsInSettingResp_INB api response
        json = new JsonPath(getFieldsInSettingResp_INB.asString());
        List<Map<String, Object>> responseDataList = json.getList("responseData");
        for (Map<String, Object> responseData : responseDataList) {
            if (responseData.get("attributeName").equals("ACCT_NO")) {
                primaryAttributeId = (int) responseData.get("id");
                System.out.println("Primary Attribute Id: " + primaryAttributeId);
            }
        }
    }

    @And("validate response data for checkPrimaryAttribute for INB")
    public void validateResponseDataForCheckPrimaryAttributeForINB() {
        //  assert that attributeId of ACCT_NO field is coming AddActivity_testUserSteps Primary AttributeId in checkPrimaryAttribute api response
        currentPrimaryAttributeId = getJsonPath(checkPrimaryAttributeResponse.asString(), "primaryAttributeId");
        assertEquals(primaryAttributeId, currentPrimaryAttributeId);
    }

    @Then("validate all data actually uploaded by {string} through bulk Upload to {string} table with {string} for INB")
    public void validateAllDataActuallyUploadedByThroughBulkUploadToTableWithForINB(String assignedUserId, String tableName, String uploadDate) {
        preprodConnection = DBConnection.getPreprodConnection();
        //  Generate today's date dynamically
        dynamicUploadDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //  comment out this line if it is required to validate a specific date which will be retrieved from feature file
        uploadDate = dynamicUploadDate;
        //  get total excel rows count from FIle directory before UI upload for further validation
        int excelRowCount = ExcelFileReader.getTotalRowCount();

        // get the time of test start from the CustomerBulkUpload_Object class
        String testStartTime = custBulkUpload.getTestStartTime();

        //  getting all trans_created_on values from the staging table for date in scope for further use
        String trans_created_onInStage_query = "SELECT trans_created_on\n" +
                "FROM " + tableName + "\n" +
                "WHERE updated_by = " + assignedUserId + "\n" +
                "AND trans_updated_int = " + uploadDate + " \n" +
                "AND trans_created_on > '" + testStartTime + "'\n" +
                "ORDER BY trans_created_on ASC";
        System.out.println("Executing trans_created_on query: " + trans_created_onInStage_query + "\n");

        List<Map<String, String>> trans_created_onResults = new ArrayList<>();

        try {
            resultSet = executeQuery(trans_created_onInStage_query, preprodConnection);
            while (resultSet.next()) {
                Map<String, String> rows = new HashMap<>();
                rows.put("trans_created_on", resultSet.getString("trans_created_on"));
                trans_created_onResults.add(rows);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        for (Map<String, String> row : trans_created_onResults) {
            trans_created_on_stage = row.get("trans_created_on");
        }

        //  get count of data actually uploaded to the staging table
        String uploadDataInStageCount_query = "SELECT COUNT(*) AS record_count\n" +
                "FROM " + tableName + "\n" +
                "WHERE updated_by = " + assignedUserId + "\n" +
                "AND trans_created_on BETWEEN '" + testStartTime + "' AND '" + trans_created_on_stage + "'";
        System.out.println("Executing count query: " + uploadDataInStageCount_query);

        try {
            resultSet = executeQuery(uploadDataInStageCount_query, preprodConnection);
            while (resultSet.next()) {
                recordCountStage = resultSet.getInt("record_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        log.info("Actual upload record count in staging table: {}", recordCountStage);

        //  validate all records from the excel are uploaded to the staging table. If not, getting count of records not uploaded
        if ((excelRowCount != recordCountStage)) {
            int recordsNotUploaded = excelRowCount - recordCountStage;
            log.warn("All records not uploaded... {} records were NOT uploaded to the staging table due to errors in the data", recordsNotUploaded);
        } else {
            assertEquals(excelRowCount, recordCountStage);
        }

        //  getting all uploaded account numbers from the staging table for further validation/ Acc no is mentioned AddActivity_testUserSteps SerialNo in stage table
        String serialNo_stage_query = "SELECT serial_no\n" +
                "FROM " + tableName + "\n" +
                "WHERE updated_by = " + assignedUserId + "\n" +
                "AND trans_updated_int = " + uploadDate + " \n" +
                "AND trans_created_on BETWEEN '" + testStartTime + "' AND '" + trans_created_on_stage + "'";
        System.out.println("Query to fetch All data from Customer Stage table: " + serialNo_stage_query);

        try {
            resultSet = executeQuery(serialNo_stage_query, preprodConnection);
            while (resultSet.next()) {
                String serialNoStage = resultSet.getString("serial_no");
                stageAccountNos.add(serialNoStage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        log.info("All Account No.s uploaded to staging table: {}", stageAccountNos);
    }

    @And("validate whether Is_moved flag turned to 1 in {string} table and for {string} and {string} for INB")
    public void validateWhetherIs_movedFlagTurnedToInTableAndForAndForINB(String tableName, String assignedUserId, String uploadDate) {
        preprodConnection = DBConnection.getPreprodConnection();
        uploadDate = dynamicUploadDate;
        long startTime = System.currentTimeMillis();
        int maxWaitTimeInSeconds = 180;     // Maximum wait time
        int pollingIntervalInSeconds = 3;   // Poll every 3 seconds
        boolean isMoved = false;

        while ((System.currentTimeMillis() - startTime) < maxWaitTimeInSeconds * 1000L) {
            String waitForIsMovedToBeOne_query = "SELECT Is_moved FROM " + tableName + "\n" +
                    "WHERE updated_by = " + assignedUserId + "\n" +
                    "AND trans_updated_int = " + uploadDate + "\n" +
                    "ORDER BY trans_created_on DESC";
            System.out.println("Executing query to check Is_moved flag: " + waitForIsMovedToBeOne_query);

            try {
                resultSet = executeQuery(waitForIsMovedToBeOne_query, preprodConnection);
                if (resultSet.next()) {
                    Is_moved = resultSet.getString("Is_moved");

                    if ("1".equals(Is_moved)) {
                        isMoved = true;
                        log.info("Is_moved flag is now 1. Proceeding with the next step...");
                        break;
                    }
                    else log.info("Is_moved flag is still 0. Executing query again...");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to execute query while checking Is_moved flag", e);
            } finally {
                closeResultSet(resultSet);
            }

            //  Wait before the next retry
            try {
                Thread.sleep(pollingIntervalInSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Polling was interrupted", e);
            }
        }

        // If Is_moved is still not 1 after the timeout, fail the test
        if (!isMoved) {
            throw new RuntimeException("Timeout: Is_moved did not change to 1 within " + maxWaitTimeInSeconds + " seconds");
        }
    }

    @Then("validate whether bulk upload performed by {string} saved under {string} table with testAccounts {string} {string} {string} {string} {string} and {string} {string} for INB")
    public void validateWhetherBulkUploadPerformedBySavedUnderTableWithtesTAccountsAndForINB(String assignedUserId, String tableName, String AccNo1, String AccNo2, String AccNo3, String AccNo4, String AccNo5, String uploadDateTime, String uploadDate) {
        preprodConnection = DBConnection.getPreprodConnection();

        String uploadDataInStage_query = "SELECT project_id, is_active, trans_created_on, serial_no, trans_updated_int\n" +
                "FROM " + tableName + "\n" +
                "WHERE updated_by = " + assignedUserId + "\n" +
                "AND response_data ->> 'F68721' = ANY(ARRAY['" + AccNo1 + "', '" + AccNo2 + "', '" + AccNo3 + "', '" + AccNo4 + "', '" + AccNo5 + "'])\n" +
                "AND trans_updated_int = " + uploadDate + " \n" +
                "ORDER BY trans_created_on ASC";
        System.out.println("Query to fetch data from Customer Stage table: " + uploadDataInStage_query);

        List<Map<String, String>> results = new ArrayList<>();
        //  stores retrieved serial_no values
        Set<String> retrievedSerialNumbers = new HashSet<>();
        try {
            resultSet = executeQuery(uploadDataInStage_query, preprodConnection);
            while (resultSet.next()) {
                Map<String, String> rows = new HashMap<>();
                rows.put("project_id", resultSet.getString("project_id"));
                rows.put("is_active", resultSet.getString("is_active"));
                rows.put("trans_created_on", resultSet.getString("trans_created_on"));
                rows.put("serial_no", resultSet.getString("serial_no"));
                rows.put("trans_updated_int", resultSet.getString("trans_updated_int"));
                results.add(rows);

                //  Stores retrieved serial_no values in a Set for further validation
                retrievedSerialNumbers.add(resultSet.getString("serial_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        for (Map<String, String> row : results) {
            INBProjectId = row.get("project_id");
            is_active = row.get("is_active");
            trans_created_on = row.get("trans_created_on");
            serial_no = row.get("serial_no");
            trans_updated_int = row.get("trans_updated_int");
            System.out.println("ProjectId: " + INBProjectId + ", is_active: " + is_active + ", trans_created_on: " + trans_created_on + ", serial_no: " + serial_no + ", trans_updated_int: " + trans_updated_int);
        }

        //  Assert that is_active is 1 for each record
        for (Map<String, String> row : results) {
            assert "1".equals(row.get("is_active")) : "Assertion failed! is_active is not 1 for serial_no: " + serial_no;
        }
        log.info("Assertion passed: All records have is_active = 1");

        //  Create a list of expected account numbers
        List<String> expectedSerialNumbers = Stream.of(AccNo1, AccNo2, AccNo3, AccNo4, AccNo5)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.toList());

        //  Counter for missing serial numbers
        missingSerialCount = 0;

        //  Assert that all expected serial numbers are present in the retrieved set
        for (String expectedSerial : expectedSerialNumbers) {
            if (!retrievedSerialNumbers.contains(expectedSerial)) {
                log.warn("Expected serial_no {} is missing in stage DB response", expectedSerial);
                softAssert.fail("Assertion failed! Expected serial_no " + expectedSerial + " is missing in stage DB response");
                missingSerialCount++;
            }
        }

        //  validate that all records from the excel are uploaded to the staging table. If not, getting count of records not uploaded
        String dataUploadedInExcelStr = custBulkUpload.getTotalRecordsValue();
        dataUploadedInExcel = Integer.parseInt(dataUploadedInExcelStr);
        int recordsNotUploaded = (dataUploadedInExcel - recordCountStage);

        if (recordsNotUploaded == 0) {
            log.info("All {} records from Excel were successfully uploaded to the staging table", dataUploadedInExcel);
        } else {
            log.warn("{} records were NOT uploaded and {} serial numbers were NOT uploaded among test accounts", recordsNotUploaded, missingSerialCount);
        }

        if (missingSerialCount > 0) {
            softAssert.fail("Test Failed:" + missingSerialCount + " serial numbers were NOT uploaded among test accounts");
        }

        //  final assertion status
        if (expectedSerialNumbers.isEmpty()) {
            log.warn("No expected serial numbers provided for validation");
        } else {
            System.out.println("All provided expected serial numbers were validated");
        }
        softAssert.assertAll();
    }

    @Then("validate whether bulk upload performed by {string} saved under {string} table from staging table with testAccounts {string} {string} {string} {string} {string} and {string} {string} for INB")
    public void validateWhetherBulkUploadPerformedBySavedUnderTableFromStagingTableWithTestAccountsAndForINB(String assignedUserId, String tableName, String AccNo1, String AccNo2, String AccNo3, String AccNo4, String AccNo5, String uploadDateTime, String uploadDate) {
        preprodConnection = DBConnection.getPreprodConnection();

        String uploadDataInCustMaster_query = "SELECT serial_no, is_active, created_by, updated_by, trans_created_on, trans_updated_on\n" +
                "FROM " + tableName + "\n" +
                "WHERE project_id = " + INBProjectId + "\n" +
                //"WHERE updated_by = " + assignedUserId + "\n" +
                "AND response_data ->> 'F68721' = ANY(ARRAY['" + AccNo1 + "', '" + AccNo2 + "', '" + AccNo3 + "', '" + AccNo4 + "', '" + AccNo5 + "'])\n" +
                //"AND trans_created_int = " + uploadDate + " \n" +
                "ORDER BY trans_created_on DESC";
        System.out.println("Query to fetch test data from Customer Master table: " + uploadDataInCustMaster_query);

        List<Map<String, String>> results = new ArrayList<>();

        try {
            resultSet = executeQuery(uploadDataInCustMaster_query, preprodConnection);
            while (resultSet.next()) {
                Map<String, String> rows = new HashMap<>();
                rows.put("serial_no", resultSet.getString("serial_no"));
                rows.put("is_active", resultSet.getString("is_active"));
                rows.put("created_by", resultSet.getString("created_by"));
                rows.put("updated_by", resultSet.getString("updated_by"));
                rows.put("trans_created_on", resultSet.getString("trans_created_on"));
                rows.put("trans_updated_on", resultSet.getString("trans_updated_on"));
                results.add(rows);

                //  Stores retrieved serial_no values in a Set for further validation
                //retrievedSerialNumbers.add(resultSet.getString("serial_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        for (Map<String, String> row : results) {
            String serial_no_testMaster = row.get("serial_no");
            String testIs_active = row.get("is_active");
            String created_by = row.get("created_by");
            String updated_by = row.get("updated_by");
            String trans_created_on = row.get("trans_created_on");
            String trans_updated_on = row.get("trans_updated_on");
            System.out.println("Serial_noTestMaster: " + serial_no_testMaster + ", is_active: " + testIs_active + ", created_by: " + created_by + ", updated_by: " + updated_by + ", trans_created_on: " + trans_created_on + ", trans_updated_on: " + trans_updated_on);
        }
    }

    @Then("validate all data actually uploaded by {string} saved under {string} table with {string} for INB")
    public void validateAllDataActuallyUploadedBySavedUnderTableWithForINB(String assignedUserId, String tableName, String uploadDate) {
        preprodConnection = DBConnection.getPreprodConnection();
        uploadDate = dynamicUploadDate;
        String testStartTime = custBulkUpload.getTestStartTime();

        //  fetching results in customer master entered after last trans_created_on over stage table during upload flow
        String uploadAllDataInCustMaster_query = "SELECT response_data ->> 'F68721' as ACC_NO_MASTER, serial_no, project_id, is_active, created_by, updated_by, trans_created_on, trans_updated_on,\n" +
                "trans_created_int, trans_updated_int\n" +
                "FROM " + tableName + "\n" +
                "WHERE updated_by = " + assignedUserId + "\n" +
                "AND trans_updated_int = " + uploadDate + " \n" +
                "AND trans_updated_on > '" + testStartTime + "'\n" +
                "ORDER BY trans_updated_on ASC";
        System.out.println("Query to fetch All data from Customer Master table: " + uploadAllDataInCustMaster_query);

        List<Map<String, String>> custMaster_Results = new ArrayList<>();
        List<String> fetchedSerialNos_Master = new ArrayList<>();
        List<String> fetchedAccNos_Master = new ArrayList<>();

        try {
            resultSet = executeQuery(uploadAllDataInCustMaster_query, preprodConnection);
            while (resultSet.next()) {
                Map<String, String> rows = new HashMap<>();
                //  fetch serial_no and account_no for further validation
                String serialNoMaster = resultSet.getString("serial_no");
                String accNoMaster = resultSet.getString("ACC_NO_MASTER");

                rows.put("ACC_NO_MASTER", resultSet.getString("ACC_NO_MASTER"));
                rows.put("serial_no", resultSet.getString("serial_no"));
                rows.put("project_id", resultSet.getString("project_id"));
                rows.put("is_active", resultSet.getString("is_active"));
                rows.put("created_by", resultSet.getString("created_by"));
                rows.put("updated_by", resultSet.getString("updated_by"));
                rows.put("trans_created_on", resultSet.getString("trans_created_on"));
                rows.put("trans_updated_on", resultSet.getString("trans_updated_on"));
                rows.put("trans_created_int", resultSet.getString("trans_created_int"));
                rows.put("trans_updated_int", resultSet.getString("trans_updated_int"));
                custMaster_Results.add(rows);
                //  Store serial numbers immediately
                fetchedSerialNos_Master.add(serialNoMaster);
                fetchedAccNos_Master.add(accNoMaster);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        for (Map<String, String> row : custMaster_Results) {
            AccNO_Master = row.get("ACC_NO_MASTER");
            serial_no_custMaster = row.get("serial_no");
            INBProjectId_custMaster = row.get("project_id");
            is_active_custMaster = row.get("is_active");
            created_by_custMaster = row.get("created_by");
            updated_by_custMaster = row.get("updated_by");
            trans_created_on_custMaster = row.get("trans_created_on");
            trans_updated_on_custMaster = row.get("trans_updated_on");
            trans_created_int_custMaster = row.get("trans_created_int");
            trans_updated_int_custMaster = row.get("trans_updated_int");
            System.out.println("Acc No: " + AccNO_Master + ", Serial_no: " + serial_no_custMaster + ", ProjectId: " + INBProjectId_custMaster + ", is_active: " + is_active_custMaster + ", created_by: " + created_by_custMaster + ", updated_by: " + updated_by_custMaster + ", trans_created_on: " + trans_created_on_custMaster + "," + "trans_updated_on: " + trans_updated_on_custMaster);
        }

        //  assert that record count in stage is matching serial no count(total record) in customer master table
        int serial_no_custMasterCount = fetchedSerialNos_Master.size();
        log.info("Total records uploaded to Customer Master table: {}", serial_no_custMasterCount);

        if (recordCountStage != serial_no_custMasterCount) {
            missingCounter = true;
            int recordMismatchInMaster = recordCountStage - serial_no_custMasterCount;
            log.warn("Mismatch in record count. {} records out of {} were NOT uploaded to Customer Master table", recordMismatchInMaster, recordCountStage);
        } else {
            assertEquals(recordCountStage, serial_no_custMasterCount);
        }

        //  getting missing serial numbers (assuming stageSerialNos contains all serialNos/AccNos)
        if (missingCounter) {
            Set<String> missingAccNosInMaster = new HashSet<>(stageAccountNos);
            missingAccNosInMaster.removeAll(fetchedAccNos_Master);

            //  Convert Set<String> to a properly formatted SQL ARRAY string
            String formattedMissingAccNosInMaster = missingAccNosInMaster.stream()
                    .map(serial -> "'" + serial + "'").collect(Collectors.joining(", "));

            //  query to get all missing serial numbers corresponds to missing account numbers in master table
            String missingSerialNoInMaster_query = "SELECT serial_no\n" +
                    "FROM channelplay_aurora.b2b_company_master\n" +
                    "WHERE response_data ->> 'F68721' ilike ANY(ARRAY[" + formattedMissingAccNosInMaster + "])";

            System.out.println("Query to fetch missing SerialNos in Master: " + missingSerialNoInMaster_query);

            List<String> missingSerialNosInMaster = new ArrayList<>();

            try {
                resultSet = executeQuery(missingSerialNoInMaster_query, preprodConnection);
                while (resultSet.next()) {
                    String missingSerialNoMaster = resultSet.getString("serial_no");
                    missingSerialNosInMaster.add(missingSerialNoMaster);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to fetch data from database after query execution", e);
            } finally {
                closeResultSet(resultSet);
            }

            if (!missingSerialNosInMaster.isEmpty()) {
                log.error("Serial nos - {} for account nos - {} respectively NOT uploaded to the master table", missingSerialNosInMaster, missingAccNosInMaster);
                softAssert.fail("Mismatch in record count. for Account Nos - '" + missingAccNosInMaster + "' corresponds to Serial Nos - '" + missingSerialNosInMaster + "'");
            }
        } else {
            log.info("Great! All records from stage were successfully uploaded to the master table");
        }

        //  assert that is_active is 1 for each record
        for (Map<String, String> row : custMaster_Results) {
            if (!"1".equals(row.get("is_active"))) {
                log.error("Assertion failed! is_active is not 1 for serial_no in Master table: {}", serial_no_custMaster);
                softAssert.fail("Assertion failed! is_active is not 1 for serial_no in master table: " + serial_no_custMaster);
            }
        }
        softAssert.assertAll();
    }
    /*public SoftAssert getAPISoftAssert() {
        return softAssert;
    }
    public void getAPISoftAssert2() {
        softAssert.assertAll();
    }*/


}
