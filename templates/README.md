# Templates Folder

This folder contains template images for AI-based element recognition.

## How to use:

1. **Take screenshots** of UI elements you want to find
2. **Crop the specific element** (button, field, etc.) 
3. **Save as PNG** with descriptive names
4. **Reference in your tests** like: `"templates/description_field.png"`

## Recommended naming convention:

- `description_field.png` - Description input field
- `submit_button.png` - Submit/Save buttons  
- `dropdown_arrow.png` - Dropdown arrows
- `customer_dropdown.png` - Customer selection dropdown
- `ok_button.png` - OK confirmation buttons

## Tips for good templates:

- **High contrast** - Clear element boundaries
- **Unique features** - Avoid generic elements that appear multiple times
- **Proper size** - Not too small (min 20x20px) or too large
- **Clean capture** - No overlapping elements or partial visibility

## Screenshot tools:

- **Windows**: Snipping Tool, Win+Shift+S
- **Third-party**: Greenshot, LightShot
- **From your test**: Use `xpathHelper.saveScreenshotForTemplate("filename.png")`

## Example usage in tests:

```java
// AI will try to find this template image if XPath fails
xpathHelper.smartFindElementWithAI(
    "Description Field",
    new String[]{"//xpath/to/element"},
    "templates/description_field.png",
    "Description"
);
```