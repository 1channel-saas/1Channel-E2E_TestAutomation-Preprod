package com.test.channelplay.cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

//@RunWith(Cucumber.class)
@CucumberOptions (
		features = {"src/test/resources/com/test/channelplay/feature"},
		glue = {"com.test.channelplay.stepDefinition", "stepDefinitions_API"},
		//tags = "not @skip and (@INB and @E2E)",
		tags = "@uniqueSerialNo_INB",
		dryRun = false,
		plugin = {"pretty", "rerun:target/rerun.txt", "timeline:target/timeline",
				"html:target/htmlReports/cucumber-reports.html", "json:target/jsonReports/cucumber-reports.json"},
		monochrome=true
		)

public class RunCucumberTest extends AbstractTestNGCucumberTests {
	private static final Logger logger = LoggerFactory.getLogger(RunCucumberTest.class);

	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios(){
		return super.scenarios();
	}

	public static void tearDown() {
		String className = new Object(){}.getClass().getEnclosingClass().getSimpleName();
		logger.info("All tags executed for class: {}", className);
		RestAssured.reset();    //clean up any resource utilized during test
		logger.info("All Features and Scenarios under test are completed!...");
	}
	
}
