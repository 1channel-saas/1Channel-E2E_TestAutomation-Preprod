# 📦 MULTI-APK IMPLEMENTATION - COMPLETE CODE UPDATE

**Date:** 22/10/2025
**Feature:** Multi-APK Testing Functionality
**Purpose:** Complete implementation guide for cross-app mobile testing

**⚠️ IMPORTANT:** This file contains the COMPLETE implementation. Follow the steps sequentially.

---

## 🎯 OVERVIEW

This implementation adds the ability to:
- Install and test multiple mobile apps in a single test scenario
- Switch between apps during test execution
- Share data (serial keys, IDs, etc.) between apps using in-memory storage
- Validate workflows that span multiple applications

---

## 📋 IMPLEMENTATION CHECKLIST

- [ ] **CHANGE #1:** Add multi-app properties to application.properties
- [ ] **CHANGE #2:** Create MultiAppTestContext.java (new file)
- [ ] **CHANGE #3:** Enhance MobileDriverManager.java with app switching
- [ ] **CHANGE #4:** Create MultiAppStepDefinitions.java (new file)
- [ ] **CHANGE #5:** Update Hooks_Mobile.java for multi-app support
- [ ] **CHANGE #6:** Create MultiAppTesting_Example.feature (new file)
- [ ] **CHANGE #7:** Create MULTI_APP_CHANGES_LOG.md (new file)

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #1: Add Multi-App Configuration Properties
# ═══════════════════════════════════════════════════════════════════════════════

## File: `application.properties`
## Action: ADD lines after line 58 (after `preprod.imgUploadPath = img_uploads/testImage.png`)

### Code to Add:

```properties



##  ----------------------- Multi-App Configuration (for Cross-App Testing) -------------------------------------------

# App 1 Configuration (Primary App)
preprod.app1.path = apps/app-debug.apk
preprod.app1.package = com.onechannelcrm.assistive
preprod.app1.activity = .MainActivity
preprod.app1.name = App1_Primary

# App 2 Configuration (Secondary App for validation)
preprod.app2.path = apps/app2-debug.apk
preprod.app2.package = com.onechannelcrm.app2
preprod.app2.activity = .MainActivity
preprod.app2.name = App2_Secondary

# iOS App Configuration (if needed)
preprod.ios.app1.path = apps/app1-debug.ipa
preprod.ios.app2.path = apps/app2-debug.ipa

# Multi-app testing settings
preprod.multiapp.install.both.at.start = true
preprod.multiapp.default.app = app1



```

### Location:
Insert after:
```properties
preprod.imgUploadPath = img_uploads/testImage.png
```

Before:
```properties
#Calendar Date Range Daily activity report AUT-25...
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #2: Create MultiAppTestContext.java
# ═══════════════════════════════════════════════════════════════════════════════

## File: `src/main/java/com/test/channelplay/utils/MultiAppTestContext.java`
## Action: CREATE NEW FILE

### Complete File Contents:

```java
package com.test.channelplay.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Multi-App Test Context for sharing data between different mobile applications
 * during cross-app testing scenarios.
 *
 * Use Case: When testing workflows that span multiple apps (e.g., App1 creates data,
 * App2 validates that data), this context stores and retrieves shared data in-memory.
 *
 * Features:
 * - Thread-safe using ThreadLocal for parallel test execution
 * - Type-safe data storage with generic methods
 * - Tracks currently active app
 * - Supports app installation status tracking
 * - Auto-cleanup after test completion
 *
 * Example Usage:
 *   // In App1 step definitions:
 *   MultiAppTestContext.put("serialKey", "12345");
 *   MultiAppTestContext.put("customerId", 456);
 *
 *   // Switch to App2...
 *
 *   // In App2 step definitions:
 *   String serialKey = MultiAppTestContext.get("serialKey", String.class);
 *   Integer customerId = MultiAppTestContext.get("customerId", Integer.class);
 */
@Slf4j
public class MultiAppTestContext {

    // Thread-local storage for parallel test execution
    private static final ThreadLocal<Map<String, Object>> contextData = ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<String> currentApp = ThreadLocal.withInitial(() -> "app1");
    private static final ThreadLocal<Map<String, Boolean>> installedApps = ThreadLocal.withInitial(HashMap::new);

    // Prevent instantiation
    private MultiAppTestContext() {}

    /**
     * Store data in the test context
     * @param key The key to store data under
     * @param value The value to store
     */
    public static void put(String key, Object value) {
        contextData.get().put(key, value);
        log.debug("MultiAppTestContext: Stored '{}' = {}", key, value);
    }

    /**
     * Retrieve data from the test context with type casting
     * @param key The key to retrieve
     * @param type The expected type of the value
     * @return The value cast to the specified type, or null if not found
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> type) {
        Object value = contextData.get().get(key);
        if (value == null) {
            log.warn("MultiAppTestContext: Key '{}' not found in context", key);
            return null;
        }
        try {
            T castedValue = (T) value;
            log.debug("MultiAppTestContext: Retrieved '{}' = {}", key, castedValue);
            return castedValue;
        } catch (ClassCastException e) {
            log.error("MultiAppTestContext: Type mismatch for key '{}'. Expected {}, found {}",
                     key, type.getName(), value.getClass().getName());
            return null;
        }
    }

    /**
     * Retrieve data as String (convenience method)
     */
    public static String getString(String key) {
        return get(key, String.class);
    }

