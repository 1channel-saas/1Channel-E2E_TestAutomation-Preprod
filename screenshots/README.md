# Screenshots Folder

This folder automatically stores debug screenshots taken during AI-enhanced test execution.

## Auto-generated screenshots:

- **Debug screenshots** when elements are not found
- **Full screen captures** for troubleshooting
- **Template creation** screenshots

## File naming:

- `description_field_debug.png` - Debug screenshot when Description field not found
- `screenshot_TIMESTAMP.png` - General debug screenshots
- `template_capture_ELEMENT.png` - Screenshots for creating new templates

## Usage:

Screenshots are automatically saved when:
1. **AI element finding fails**
2. **Manual screenshot capture**: `xpathHelper.saveScreenshotForTemplate("filename")`
3. **Debug mode enabled** in AIElementFinder

## Cleanup:

This folder may grow large over time. Regularly clean old screenshots or add to `.gitignore` if not needed for version control.