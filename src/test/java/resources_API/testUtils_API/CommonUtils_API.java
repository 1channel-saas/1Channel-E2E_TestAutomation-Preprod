package resources_API.testUtils_API;

import com.test.channelplay.utils.AuthManager_API;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities_API.DBConnection;
import utilities_API.GetProperty_API;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonUtils_API {
    RequestSpecification commonRequest, fileUploadRequest;
    ResponseSpecification commonResponse;
    int statusCode;
    Response response;
    PrintStream logStream;
    private static CommonUtils_API instance = null;
    private final GetApiResponseObject getApiResponseObject;
    private String sessionToken;
    private static final Logger Log = LoggerFactory.getLogger(CommonUtils_API.class);


    // CommonUtils_API instance object created to concatenate all API logs in APITrace_logstream.txt when a step involves multiple API calls.
    public static synchronized CommonUtils_API getInstance() {
        if (instance == null) {
            instance = new CommonUtils_API();
        }
        return instance;
    }

    public CommonUtils_API() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        this.sessionToken = sessionToken;
    }




    //  ** method for RequestSpecification
    public RequestSpecification requestSpec(String API_log) {
        if (commonRequest == null) {
            try {
                logStream = new PrintStream(new FileOutputStream("API_logstream.txt"));
                logStream.println("INFO: Starting API requests for: " + API_log);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Logging file could not be created", e);
            }
            commonRequest = new RequestSpecBuilder().setBaseUri(GetProperty_API.value("baseURL"))
                    .addFilter(RequestLoggingFilter.logRequestTo(logStream))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL, logStream)) //Log all response details to file
                    .setContentType(ContentType.JSON)
                    .build();
            return commonRequest;
        }
        else {
            logStream.println("INFO: Continuing API requests for: " + API_log);
        }
        return commonRequest;
    }


    //  ** method for RequestSpecification for file uploads
    public RequestSpecification requestSpecFileUpload(String API_log) {
        if (fileUploadRequest == null) {
            try {
                if (logStream == null) {
                    logStream = new PrintStream(new FileOutputStream("API_logstream.txt"));
                }
                logStream.println("INFO: File upload API request for: " + API_log);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Logging file could not be created", e);
            }
            fileUploadRequest = new RequestSpecBuilder()
                    .setBaseUri(GetProperty_API.value("baseURL"))
                    .addFilter(new FileUploadLoggingFilter(logStream))
                    .build();
            return fileUploadRequest;
        }
        else {
            logStream.println("INFO: Continuing API requests for: " + API_log);
        }
        return fileUploadRequest;
        }


    //    ** method for ResponseSpecification
    public ResponseSpecification responseSpec() {
        commonResponse = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(10000L))
                .expectContentType(ContentType.JSON)
                .build();
        return commonResponse;
    }


//    ** method for StatusCode
    public void validateStatusCode() {
        response = getApiResponseObject.getResponse();
        statusCode = response.statusCode();
        //  Safely extract status codes from response body
        Integer bodyStatusCode = getJsonPath(response.asString(), "statusCode");
        Integer bodyStatus = getJsonPath(response.asString(), "status");

        if ((statusCode == 200 || statusCode == 201 || statusCode == 204) ||
                (response.statusLine().equals("HTTP/1.1 200") ||
                        (bodyStatusCode != null && (bodyStatusCode.equals(200) || bodyStatusCode.equals(201))) ||
                        (bodyStatus != null && (bodyStatus.equals(200) || bodyStatus.equals(201))))) {
            Log.info("API response is successful with status code: {}", statusCode);
        } else {
            Map<Integer, String> statusMessages = Map.of(
                    400, "API returned 400 Bad Request",
                    401, "API returned 401 Unauthorized",
                    204, "API returned 204 No Content",
                    403, "API returned 403 Forbidden",
                    404, "API returned 404 Not Found",
                    405, "API returned 405 Method Not Allowed",
                    409, "Conflict",
                    422, "API returned 422 Unprocessable Entity",
                    500, "API returned 500 Internal Server Error",
                    503, "API returned 503 Service Unavailable"
            );
            //  API http inline response is 200 but response body has a failure status code
            if (statusCode == 200) {
                Log.error(statusMessages.getOrDefault(statusCode, "Unexpected status code: " + getJsonPath(response.asString(), "statusCode")));
            }
            else
                Log.warn(statusMessages.getOrDefault(statusCode, "Unexpected status code: " + statusCode));

            String failureMessage = getJsonPath(response.asString(), "message");
            String failureReason = getJsonPath(response.asString(), "responseData");

            if (failureMessage != null && !failureMessage.isEmpty()) {
                Log.info("Failure Message: {}", failureMessage);
            }

            if (failureReason != null && !failureReason.isEmpty()) {
                Log.warn("Failure Reason: {}", failureReason);
            }

            //  API http inline response is 200 but response body has a failure status code
            if (statusCode == 200) {
                throw new AssertionError("API failing with status code: " + getJsonPath(response.asString(), "statusCode"));
            }
            else
                throw new AssertionError("API returned failure status code: " + statusCode);
        }
    }


//    ** method for ApiExecutionTime
    public void validateApiExecutionTime() {
        response = getApiResponseObject.getResponse();
        long response_time = response.time();
        Assert.assertTrue(response_time < 10000);
        Log.info("API Response Time: {}", response_time + " ms");
    }