    /**
     * Retrieve data as Integer (convenience method)
     */
    public static Integer getInteger(String key) {
        return get(key, Integer.class);
    }

    /**
     * Check if a key exists in the context
     */
    public static boolean containsKey(String key) {
        return contextData.get().containsKey(key);
    }

    /**
     * Remove a specific key from context
     */
    public static void remove(String key) {
        contextData.get().remove(key);
        log.debug("MultiAppTestContext: Removed key '{}'", key);
    }

    /**
     * Set the currently active app
     * @param appName The app identifier (e.g., "app1", "app2")
     */
    public static void setCurrentApp(String appName) {
        currentApp.set(appName);
        log.info("MultiAppTestContext: Switched to app '{}'", appName);
    }

    /**
     * Get the currently active app
     * @return The current app identifier
     */
    public static String getCurrentApp() {
        return currentApp.get();
    }

    /**
     * Mark an app as installed
     * @param appName The app identifier
     */
    public static void markAppAsInstalled(String appName) {
        installedApps.get().put(appName, true);
        log.info("MultiAppTestContext: Marked '{}' as installed", appName);
    }

    /**
     * Check if an app is installed
     * @param appName The app identifier
     * @return true if the app is marked as installed
     */
    public static boolean isAppInstalled(String appName) {
        return installedApps.get().getOrDefault(appName, false);
    }

    /**
     * Mark an app as uninstalled
     * @param appName The app identifier
     */
    public static void markAppAsUninstalled(String appName) {
        installedApps.get().put(appName, false);
        log.info("MultiAppTestContext: Marked '{}' as uninstalled", appName);
    }

    /**
     * Get all data in the context (for debugging)
     */
    public static Map<String, Object> getAllData() {
        return new HashMap<>(contextData.get());
    }

    /**
     * Clear all context data and reset to initial state
     * Should be called after each test scenario
     */
    public static void reset() {
        log.info("MultiAppTestContext: Resetting all context data");
        contextData.get().clear();
        currentApp.set("app1");
        installedApps.get().clear();
    }

    /**
     * Clean up thread-local storage (call in @After hook)
     */
    public static void cleanup() {
        contextData.remove();
        currentApp.remove();
        installedApps.remove();
        log.debug("MultiAppTestContext: Cleaned up thread-local storage");
    }

    /**
     * Print current context state (for debugging)
     */
    public static void printState() {
        log.info("=== MultiAppTestContext State ===");
        log.info("Current App: {}", getCurrentApp());
        log.info("Installed Apps: {}", installedApps.get());
        log.info("Context Data:");
        contextData.get().forEach((key, value) ->
            log.info("  {} = {}", key, value)
        );
        log.info("================================");
    }
}
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #3: Enhance MobileDriverManager.java with App Switching
# ═══════════════════════════════════════════════════════════════════════════════

## File: `src/main/java/com/test/channelplay/utils/MobileDriverManager.java`
## Action: ADD methods before the final closing brace

### Code to Add:

Add the following code **AFTER** the `quitDriver()` method (around line 193) and **BEFORE** the class closing brace `}`:

