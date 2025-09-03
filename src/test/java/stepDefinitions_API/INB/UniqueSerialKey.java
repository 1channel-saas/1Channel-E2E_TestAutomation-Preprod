package stepDefinitions_API.INB;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resources_API.testUtils_API.CommonUtils_API;
import resources_API.testUtils_API.GetApiResponseObject;
import utilities_API.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniqueSerialKey extends CommonUtils_API {

    private static final Logger log = LoggerFactory.getLogger(CustomerBulkUpload_APISteps.class);

    private Connection preprodConnection;
    ResultSet resultSet;
    private final GetApiResponseObject getApiResponseObject;

    public UniqueSerialKey() {
        this.getApiResponseObject = GetApiResponseObject.getInstance();
        CommonUtils_API.getInstance();
    }



    //  scenario - uniqueSerialNo_INB

    @When("validate all serial_Nos are unique for {string} for users under {string} table for INB")
    public void validateAllSerial_NosAreUniqueForForUsersUnderTableForINB(String project_id, String tableName) {
        System.out.println("\n========== Processing Project ID: " + project_id + " ==========");
        System.out.println("Table Name: " + tableName);
        
        preprodConnection = DBConnection.getPreprodConnection();
        
        //  Check if there are any records for this project_id
        String checkRecordsQuery = "SELECT COUNT(*) as total_count FROM " + tableName + 
                                  " WHERE project_id = " + project_id;
        System.out.println("Checking total records: " + checkRecordsQuery);
        
        try {
            ResultSet countResult = executeQuery(checkRecordsQuery, preprodConnection);
            if (countResult.next()) {
                int totalCount = countResult.getInt("total_count");
                System.out.println("Total records found for project_id " + project_id + ": " + totalCount);
            }
            closeResultSet(countResult);
        } catch (SQLException e) {
            System.err.println("Error checking record count: " + e.getMessage());
        }
        
        String serialNoOccurance_query = "SELECT \n" +
                "    COALESCE(NULLIF(TRIM(u.serial_no), ''), '<BLANK_OR_NULL>') AS serial_no_display,\n" +
                "    u.serial_no AS original_serial_no,\n" +
                "    u.user_id,\n" +
                "    u.active,\n" +
                "    u.owner_id,\n" +
                "\tu.project_id,\n" +
                "    dup.occurrence\n" +
                "FROM " + tableName + " u\n" +
                "JOIN (\n" +
                "    SELECT serial_no, COUNT(*) AS occurrence\n" +
                "    FROM " + tableName + " \n" +
                "    WHERE project_id = " + project_id + " \n" +
                "    GROUP BY serial_no\n" +
                "    HAVING COUNT(*) > 1\n" +
                ") dup ON u.serial_no IS NOT DISTINCT FROM dup.serial_no\n" +
                "WHERE u.project_id = " + project_id+ " \n" +
                "ORDER BY dup.occurrence DESC, u.user_id;";
        System.out.println("\nExecuting duplicate check query: " + serialNoOccurance_query);

        List<Map<String, String>> serialNoOccuranceResults = new ArrayList<>();
        List<Map<String, String>> nullOrBlankDuplicates = new ArrayList<>();
        List<Map<String, String>> validKeyDuplicates = new ArrayList<>();

        try {
            resultSet = executeQuery(serialNoOccurance_query, preprodConnection);
            int recordCount = 0;
            while (resultSet.next()) {
                recordCount++;
                Map<String, String> row = new HashMap<>();
                row.put("serial_no_display", resultSet.getString("serial_no_display"));
                row.put("original_serial_no", resultSet.getString("original_serial_no"));
                row.put("user_id", resultSet.getString("user_id"));
                row.put("active", resultSet.getString("active"));
                row.put("owner_id", resultSet.getString("owner_id"));
                row.put("project_id", resultSet.getString("project_id"));
                row.put("occurrence", resultSet.getString("occurrence"));
                serialNoOccuranceResults.add(row);
                
                // Categorize duplicates
                String originalSerialNo = resultSet.getString("original_serial_no");
                if (originalSerialNo == null || originalSerialNo.trim().isEmpty()) {
                    nullOrBlankDuplicates.add(row);
                } else {
                    validKeyDuplicates.add(row);
                }
            }
            System.out.println("Duplicate records found: " + recordCount);
        } catch (SQLException e) {
            System.err.println("ERROR executing query for project_id " + project_id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }
        
        // Check for duplicates and handle them appropriately
        if (serialNoOccuranceResults.isEmpty()) {
            System.out.println("No duplicate serial numbers found for project_id: " + project_id);
        } else {
            System.out.println("\n Duplicate serial numbers found for project_id: " + project_id);
            
            //  Handle null/blank duplicates with WARNING
            if (!nullOrBlankDuplicates.isEmpty()) {
                System.out.println("\n[WARNING] Null or blank serial numbers found with duplicates:");
                for (Map<String, String> row : nullOrBlankDuplicates) {
                    String serialNo = row.get("serial_no_display");
                    String occurrence = row.get("occurrence");
                    String userId = row.get("user_id");
                    String isActive = row.get("active");
                    String owner = row.get("owner_id");
                    System.out.println("  [WARN] Serial No: " + serialNo + 
                                     ", User ID: " + userId + 
                                     ", Active: " + isActive + 
                                     ", Owner: " + owner + 
                                     ", Occurrence: " + occurrence);
                }
            }
            
            //  Handle valid key duplicates with ERROR and assertion failure
            if (!validKeyDuplicates.isEmpty()) {
                System.err.println("\n[ERROR] Valid serial keys found with duplicates:");
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Duplicate valid serial keys found for project_id ").append(project_id).append(":\n");
                
                for (Map<String, String> row : validKeyDuplicates) {
                    String serialNo = row.get("serial_no_display");
                    String occurrence = row.get("occurrence");
                    String userId = row.get("user_id");
                    String isActive = row.get("active");
                    String owner = row.get("owner_id");
                    
                    String errorLine = String.format("  [ERROR] Serial No: %s, User ID: %s, Active: %s, Owner: %s, Occurrence: %s",
                                                    serialNo, userId, isActive, owner, occurrence);
                    System.err.println(errorLine);
                    errorMessage.append(errorLine).append("\n");
                }
                
                //  Fail the test with assertion error
                throw new AssertionError(errorMessage.toString());
            }
        }
        System.out.println("========== Completed Project ID: " + project_id + " ==========\n");
    }

    @Then("validate all serial_Nos are unique for {string} for customers under {string} table for INB")
    public void validateAllSerial_NosAreUniqueForForCustomersUnderTableForINB(String project_id, String tableName) {
        System.out.println("\n========== Processing Customer Serial Numbers for Project ID: " + project_id + " ==========");
        System.out.println("Table Name: " + tableName);

        preprodConnection = DBConnection.getPreprodConnection();

        //  Check if there are any records for this project_id
        String checkRecordsQuery = "SELECT COUNT(*) as total_count FROM " + tableName +
                " WHERE project_id = " + project_id;
        System.out.println("Checking total customer records: " + checkRecordsQuery);

        try {
            ResultSet countResult = executeQuery(checkRecordsQuery, preprodConnection);
            if (countResult.next()) {
                int totalCount = countResult.getInt("total_count");
                System.out.println("Total customer records found for project_id " + project_id + ": " + totalCount);
            }
            closeResultSet(countResult);
        } catch (SQLException e) {
            System.err.println("Error checking customer record count: " + e.getMessage());
        }

        String serialNoOccurance_query = "SELECT \n" +
                "    COALESCE(NULLIF(TRIM(c.serial_no), ''), '<BLANK_OR_NULL>') AS serial_no_display,\n" +
                "    c.serial_no AS original_serial_no,\n" +
                "    c.id,\n" +
                "    c.created_by,\n" +
                "    c.updated_by,\n" +
                "    c.owner_id,\n" +
                "    c.is_active,\n" +
                "\tc.project_id,\n" +
                "    dup.occurrence\n" +
                "FROM " + tableName + " c\n" +
                "JOIN (\n" +
                "    SELECT serial_no, COUNT(*) AS occurrence\n" +
                "    FROM " + tableName + " \n" +
                "    WHERE project_id = " + project_id + " \n" +
                "    GROUP BY serial_no\n" +
                "    HAVING COUNT(*) > 1\n" +
                ") dup ON c.serial_no IS NOT DISTINCT FROM dup.serial_no\n" +
                "WHERE c.project_id = " + project_id+ " \n" +
                "ORDER BY dup.occurrence DESC, c.id;";
        System.out.println("\nExecuting customer duplicate check query: " + serialNoOccurance_query);

        List<Map<String, String>> serialNoOccuranceResults = new ArrayList<>();
        List<Map<String, String>> nullOrBlankDuplicates = new ArrayList<>();
        List<Map<String, String>> validKeyDuplicates = new ArrayList<>();

        try {
            resultSet = executeQuery(serialNoOccurance_query, preprodConnection);
            int recordCount = 0;
            while (resultSet.next()) {
                recordCount++;
                Map<String, String> row = new HashMap<>();
                row.put("serial_no_display", resultSet.getString("serial_no_display"));
                row.put("original_serial_no", resultSet.getString("original_serial_no"));
                row.put("id", resultSet.getString("id"));
                row.put("active", resultSet.getString("is_active"));
                row.put("owner_id", resultSet.getString("owner_id"));
                row.put("project_id", resultSet.getString("project_id"));
                row.put("occurrence", resultSet.getString("occurrence"));
                row.put("created_by", resultSet.getString("created_by"));
                row.put("updated_by", resultSet.getString("updated_by"));
                serialNoOccuranceResults.add(row);

                // Categorize duplicates
                String originalSerialNo = resultSet.getString("original_serial_no");
                if (originalSerialNo == null || originalSerialNo.trim().isEmpty()) {
                    nullOrBlankDuplicates.add(row);
                } else {
                    validKeyDuplicates.add(row);
                }
            }
            System.out.println("Customer duplicate records found: " + recordCount);
        } catch (SQLException e) {
            System.err.println("ERROR executing customer query for project_id " + project_id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch customer data from database after query execution", e);
        } finally {
            closeResultSet(resultSet);
        }

        // Check for duplicates and handle them appropriately
        if (serialNoOccuranceResults.isEmpty()) {
            System.out.println("No duplicate serial numbers found for customer project_id: " + project_id);
        } else {
            System.out.println("\n Duplicate customer serial numbers found for project_id: " + project_id);

            //  Handle null/blank duplicates with WARNING
            if (!nullOrBlankDuplicates.isEmpty()) {
                System.out.println("\n[WARNING] Null or blank customer serial numbers found with duplicates:");
                for (Map<String, String> row : nullOrBlankDuplicates) {
                    String serialNo = row.get("serial_no_display");
                    String companyId = row.get("id");
                    String owner = row.get("owner_id");
                    String isActive = row.get("active");
                    String createdBy = row.get("created_by");
                    String updatedBy = row.get("updated_by");
                    String occurrence = row.get("occurrence");
                    System.out.println("  [WARN] Serial No: " + serialNo +
                            ", Company ID: " + companyId +
                            ", Owner: " + owner +
                            ", Active: " + isActive +
                            ", Created By: " + createdBy +
                            ", Updated By: " + updatedBy +
                            ", Occurrence: " + occurrence);
                }
            }

            //  Handle valid key duplicates with ERROR and assertion failure
            if (!validKeyDuplicates.isEmpty()) {
                System.err.println("\n[ERROR] Valid customer serial keys found with duplicates:");
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Duplicate valid customer serial keys found for project_id ").append(project_id).append(":\n");

                for (Map<String, String> row : validKeyDuplicates) {
                    String serialNo = row.get("serial_no_display");
                    String occurrence = row.get("occurrence");
                    String companyId = row.get("id");
                    String isActive = row.get("active");
                    String owner = row.get("owner_id");

                    String errorLine = String.format("  [ERROR] Serial No: %s, Company ID: %s, Active: %s, Owner: %s, Occurrence: %s",
                            serialNo, companyId, isActive, owner, occurrence);
                    System.err.println(errorLine);
                    errorMessage.append(errorLine).append("\n");
                }

                //  Fail the test with assertion error
                throw new AssertionError(errorMessage.toString());
            }
        }
        System.out.println("========== Completed Customer Project ID: " + project_id + " ==========\n");
    }

}
