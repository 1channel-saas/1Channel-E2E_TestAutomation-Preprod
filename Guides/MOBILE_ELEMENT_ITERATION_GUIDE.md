# Mobile Element Iteration Guide

## Template Capture Configuration

### Auto-Capture Template Parameters
When templates are automatically captured, the following parameters are saved:

1. **Image Dimensions**: Captured at the element's actual size (width × height in pixels)
2. **Aspect Ratio**: Automatically calculated as width/height
3. **Screen Resolution**: Captures the device's screen size for relative positioning
4. **Relative Position**: Stored as percentages (0.0 to 1.0) of screen dimensions
5. **Visual Hash**: 64-bit perceptual hash for duplicate detection
6. **Dominant Colors**: Top 3 colors extracted from the image
7. **Pixel Density**: Calculated to detect text/icon presence

### Emulator Specifications
- **Resolution**: 1080×2400 pixels
- **DPI**: 420 (xxhdpi)
- **Device**: emulator-5554

### Manual Screenshot Capture Process

#### Method 1: Using ADB Commands
```bash
# Take screenshot
adb -s emulator-5554 shell screencap -p /sdcard/element.png
adb pull /sdcard/element.png

# Or direct to file
adb -s emulator-5554 exec-out screencap -p > screenshot.png
```

#### Method 2: Using Emulator Shortcut
1. Navigate to the screen with your element in the emulator
2. Press `Ctrl + S` to take screenshot
3. Default save location:
   - Windows: `C:\Users\Soumya\Pictures\Screenshots\` or `C:\Users\Soumya\Documents\`
4. Crop to element only (not full screen)
5. Save to: `templates/manual_captured_images/`

#### Method 3: Android Studio Layout Inspector
1. Open Android Studio
2. View → Tool Windows → Layout Inspector
3. Select running emulator
4. Click on element → Right-click → "Save Screenshot"

### Manual Template Requirements
- ✅ **Crop to element only** (not full 1080×2400 screen)
- ✅ **PNG format**
- ✅ **Consistent naming** (lowercase, underscores)
- ✅ **Metadata JSON** generated automatically
- ✅ **Same visual hash algorithm** for duplicate detection

### Generate Metadata for Manual Templates
```bash
# Process single template
mvn exec:java -Dexec.mainClass="com.test.channelplay.mobile.screens.config_Helper.ManualTemplateHelper" -Dexec.args="templates/manual_captured_images/yourElement.png yourElementName"

# Process all templates in folder
mvn exec:java -Dexec.mainClass="com.test.channelplay.mobile.screens.config_Helper.ManualTemplateHelper" -Dexec.args="--all"

# Validate template
mvn exec:java -Dexec.mainClass="com.test.channelplay.mobile.screens.config_Helper.ManualTemplateHelper" -Dexec.args="--validate templates/manual_captured_images/yourElement.png"
```

## Select Customer Dropdown - Element Iteration

### XPath Strategies for Customer List

Based on the Select Customer dropdown UI analysis, here are the different XPath patterns:

1. **Header/Title**: `//android.widget.EditText` (Search field)
2. **Customer List Container**: `//android.widget.FrameLayout[@resource-id="android:id/content"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View`
3. **Specific Customer**: `//android.view.View[@content-desc="rest@2"]`
4. **OK Button**: `//android.widget.Button[@content-desc="OK"]`
5. **Cancel Button**: `//android.widget.Button[@content-desc="CANCEL"]`

### Implementation Methods

#### Method 1: Direct Selection by Content-Desc (Most Reliable)
```java
// Select specific customer directly
WebElement specificCustomer = driver.findElement(By.xpath("//android.view.View[@content-desc='rest@2']"));
specificCustomer.click();
System.out.println("Selected customer: rest@2");
```

#### Method 2: Iterate Through All List Items
```java
// Get all customer items in the list
List<WebElement> customerList = driver.findElements(By.xpath(selectCustomerFrameList_xpath));

if (!customerList.isEmpty()) {
    for (int i = 0; i < customerList.size(); i++) {
        try {
            WebElement customer = customerList.get(i);
            String customerText = customer.getAttribute("content-desc");
            if (customerText == null) {
                customerText = customer.getText();
            }

            // Check if this is the customer we want
            if (customerText != null && customerText.contains("rest@2")) {
                customer.click();
                System.out.println("Selected customer: " + customerText);
                break;
            } else if (i == 0) {
                // Or just click the first customer
                customer.click();
                System.out.println("Selected first customer in list");
                break;
            }
        } catch (Exception ex) {
            log.debug("Could not click customer at index " + i);
        }
    }
}
```

#### Method 3: Dynamic Selection by Name
```java
public void selectCustomerByName(String customerName) {
    // Open dropdown
    WebElement dropdown = driver.findElement(By.xpath(SelectCustomer_dropdown));
    dropdown.click();
    commonUtils.sleep(1000);

    // Search for customer by content-desc
    String customerXPath = String.format("//android.view.View[@content-desc='%s']", customerName);
    try {
        WebElement customer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(customerXPath)));
        customer.click();
        System.out.println("Selected customer: " + customerName);
    } catch (Exception e) {
        // Try alternative approaches
        List<WebElement> allViews = driver.findElements(By.xpath("//android.view.View"));
        for (WebElement view : allViews) {
            String desc = view.getAttribute("content-desc");
            if (desc != null && desc.contains(customerName)) {
                view.click();
                System.out.println("Found and selected customer: " + desc);
                break;
            }
        }
    }
}
```

### Best Practices for List Iteration

1. **Always check if list is not empty** before iteration
2. **Use content-desc attribute** for Flutter/mobile apps (more reliable than text)
3. **Implement try-catch** for each element interaction
4. **Add appropriate waits** between interactions
5. **Log actions** for debugging purposes
6. **Have fallback strategies** (e.g., select first item if specific item not found)

### Common Issues and Solutions

| Issue | Solution |
|-------|----------|
| Elements not found | Use multiple XPath strategies with fallbacks |
| Stale element exceptions | Re-find elements within the loop |
| Content-desc is null | Fall back to getText() method |
| Dynamic loading | Add explicit waits before finding elements |
| List not fully loaded | Add sleep/wait after opening dropdown |

### Framework Integration

The updated `AddActivityPage.java` includes the iteration logic in the `selectCustomerFromSelectCustomerDropdown()` method, which:
- Opens the dropdown
- Waits for list to load
- Attempts to find specific customer
- Falls back to selecting first customer if needed
- Properly handles exceptions