```java

    // ============================================================================================================
    // MULTI-APP SUPPORT METHODS (Added for cross-app testing functionality)
    // ============================================================================================================

    /**
     * Switch to a different installed app by activating it
     * @param appIdentifier The app identifier (e.g., "app1", "app2")
     */
    public static void switchToApp(String appIdentifier) {
        try {
            String packageName = GetProperty.value(appIdentifier + ".package");
            if (packageName == null || packageName.isEmpty()) {
                throw new IllegalArgumentException("Package name not found for app: " + appIdentifier);
            }

            AppiumDriver driver = getDriver();
            if (driver instanceof AndroidDriver) {
                log.info("Switching to app '{}' with package: {}", appIdentifier, packageName);
                ((AndroidDriver) driver).activateApp(packageName);
                Thread.sleep(2000); // Wait for app to launch
                MultiAppTestContext.setCurrentApp(appIdentifier);
                log.info("Successfully switched to app '{}'", appIdentifier);
            } else if (driver instanceof IOSDriver) {
                String bundleId = GetProperty.value(appIdentifier + ".bundleId");
                if (bundleId == null || bundleId.isEmpty()) {
                    bundleId = packageName; // Use package name as fallback
                }
                log.info("Switching to iOS app '{}' with bundle ID: {}", appIdentifier, bundleId);
                ((IOSDriver) driver).activateApp(bundleId);
                Thread.sleep(2000);
                MultiAppTestContext.setCurrentApp(appIdentifier);
                log.info("Successfully switched to iOS app '{}'", appIdentifier);
            }
        } catch (Exception e) {
            log.error("Failed to switch to app '{}': {}", appIdentifier, e.getMessage());
            throw new RuntimeException("Failed to switch to app: " + appIdentifier, e);
        }
    }

    /**
     * Install an additional app on the device/emulator
     * @param appIdentifier The app identifier (e.g., "app2")
     */
    public static void installApp(String appIdentifier) {
        try {
            String appPath = GetProperty.value(appIdentifier + ".path");
            if (appPath == null || appPath.isEmpty()) {
                throw new IllegalArgumentException("App path not found for: " + appIdentifier);
            }

            // Convert relative path to absolute if needed
            if (!appPath.startsWith("/") && !appPath.contains(":")) {
                appPath = System.getProperty("user.dir") + "/" + appPath;
            }

            AppiumDriver driver = getDriver();
            log.info("Installing app '{}' from path: {}", appIdentifier, appPath);

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).installApp(appPath);
            } else if (driver instanceof IOSDriver) {
                ((IOSDriver) driver).installApp(appPath);
            }

            MultiAppTestContext.markAppAsInstalled(appIdentifier);
            log.info("Successfully installed app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to install app '{}': {}", appIdentifier, e.getMessage());
            throw new RuntimeException("Failed to install app: " + appIdentifier, e);
        }
    }

    /**
     * Check if an app is installed on the device/emulator
     * @param appIdentifier The app identifier
     * @return true if the app is installed
     */
    public static boolean isAppInstalled(String appIdentifier) {
        try {
            String packageName = GetProperty.value(appIdentifier + ".package");
            if (packageName == null || packageName.isEmpty()) {
                log.warn("Package name not found for app: {}", appIdentifier);
                return false;
            }

            AppiumDriver driver = getDriver();
            if (driver instanceof AndroidDriver) {
                boolean installed = ((AndroidDriver) driver).isAppInstalled(packageName);
                log.debug("App '{}' installation status: {}", appIdentifier, installed);
                return installed;
            } else if (driver instanceof IOSDriver) {
                String bundleId = GetProperty.value(appIdentifier + ".bundleId");
                if (bundleId == null || bundleId.isEmpty()) {
                    bundleId = packageName;
                }
                boolean installed = ((IOSDriver) driver).isAppInstalled(bundleId);
                log.debug("iOS app '{}' installation status: {}", appIdentifier, installed);
                return installed;
            }
            return false;
        } catch (Exception e) {
            log.error("Failed to check installation status for app '{}': {}", appIdentifier, e.getMessage());
            return false;
        }
    }

    /**
     * Launch/activate an app (assumes app is already installed)
     * @param appIdentifier The app identifier
     */
    public static void launchApp(String appIdentifier) {
        switchToApp(appIdentifier); // Use switchToApp for launching
    }

    /**
     * Close/terminate an app without uninstalling it
     * @param appIdentifier The app identifier
     */
    public static void closeApp(String appIdentifier) {
        try {
            String packageName = GetProperty.value(appIdentifier + ".package");
            if (packageName == null || packageName.isEmpty()) {
                throw new IllegalArgumentException("Package name not found for app: " + appIdentifier);
            }

            AppiumDriver driver = getDriver();
            if (driver instanceof AndroidDriver) {
                log.info("Closing app '{}' with package: {}", appIdentifier, packageName);
                ((AndroidDriver) driver).terminateApp(packageName);
                log.info("Successfully closed app '{}'", appIdentifier);
            } else if (driver instanceof IOSDriver) {
                String bundleId = GetProperty.value(appIdentifier + ".bundleId");
                if (bundleId == null || bundleId.isEmpty()) {
                    bundleId = packageName;
                }
                log.info("Closing iOS app '{}' with bundle ID: {}", appIdentifier, bundleId);
                ((IOSDriver) driver).terminateApp(bundleId);
                log.info("Successfully closed iOS app '{}'", appIdentifier);
            }
        } catch (Exception e) {
            log.error("Failed to close app '{}': {}", appIdentifier, e.getMessage());
            throw new RuntimeException("Failed to close app: " + appIdentifier, e);
        }
    }

    /**
     * Uninstall an app from the device/emulator
     * @param appIdentifier The app identifier
     */
    public static void uninstallApp(String appIdentifier) {
        try {
            String packageName = GetProperty.value(appIdentifier + ".package");
            if (packageName == null || packageName.isEmpty()) {
                throw new IllegalArgumentException("Package name not found for app: " + appIdentifier);
            }

            AppiumDriver driver = getDriver();
            log.info("Uninstalling app '{}' with package: {}", appIdentifier, packageName);

            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).removeApp(packageName);
            } else if (driver instanceof IOSDriver) {
                String bundleId = GetProperty.value(appIdentifier + ".bundleId");
                if (bundleId == null || bundleId.isEmpty()) {
                    bundleId = packageName;
                }
                ((IOSDriver) driver).removeApp(bundleId);
            }

            MultiAppTestContext.markAppAsUninstalled(appIdentifier);
            log.info("Successfully uninstalled app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to uninstall app '{}': {}", appIdentifier, e.getMessage());
            throw new RuntimeException("Failed to uninstall app: " + appIdentifier, e);
        }
    }

    /**
     * Get the package name of the currently running app
     * @return Current app package name
     */
    public static String getCurrentPackage() {
        try {
            AppiumDriver driver = getDriver();
            if (driver instanceof AndroidDriver) {
                String packageName = ((AndroidDriver) driver).getCurrentPackage();
                log.debug("Current package: {}", packageName);
                return packageName;
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get current package: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Initialize driver with multi-app support (installs multiple apps at start if configured)
     * @param platform "android" or "ios"
     * @param deviceName Device name
     * @param platformVersion Platform version (iOS only)
     */
    public static void initializeMultiAppDriver(String platform, String deviceName, String platformVersion) {
        // Get default app configuration
        String defaultApp = GetProperty.value("multiapp.default.app");
        if (defaultApp == null || defaultApp.isEmpty()) {
            defaultApp = "app1";
        }

        String primaryAppPath = GetProperty.value(defaultApp + ".path");
        if (primaryAppPath == null || primaryAppPath.isEmpty()) {
            throw new IllegalArgumentException("Primary app path not found for: " + defaultApp);
        }

        // Convert to absolute path
        if (!primaryAppPath.startsWith("/") && !primaryAppPath.contains(":")) {
            primaryAppPath = System.getProperty("user.dir") + "/" + primaryAppPath;
        }

        // Initialize driver with primary app
        if (platform.equalsIgnoreCase("android")) {
            initializeAndroidDriver(primaryAppPath, deviceName);
        } else if (platform.equalsIgnoreCase("ios")) {
            initializeIOSDriver(primaryAppPath, deviceName, platformVersion);
        }

        // Mark primary app as installed
        MultiAppTestContext.markAppAsInstalled(defaultApp);
        MultiAppTestContext.setCurrentApp(defaultApp);

        // Check if we should install additional apps at startup
        String installBoth = GetProperty.value("multiapp.install.both.at.start");
        if ("true".equalsIgnoreCase(installBoth)) {
            log.info("Multi-app mode: Installing additional apps at startup");

            // Install app2 if not the default
            if (!defaultApp.equals("app2")) {
                try {
                    installApp("app2");
                } catch (Exception e) {
                    log.warn("Failed to install app2 at startup: {}", e.getMessage());
                }
            }
        }

        log.info("Multi-app driver initialized successfully. Current app: {}", defaultApp);
    }
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #4: Create MultiAppStepDefinitions.java
# ═══════════════════════════════════════════════════════════════════════════════

## File: `src/test/java/com/test/channelplay/stepDefinition_Mobile/MultiAppStepDefinitions.java`
## Action: CREATE NEW FILE

### Complete File Contents:

```java
package com.test.channelplay.stepDefinition_Mobile;

