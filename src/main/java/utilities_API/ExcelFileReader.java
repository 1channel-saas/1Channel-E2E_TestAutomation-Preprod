package utilities_API;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelFileReader {
    private static final Logger Log = LoggerFactory.getLogger(ExcelFileReader.class);
    private static final String DIRECTORY_PATH = Paths.get("Data_Files").toAbsolutePath().toString();

    private ExcelFileReader() {}

    public static File[] getExcelFiles() {
        File folder = new File(DIRECTORY_PATH);
        //return folder.listFiles();
        return folder.listFiles((dir, name) -> name.endsWith(".xlsx") || name.endsWith(".xls"));
    }

    public static List<List<String>> readExcelFile(File file) {
        List<List<String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            for (Sheet sheet : workbook) {
                //  Flag to track the excel header row
                boolean isHeader = true;
                for (Row row : sheet) {
                    //  Skip empty rows
                    if (row.getPhysicalNumberOfCells() == 0) {
                        continue;
                    }
                    //  Skip the excel header row
                    if (isHeader) {
                        isHeader = false;
                        continue;
                    }
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        String cellValue = cell.toString().trim();
                        //  Reads all cell data as string
                        rowData.add(cellValue);
                    }
                    data.add(rowData);
                }
            }
        } catch (IOException e) {
            Log.error("Error reading Excel file: " + file.getName(), e);
        }
        return data;
    }


    //  method to get total row count from raw excel before UI upload
    public static int getTotalRowCount() {
        Log.info("Reading Excel files from directory...");
        File[] files = getExcelFiles();
        if (files == null || files.length == 0) {
            Log.warn("No Excel files found in the directory: {}", DIRECTORY_PATH);
            return 0;
        }

        int totalRowCount = 0;
        for (File file : files) {
            Log.info("Processing file to get Row count before UI upload: {}", file.getName());
            totalRowCount += readExcelFile(file).size();
        }

        Log.info("Total rows processed from all Excel files before UI upload: {}", totalRowCount);
        return totalRowCount;
    }


    //  Method to get multiple values of a specific column using its header name
    public static List<Map<String, String>> getColumnValues(File file, List<String> columnNames) {
        List<Map<String, String>> rowDataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Read only the first sheet
            if (sheet == null) {
                Log.warn("Sheet not found in file: {}", file.getName());
                return rowDataList;
            }

            Map<String, Integer> headerIndexMap = new HashMap<>();
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    // Read header row and map column names to indexes
                    for (Cell cell : row) {
                        headerIndexMap.put(cell.toString().trim(), cell.getColumnIndex());
                    }
                    isHeader = false;
                    continue;
                }

                // Create a map to store values for the requested columns
                Map<String, String> rowData = new HashMap<>();
                for (String columnName : columnNames) {
                    Integer colIndex = headerIndexMap.get(columnName);
                    if (colIndex != null) {
                        Cell cell = row.getCell(colIndex);
                        rowData.put(columnName, (cell != null) ? cell.toString().trim() : ""); // Store column value
                    } else {
                        rowData.put(columnName, "N/A"); // Column not found in file
                    }
                }
                rowDataList.add(rowData);
            }
        } catch (IOException e) {
            Log.error("Error reading Excel file: " + file.getName(), e);
        }
        return rowDataList;
    }


}
