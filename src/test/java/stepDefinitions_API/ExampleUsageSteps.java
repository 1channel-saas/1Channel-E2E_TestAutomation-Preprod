package stepDefinitions_API;

import io.cucumber.java.en.Then;
import stepDefinitions_API.setting_Microservice.SettingsAndConfigSteps;
import org.junit.Assert;

/**
 * Example class showing how to use the singularName from SettingsAndConfigSteps
 */
public class ExampleUsageSteps {
    
    @Then("I validate the singular name from settings")
    public void validateSingularNameFromSettings() {
        // Get the singularName stored by SettingsAndConfigSteps
        String singularName = SettingsAndConfigSteps.getSingularNameJson();
        
        System.out.println("Retrieved singularName in another class: " + singularName);
        
        // Perform validations
        Assert.assertNotNull("Singular name should not be null", singularName);
        // Add more validations as needed
    }
    
    @Then("I compare singular name with {string}")
    public void compareSingularNameWith(String expectedValue) {
        String actualSingularName = SettingsAndConfigSteps.getSingularNameJson();
        
        Assert.assertEquals("Singular name should match expected value", 
                          expectedValue, actualSingularName);
    }
    
    @Then("I use singular name for further processing")
    public void useSingularNameForProcessing() {
        String singularName = SettingsAndConfigSteps.getSingularNameJson();
        
        if (singularName != null) {
            // Use the singularName for whatever processing you need
            System.out.println("Processing with singularName: " + singularName);
            
            // Example: You can also update it if needed
            SettingsAndConfigSteps.setSingularNameJson(singularName + "_modified");
        }
    }
}