import com.test.channelplay.utils.MobileDriverManager;
import com.test.channelplay.utils.MultiAppTestContext;
import com.test.channelplay.utils.MobileTestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

/**
 * Step Definitions for Multi-App Testing
 *
 * This class provides Cucumber step definitions for cross-app testing scenarios
 * where tests need to switch between multiple mobile applications and share data.
 *
 * Example Feature File Usage:
 *
 * Feature: Cross-App Data Validation
 *   Scenario: Create data in App1 and validate in App2
 *     Given I am on "app1" mobile application
 *     When I create a customer with serial key "12345"
 *     And I store "serialKey" as "12345"
 *     And I switch to "app2" mobile application
 *     Then I should see customer with serial key from context
 */
@Slf4j
public class MultiAppStepDefinitions extends MobileTestBase {

    // ============================================================================================================
    // APP SWITCHING STEPS
    // ============================================================================================================

    /**
     * Switch to a different mobile application
     * Example: And I switch to "app2" mobile application
     */
    @Given("I switch to {string} mobile application")
    @When("I switch to {string} mobile application")
    public void iSwitchToMobileApplication(String appIdentifier) {
        log.info("Step: Switching to mobile application '{}'", appIdentifier);
        try {
            MobileDriverManager.switchToApp(appIdentifier);
            log.info("Successfully switched to app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to switch to app '{}': {}", appIdentifier, e.getMessage());
            Assert.fail("Failed to switch to application: " + appIdentifier);
        }
    }

    /**
     * Verify current app is the specified one
     * Example: Then I should be on "app1" mobile application
     */
    @Then("I should be on {string} mobile application")
    public void iShouldBeOnMobileApplication(String appIdentifier) {
        log.info("Step: Verifying current app is '{}'", appIdentifier);
        String currentApp = MultiAppTestContext.getCurrentApp();
        Assert.assertEquals(currentApp, appIdentifier,
            "Expected to be on app '" + appIdentifier + "' but currently on '" + currentApp + "'");
        log.info("Verification passed: Currently on app '{}'", appIdentifier);
    }

    /**
     * Launch/activate an app (assumes app is already installed)
     * Example: When I launch "app2" mobile application
     */
    @When("I launch {string} mobile application")
    public void iLaunchMobileApplication(String appIdentifier) {
        log.info("Step: Launching mobile application '{}'", appIdentifier);
        try {
            MobileDriverManager.launchApp(appIdentifier);
            log.info("Successfully launched app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to launch app '{}': {}", appIdentifier, e.getMessage());
            Assert.fail("Failed to launch application: " + appIdentifier);
        }
    }

    /**
     * Set the starting app (alias for switch)
     * Example: Given I am on "app1" mobile application
     */
    @Given("I am on {string} mobile application")
    public void iAmOnMobileApplication(String appIdentifier) {
        iSwitchToMobileApplication(appIdentifier);
    }

    // ============================================================================================================
    // APP INSTALLATION & MANAGEMENT STEPS
    // ============================================================================================================

