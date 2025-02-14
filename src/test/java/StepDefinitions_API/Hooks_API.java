package StepDefinitions_API;

import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import resources_API.testUtils_API.CommonUtils_API;
import utilities_API.DBConnection;

public class Hooks_API extends CommonUtils_API {
    private static int scenarioCount = 0;
    private static int completedScenarios = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks_API.class);


    @Before(value = "@postgres", order = 0)
    public void isConnectionSuccessful() {
        testDBConnection();
    }

    @Before(order = 1)
    public void beforeScenarios() {
        scenarioCount++;
    }

    @Before(order = 2)
    public void envInfo(Scenario scenario) {
        LOGGER.info("Running scenario: {}. Running environment: {}", scenario.getName(), System.getProperty("environment"));
    }



    @After(order = 0)
    public void getFailedScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            LOGGER.error("Failed scenario: {}", scenario.getName());
        }
    }

    @After(order = 1)
    public void afterScenarios() {
        completedScenarios++;
        if (completedScenarios == scenarioCount) {
            LOGGER.info("Scenario #{} completed!...", scenarioCount);
        }
    }


//    @AfterAll(order = 2) // need to import org.junit.jupiter.api. part of jUnit 5
    @AfterSuite
    public static void closeDBConnections() {
        try {
            DBConnection.closeConnections();
            LOGGER.info("Closing database connections from Hooks if exists......");
        } catch (Exception e) {
            LOGGER.error("Error while closing database connections from Hooks", e);
        }
    }

}
