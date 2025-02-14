package utilities_API;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelFileReader {
    private static final Logger Log = LoggerFactory.getLogger(ExcelFileReader.class);
    private ExcelFileReader() {}

    public static void readExcelFile(String filePath) throws IOException {
        //  Open the Excel file
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);

        //  Create Workbook instance for XLSX file (XSSFWorkbook)
        Workbook workbook = new XSSFWorkbook(fis);

        //  Get the first sheet
        Sheet sheet = workbook.getSheetAt(0);

        //  Read and modify the Excel file
        for (Row row : sheet) {
            for (Cell cell : row) {
                // Modify only string cells (you can adjust logic as needed)
                if (cell.getCellType() == CellType.STRING) {
                    String originalValue = cell.getStringCellValue();
                    // Dynamically modify the value
                    String modifiedValue = originalValue + "_Modified";
                    cell.setCellValue(modifiedValue);
                }
                // Handle numeric cells
                else if (cell.getCellType() == CellType.NUMERIC) {
                    double originalValue = cell.getNumericCellValue();
                    // Example: Increase numeric values by 1
                    cell.setCellValue(originalValue + 1);
                }
            }
        }

        //  Close the FileInputStream
        fis.close();

        //  Save the changes to the same file
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);

        //  Close resources
        fos.close();
        workbook.close();

        Log.info("Excel file data updated and saved successfully");
    }

}
