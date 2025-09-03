package stepDefinitions_API.setting_Microservice;

import com.test.channelplay.utils.SharedTestData;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo_API.ResponsePojo.Setting.CreateActivitySettingResp_pojo;
import resources_API.payload_API.Setting.ActivitiesSettings_payload;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class ActivitySettingSteps extends CommonUtils_API {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response, createActivitiesSettingsResponse;
    Endpoints endpoints;
    String bearerToken;
    CreateActivitySettingResp_pojo createActivitySettingRespPojo;
    ActivitiesSettings_payload activitiesSettingsPayload = new ActivitiesSettings_payload();
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();

    public ActivitySettingSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }




    //  scenario - createActivitiesSettings

    @When("add request for createActivitiesSettings")
    public void addRequestForCreateActivitiesSettings() {
        response = getApiResponseObject.getResponse();
        bearerToken = getToken(response);
        reqspec = given().spec(commonUtils.requestSpec("createActivitiesSettings")).body(activitiesSettingsPayload.createActivityPayload());
        resspec = responseSpec();
    }

    @Then("user submit {string} with {string} request for createActivitiesSettings")
    public void userSubmitWithRequestForCreateActivitiesSettings(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);
        createActivitiesSettingsResponse = reqspec.when().header("Authorization", "Bearer " + bearerToken)
                .post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        createActivitySettingRespPojo = createActivitiesSettingsResponse.as(CreateActivitySettingResp_pojo.class);
    }

    @Then("validate createActivitiesSettings response")
    public void validateCreateActivitiesSettingsResponse() {
        //  projectId
        int currentProjectId = createActivitySettingRespPojo.getResponseData().getProjectId();
        System.out.println("Create Activity projectId: " + currentProjectId);

        //  activityId
        String currentActivitySettId = String.valueOf(createActivitySettingRespPojo.getResponseData().getActivityId());
        System.out.println("Create Activity activityId: " + currentActivitySettId);

        //  activityName
        String currentActivityName = createActivitySettingRespPojo.getResponseData().getActivityName();
        System.out.println("Created custom Activity from API: " + currentActivityName);
        assertEquals(activitiesSettingsPayload.getActivityName(), currentActivityName);

        //  store in currentActivityName for access from UI classes
        SharedTestData.setCurrentActivityName(currentActivityName);

        //  serialKey
        String currentSerialKey = createActivitySettingRespPojo.getResponseData().getSerialKey();
        assertEquals(activitiesSettingsPayload.getSerialKey(), currentSerialKey);
    }

}
