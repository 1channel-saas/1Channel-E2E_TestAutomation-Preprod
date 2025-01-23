package com.test.channelplay;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
		features = "src/test/resources/com/test/channelplay/feature",
		glue = {"com.test.channelplay.stepDefinition"},
		tags = "@AUT2",
		dryRun = false,
		plugin = {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "rerun:target/rerun.txt", "timeline:target/timeline",},
		monochrome=true
		)

public class RunCucumberTest extends AbstractTestNGCucumberTests{

	@Override
	@DataProvider(parallel = true)
	public Object[][] scenarios(){
		return super.scenarios();
	}
	
}
