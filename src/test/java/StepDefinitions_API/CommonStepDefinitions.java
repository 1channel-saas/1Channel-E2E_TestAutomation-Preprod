package StepDefinitions_API;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.GetApiResponseObject;
import utilities_API.DBConnection;

public class CommonStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(CommonStepDefinitions.class);

//    This is common step definitions which can be directly accessed with below mentioned gherkin.
    private final CommonUtils_API commonUtilsApi;
    private final GetApiResponseObject getApiResponseObject;

    public CommonStepDefinitions() {
        this.commonUtilsApi = new CommonUtils_API();
        this.getApiResponseObject = GetApiResponseObject.getInstance();
    }


//    gherkin: validate token is generated
    @When("validate token is generated")
    public void validateTokenIsGenerated() {
        commonUtilsApi.getTokenFromResponse();
    }


//    gherkin: validate API call is success with status code 200 or handle error
    @Then("validate API call is success with status code 200 or handle error")
    public void apiCallIsSuccessWithStatusCode200() {
        commonUtilsApi.validateStatusCode();
    }



//    gherkin: validate ApiResponse execution time
    @Then("validate ApiResponse execution time")
    public void validateApiResponseExecutionTime() {
        commonUtilsApi.validateApiExecutionTime();
    }


//    ApiResponseBody success parameter
//    gherkin: validate "string" is "string" in responseBody
    @And("validate {string} is {string} in responseBody")
    public void validateIsInResponseBody(String keyValue, String expValue) {
        Response response = getApiResponseObject.getResponse();
        commonUtilsApi.validateDataInResponseBody(keyValue, expValue, response.asString());
    }


//    ApiResponseBody partial text parameter
//    gherkin: validate "string" contains partialText "string" in responseBody
    @And("validate {string} contains partialText {string} in responseBody")
    public void validateContainsPartialTextInResponseBody(String keyValue, String expParialValue) {
        Response response = getApiResponseObject.getResponse();
        commonUtilsApi.validatePartialDataInResponseBody(keyValue, expParialValue, response.asString());
    }


//    gherkin: establish connection with postgres DB
    @Then("establish connection with postgres DB")
    public void setUpConnections() {
        DBConnection.getPreprodConnection();
        DBConnection.getControllerConnection();
        logger.info("Establishing database connections for scenario");
    }

}
