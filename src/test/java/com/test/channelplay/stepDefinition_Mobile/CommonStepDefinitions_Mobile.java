package com.test.channelplay.stepDefinition_Mobile;

import com.test.channelplay.mobile.CRMAppLoginObject;
import com.test.channelplay.utils.CommonUtils;
import com.test.channelplay.utils.GetProperty;
import com.test.channelplay.utils.MobileTestBase;
import io.cucumber.java.en.Given;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources_API.payload_API.Login.Login_Payload;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;

import static io.restassured.RestAssured.given;

public class CommonStepDefinitions_Mobile extends MobileTestBase {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    String currResponse, projectName, firstName, projectId;
    Endpoints endpoints;
    Response response;
    JsonPath json;
    private final GetApiResponseObject getApiResponseObject;
    Login_Payload payload = new Login_Payload();
    CRMAppLoginObject appLogin = new CRMAppLoginObject();
    private final CommonUtils_API commonUtilsApi = CommonUtils_API.getInstance();
    CommonUtils commonUtils = new CommonUtils();

    public CommonStepDefinitions_Mobile() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }




    //  gherkin: User logIn to CRM mobile App with testUser creds
    @Given("User logIn to CRM mobile App with testUser creds")
    public void userLogInToCRMMobileAppWithTestUserCreds() {
        //  call mobile app login
        appLogin.loginToMobile(GetProperty.value("testUsername"),GetProperty.value("testPassword"));

        // call API to fetch user data
        reqspec = given().spec(commonUtilsApi.requestSpec("loginAPI")).body(payload.loginTestUserPayload());
        resspec = commonUtilsApi.responseSpec();
        endpoints = Endpoints.valueOf("loginAPI");

        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(response);
        currResponse = response.asString();

        //  validate response is 200
        commonUtilsApi.validateStatusCode();

        json = new JsonPath(currResponse);

        // projectCount
        int noOfAssignedProjects = json.getInt("user.userProject.size()");

        // projectId
        for (int i = 0; i < noOfAssignedProjects; i++) {
            projectId = json.getString("user.userProject[" + i + "].project.projectId");
            System.out.println("projectId for project " + i + ": " + projectId);
        }

        // projectName
        for (int j = 0; j < noOfAssignedProjects; j++) {
            String projectNames = json.getString("user.userProject[" + j + "].project.projectName");
            System.out.println("projectName for project " + j + ": " + projectNames);

            if (json.getInt("user.userProject[" + j + "].project.projectId") == Integer.parseInt(projectId)) {
                projectName = json.getString("user.userProject[" + j + "].project.projectName");
                firstName = json.getString("user.firstName");
            }
            System.out.println("projectName: " + projectName);
            System.out.println("firstName: " + firstName);
        }

        // call landingPage
        commonUtils.sleep(1000);
        appLogin.verifyOnLandingPage(projectName, firstName);
        commonUtils.sleep(1000);

        //  close Hamburger Menu
        appLogin.closeHamburgerMenu();
        commonUtils.sleep(1000);
    }

}