//    ** method for get Token from login APi Response
    public void getTokenFromResponse() {
        response = getApiResponseObject.getResponse();
        String bearerToken = response.jsonPath().getString("token");
        Log.info("current Token: {}", bearerToken);
    }


//    ** generic method to fetch Token from any API Response
    public String getToken(Response response) {
        if (response != null) {
            return response.jsonPath().getString("token");
        }
        else {
            throw new NullPointerException("This Response is null");
        }
    }


//    ** method for get Token from loginOTP APi Response for mobile App
    public String getOTPtoken(Response response) {
        if (response != null) {
            return response.jsonPath().getString("responseData.token");
        }
        else {
            throw new NullPointerException("This Response is null");
        }
    }


//    ** method for get Token from web application UI local storage
    public String getTokenFromUI() {
        return AuthManager_API.getAuthToken();
    }


//    ** method for validate success in ResponseBody
    public void validateDataInResponseBody(String keyCode, String expCode, String responseBody) {
        JsonPath js = new JsonPath(responseBody);
        assertEquals(expCode, js.getString(keyCode));
    }


//    ** method for validate success in ResponseBody with partial string match
    public void validatePartialDataInResponseBody(String keyCode, String expPartialText, String responseBody) {
        JsonPath js = new JsonPath(responseBody);
        String partialText = js.get(keyCode);
        assertTrue(partialText.contains(expPartialText));
    }


//    ** method for JsonPath
    public <T> T getJsonPath(String response, String key) {
        JsonPath js = new JsonPath(response);
        return js.get(key);
    }


//    ** method to add random String
    public static String generateRandomString(int n){
        String alphaNumericString = "abcdefghijklmnopqrstuvxyz0123456789";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index  = (int)(alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }
        return sb.toString();
    }


//    ** method to read data from json Files
    public String readJsonFile(String filePath) {
        String jsonContent = "";
        try {
            jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
        throw new RuntimeException("Error reading JSON file from path: " + filePath, e);
        }
        return jsonContent;
    }


    //  ** utility method to check if all provided strings are non-null and non-empty
    public boolean areAllNotEmpty(String... strings) {
        for (String str : strings) {
            if (str == null || str.isEmpty()) {
                return false;
            }
        }
        return true;
    }


    //  ** Generate current userTime
    public String getCurrentUserTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }


    //  ** Get the system's default time zone
    public String getDefaultTimeZone() {
        return ZoneId.systemDefault().getId();
    }


/*    public String getJsonPath(String response, String key) {
        JsonPath js = new JsonPath(response);
        Object value = js.get(key);
        return value != null ? value.toString() : null;
    }   */



    //  ** method for sleep
    public void sleepInSeconds(long seconds) {
        if (seconds < 0 || seconds > Long.MAX_VALUE / 1000) {
            throw new IllegalArgumentException("Seconds value is too large or negative.");
        }
        long milliseconds = seconds * 1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }



    //  ** methos for sessionTimeout
//    sessionTimeout() method actively checks the session every minute, useful if the session to expire before scheduled time.
//    currently not in use AddActivity_testUserSteps only one scenario required to handle session.
    public void sessionTimeout() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable checkSession = () -> {
            boolean sessionValid = checkSessionStatus();    // Check if the session is still valid
            if (!sessionValid) {
                scheduler.shutdown();   //Stop further scheduling if the session has expired
            }
        };

        scheduler.scheduleAtFixedRate(checkSession, 0, 1, TimeUnit.MINUTES);

        try {
            if (!scheduler.awaitTermination(60, TimeUnit.MINUTES)) {
                scheduler.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow(); // Handle interruption by shutting down immediately
            Thread.currentThread().interrupt(); // Restore interrupted status
            throw new RuntimeException(e);
        }
    }
    public boolean checkSessionStatus() {
        response = getApiResponseObject.getResponse();
        int statusCode = response.getStatusCode();

        // Check the response status code to determine if the session is still valid
        if (statusCode == 200) {
            System.out.println("Session is still valid.");
            return true;
        }
        else if (statusCode == 401) {
            System.out.println("Session has expired.");
            return false;
        }
        else {
            System.out.println("Unexpected status code: " +statusCode);
            return false;
        }
    }



    //  method for test DB connections
    public void testDBConnection() {

        // Test Preprod DB connection
        Connection preprodConnection = DBConnection.getPreprodConnection();
        if (preprodConnection != null) {
            Log.info("Preprod database test connection is successful");
        } else {
            throw new RuntimeException("test connection for Preprod database Failed");
        }

        // Test Controller DB connection
        Connection controllerConnection = DBConnection.getControllerConnection();
        if (controllerConnection != null) {
            Log.info("Controller database test connection is successful");
        } else {
            throw new RuntimeException("test connection for Controller database Failed");
        }
    }

    //  method for DB execute query

    /**
     * Execute a query on a specific database connection.
     *
     * @param query      The SQL query to execute.
     * @param connection The database connection to use.
     * @return ResultSet containing the query results.
     */
    public static ResultSet executeQuery(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to execute query: " + query, e);
        }
    }

    /**
     * Close the ResultSet safely.
     *
     * @param resultSet The ResultSet to close.
     */
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}