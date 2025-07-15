package org.ojx.config;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Singleton class for managing application-wide settings and configurations.
 * Provides centralized access to configuration properties.
 */
public class ApplicationSettings {
    
    private static volatile ApplicationSettings instance;
    private final Properties settings;
    
    // Default settings
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_MAX_SUBMISSION_SIZE = 1000000; // 1MB
    private static final int DEFAULT_SESSION_TIMEOUT = 3600; // 1 hour
    private static final String DEFAULT_THEME = "default";
    
    // Private constructor
    private ApplicationSettings() {
        this.settings = new Properties();
        loadDefaultSettings();
        loadCustomSettings();
    }
    
    /**
     * Thread-safe singleton implementation
     */
    public static ApplicationSettings getInstance() {
        if (instance == null) {
            synchronized (ApplicationSettings.class) {
                if (instance == null) {
                    instance = new ApplicationSettings();
                }
            }
        }
        return instance;
    }
    
    private void loadDefaultSettings() {
        settings.setProperty("app.page.size", String.valueOf(DEFAULT_PAGE_SIZE));
        settings.setProperty("app.submission.max.size", String.valueOf(DEFAULT_MAX_SUBMISSION_SIZE));
        settings.setProperty("app.session.timeout", String.valueOf(DEFAULT_SESSION_TIMEOUT));
        settings.setProperty("app.theme", DEFAULT_THEME);
        settings.setProperty("app.name", "OJX - Online Judge System");
        settings.setProperty("app.version", "1.0.0");
    }
    
    private void loadCustomSettings() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");
            if (inputStream != null) {
                Properties customProps = new Properties();
                customProps.load(inputStream);
                
                // Override defaults with custom settings
                for (String key : customProps.stringPropertyNames()) {
                    settings.setProperty(key, customProps.getProperty(key));
                }
                inputStream.close();
            }
        } catch (IOException e) {
            ApplicationLogger.getInstance().warning("Could not load custom application.properties: " + e.getMessage());
        }
    }
    
    /**
     * Get a string property
     */
    public String getProperty(String key) {
        return settings.getProperty(key);
    }
    
    /**
     * Get a string property with default value
     */
    public String getProperty(String key, String defaultValue) {
        return settings.getProperty(key, defaultValue);
    }
    
    /**
     * Get an integer property
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = settings.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            ApplicationLogger.getInstance().warning("Invalid integer property: " + key + " = " + settings.getProperty(key));
            return defaultValue;
        }
    }
    
    /**
     * Get a boolean property
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = settings.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    // Convenience methods for common settings
    public int getPageSize() {
        return getIntProperty("app.page.size", DEFAULT_PAGE_SIZE);
    }
    
    public int getMaxSubmissionSize() {
        return getIntProperty("app.submission.max.size", DEFAULT_MAX_SUBMISSION_SIZE);
    }
    
    public int getSessionTimeout() {
        return getIntProperty("app.session.timeout", DEFAULT_SESSION_TIMEOUT);
    }
    
    public String getApplicationName() {
        return getProperty("app.name", "OJX");
    }
    
    public String getApplicationVersion() {
        return getProperty("app.version", "1.0.0");
    }
    
    public String getTheme() {
        return getProperty("app.theme", DEFAULT_THEME);
    }
    
    /**
     * Set a property (runtime only, not persisted)
     */
    public void setProperty(String key, String value) {
        settings.setProperty(key, value);
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton instance cannot be cloned");
    }
}
