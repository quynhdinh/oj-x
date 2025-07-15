package org.ojx.config;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Singleton Logger Manager for centralized logging configuration.
 * Ensures consistent logging setup across the entire application.
 */
public class ApplicationLogger {
    
    private static volatile ApplicationLogger instance;
    private final Logger logger;
    
    // Private constructor
    private ApplicationLogger() {
        this.logger = Logger.getLogger("OJX-Application");
        configureLogger();
    }
    
    /**
     * Thread-safe singleton implementation
     */
    public static ApplicationLogger getInstance() {
        if (instance == null) {
            synchronized (ApplicationLogger.class) {
                if (instance == null) {
                    instance = new ApplicationLogger();
                }
            }
        }
        return instance;
    }
    
    private void configureLogger() {
        // Create custom console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        
        // Configure logger
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);
    }
    
    /**
     * Get the application logger instance
     */
    public Logger getLogger() {
        return logger;
    }
    
    /**
     * Get a logger for a specific class
     */
    public Logger getLogger(Class<?> clazz) {
        Logger classLogger = Logger.getLogger(clazz.getName());
        classLogger.setParent(this.logger);
        return classLogger;
    }
    
    /**
     * Log info message
     */
    public void info(String message) {
        logger.info(message);
    }
    
    /**
     * Log warning message
     */
    public void warning(String message) {
        logger.warning(message);
    }
    
    /**
     * Log error message
     */
    public void severe(String message) {
        logger.severe(message);
    }
    
    /**
     * Log error with exception
     */
    public void severe(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton instance cannot be cloned");
    }
}
