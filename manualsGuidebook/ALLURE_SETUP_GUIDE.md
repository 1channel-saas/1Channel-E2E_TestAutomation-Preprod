# Allure Report Setup Guide

## Overview
Allure Framework is now integrated into your test automation project. This guide explains how to use it.

## What's Been Added

### 1. **Configuration Files**
- `src/test/resources/allure.properties` - Main Allure configuration
- `src/test/resources/categories.json` - Test failure categorization
- `src/test/resources/environment.properties` - Environment info for reports

### 2. **Hook Classes**
- `AllureHooks.java` - Web test hooks with screenshot on failure
- `AllureHooks_Mobile.java` - Mobile test hooks with device info capture

### 3. **Maven Configuration**
- Updated `pom.xml` with Allure Maven plugin
- Added to Cucumber runner plugin list

### 4. **Helper Scripts**
- `allure-serve.bat` - Windows batch script for report generation
- `allure-serve.sh` - Linux/Mac shell script for report generation

## How to Use

### Running Tests with Allure

#### Option 1: Using Helper Scripts
```bash
# Windows
allure-serve.bat

# Linux/Mac
chmod +x allure-serve.sh
./allure-serve.sh
```

#### Option 2: Using Maven Commands
```bash
# Run tests
mvn clean test

# Generate report from results
mvn allure:report

# Open report in browser (starts local server)
mvn allure:serve

# Generate and open report
mvn clean test allure:serve
```

### Report Location
- **Results:** `target/allure-results/`
- **Report:** `target/allure-report/`
- **Access:** Open `target/allure-report/index.html` in browser

## Features Available

### 1. **Test Categorization**
Tests are automatically categorized based on failure type:
- Product Defects (Assertion failures)
- Test Defects (Element not found, timeouts)
- Environment Issues (WebDriver, connection issues)
- Mobile App Issues (Appium-specific)
- API Issues (Rest Assured failures)

### 2. **Automatic Screenshots**
- Screenshots captured automatically on test failure
- Attached to both Allure and Cucumber reports
- Mobile tests include device information

### 3. **Test Metadata**
- Feature and Story grouping
- Tag-based labeling
- Severity levels (based on @critical, @major, @minor tags)
- Platform information (web/mobile)

### 4. **Report Features**
- **Overview Dashboard** - Test execution summary with graphs
- **Suites View** - Tests grouped by features
- **Graphs** - Status distribution, severity, duration
- **Timeline** - Visual test execution timeline
- **Categories** - Failure analysis
- **Environment** - Test environment details

## Adding Custom Information to Tests

### Using Tags for Severity
```gherkin
@critical
Scenario: Critical business flow

@major
Scenario: Important functionality

@minor
Scenario: Nice-to-have feature
```

### Programmatic Additions in Step Definitions
```java
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

// Add description
Allure.description("This test verifies login functionality");

// Add links
Allure.link("TEST-123", "https://jira.example.com/TEST-123");

// Add custom labels
Allure.label("owner", "John Doe");
Allure.label("layer", "web");

// Add attachments
Allure.addAttachment("Request", "application/json", requestBody);
Allure.addAttachment("Response", "application/json", responseBody);

// Create sub-steps
@Step("Perform login with user: {username}")
public void login(String username, String password) {
    // login code
}
```

## Troubleshooting

### Issue: Allure results not generated
**Solution:** Ensure Allure plugin is in Cucumber runner:
```java
plugin = {..., "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm"}
```

### Issue: Report not opening
**Solution:** Use `mvn allure:serve` instead of opening HTML directly

### Issue: Missing test history
**Solution:** Copy `target/allure-report/history` to `target/allure-results/history` before next run

## Next Steps

1. **Run a test to verify setup:**
   ```bash
   mvn clean test -Dcucumber.filter.tags="@offsiteActivity"
   mvn allure:serve
   ```

2. **Customize environment.properties** with your actual values

3. **Update issue/TMS links** in allure.properties with your tracking system URLs

4. **Add @severity tags** to your scenarios for better categorization

## Hybrid Reporting Approach

This framework uses a **hybrid reporting strategy**:

### 1. **Allure Report** - For Test Results
- Test execution results (pass/fail)
- Error messages and stack traces
- Manual screenshots (using ScreenshotHelper)
- Test trends and history
- Failure categorization

**Generate:** `mvn allure:serve`

### 2. **HTML Gallery Report** - For Test Flow Visualization
- Automatic test flow screenshots (every step)
- Side-by-side APP/WEB comparison
- Visual gallery with thumbnails
- Modal popup for full-size viewing
- Chronological test execution flow

**Generate:** `java -cp "target/classes" com.test.channelplay.utils.TestFlowHtmlReportGenerator`

### Why Hybrid?
- **Best of both worlds**: Allure's analytics + HTML's visual gallery
- **Performance**: Test flow screenshots stay on disk, not bloating Allure
- **User Experience**: Gallery view better for visual comparison
- **CI/CD Friendly**: Allure for automation, HTML for manual review

### Quick Commands:
```bash
# After test execution, generate both reports:
mvn allure:serve                          # View test results
java TestFlowHtmlReportGenerator          # View visual flow

# Or use the helper script:
allure-serve.bat                          # Option 1 for Allure
# Then manually run HTML generator
```