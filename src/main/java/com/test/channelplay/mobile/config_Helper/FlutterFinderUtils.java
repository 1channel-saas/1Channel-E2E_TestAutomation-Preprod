package com.test.channelplay.mobile.config_Helper;

import io.github.ashwith.flutter.FlutterFinder;
import io.github.ashwith.flutter.FlutterElement;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import java.time.Duration;
import java.util.Arrays;

public class FlutterFinderUtils {
    
    private final AppiumDriver driver;
    private final FlutterFinder finder;
    
    public FlutterFinderUtils(AppiumDriver driver) {
        this.driver = driver;
        this.finder = new FlutterFinder(driver);
    }
    
    // Find element by Flutter key
    public FlutterElement findByValueKey(String key) {
        return finder.byValueKey(key);
    }
    
    // Find element by text
    public FlutterElement findByText(String text) {
        return finder.byText(text);
    }
    
    // Find element by tooltip
    public FlutterElement findByTooltip(String tooltip) {
        return finder.byToolTip(tooltip);
    }
    
    // Find element by type
    public FlutterElement findByType(String type) {
        return finder.byType(type);
    }
    
    // Find element by semantic label
    public FlutterElement findBySemanticsLabel(String label) {
        return finder.bySemanticsLabel(label);
    }
    
    // Find descendant element
    public FlutterElement findDescendant(FlutterElement parent, String childKey) {
        // byDescendant requires 4 parameters: parent, child, matchRoot, firstMatchOnly
        return finder.byDescendant(parent, finder.byValueKey(childKey), false, true);
    }
    
    // Find ancestor element
    public FlutterElement findAncestor(FlutterElement child, String parentKey) {
        // byAncestor requires 4 parameters: child, parent, matchRoot, firstMatchOnly
        return finder.byAncestor(child, finder.byValueKey(parentKey), false, true);
    }
    
    // Wait for element to be present
    public FlutterElement waitForElement(String key, Duration timeout) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = timeout.toMillis();
        
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {
                FlutterElement element = findByValueKey(key);
                if (element != null) {
                    return element;
                }
            } catch (Exception e) {
                // Element not found yet, continue waiting
            }
            
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        throw new RuntimeException("Element with key '" + key + "' not found after " + timeout.getSeconds() + " seconds");
    }
    
    
    // Tap on element by coordinates
    public void tapByCoordinates(int x, int y) {
        // Using W3C Actions API for tap gesture (Appium 9.0.0+)
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tapSequence = new Sequence(finger, 1);
        
        tapSequence.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tapSequence.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tapSequence.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        
        driver.perform(Arrays.asList(tapSequence));
    }
    
    // Get page source for debugging
    public String getPageSource() {
        return driver.getPageSource();
    }
    
    // Find multiple elements by text pattern (for company lists)
    public void clickElementByIndex(String textPattern, int index) {
        // This would need custom implementation based on your Flutter widget structure
        // You may need to use XPath approach instead for better reliability
        throw new UnsupportedOperationException("Use FlutterXPathHelper.clickCompanyByIndex() instead for better reliability");
    }

}