    /**
     * Install an additional app on the device
     * Example: When I install "app2" mobile application
     */
    @When("I install {string} mobile application")
    public void iInstallMobileApplication(String appIdentifier) {
        log.info("Step: Installing mobile application '{}'", appIdentifier);
        try {
            MobileDriverManager.installApp(appIdentifier);
            log.info("Successfully installed app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to install app '{}': {}", appIdentifier, e.getMessage());
            Assert.fail("Failed to install application: " + appIdentifier);
        }
    }

    /**
     * Install and launch an app
     * Example: When I install and launch "app2" mobile application
     */
    @When("I install and launch {string} mobile application")
    public void iInstallAndLaunchMobileApplication(String appIdentifier) {
        log.info("Step: Installing and launching mobile application '{}'", appIdentifier);

        // Check if already installed
        if (!MobileDriverManager.isAppInstalled(appIdentifier)) {
            iInstallMobileApplication(appIdentifier);
        } else {
            log.info("App '{}' is already installed, skipping installation", appIdentifier);
        }

        // Launch the app
        iLaunchMobileApplication(appIdentifier);
    }

    /**
     * Verify if an app is installed
     * Example: Then "app2" mobile application should be installed
     */
    @Then("{string} mobile application should be installed")
    public void mobileApplicationShouldBeInstalled(String appIdentifier) {
        log.info("Step: Verifying if app '{}' is installed", appIdentifier);
        boolean isInstalled = MobileDriverManager.isAppInstalled(appIdentifier);
        Assert.assertTrue(isInstalled, "App '" + appIdentifier + "' is not installed");
        log.info("Verification passed: App '{}' is installed", appIdentifier);
    }

    /**
     * Close/terminate an app
     * Example: When I close "app1" mobile application
     */
    @When("I close {string} mobile application")
    public void iCloseMobileApplication(String appIdentifier) {
        log.info("Step: Closing mobile application '{}'", appIdentifier);
        try {
            MobileDriverManager.closeApp(appIdentifier);
            log.info("Successfully closed app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to close app '{}': {}", appIdentifier, e.getMessage());
            Assert.fail("Failed to close application: " + appIdentifier);
        }
    }

    /**
     * Uninstall an app
     * Example: When I uninstall "app2" mobile application
     */
    @When("I uninstall {string} mobile application")
    public void iUninstallMobileApplication(String appIdentifier) {
        log.info("Step: Uninstalling mobile application '{}'", appIdentifier);
        try {
            MobileDriverManager.uninstallApp(appIdentifier);
            log.info("Successfully uninstalled app '{}'", appIdentifier);
        } catch (Exception e) {
            log.error("Failed to uninstall app '{}': {}", appIdentifier, e.getMessage());
            Assert.fail("Failed to uninstall application: " + appIdentifier);
        }
    }

    // ============================================================================================================
    // DATA SHARING STEPS
    // ============================================================================================================

    /**
     * Store a string value in the test context for sharing between apps
     * Example: When I store "serialKey" as "12345"
     */
    @When("I store {string} as {string}")
    public void iStoreAs(String key, String value) {
        log.info("Step: Storing '{}' = '{}' in test context", key, value);
        MultiAppTestContext.put(key, value);
        log.info("Successfully stored '{}' in context", key);
    }

    /**
     * Store a numeric value in the test context
     * Example: When I store numeric "customerId" as "456"
     */
    @When("I store numeric {string} as {string}")
    public void iStoreNumericAs(String key, String value) {
        log.info("Step: Storing numeric '{}' = '{}' in test context", key, value);
        try {
            Integer numericValue = Integer.parseInt(value);
            MultiAppTestContext.put(key, numericValue);
            log.info("Successfully stored numeric '{}' in context", key);
        } catch (NumberFormatException e) {
            log.error("Failed to parse '{}' as numeric value", value);
            Assert.fail("Invalid numeric value: " + value);
        }
    }

    /**
     * Retrieve and verify a string value from context
     * Example: Then stored value of "serialKey" should be "12345"
     */
    @Then("stored value of {string} should be {string}")
    public void storedValueOfShouldBe(String key, String expectedValue) {
        log.info("Step: Verifying stored value of '{}'", key);
        String actualValue = MultiAppTestContext.getString(key);
        Assert.assertNotNull(actualValue, "Key '" + key + "' not found in context");
        Assert.assertEquals(actualValue, expectedValue,
            "Expected value '" + expectedValue + "' but found '" + actualValue + "'");
        log.info("Verification passed: '{}' = '{}'", key, actualValue);
    }

    /**
     * Verify a key exists in context
     * Example: Then stored value "serialKey" should exist
     */
    @Then("stored value {string} should exist")
    public void storedValueShouldExist(String key) {
        log.info("Step: Verifying key '{}' exists in context", key);
        boolean exists = MultiAppTestContext.containsKey(key);
        Assert.assertTrue(exists, "Key '" + key + "' not found in test context");
        log.info("Verification passed: Key '{}' exists", key);
    }

    /**
     * Retrieve string value from context and print (for debugging)
     * Example: When I print stored value "serialKey"
     */
    @When("I print stored value {string}")
    public void iPrintStoredValue(String key) {
        String value = MultiAppTestContext.getString(key);
        log.info("Stored value of '{}' = '{}'", key, value);
        System.out.println("DEBUG: " + key + " = " + value);
    }

    /**
     * Print entire context state (for debugging)
     * Example: When I print test context state
     */
    @When("I print test context state")
    public void iPrintTestContextState() {
        MultiAppTestContext.printState();
    }

