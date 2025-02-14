package com.test.channelplay.cucumber.Options;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.*;

@CucumberOptions(features = "@target/rerun.txt",
plugin = {"pretty", "rerun:target/rerun.txt", "timeline:target/timeline"}, monochrome=true)


public class ReRunCucumberTest extends AbstractTestNGCucumberTests{
	
}
