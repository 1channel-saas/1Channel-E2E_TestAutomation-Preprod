package utilities_API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.Properties;

public class DBConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBConnection.class);
    private DBConnection() {}
    private static Connection preprodConnection;
    private static Connection controllerConnection;
    private static final String preprodDB_url = DBConfigFileReader.get("db.preprod_url");
    private static final String controllerDB_url = DBConfigFileReader.get("db.controller_url");
    private static final String db_username = DBConfigFileReader.get("db.username");
    private static final String db_password = DBConfigFileReader.get("db.password");


    //  method to Generate Connection Properties
    private static Properties getConnectionProperties() {
        Properties connectionProps = new Properties();
        connectionProps.setProperty("user", db_username);
        connectionProps.setProperty("password", db_password);
        connectionProps.setProperty("connectTimeout", "10");
        connectionProps.setProperty("socketTimeout", "10");
        return connectionProps;
    }

    public static Connection getPreprodConnection() {
        if (preprodConnection == null) {
            try {
                preprodConnection = DriverManager.getConnection(preprodDB_url, getConnectionProperties());
                LOGGER.info("Connected to Preprod database");
            } catch (SQLTimeoutException timeoutException) {
                    //  Handle timeout-specific error
                    String errorMsg = "Connection timed out while connecting to Preprod database";
                    throw new RuntimeException(errorMsg, timeoutException);
            } catch (SQLException e) {
                //  Handle other SQL-related errors
                String errorMsg = "Failed to connect to Preprod database. Cause: " + e.getMessage();
                throw new RuntimeException(errorMsg, e);
            }
        }
        return preprodConnection;
    }

    public static Connection getControllerConnection() {
        if (controllerConnection == null) {
            try {
                controllerConnection = DriverManager.getConnection(controllerDB_url, getConnectionProperties());
                LOGGER.info("Connected to Controller database");
            } catch (SQLTimeoutException timeoutException) {
                //  Handle timeout-specific error
                String errorMsg = "Connection timed out while connecting to Controller database";
                throw new RuntimeException(errorMsg, timeoutException);
            } catch (SQLException e) {
                //  Handle other SQL-related errors
                String errorMsg = "Failed to connect to Controller database. Cause: " + e.getMessage();
                throw new RuntimeException(errorMsg, e);
            }
        }
        return controllerConnection;
    }


    public static void closeConnections() {
        try {
            if (preprodConnection != null) {
                preprodConnection.close();
                preprodConnection = null;
                LOGGER.info("Closed Preprod database connection");
            }
            if (controllerConnection != null) {
                controllerConnection.close();
                controllerConnection = null;
                LOGGER.info("Closed Controller database connection");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to close database connections", e);
        }
    }

}
