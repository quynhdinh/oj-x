package org.ojx.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    
    private static String DB_URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    static {
        loadEnvironmentVariables();
    }
    
    private static void loadEnvironmentVariables() {
        try {
            Properties props = new Properties();
            InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream(".env");
            
            if (inputStream == null) {
                throw new RuntimeException("Configuration file .env not found in resources");
            }
            
            props.load(inputStream);
            
            DB_URL = props.getProperty("DB_URL");
            USERNAME = props.getProperty("DB_USERNAME");
            PASSWORD = props.getProperty("DB_PASSWORD");
            
            if (DB_URL == null || USERNAME == null || PASSWORD == null) {
                throw new RuntimeException("Missing required database configuration in .env file");
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error loading .env file: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        System.out.println("Getting connection...");
        return conn;
    }
}
