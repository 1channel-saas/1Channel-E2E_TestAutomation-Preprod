package com.test.channelplay.cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.*;

@CucumberOptions(
		features = "@target/rerun.txt",
		glue = {"com.test.channelplay.stepDefinition", "com.test.channelplay.stepDefinition_Mobile", "stepDefinitions_API"},
		plugin = {"pretty", "rerun:target/rerun.txt", "timeline:target/timeline"},
		monochrome=true
)


public class ReRunCucumberTest extends AbstractTestNGCucumberTests {
}
