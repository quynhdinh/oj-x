package org.ojx.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton class for managing database configuration.
 * Ensures only one instance exists and provides centralized configuration management.
 */
public class DatabaseConfigManager {
    
    private static volatile DatabaseConfigManager instance;
    private final String dbUrl;
    private final String username;
    private final String password;
    
    // Private constructor to prevent external instantiation
    private DatabaseConfigManager() {
        Properties props = loadProperties();
        this.dbUrl = props.getProperty("DB_URL");
        this.username = props.getProperty("DB_USERNAME");
        this.password = props.getProperty("DB_PASSWORD");
        
        validateConfiguration();
    }
    
    /**
     * Double-checked locking singleton implementation for thread safety
     */
    public static DatabaseConfigManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfigManager.class) {
                if (instance == null) {
                    instance = new DatabaseConfigManager();
                }
            }
        }
        return instance;
    }
    
    private Properties loadProperties() {
        try {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(".env");
            
            if (inputStream == null) {
                throw new RuntimeException("Configuration file .env not found in resources");
            }
            
            props.load(inputStream);
            return props;
            
        } catch (IOException e) {
            throw new RuntimeException("Error loading .env file: " + e.getMessage(), e);
        }
    }
    
    private void validateConfiguration() {
        if (dbUrl == null || username == null || password == null) {
            throw new RuntimeException("Missing required database configuration in .env file");
        }
        
        if (dbUrl.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new RuntimeException("Database configuration cannot be empty");
        }
    }
    
    // Getters
    public String getDbUrl() {
        return dbUrl;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton instance cannot be cloned");
    }
}
