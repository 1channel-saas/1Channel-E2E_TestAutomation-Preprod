# BrowserMob Proxy Implementation Plan for API Interception

## Overview
This document outlines the complete implementation plan for integrating BrowserMob Proxy into the existing Appium + RestAssured test framework to capture and validate API calls triggered by UI actions.

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Phase 1: Setup & Dependencies](#phase-1-setup--dependencies)
3. [Phase 2: Driver Integration](#phase-2-driver-integration)
4. [Phase 3: API Interceptor Helper](#phase-3-api-interceptor-helper)
5. [Phase 4: Validation Integration](#phase-4-validation-integration)
6. [Phase 5: Update Test Classes](#phase-5-update-test-classes)
7. [Phase 6: Test Implementation](#phase-6-test-implementation)
8. [Phase 7: Configuration](#phase-7-configuration)
9. [Phase 8: Challenges & Solutions](#phase-8-challenges--solutions)
10. [Phase 9: Benefits](#phase-9-benefits)
11. [Implementation Checklist](#implementation-checklist)

## Architecture Overview

```
┌─────────────┐      ┌──────────────┐      ┌──────────────┐
│  Mobile App │ <--> │ BrowserMob   │ <--> │   Backend    │
│   (Appium)  │      │    Proxy     │      │   API Server │
└─────────────┘      └──────────────┘      └──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │ HAR Capture  │
                    │  (Requests/  │
                    │  Responses)  │
                    └──────────────┘
                            │
                            ▼
                    ┌──────────────┐
                    │ RestAssured  │
                    │  Validation  │
                    └──────────────┘
```

## Phase 1: Setup & Dependencies

### 1.1 Add Maven Dependencies

Add to `pom.xml`:

```xml
<!-- BrowserMob Proxy Core -->
<dependency>
    <groupId>net.lightbody.bmp</groupId>
    <artifactId>browsermob-core</artifactId>
    <version>2.1.5</version>
</dependency>

<!-- For HAR file manipulation (optional) -->
<dependency>
    <groupId>de.sstoehr</groupId>
    <artifactId>har-reader</artifactId>
    <version>2.2.0</version>
</dependency>
```

### 1.2 Create ProxyManager Utility Class

Create `src/main/java/com/test/channelplay/utils/ProxyManager.java`:

```java
package com.test.channelplay.utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.EnumSet;

public class ProxyManager {
    private static final Logger log = LoggerFactory.getLogger(ProxyManager.class);
    private static BrowserMobProxy proxy;
    private static int proxyPort;
    private static String proxyHost;

    /**
     * Start the BrowserMob Proxy server
     */
    public static void startProxy() {
        try {
            proxy = new BrowserMobProxyServer();
            proxy.setTrustAllServers(true);  // Accept all SSL certificates
            proxy.setHarCaptureTypes(EnumSet.allOf(CaptureType.class));

            // Start on random available port
            proxy.start(0);
            proxyPort = proxy.getPort();
            proxyHost = InetAddress.getLocalHost().getHostAddress();

            log.info("Proxy started on {}:{}", proxyHost, proxyPort);
        } catch (Exception e) {
            log.error("Failed to start proxy: {}", e.getMessage());
            throw new RuntimeException("Proxy initialization failed", e);
        }
    }

    /**
     * Get Selenium Proxy configuration
     */
    public static Proxy getSeleniumProxy() {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        seleniumProxy.setHttpProxy(proxyHost + ":" + proxyPort);
        seleniumProxy.setSslProxy(proxyHost + ":" + proxyPort);
        return seleniumProxy;
    }

    /**
     * Get proxy URL for Appium capabilities
     */
    public static String getProxyUrl() {
        return proxyHost + ":" + proxyPort;
    }

    /**
     * Start recording network traffic
     */
    public static void startRecording(String harName) {
        if (proxy != null) {
            proxy.newHar(harName);
            log.info("Started recording HAR: {}", harName);
        }
    }

    /**
     * Stop recording and get HAR
     */
    public static Har stopRecording() {
        if (proxy != null) {
            Har har = proxy.getHar();
            log.info("Stopped recording. Captured {} entries",
                    har.getLog().getEntries().size());
            return har;
        }
        return null;
    }

    /**
     * Get current HAR without stopping recording
     */
    public static Har getCurrentHar() {
        return proxy != null ? proxy.getHar() : null;
    }

    /**
     * Stop and cleanup proxy
     */
    public static void stopProxy() {
        if (proxy != null) {
            proxy.stop();
            log.info("Proxy stopped");
        }
    }

    /**
     * Add request filter
     */
    public static void addRequestFilter(String pattern) {
        if (proxy != null) {
            proxy.addRequestFilter((request, contents, messageInfo) -> {
                if (request.getUri().contains(pattern)) {
                    log.debug("Captured request: {} {}",
                            request.getMethod(), request.getUri());
                }
                return null;
            });
        }
    }

    /**
     * Add response filter
     */
    public static void addResponseFilter(String pattern) {
        if (proxy != null) {
            proxy.addResponseFilter((response, contents, messageInfo) -> {
                if (messageInfo.getOriginalUrl().contains(pattern)) {
                    log.debug("Captured response: {} - Status: {}",
                            messageInfo.getOriginalUrl(), response.getStatus());
                }
            });
        }
    }
}
```

## Phase 2: Driver Integration

### 2.1 Update MobileDriverManager

Modify `src/main/java/com/test/channelplay/utils/MobileDriverManager.java`:

```java
// Add to imports
import com.test.channelplay.utils.ProxyManager;
import org.openqa.selenium.Proxy;

// In setupDriver() method, before driver initialization:
private void setupDriverWithProxy(DesiredCapabilities capabilities) {
    String enableProxy = ConfigReader.getProperty("enable.proxy", "false");

    if ("true".equalsIgnoreCase(enableProxy)) {
        // Start proxy if not already running
        ProxyManager.startProxy();

        // For Android
        if (platformName.equalsIgnoreCase("Android")) {
            // For emulator
            Proxy seleniumProxy = ProxyManager.getSeleniumProxy();
            capabilities.setCapability("proxy", seleniumProxy);

            // For real device (requires device to be on same network)
            capabilities.setCapability("androidDeviceSocket",
                ProxyManager.getProxyUrl());

            log.info("Configured Android driver with proxy: {}",
                ProxyManager.getProxyUrl());
        }

        // Start recording
        ProxyManager.startRecording("mobile-test-" + System.currentTimeMillis());
    }
}

// In tearDown() method:
public void tearDown() {
    // Save HAR file before stopping
    if (ProxyManager.getCurrentHar() != null) {
        Har har = ProxyManager.stopRecording();
        // Optionally save HAR to file for debugging
        saveHarFile(har);
    }

    // Stop proxy
    ProxyManager.stopProxy();

    // Then stop driver
    if (driver != null) {
        driver.quit();
    }
}
```

### 2.2 Handle Real Device Proxy Setup

For real devices, additional setup needed:

```java
public class DeviceProxyConfigurator {

    /**
     * Configure WiFi proxy on Android device
     * Device must be on same network as test machine
     */
    public static void configureAndroidWifiProxy(String deviceId) {
        String proxyHost = ProxyManager.getProxyUrl().split(":")[0];
        String proxyPort = ProxyManager.getProxyUrl().split(":")[1];

        // ADB commands to set proxy
        String[] commands = {
            String.format("adb -s %s shell settings put global http_proxy %s:%s",
                deviceId, proxyHost, proxyPort),
            String.format("adb -s %s shell settings put global global_http_proxy_host %s",
                deviceId, proxyHost),
            String.format("adb -s %s shell settings put global global_http_proxy_port %s",
                deviceId, proxyPort)
        };

        for (String cmd : commands) {
            executeCommand(cmd);
        }
    }

    /**
     * Clear WiFi proxy after test
     */
    public static void clearAndroidWifiProxy(String deviceId) {
        executeCommand(String.format(
            "adb -s %s shell settings delete global http_proxy", deviceId));
        executeCommand(String.format(
            "adb -s %s shell settings delete global global_http_proxy_host", deviceId));
        executeCommand(String.format(
            "adb -s %s shell settings delete global global_http_proxy_port", deviceId));
    }
}
```

## Phase 3: API Interceptor Helper

### 3.1 Create APIInterceptor Class

Create `src/main/java/com/test/channelplay/mobile/APIInterceptor.java`:

```java
package com.test.channelplay.mobile;

import com.test.channelplay.utils.ProxyManager;
import io.restassured.path.json.JsonPath;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class APIInterceptor {
    private static final Logger log = LoggerFactory.getLogger(APIInterceptor.class);

    /**
     * Captured API call details
     */
    public static class CapturedAPI {
        public String method;
        public String url;
        public String requestBody;
        public String responseBody;
        public int statusCode;
        public long responseTime;
        public Map<String, String> requestHeaders;
        public Map<String, String> responseHeaders;

        @Override
        public String toString() {
            return String.format("%s %s - Status: %d, Time: %dms",
                method, url, statusCode, responseTime);
        }
    }

    /**
     * Capture specific API call by URL pattern and method
     */
    public static CapturedAPI captureAPICall(String urlPattern, String method) {
        Har har = ProxyManager.getCurrentHar();
        if (har == null) {
            log.warn("No HAR data available");
            return null;
        }

        return har.getLog().getEntries().stream()
            .filter(entry -> entry.getRequest().getUrl().contains(urlPattern))
            .filter(entry -> entry.getRequest().getMethod().equalsIgnoreCase(method))
            .map(APIInterceptor::convertToCapturedAPI)
            .findFirst()
            .orElse(null);
    }

    /**
     * Get all API calls matching pattern
     */
    public static List<CapturedAPI> getAllAPICalls(String urlPattern) {
        Har har = ProxyManager.getCurrentHar();
        if (har == null) {
            return new ArrayList<>();
        }

        return har.getLog().getEntries().stream()
            .filter(entry -> entry.getRequest().getUrl().contains(urlPattern))
            .map(APIInterceptor::convertToCapturedAPI)
            .collect(Collectors.toList());
    }

    /**
     * Get the last API call matching pattern
     */
    public static CapturedAPI getLastAPICall(String urlPattern) {
        List<CapturedAPI> allCalls = getAllAPICalls(urlPattern);
        return allCalls.isEmpty() ? null :
               allCalls.get(allCalls.size() - 1);
    }

    /**
     * Get POST request body for specific endpoint
     */
    public static String getPOSTRequestBody(String urlPattern) {
        CapturedAPI api = captureAPICall(urlPattern, "POST");
        return api != null ? api.requestBody : null;
    }

    /**
     * Get response for specific endpoint
     */
    public static String getResponse(String urlPattern, String method) {
        CapturedAPI api = captureAPICall(urlPattern, method);
        return api != null ? api.responseBody : null;
    }

    /**
     * Convert HAR entry to CapturedAPI object
     */
    private static CapturedAPI convertToCapturedAPI(HarEntry entry) {
        CapturedAPI api = new CapturedAPI();

        // Request details
        api.method = entry.getRequest().getMethod();
        api.url = entry.getRequest().getUrl();
        api.requestBody = entry.getRequest().getPostData() != null ?
                         entry.getRequest().getPostData().getText() : null;

        // Response details
        api.responseBody = entry.getResponse().getContent() != null ?
                          entry.getResponse().getContent().getText() : null;
        api.statusCode = entry.getResponse().getStatus();
        api.responseTime = entry.getTime();

        // Headers
        api.requestHeaders = entry.getRequest().getHeaders().stream()
            .collect(Collectors.toMap(h -> h.getName(), h -> h.getValue()));
        api.responseHeaders = entry.getResponse().getHeaders().stream()
            .collect(Collectors.toMap(h -> h.getName(), h -> h.getValue()));

        log.debug("Converted API call: {}", api);
        return api;
    }

    /**
     * Wait for API call to complete
     */
    public static CapturedAPI waitForAPICall(String urlPattern, String method,
                                            int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000;

        while (System.currentTimeMillis() - startTime < timeout) {
            CapturedAPI api = captureAPICall(urlPattern, method);
            if (api != null) {
                return api;
            }

            try {
                Thread.sleep(500); // Check every 500ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.warn("Timeout waiting for API call: {} {}", method, urlPattern);
        return null;
    }
}
```

## Phase 4: Validation Integration

### 4.1 Create ProxyValidationHelper

Create `src/main/java/com/test/channelplay/utils/ProxyValidationHelper.java`:

```java
package com.test.channelplay.utils;

import com.test.channelplay.mobile.APIInterceptor;
import com.test.channelplay.mobile.APIInterceptor.CapturedAPI;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ProxyValidationHelper {
    private static final Logger log = LoggerFactory.getLogger(ProxyValidationHelper.class);

    /**
     * Validate POST request body
     */
    public static void validatePOSTRequest(String urlPattern,
                                          Map<String, Object> expectedFields) {
        CapturedAPI api = APIInterceptor.captureAPICall(urlPattern, "POST");
        Assert.assertNotNull(api, "POST request not found for: " + urlPattern);

        JsonPath requestJson = new JsonPath(api.requestBody);

        expectedFields.forEach((field, expectedValue) -> {
            Object actualValue = requestJson.get(field);
            assertThat(String.format("Field '%s' validation failed", field),
                      actualValue, equalTo(expectedValue));
            log.info("Validated request field '{}': {}", field, actualValue);
        });
    }

    /**
     * Validate API response
     */
    public static void validateResponse(String urlPattern,
                                       int expectedStatus,
                                       Map<String, Object> expectedFields) {
        CapturedAPI api = APIInterceptor.getLastAPICall(urlPattern);
        Assert.assertNotNull(api, "API response not found for: " + urlPattern);

        // Validate status code
        assertThat("Status code mismatch",
                  api.statusCode, equalTo(expectedStatus));
        log.info("Validated status code: {}", api.statusCode);

        // Validate response body
        if (expectedFields != null && !expectedFields.isEmpty()) {
            JsonPath responseJson = new JsonPath(api.responseBody);

            expectedFields.forEach((field, expectedValue) -> {
                Object actualValue = responseJson.get(field);
                assertThat(String.format("Response field '%s' validation failed", field),
                          actualValue, equalTo(expectedValue));
                log.info("Validated response field '{}': {}", field, actualValue);
            });
        }
    }

    /**
     * Extract value from response
     */
    public static String extractFromResponse(String urlPattern, String jsonPath) {
        CapturedAPI api = APIInterceptor.getLastAPICall(urlPattern);
        if (api == null || api.responseBody == null) {
            log.warn("No response found for: {}", urlPattern);
            return null;
        }

        JsonPath json = new JsonPath(api.responseBody);
        String value = json.getString(jsonPath);
        log.info("Extracted '{}' = '{}'", jsonPath, value);
        return value;
    }

    /**
     * Validate response time
     */
    public static void validateResponseTime(String urlPattern,
                                           long maxResponseTime) {
        CapturedAPI api = APIInterceptor.getLastAPICall(urlPattern);
        Assert.assertNotNull(api, "API call not found for: " + urlPattern);

        assertThat("Response time exceeded threshold",
                  api.responseTime, lessThanOrEqualTo(maxResponseTime));
        log.info("Response time {} ms is within threshold {} ms",
                api.responseTime, maxResponseTime);
    }

    /**
     * Compare UI data with API response
     */
    public static void compareUIWithAPI(String uiValue,
                                       String urlPattern,
                                       String jsonPath) {
        String apiValue = extractFromResponse(urlPattern, jsonPath);

        assertThat("UI and API values don't match",
                  uiValue, equalTo(apiValue));
        log.info("UI value '{}' matches API value", uiValue);
    }

    /**
     * Validate complete activity creation flow
     */
    public static ActivityValidationResult validateActivityCreation(
            String customerName, String title, String description) {

        ActivityValidationResult result = new ActivityValidationResult();

        // Capture POST request
        CapturedAPI postAPI = APIInterceptor.captureAPICall(
            "/api/activities", "POST");

        if (postAPI != null) {
            JsonPath request = new JsonPath(postAPI.requestBody);
            JsonPath response = new JsonPath(postAPI.responseBody);

            // Validate request
            result.requestValid =
                customerName.equals(request.getString("customer")) &&
                title.equals(request.getString("title")) &&
                description.equals(request.getString("description"));

            // Extract from response
            result.activityId = response.getString("data.id");
            result.serialNumber = response.getString("data.serialNumber");
            result.statusCode = postAPI.statusCode;

            log.info("Activity creation validated. ID: {}, Serial: {}",
                    result.activityId, result.serialNumber);
        }

        return result;
    }

    /**
     * Result class for activity validation
     */
    public static class ActivityValidationResult {
        public boolean requestValid;
        public String activityId;
        public String serialNumber;
        public int statusCode;
    }
}
```

## Phase 5: Update Test Classes

### 5.1 Update AddActivityPage

Modify `src/main/java/com/test/channelplay/mobile/screens/AddActivityPage.java`:

```java
// Add to imports
import com.test.channelplay.mobile.APIInterceptor;
import com.test.channelplay.utils.ProxyValidationHelper;
import io.restassured.path.json.JsonPath;

public class AddActivityPage extends MobileTestBase {

    /**
     * Click Save and capture API response
     */
    public ActivityCreationResult clickOnSaveToSubmitOffsiteActivityWithCapture() {
        setupPageElements();

        // Clear previous recordings if any
        ProxyManager.startRecording("save-activity-" + System.currentTimeMillis());

        log.info("Clicking Save button to submit offsite activity");
        saveButton.click();

        // Wait for API call to complete
        APIInterceptor.CapturedAPI saveAPI = APIInterceptor.waitForAPICall(
            "/api/activities", "POST", 10); // 10 second timeout

        ActivityCreationResult result = new ActivityCreationResult();

        if (saveAPI != null) {
            log.info("Captured POST response: Status {}", saveAPI.statusCode);

            // Parse response
            JsonPath json = new JsonPath(saveAPI.responseBody);
            result.success = saveAPI.statusCode == 201 || saveAPI.statusCode == 200;
            result.activityId = json.getString("data.id");
            result.serialNumber = json.getString("data.serialNumber");
            result.responseBody = saveAPI.responseBody;
            result.requestBody = saveAPI.requestBody;

            log.info("Activity created - ID: {}, Serial: {}",
                    result.activityId, result.serialNumber);
        } else {
            log.error("Failed to capture API response for activity creation");
            result.success = false;
        }

        commonUtils.sleep(2000);
        return result;
    }

    /**
     * Verify activity with API validation
     */
    public void verifyActivityWithAPIValidation(String customerName) {
        // UI verification
        verifyActivityIsShowingInListAndFetchActivityDetailsForValidation(customerName);

        // API validation
        String serialFromUI = extractSerialNumber(); // Your existing method

        // Validate against captured API response
        ProxyValidationHelper.compareUIWithAPI(
            serialFromUI,
            "/api/activities",
            "data.serialNumber"
        );

        // Additional validations
        ProxyValidationHelper.validateResponse(
            "/api/activities",
            201,
            Map.of(
                "data.customer", customerName,
                "status", "success"
            )
        );
    }

    /**
     * Result class for activity creation
     */
    public static class ActivityCreationResult {
        public boolean success;
        public String activityId;
        public String serialNumber;
        public String responseBody;
        public String requestBody;
    }
}
```

## Phase 6: Test Implementation

### 6.1 Create Complete Test Example

Create `src/test/java/com/test/channelplay/tests/OffsiteActivityAPITest.java`:

```java
package com.test.channelplay.tests;

import com.test.channelplay.mobile.APIInterceptor;
import com.test.channelplay.mobile.screens.activities.AddActivityApp_testUserPage;
import com.test.channelplay.mobile.screens.activities.AddActivity_testUserPage;
import com.test.channelplay.utils.ProxyManager;
import com.test.channelplay.utils.ProxyValidationHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.*;
import org.testng.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class OffsiteActivityAPITest extends MobileTestBase {

   private AddActivityApp_testUserPage addActivityTestUserPage;

   @BeforeClass
   public void setupProxy() {
      // Enable proxy for this test class
      System.setProperty("enable.proxy", "true");
      ProxyManager.startProxy();
   }

   @BeforeMethod
   public void setup() {
      // Start recording for each test
      ProxyManager.startRecording("test-" + System.currentTimeMillis());
      addActivityPage = new AddActivityApp_testUserPage();
   }

   @Test
   public void testOffsiteActivityWithCompleteAPIValidation() {
      // Test data
      String customerName = "rest@3";
      String title = "Test Activity";
      String description = "Test Description";

      // Step 1: UI Actions
      log.info("Starting UI actions for offsite activity creation");
      addActivityPage.clicksOnActivitiesMenu();
      addActivityPage.clicksOnOffsiteActivityOption();
      addActivityPage.clicksOnAddButtonToAddNewOffsiteActivity();
      addActivityPage.selectCustomerFromSelectCustomerDropdown(customerName);
      addActivityPage.clicksOnOKButtonOnCustomerSelectionFrame();
      addActivityPage.enterTextIntoDescriptionBox(description);
      addActivityPage.enterNameIntoTitleField(title);
      addActivityPage.selectDateInPerformDateField();

      // Step 2: Save and capture API
      log.info("Saving activity and capturing API response");
      AddActivityApp_testUserPage.ActivityCreationResult result =
              addActivityPage.clickOnSaveToSubmitOffsiteActivityWithCapture();

      // Step 3: Validate POST request body
      log.info("Validating POST request body");
      ProxyValidationHelper.validatePOSTRequest(
              "/api/activities",
              Map.of(
                      "customer", customerName,
                      "title", title,
                      "description", description,
                      "type", "OFFSITE"
              )
      );

      // Step 4: Validate response
      log.info("Validating API response");
      ProxyValidationHelper.validateResponse(
              "/api/activities",
              201,  // Created
              Map.of(
                      "status", "success",
                      "data.type", "OFFSITE"
              )
      );

      // Step 5: Extract values from response
      String activityId = ProxyValidationHelper.extractFromResponse(
              "/api/activities", "data.id");
      String serialNumber = ProxyValidationHelper.extractFromResponse(
              "/api/activities", "data.serialNumber");

      Assert.assertNotNull(activityId, "Activity ID should not be null");
      Assert.assertTrue(serialNumber.startsWith("OFF"),
              "Serial number should start with OFF");

      // Step 6: Validate with RestAssured GET
      log.info("Cross-validating with RestAssured GET request");
      given()
              .header("Authorization", "Bearer " + getAuthToken())
              .pathParam("id", activityId)
              .when()
              .get("/api/activities/{id}")
              .then()
              .statusCode(200)
              .body("id", equalTo(activityId))
              .body("serialNumber", equalTo(serialNumber))
              .body("customer", equalTo(customerName))
              .body("title", equalTo(title));

      // Step 7: UI verification
      log.info("Verifying activity in UI list");
      addActivityPage.verifyActivityWithAPIValidation(customerName);

      // Step 8: Performance validation
      ProxyValidationHelper.validateResponseTime("/api/activities", 3000);

      log.info("Test completed successfully");
   }

   @Test
   public void testMultipleAPICallsCapture() {
      // Perform UI actions that trigger multiple API calls
      addActivityPage.clicksOnActivitiesMenu();

      // Get all API calls
      List<APIInterceptor.CapturedAPI> allAPIs =
              APIInterceptor.getAllAPICalls("/api");

      log.info("Total API calls captured: {}", allAPIs.size());

      // Validate each API call
      allAPIs.forEach(api -> {
         log.info("API Call: {}", api);
         Assert.assertTrue(api.statusCode < 500,
                 "No server errors expected");
      });
   }

   @Test
   public void testAPIErrorHandling() {
      // Test with invalid data to capture error response
      addActivityPage.clicksOnSaveToSubmitOffsiteActivity();

      // Capture error response
      APIInterceptor.CapturedAPI errorAPI =
              APIInterceptor.getLastAPICall("/api/activities");

      if (errorAPI != null && errorAPI.statusCode >= 400) {
         JsonPath errorJson = new JsonPath(errorAPI.responseBody);
         String errorMessage = errorJson.getString("error.message");
         log.info("Captured error: {}", errorMessage);

         // Validate error response structure
         Assert.assertNotNull(errorMessage, "Error message should be present");
      }
   }

   @AfterMethod
   public void saveHARForDebugging() {
      // Save HAR file for failed tests
      if (testResult.getStatus() == ITestResult.FAILURE) {
         Har har = ProxyManager.stopRecording();
         String fileName = "har-" + testResult.getName() +
                 "-" + System.currentTimeMillis() + ".har";
         saveHarToFile(har, fileName);
      }
   }

   @AfterClass
   public void tearDownProxy() {
      ProxyManager.stopProxy();
   }
}
```

## Phase 7: Configuration

### 7.1 Update application.properties

Add to `src/main/resources/application.properties`:

```properties
# BrowserMob Proxy Configuration
enable.proxy=false  # Set to true for API interception tests
proxy.capture.headers=true
proxy.capture.content=true
proxy.capture.binary=false
proxy.capture.cookies=true

# Proxy filters (comma separated patterns)
proxy.filter.urls=/api/activities,/api/customers,/api/auth

# HAR file storage
proxy.har.storage.path=test-output/har-files/
proxy.har.save.on.failure=true

# Performance thresholds
proxy.performance.threshold.ms=3000
```

### 7.2 Create TestNG Suite for API Tests

Create `src/test/resources/testng-api.xml`:

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="API Interception Test Suite">
    <parameter name="enable.proxy" value="true"/>

    <test name="Offsite Activity API Tests">
        <classes>
            <class name="com.test.channelplay.tests.OffsiteActivityAPITest"/>
        </classes>
    </test>
</suite>
```

## Phase 8: Challenges & Solutions

| Challenge | Solution | Implementation |
|-----------|----------|----------------|
| **SSL/HTTPS Issues** | Trust all certificates in test | `proxy.setTrustAllServers(true)` |
| **Real Device Proxy** | Configure WiFi proxy via ADB | Use DeviceProxyConfigurator class |
| **Large Response Bodies** | Filter and compress | Set capture types and size limits |
| **Performance Impact** | Enable selectively | Use property flag `enable.proxy` |
| **Certificate Pinning** | Disable in test builds | Requires app modification |
| **Proxy Detection** | Some apps detect proxy | May need app config change |
| **Dynamic Ports** | Handle port conflicts | Use random port `proxy.start(0)` |
| **Network Latency** | Increased response time | Adjust timeout thresholds |
| **HAR File Size** | Large files for long tests | Implement rolling capture |
| **Parallel Execution** | Port conflicts | Use ThreadLocal proxy instances |

### 8.1 Handling Certificate Pinning

If the app has certificate pinning:

```java
// Option 1: Disable in test build
// Requires app code modification

// Option 2: Install proxy certificate on device
public static void installProxyCertificate() {
    // Generate and install BrowserMob CA certificate
    String certPath = proxy.getCertificatePath();
    adb.push(certPath, "/sdcard/browsermob.crt");
    // Manual installation required via Settings
}

// Option 3: Use reverse proxy
public static void setupReverseProxy() {
    // Route traffic through corporate proxy that handles SSL
}
```

### 8.2 Handling Large Responses

```java
public class ResponseSizeFilter {
    public static void limitResponseSize() {
        proxy.addResponseFilter((response, contents, messageInfo) -> {
            if (contents.getBinaryContents().length > 1_000_000) {
                // Truncate large responses
                contents.setTextContents("{\"truncated\": true, \"size\": " +
                    contents.getBinaryContents().length + "}");
            }
        });
    }
}
```

## Phase 9: Benefits

### 9.1 Complete API Visibility
- Capture exact POST/PUT request bodies sent by UI
- See actual responses received by the app
- Monitor all API calls triggered by UI actions
- Track API performance metrics

### 9.2 Enhanced Validation
- Validate request payloads match UI inputs
- Verify response data matches UI display
- Cross-validate with RestAssured GET calls
- Ensure data consistency

### 9.3 Debugging Capabilities
- Save HAR files for failed tests
- Analyze complete request/response cycle
- Identify API issues vs UI issues
- Performance bottleneck identification

### 9.4 Test Coverage
- Test error scenarios by capturing error responses
- Validate authorization headers
- Check API versioning
- Monitor third-party API calls

### 9.5 Integration Benefits
- Works with existing RestAssured setup
- Compatible with Appium framework
- Supports both emulators and real devices
- Can be enabled/disabled per test

## Implementation Checklist

### Phase 1 - Basic Setup
- [ ] Add BrowserMob Proxy dependency to pom.xml
- [ ] Create ProxyManager utility class
- [ ] Test proxy startup and shutdown

### Phase 2 - Driver Integration
- [ ] Update MobileDriverManager
- [ ] Configure capabilities for Android
- [ ] Test with emulator
- [ ] Test with real device

### Phase 3 - API Interception
- [ ] Create APIInterceptor class
- [ ] Implement capture methods
- [ ] Test API capture functionality

### Phase 4 - Validation
- [ ] Create ProxyValidationHelper
- [ ] Implement validation methods
- [ ] Create comparison utilities

### Phase 5 - Test Integration
- [ ] Update AddActivityPage
- [ ] Add capture methods
- [ ] Implement result classes

### Phase 6 - Test Creation
- [ ] Create example test class
- [ ] Implement full validation flow
- [ ] Add error handling tests

### Phase 7 - Configuration
- [ ] Update properties files
- [ ] Create TestNG suite
- [ ] Configure logging

### Phase 8 - Optimization
- [ ] Handle SSL issues
- [ ] Setup for real devices
- [ ] Implement filters
- [ ] Add performance checks

### Phase 9 - Documentation
- [ ] Document setup process
- [ ] Create usage examples
- [ ] Add troubleshooting guide

## Troubleshooting Guide

### Common Issues and Solutions

1. **Proxy not starting**
   - Check port availability
   - Verify network permissions
   - Check firewall settings

2. **No API calls captured**
   - Verify proxy configuration in capabilities
   - Check app is using HTTP/HTTPS
   - Ensure recording is started

3. **SSL errors**
   - Set `proxy.setTrustAllServers(true)`
   - Install proxy certificate on device
   - Check certificate pinning

4. **Real device issues**
   - Ensure device on same network
   - Configure WiFi proxy settings
   - Check ADB connectivity

5. **Large HAR files**
   - Implement size filters
   - Use rolling capture
   - Filter binary content

## Best Practices

1. **Enable proxy selectively** - Only for API tests
2. **Clean up resources** - Always stop proxy in tearDown
3. **Save HAR on failure** - For debugging
4. **Filter sensitive data** - Remove tokens from logs
5. **Monitor performance** - Proxy adds latency
6. **Use parallel execution carefully** - Manage port conflicts
7. **Implement retry logic** - For network issues
8. **Version control HAR files** - Exclude from git

## Conclusion

This implementation plan provides a complete solution for intercepting and validating API calls triggered by UI actions. The BrowserMob Proxy integration allows you to:

- Capture actual API requests and responses
- Validate data consistency between UI and API
- Debug issues more effectively
- Enhance test coverage

The solution integrates seamlessly with your existing Appium and RestAssured framework while providing powerful API interception capabilities.