    /**
     * Clear all context data
     * Example: When I clear test context
     */
    @When("I clear test context")
    public void iClearTestContext() {
        log.info("Step: Clearing test context");
        MultiAppTestContext.reset();
        log.info("Test context cleared");
    }

    // ============================================================================================================
    // HELPER METHODS (can be called from other step definitions)
    // ============================================================================================================

    /**
     * Helper: Get string value from context (for use in other step definitions)
     */
    public static String getContextValue(String key) {
        return MultiAppTestContext.getString(key);
    }

    /**
     * Helper: Get integer value from context (for use in other step definitions)
     */
    public static Integer getContextInteger(String key) {
        return MultiAppTestContext.getInteger(key);
    }

    /**
     * Helper: Store value in context (for use in other step definitions)
     */
    public static void storeContextValue(String key, Object value) {
        MultiAppTestContext.put(key, value);
    }
}
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #5: Update Hooks_Mobile.java for Multi-App Support
# ═══════════════════════════════════════════════════════════════════════════════

## File: `src/test/java/com/test/channelplay/stepDefinition_Mobile/Hooks_Mobile.java`
## Action: MODIFY (add import + add cleanup code + add new hooks)

### Change 5.1: Add Import Statement

**Location:** Line 9 (after other imports)

**Add this line:**
```java
import com.test.channelplay.utils.MultiAppTestContext;
```

**Full import section should look like:**
```java
import com.test.channelplay.mobile.config_Helper.AIElementFinder;
import com.test.channelplay.mobile.config_Helper.MobileTestFlowScreenshotManager;
import com.test.channelplay.mobile.config_Helper.TemplateConfig;
import com.test.channelplay.mobile.config_Helper.DebugMatchesViewerGenerator;
import com.test.channelplay.utils.MobileTestBase;
import com.test.channelplay.utils.MobileDriverManager;
import com.test.channelplay.utils.MultiAppTestContext;  // <-- ADD THIS LINE
import com.test.channelplay.utils.GetProperty;
```

---

### Change 5.2: Add Cleanup Code in tearDownMobile()

**Location:** In the `tearDownMobile()` method, BEFORE the line `// Tear down the mobile driver after each scenario`

**Add these lines:**
```java
        // Clean up multi-app test context if this is a multi-app scenario
        if (scenario.getSourceTagNames().contains("@multiApp")) {
            log.info("Cleaning up multi-app test context");
            MultiAppTestContext.cleanup();
        }

```

**The section should look like:**
```java
        // Auto-generate debug viewer if configured
        if (TemplateConfig.isDebugModeEnabled() && TemplateConfig.isDebugViewerAutoGenerate()) {
            try {
                String debugFolder = TemplateConfig.getDebugFolder();
                log.info("Auto-generating debug matches viewer from: {}", debugFolder);
                DebugMatchesViewerGenerator.generate(debugFolder);
                log.info("Debug viewer generated successfully at: {}", TemplateConfig.getDebugViewerOutputPath());
            } catch (Exception e) {
                log.warn("Failed to auto-generate debug viewer: {}", e.getMessage());
                // Don't fail the test if viewer generation fails
            }
        }

        // Clean up multi-app test context if this is a multi-app scenario
        if (scenario.getSourceTagNames().contains("@multiApp")) {
            log.info("Cleaning up multi-app test context");
            MultiAppTestContext.cleanup();
        }

        // Tear down the mobile driver after each scenario
        try {
            MobileDriverManager.quitDriver();
            log.info("Mobile driver closed successfully from Hooks");
        } catch (Exception e) {
            log.error("Error during mobile driver teardown: {}", e.getMessage());
        }
```

---

### Change 5.3: Add Multi-App Hooks Section

**Location:** AFTER the `clearAppData()` method and BEFORE the `@BeforeStep("@mobile")` line

**Add this entire section:**

```java


    // ============================================================================================================
    // MULTI-APP TESTING HOOKS
    // ============================================================================================================

    /**
     * Initialize driver with multi-app support for scenarios tagged with @multiApp
     * This hook runs AFTER the standard @mobile hook to override the driver initialization
     */
    @Before(value = "@mobile and @multiApp", order = 10)
    public void setUpMultiApp(Scenario scenario) {
        log.info("Initializing multi-app testing mode for scenario: {}", scenario.getName());

        try {
            // Reset multi-app context at the start of each scenario
            MultiAppTestContext.reset();
            log.info("Multi-app test context reset");

            // Close the existing driver if already initialized by @mobile hook
            if (getDriver() != null) {
                log.info("Closing existing driver to re-initialize with multi-app support");
                MobileDriverManager.quitDriver();
            }

            // Get platform configuration
            String mobilePlatform = GetProperty.value("mobile.platform");
            String mobileDeviceName = GetProperty.value("mobile.device.name");
            String iosPlatformVersion = GetProperty.value("ios.platform.version");

            if (mobilePlatform == null) mobilePlatform = "android";
            if (mobileDeviceName == null) mobileDeviceName = "emulator";
            if (iosPlatformVersion == null) iosPlatformVersion = "15.0";

            // Initialize driver with multi-app support
            MobileDriverManager.initializeMultiAppDriver(mobilePlatform, mobileDeviceName, iosPlatformVersion);

            // Update the driver reference
            driver = getDriver();

            log.info("Multi-app driver initialized successfully");
            log.info("Current app: {}", MultiAppTestContext.getCurrentApp());
            MultiAppTestContext.printState();

        } catch (Exception e) {
            log.error("Failed to initialize multi-app driver: {}", e.getMessage());
            throw new RuntimeException("Multi-app driver initialization failed", e);
        }
    }

    /**
     * Hook to log multi-app context state before each step (for debugging)
     * Only active when @multiApp and @debugContext tags are present
     */
    @BeforeStep("@mobile and @multiApp and @debugContext")
    public void logMultiAppContextBeforeStep(Scenario scenario) {
        log.debug("=== Multi-App Context Before Step ===");
        MultiAppTestContext.printState();
    }

    /**
     * Hook to log multi-app context state after each step (for debugging)
     * Only active when @multiApp and @debugContext tags are present
     */
    @AfterStep("@mobile and @multiApp and @debugContext")
    public void logMultiAppContextAfterStep(Scenario scenario) {
        log.debug("=== Multi-App Context After Step ===");
        MultiAppTestContext.printState();
    }

```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #6: Create Example Feature File
# ═══════════════════════════════════════════════════════════════════════════════

