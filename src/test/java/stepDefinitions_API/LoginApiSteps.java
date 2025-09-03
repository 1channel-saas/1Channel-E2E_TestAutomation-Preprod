package stepDefinitions_API;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources_API.payload_API.Login.Login_Payload;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;
import utilities_API.GetProperty_API;

import static io.restassured.RestAssured.given;

public class LoginApiSteps extends CommonUtils_API {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    static Response response;
    Endpoints endpoints;
    private final GetApiResponseObject getApiResponseObject;
    Login_Payload payload = new Login_Payload();


    //This will ensure single CommonUtils instance will be created throughout the execution.
    //Calling getInstance() ensures you are interacting with that single shared instance.
    //This is particularly useful if CommonUtils maintains state that should be consistent across different parts of your application.i.e. Logging.txt
    private final CommonUtils_API commonUtilsApi = CommonUtils_API.getInstance();

    public LoginApiSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }


    //  1Channel portal + app
    @When("user submit {string} with {string} request for login")
    public void userSubmitWithPOSTRequestForLogin(String endpoint, String httpMethod) {
        reqspec = given().spec(commonUtilsApi.requestSpec(endpoint)).body(payload.loginPayload());
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(response);
        commonUtilsApi.validateStatusCode();
    }



    //  1Channel portal + app with test user creds
    @When("user submit {string} with {string} request for login with testUser creds")
    public void userSubmitWithPOSTRequestForLoginWithTestUser(String endpoint, String httpMethod) {
        reqspec = given().spec(commonUtilsApi.requestSpec(endpoint)).body(payload.loginTestUserPayload());
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(response);
        commonUtilsApi.validateStatusCode();
    }



    //  Loyalty Portal
    @When("user submit {string} with {string} request for loyalty")
    public void userSubmitWithRequestForLoyalty(String endpoint, String POST) {
        reqspec = given().spec(commonUtilsApi.requestSpec("loginAPI_loyalty")).body(payload.loginLoyaltyPayload());
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(response);
    }



    //  Loyalty mobile app - request_login_otp
    @When("user submit {string} with {string} request for App-login with request_login_otp")
    public void userSubmitWithRequestForApploginWithRequest_login_otp(String endpoint, String httpMethod) {
        reqspec = given().spec(commonUtilsApi.requestSpec("request_login_otp")).body(payload.loginMobileReqLoginOtpPayload());
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().queryParam("projectId", GetProperty_API.value("ProjectIdLoyalty"))
                .post(endpoints.getValOfEndpoint()).then().extract().response();

        getApiResponseObject.setResponse(response);
    }



    //  Loyalty mobile app - requestOTP
    @When("user submit {string} with {string} request for App-login with requestOTP")
    public void userSubmitWithRequestForAppLoginWithRequestOTP(String endpoint, String httpMethod) {
        reqspec = given().spec(commonUtilsApi.requestSpec("requestOTP")).queryParam("projectId", GetProperty_API.value("ProjectIdLoyalty"))
                .queryParam("sendOtp", GetProperty_API.value("sendOTP")).body(payload.loginMobileReqOtpPayload());
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(response);
    }

}