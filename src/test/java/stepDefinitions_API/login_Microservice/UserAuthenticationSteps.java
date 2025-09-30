package stepDefinitions_API.login_Microservice;

import com.test.channelplay.utils.GetProperty;
import io.cucumber.java.en.And;
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
import utilities_API.DBConnection;
import utilities_API.GetProperty_API;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class UserAuthenticationSteps extends CommonUtils_API {

    RequestSpecification reqspec;
    ResponseSpecification resspec;
    Response response, resetPasswordResponse, passValidateTokenResponse, updatePassResponse, resetPassMobileResponse,
            resetPassMobileValidateOtpResponse;
    String loginResponse, passValidateTokenPayload, updatePassPayload, updatePassUserEmail, generatedUsername, generatedPassword,
            generatedFirstName, generatedLastName, generatedEmail, fetchedOTP;
    int passValidateTokenUserId, fetchedUserId;
    JsonPath json;
    Endpoints endpoints;
    int noOfAssignedProjects;
    Connection preprodConnection;
    ResultSet resultSet;
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




    //  scenario - forgotPasswordAPI_Email OR resetPassword

    // reset password via email
    @Given("user submit {string} with {string} request for forgotPasswordAPI_Email")
    public void userSubmitWithRequestForForgotPasswordAPIEmail(String endpoint, String httpMethod) {
        //  submitting resetPassword api generates UUID token internally which are not exposed in response. Therefore we are using specific email and
        //  corresponding token from assistive_controller_qa (from b2b_reset_password_tokens).
        //  We are assuming that this token is already available in our test environment.
        String resetPassUserEmail = "resetpass@mailinator.com";
        String resetPassPayload = String.format("{ \"email\": \"%s\"}", resetPassUserEmail);
        reqspec = given().spec(commonUtils.requestSpec("resetPassword")).body(resetPassPayload);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        resetPasswordResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(resetPasswordResponse);
    }

    @When("user submit {string} with {string} request for passValidateToken")
    public void userSubmitWithRequestForPassValidateToken(String endpoint, String httpMethod) {
        //  submitting resetPassword api generates UUID token internally which are not exposed in response. Therefore we are using specific email and
        //  corresponding token from assistive_controller_qa (from b2b_reset_password_tokens).
        //  We are assuming that this token is already available in our test environment.
        String passValidateToken = "97d3022a-83ec-4091-bf7f-e3882be86585";
        passValidateTokenPayload = String.format("{ \"token\": \"%s\"}", passValidateToken);
        reqspec = given().spec(commonUtils.requestSpec("passValidateToken")).body(passValidateTokenPayload);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        passValidateTokenResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(passValidateTokenResponse);
    }

    @And("validate response data for passValidateToken")
    public void validateResponseDataForPassValidateToken() {
        passValidateTokenUserId = getJsonPath(passValidateTokenResponse.asString(), "responseData.userId");
    }

    @Then("add request for updatePassword")
    public void addRequestForUpdatePassword() {
        //  we are using same signup payload for update password AddActivity_testUserSteps json structure is same. Only change placeholder values when required.
        updatePassPayload = readJsonFile("src/test/java/resources_API/payload_API/json_Files/userAuth/signupPayload.json");
        updatePassUserEmail = "resetpass@mailinator.com";
        generatedUsername = updatePassUserEmail;
        generatedPassword = "123456789";
        generatedFirstName = "";
        generatedLastName = "";
        generatedEmail = updatePassUserEmail;

        updatePassPayload = updatePassPayload.replace("\"user_name\"", "\"" + generatedUsername + "\"");
        updatePassPayload = updatePassPayload.replace("\"password_hash\"", "\"" + generatedPassword + "\"");
        updatePassPayload = updatePassPayload.replace("\"first_name\"", "\"" + generatedFirstName + "\"");
        updatePassPayload = updatePassPayload.replace("\"last_name\"", "\"" + generatedLastName + "\"");
        updatePassPayload = updatePassPayload.replace("\"email_id\"", "\"" + generatedEmail + "\"");
        updatePassPayload = updatePassPayload.replace("\"access_token\"", "\"" + "access-token-0.000123" + generateRandomString(5) + "\"");
        updatePassPayload = updatePassPayload.replace("\"refresh_token\"", "\"" + "access-token-0.100456" + generateRandomString(5) + "\"");

        reqspec = given().spec(commonUtils.requestSpec("updatePassword")).body(updatePassPayload);
        resspec = responseSpec();
    }

    @When("user submit {string} with {string} request for updatePassword")
    public void userSubmitWithRequestForUpdatePassword(String endpoint, String httpMethod) {
        endpoints = Endpoints.valueOf(endpoint);
        updatePassResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(updatePassResponse);
    }

    @And("validate response data for updatePassword")
    public void validateResponseDataForUpdatePassword() {
//        assertEquals(passValidateTokenUserId, (int) getJsonPath(updatePassResponse.asString(), "responseData.userId"));
        assertEquals(updatePassUserEmail, getJsonPath(updatePassResponse.asString(), "responseData.userName"));
    }




    //  scenario - forgotPasswordAPI_Mobile OR resetPassword

    // reset password via mobile number
    @Given("user submit {string} with {string} request for forgotPasswordAPI_Mobile")
    public void userSubmitWithRequestForForgotPasswordAPIMobile(String endpoint, String httpMethod) {
        //  submitting resetPassword api generates UUID token internally which are not exposed in response. Therefore we are using specific email and
        //  corresponding token from assistive_controller_qa (from b2b_reset_password_tokens).
        //  We are assuming that this token is already available in our test environment.
        String mobileNumber = GetProperty_API.value("mobileNumber");
        String resetPassMobile_payload = String.format("{ \"mobileNumber\": \"%s\"}", mobileNumber);
        reqspec = given().spec(commonUtils.requestSpec("requestOTPV2")).body(resetPassMobile_payload);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        resetPassMobileResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(resetPassMobileResponse);
    }

    @Then("fetch otp from DB table {string} for forgotPasswordAPI_Mobile")
    public void fetchOtpFromDBTableForForgotPasswordAPI_Mobile(String tableName) {
        preprodConnection = DBConnection.getControllerConnection();
        String mobileNoOtp = GetProperty.value("mobileNo_forgotPassword");

        String fetchOtpFromDB_query = "SELECT id, user_email, user_id, phone_number, otp\n" +
                "FROM " + tableName + " \n" +
                "WHERE phone_number = '" + mobileNoOtp + "'\n" +
                "ORDER BY id DESC LIMIT 1";
        System.out.println("Query to fetch token from: " + fetchOtpFromDB_query);

        List<Map<String, String>> fetchOtpResult = new ArrayList<>();

        try {
            resultSet = executeQuery(fetchOtpFromDB_query, preprodConnection);
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("id", resultSet.getString("id"));
                row.put("user_email", resultSet.getString("user_email"));
                row.put("user_id", resultSet.getString("user_id"));
                row.put("phone_number", resultSet.getString("phone_number"));
                row.put("otp", resultSet.getString("otp"));
                fetchOtpResult.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }

        String otp = null;
        String userId = null;
        for (Map<String, String> row : fetchOtpResult) {
            String Id = row.get("id");
            String userEmail = row.get("user_email");
            userId = row.get("user_id");
            String phoneNumber = row.get("phone_number");
            otp = row.get("otp");
            System.out.println("Fetched OTP details - id: " + Id + ", user_email: " + userEmail + ", user_id: " + userId +
                    ", phone_number: " + phoneNumber + ", otp: " + otp);
        }
        fetchedOTP = String.valueOf(otp);
        fetchedUserId = Integer.parseInt(String.valueOf(userId));
    }

    @Then("user submit {string} with {string} request to validate otp for forgotPasswordAPI_Mobile")
    public void userSubmitWithRequestToValidateOtpForForgotPasswordAPI_Mobile(String endpoint, String httpMethod) {
        String resetPassValidateOtpMobile_payload = String.format("{ \"otp\": \"%s\", \"userId\": %s }", fetchedOTP, fetchedUserId);

        reqspec = given().spec(commonUtils.requestSpec("validateOTPV2")).body(resetPassValidateOtpMobile_payload);
        resspec = responseSpec();
        endpoints = Endpoints.valueOf(endpoint);

        resetPassMobileValidateOtpResponse = reqspec.when().post(endpoints.getValOfEndpoint()).then().spec(resspec).extract().response();

        getApiResponseObject.setResponse(resetPassMobileValidateOtpResponse);
    }

}