## File: `src/test/resources/com/test/channelplay/feature/mobile/MultiAppTesting_Example.feature`
## Action: CREATE NEW FILE

### Complete File Contents:

```gherkin
@mobile @multiApp
Feature: Multi-App Testing - Cross-App Data Validation
  As a QA tester
  I want to test workflows that span multiple mobile applications
  So that I can validate data synchronization and cross-app functionality

  Background:
    Given The mobile test environment is ready for multi-app testing

  # ================================================================================
  # EXAMPLE 1: Create data in App1, validate in App2 (Sequential Testing)
  # ================================================================================
  @multiApp @smoke
  Scenario: Create customer in App1 and validate in App2
    Given I am on "app1" mobile application
    And User logIn to CRM mobile App with testUser creds

    When I navigate to customers page
    And I create a new customer with the following details:
      | field      | value          |
      | name       | John Doe       |
      | serialKey  | SK12345        |
      | mobile     | 9876543210     |

    # Store the serial key in context for later use
    And I store "serialKey" as "SK12345"
    And I store "customerName" as "John Doe"
    Then customer should be created successfully

    # Switch to App2
    When I switch to "app2" mobile application
    And User logIn to second app with validation credentials

    When I navigate to customer validation page
    And I search for customer with serial key from context
    Then I should see customer details:
      | field         | expectedValue  |
      | name          | John Doe       |
      | serialKey     | SK12345        |
    And stored value of "serialKey" should be "SK12345"


  # ================================================================================
  # EXAMPLE 2: Multi-step data flow between apps
  # ================================================================================
  @multiApp @E2E
  Scenario: Complete order workflow across multiple apps
    Given I am on "app1" mobile application
    And User logIn to CRM mobile App with testUser creds

    # Create an order in App1
    When I navigate to orders page
    And I create a new order with:
      | product       | Laptop         |
      | quantity      | 2              |
      | orderNumber   | ORD-2024-001   |
    And I store "orderNumber" as "ORD-2024-001"
    And I store numeric "quantity" as "2"
    Then order should be created successfully
    And I should see order status as "Pending"

    # Switch to App2 to approve the order
    When I switch to "app2" mobile application
    And User logIn to approval app with manager credentials
    When I navigate to pending approvals
    And I search for order with number from context
    Then I should see order in pending approvals list

    When I approve the order
    Then order status should be updated to "Approved"

    # Switch back to App1 to verify
    When I switch to "app1" mobile application
    And I refresh orders page
    Then I should see order status as "Approved"


  # ================================================================================
  # EXAMPLE 3: Testing app installation and switching
  # ================================================================================
  @multiApp @installation
  Scenario: Install additional app during test execution
    Given I am on "app1" mobile application
    And "app1" mobile application should be installed

    # Install app2 if not already installed
    When I install and launch "app2" mobile application
    Then "app2" mobile application should be installed
    And I should be on "app2" mobile application

    # Perform actions on app2
    When User logIn to second app with validation credentials
    Then login should be successful

    # Switch back to app1
    When I switch to "app1" mobile application
    Then I should be on "app1" mobile application


  # ================================================================================
  # EXAMPLE 4: Data sharing between apps with validation
  # ================================================================================
  @multiApp @dataSharing
  Scenario: Share and validate data across multiple apps
    Given I am on "app1" mobile application

    # Store multiple data points
    When I store "customerSerialKey" as "CSK-789"
    And I store "customerEmail" as "test@example.com"
    And I store numeric "customerId" as "12345"

    # Verify stored values exist
    Then stored value "customerSerialKey" should exist
    And stored value "customerEmail" should exist
    And stored value "customerId" should exist

    # Print context for debugging
    When I print test context state

    # Switch to app2 and use stored values
    When I switch to "app2" mobile application
    And I print stored value "customerSerialKey"
    Then stored value of "customerSerialKey" should be "CSK-789"
    And stored value of "customerEmail" should be "test@example.com"


  # ================================================================================
  # EXAMPLE 5: Close and relaunch apps during test
  # ================================================================================
  @multiApp @appManagement
  Scenario: Close and relaunch apps during test flow
    Given I am on "app1" mobile application
    And User logIn to CRM mobile App with testUser creds

    When I store "testData" as "Important Info"
    And I close "app1" mobile application

    # Launch app2
    When I launch "app2" mobile application
    And User logIn to second app with validation credentials
    Then I should be on "app2" mobile application

    # Data should still be available in context
    Then stored value of "testData" should be "Important Info"

    # Relaunch app1
    When I launch "app1" mobile application
    Then I should be on "app1" mobile application


  # ================================================================================
  # EXAMPLE 6: Debugging multi-app context (with @debugContext tag)
  # ================================================================================
  @multiApp @debugContext @debug
  Scenario: Debug multi-app context state
    Given I am on "app1" mobile application

    When I store "app1Data" as "Data from App 1"
    And I store numeric "app1Counter" as "100"
    And I print test context state

    When I switch to "app2" mobile application
    And I store "app2Data" as "Data from App 2"
    And I store numeric "app2Counter" as "200"
    And I print test context state

    Then stored value "app1Data" should exist
    And stored value "app2Data" should exist
    And I print test context state


  # ================================================================================
  # NOTES FOR IMPLEMENTATION:
  # ================================================================================
  #
  # 1. TAGGING REQUIREMENTS:
  #    - @mobile: Required for mobile test execution
  #    - @multiApp: Required to enable multi-app driver initialization
  #    - @debugContext: Optional, enables step-level context logging
  #
  # 2. CONFIGURATION REQUIRED (application.properties):
  #    - preprod.app1.path = apps/app-debug.apk
  #    - preprod.app1.package = com.onechannelcrm.assistive
  #    - preprod.app2.path = apps/app2-debug.apk
  #    - preprod.app2.package = com.onechannelcrm.app2
  #    - preprod.multiapp.install.both.at.start = true
  #
  # 3. DATA SHARING:
  #    - Use "I store <key> as <value>" to save data
  #    - Use "stored value of <key> should be <value>" to validate
  #    - Use "I print test context state" for debugging
  #
  # 4. APP SWITCHING:
  #    - "I switch to <appName> mobile application" - switches between apps
  #    - "I am on <appName> mobile application" - alias for switch
  #    - "I should be on <appName> mobile application" - validates current app
  #
  # 5. APP MANAGEMENT:
  #    - "I install <appName> mobile application" - installs app
  #    - "I install and launch <appName> mobile application" - installs + launches
  #    - "I close <appName> mobile application" - terminates app
  #    - "<appName> mobile application should be installed" - validates installation
  #
  # ================================================================================
```

