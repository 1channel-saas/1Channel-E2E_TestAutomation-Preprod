package stepDefinitions_API.Transaction_Microservice.activities;

import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;
import utilities_API.DBConnection;
import utilities_API.GetProperty_API;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class AddEditActivityAPI_Offsite_testUser extends CommonUtils_API {

    private static final Logger log = LoggerFactory.getLogger(AddEditActivityAPI_Offsite_testUser.class);

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response, uploadFieldImageResponse, addEditActivity_OffsiteResponse, deleteActivityResponse;
    Endpoints endpoints;
    JsonPath json;
    int currActivityId;
    String bearerToken, jsonContentAddEditAct, uploadImage_APIRespName, currSerialKey, is_active, trans_created_int, trans_unique_key;
    private Connection preprodConnection;
    ResultSet resultSet;
    LocalDateTime dateTime = LocalDateTime.now();
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();

    public AddEditActivityAPI_Offsite_testUser() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();

    }




    //  scenario - offsiteActivity

    @When("create request for uploadFieldImage api for offsiteActivity")
    public void createRequestForUploadFieldImageApiForOffsiteActivity() {
        response = getApiResponseObject.getResponse();
        bearerToken = getToken(response);
        File imageFile = new File(GetProperty.value("imgUploadPath"));

        String projectId = GetProperty_API.value("testUserProjectId");
        int moduleType = 3378;  // fixed for offsiteActivity

        reqspec = given().spec(commonUtils.requestSpecFileUpload("uploadFieldImage"))
                .header("Authorization", "Bearer " + bearerToken)
                .formParam("projectId", projectId)
                .formParam("moduleType", moduleType)
                .multiPart("image", imageFile);
        resspec = responseSpec();
    }

    @Then("submit {string} api with {string} request to fetch image path for offsiteActivity")
    public void submitApiWithRequestToFetchImagePathForOffsiteActivity(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);
        uploadFieldImageResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(uploadFieldImageResponse);
    }

    @And("fetch uploaded image name from responseBody to use in request of addEditActivity for offsiteActivity")
    public void fetchUploadedImageNameFromResponseBodyToUseInRequestOfAddEditActivityForOffsiteActivity() {
        String fetchUploadFieldImageResp = getApiResponseObject.getResponse().asString();
        uploadImage_APIRespName = getJsonPath(fetchUploadFieldImageResp, "responseData.name");
        System.out.println("image name in API response: " + uploadImage_APIRespName);
    }

    @When("create request for addEditActivity api for offsiteActivity")
    public void createRequestForAddEditActivityApiForOffsiteActivity() {
        String transUniqueKey = String.valueOf(System.currentTimeMillis());
        //  trans_CreatedOn & trans_UpdatedOn
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        //  perform_Date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = dateTime.format(dateFormatter);

        jsonContentAddEditAct = readJsonFile("src/test/java/resources_API/payload_API/json_Files/activities/testUser/addEditActivity_Offsite.json");

        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{project_Id}}", GetProperty_API.value("testUserProjectId"));
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{app_Version}}", GetProperty.value("appVersion"));
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{trans_UniqueKey}}", transUniqueKey);
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{trans_CreatedOn}}", formattedDateTime);
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{trans_UpdatedOn}}", formattedDateTime);
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{uploadImage_APIRespName}}", uploadImage_APIRespName);
        jsonContentAddEditAct = jsonContentAddEditAct.replace("{{perform_Date}}", formattedDate);

        reqspec = given().spec(commonUtils.requestSpec("addEditActivity_Offsite_testUser")).body(jsonContentAddEditAct);
        resspec = responseSpec();
    }

    @Then("submit {string} api with {string} request to add offsiteActivity")
    public void submitApiWithRequestToAddOffsiteActivity(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);
        addEditActivity_OffsiteResponse = reqspec.when().header("Authorization", "Bearer " + bearerToken)
                .post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(addEditActivity_OffsiteResponse);
    }

    @Then("verify activityId and serialKey is present in responseBody")
    public void verifyActivityIdAndSerialKeyIsPresentInResponseBody() {
        json = new JsonPath(addEditActivity_OffsiteResponse.asString());
        currActivityId = json.getInt("responseData.activityId");
        currSerialKey = json.getString("responseData.serialKey");
        log.info("offsiteAct_testUser ActivityId: {} | offsiteAct_testUser SerialKey: {}", currActivityId, currSerialKey);
        System.out.println("offsite activity created and verified");
    }

    @Then("verify whether created activity is present in database {string} table for offsiteActivity")
    public void verifyWhetherCreatedActivityIsPresentInDatabaseTableForOffsiteActivity(String tablename) {
        preprodConnection = DBConnection.getPreprodConnection();

        String offsiteActTestUser_query = "SELECT module_type, project_id, is_active, trans_created_int, trans_unique_key\n" +
                "FROM " + tablename + "\n" +
                "WHERE id = " + currActivityId + " AND serial_no = '" + currSerialKey + "'";
        System.out.println("offsiteActTestUser_query: " + offsiteActTestUser_query);

        List<Map<String, String>> offsiteActTestUserResults = new ArrayList<>();

        if (!(currActivityId == 0 || currSerialKey == null) || !(currSerialKey.isEmpty())) {
            try {
                resultSet = executeQuery(offsiteActTestUser_query, preprodConnection);
                while (resultSet.next()) {
                    Map<String, String> row = new HashMap<>();
                    row.put("module_type", resultSet.getString("module_type"));
                    row.put("project_id", resultSet.getString("project_id"));
                    row.put("is_active", resultSet.getString("is_active"));
                    row.put("trans_created_int", resultSet.getString("trans_created_int"));
                    row.put("trans_unique_key", resultSet.getString("trans_unique_key"));
                    offsiteActTestUserResults.add(row);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to fetch data from database after query execution", e);
            } finally {
                closeResultSet(resultSet);
            }

            for (Map<String, String> row : offsiteActTestUserResults) {
                String module_type = row.get("module_type");
                String project_id = row.get("project_id");
                is_active = row.get("is_active");
                trans_created_int = row.get("trans_created_int");
                trans_unique_key = row.get("trans_unique_key");

                log.info("module_type: {} | project_id: {} | is_active: {} | trans_created_int: {} | trans_unique_key: {}",
                        module_type, project_id, is_active, trans_created_int, trans_unique_key);
            }
        } else {
            log.error("Either currActivityId is 0 or currSerialKey is null/empty, cannot fetch activity from DB");
        }
    }

    @And("validate created activity is Active and created date is today and trans unique key is generated in database for offsiteActivity")
    public void validateCreatedActivityIsActiveAndCreatedDateIsTodayAndTransUniqueKeyIsGeneratedInDatabaseForOffsiteActivity() {
        //  assert activity is active
        assertEquals(1, Integer.parseInt(is_active));

        //  assert activity created date is today
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String transcreatedDate = dateTime.format(dateFormatter);
        assertEquals(transcreatedDate, trans_created_int);

        //  trans unique key is generated in DB
        Assert.assertFalse(trans_unique_key.isEmpty());

        System.out.println("offsite activity validation in DB completed");
    }

    @Then("submit {string} api with {string} request to delete created offsite activity")
    public void submitApiWithRequestToDeleteCreatedOffsiteActivity(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);

        reqspec = given().spec(commonUtils.requestSpec("deleteActivityData_testUser"))
                .header("Authorization", "Bearer " + bearerToken)
                .queryParam("projectId", GetProperty_API.value("testUserProjectId"))
                .queryParam("moduleType", 3378)
                .queryParam("activityId", currActivityId);

        deleteActivityResponse = reqspec.when().delete(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(deleteActivityResponse);
        log.info("Activity deleted successfully for activityId: {}", currActivityId);
    }

}
