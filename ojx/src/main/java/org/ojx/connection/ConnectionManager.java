package org.ojx.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.ojx.config.DatabaseConfigManager;

public class ConnectionManager {
    
    private static final DatabaseConfigManager dbConfig = DatabaseConfigManager.getInstance();
    
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
            dbConfig.getDbUrl(), 
            dbConfig.getUsername(), 
            dbConfig.getPassword()
        );
        System.out.println("Getting connection...");
        return conn;
    }
}