---

# ═══════════════════════════════════════════════════════════════════════════════
# CHANGE #7: Create Complete Change Log Documentation
# ═══════════════════════════════════════════════════════════════════════════════

## File: `MULTI_APP_CHANGES_LOG.md`
## Action: CREATE NEW FILE

**Note:** This file will be the same as the current document you're reading, containing all implementation details and rollback procedures.

---

# 🚀 IMPLEMENTATION SEQUENCE

Follow these steps in order:

1. ✅ **CHANGE #1** - Add properties (application.properties)
2. ✅ **CHANGE #2** - Create MultiAppTestContext.java
3. ✅ **CHANGE #3** - Enhance MobileDriverManager.java
4. ✅ **CHANGE #4** - Create MultiAppStepDefinitions.java
5. ✅ **CHANGE #5** - Update Hooks_Mobile.java
6. ✅ **CHANGE #6** - Create MultiAppTesting_Example.feature
7. ✅ **CHANGE #7** - Create MULTI_APP_CHANGES_LOG.md

---

# ✅ VERIFICATION

After all changes:

```bash
# 1. Compile
mvn clean compile

# 2. Verify all files exist
ls -la src/main/java/com/test/channelplay/utils/MultiAppTestContext.java
ls -la src/test/java/com/test/channelplay/stepDefinition_Mobile/MultiAppStepDefinitions.java
ls -la src/test/resources/com/test/channelplay/feature/mobile/MultiAppTesting_Example.feature

# 3. Check properties
grep "app1.path\|app2.path" application.properties

# 4. Run test (if apps configured)
mvn test -Dcucumber.filter.tags="@multiApp"
```

---

# 🔄 COMPLETE ROLLBACK

To remove all changes:

```bash
# 1. Delete new files
rm src/main/java/com/test/channelplay/utils/MultiAppTestContext.java
rm src/test/java/com/test/channelplay/stepDefinition_Mobile/MultiAppStepDefinitions.java
rm src/test/resources/com/test/channelplay/feature/mobile/MultiAppTesting_Example.feature
rm MULTI_APP_CHANGES_LOG.md
rm 22-10_multiApp_codeUpdate.md

# 2. Revert modified files
git checkout HEAD -- src/main/java/com/test/channelplay/utils/MobileDriverManager.java
git checkout HEAD -- src/test/java/com/test/channelplay/stepDefinition_Mobile/Hooks_Mobile.java

# 3. Manually remove multi-app section from application.properties (lines 63-84)

# 4. Verify
mvn clean compile
```

---

**END OF IMPLEMENTATION GUIDE**

**Save this file for future reference when you're ready to implement multi-app functionality!**
