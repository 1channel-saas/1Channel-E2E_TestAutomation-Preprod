package stepDefinitions_API.setting_Microservice;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import lombok.Setter;
import resources_API.payload_API.Setting.CompanySettings_AccSetUp_payload;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;
import com.test.channelplay.utils.SharedTestData;

import static io.restassured.RestAssured.given;

public class SettingsAndConfigSteps extends CommonUtils_API {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response, getCompanySettingsResp;
    String bearerToken, getCompanySettingsResponse;
    Endpoints endpoints;
    JsonPath json;
    //  Instance variable to store singularName
    @Setter
    @Getter
    private static String singularNameJson;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();
    CompanySettings_AccSetUp_payload companySett_AccSetUpPayload = new CompanySettings_AccSetUp_payload();

    public SettingsAndConfigSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }
    

    

    //  scenario - getCompanySettings (Account SetUp -> Company Settings)

    @Then("add request for getCompanySettings API")
    public void addRequestForGetCompanySettingsAPI() {
        response = getApiResponseObject.getResponse();
        bearerToken = getToken(response);
        reqspec = given().spec(commonUtils.requestSpec("getCompanySettings_AccSetUp")).body(companySett_AccSetUpPayload.companySettings_AccSetUp_payload());
        resspec = responseSpec();
    }

    @Then("submit {string} with {string} request for b2b-2943")
    public void submitWithRequestForBB(String endpoint, String POST) {
        endpoints = Endpoints.valueOf(endpoint);
        getCompanySettingsResp = reqspec.when().header("Authorization", "Bearer " + bearerToken)
                .post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getCompanySettingsResponse = getCompanySettingsResp.asString();
    }

    @And("validate response data to get singularName")
    public void validateResponseDataToGetSingularName() {
        json = new JsonPath(getCompanySettingsResponse);
        //  Store in local static variable
        singularNameJson = json.get("responseData.singularName");
        System.out.println("singularName from getCompanySettings API response: " + singularNameJson);
        
        //  store in SharedTestData for access from UI classes
        SharedTestData.setSingularNameJson(singularNameJson);
    }

}