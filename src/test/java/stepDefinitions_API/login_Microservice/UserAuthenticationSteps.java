package stepDefinitions_API.login_Microservice;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.Endpoints;
import resources_API.testUtils_API.GetApiResponseObject;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class UserAuthenticationSteps extends CommonUtils_API {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response;
    String loginResponse;
    JsonPath json;
    Endpoints endpoints;
    int noOfAssignedProjects;
    private final GetApiResponseObject getApiResponseObject;
    private final CommonUtils_API commonUtils = CommonUtils_API.getInstance();

    public UserAuthenticationSteps() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }


//    scenario - portalLogin

    @Given("add loginAPI payload with {string} and {string}")
    public void addLoginAPIPayloadWithAnd(String username, String password) {
        String login_Payload = String.format("{ \"username\": \"%s\", \"password\": \"%s\" }", username, password);
        reqspec = given().spec(commonUtils.requestSpec("loginAPI")).body(login_Payload);
        resspec = responseSpec();
    }

    @When("user submit {string} with {string} request for loginPortal")
    public void userSubmitWithPOSTRequestForLoginPortal(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);
        response = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();
        loginResponse = response.asString();

        getApiResponseObject.setResponse(response);
    }

    @Then("projectId {string} is validated")
    public void projectIdIsValidated(String project_id) {
        json = new JsonPath(loginResponse);
        int projectId = json.getInt("user.userProject[0].project.projectId");
        assertEquals(project_id, String.valueOf(projectId));
    }

    @Then("validate count of assigned projects and display project_name {string}")
    public void validateCountOfAssignedProjectsAndDisplayProject_name(String project_name) {
        json = new JsonPath(loginResponse);
        //projectCount
        noOfAssignedProjects = json.getInt("user.userProject.size()");
        assertEquals("1", String.valueOf(noOfAssignedProjects));
        //projectName
        for (int i = 0; i < noOfAssignedProjects; i++) {
            String projectName = json.getString("user.userProject[" + i + "].project.projectName");
            System.out.println("projectName " + i + ": " + projectName);

            if (noOfAssignedProjects > 0) {
                assertEquals(project_name, json.getString("user.userProject[0].project.projectName"));
                System.out.println("project logo img " + i + ":" + (json.get("user.userProject[" + i + "].project.projectLogoImage")));
            }
        }
    }




//    scenario - confirmLogin

    @Then("submit {string} api with {string} request for confirmLogin")
    public void submitApiWithRequestForConfirmLogin(String endpoint, String httpMethod) {
    }

}