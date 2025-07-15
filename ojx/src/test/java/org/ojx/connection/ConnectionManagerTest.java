package org.ojx.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConnectionManager Tests")
class ConnectionManagerTest {

    @Nested
    @DisplayName("Environment Configuration Tests")
    class EnvironmentConfigurationTests {

        @Test
        @DisplayName("Should load configuration from .env file")
        void shouldLoadConfigurationFromEnvFile() throws Exception {
            // This test verifies that the static block loads configuration correctly
            // Since the static block runs when the class is loaded, we can only verify
            // that the fields are populated (not null)
            
            Field dbUrlField = ConnectionManager.class.getDeclaredField("DB_URL");
            Field usernameField = ConnectionManager.class.getDeclaredField("USERNAME");
            Field passwordField = ConnectionManager.class.getDeclaredField("PASSWORD");
            
            dbUrlField.setAccessible(true);
            usernameField.setAccessible(true);
            passwordField.setAccessible(true);

            String dbUrl = (String) dbUrlField.get(null);
            String username = (String) usernameField.get(null);
            String password = (String) passwordField.get(null);

            // Assert that configuration was loaded
            assertNotNull(dbUrl, "DB_URL should be loaded from .env file");
            assertNotNull(username, "USERNAME should be loaded from .env file");
            assertNotNull(password, "PASSWORD should be loaded from .env file");
            
            assertFalse(dbUrl.trim().isEmpty(), "DB_URL should not be empty");
            assertFalse(username.trim().isEmpty(), "USERNAME should not be empty");
            assertFalse(password.trim().isEmpty(), "PASSWORD should not be empty");
        }

        @Test
        @DisplayName("Should validate database URL format")
        void shouldValidateDatabaseUrlFormat() throws Exception {
            Field dbUrlField = ConnectionManager.class.getDeclaredField("DB_URL");
            dbUrlField.setAccessible(true);
            String dbUrl = (String) dbUrlField.get(null);

            // Basic validation that it looks like a JDBC URL
            assertTrue(dbUrl.startsWith("jdbc:"), "DB_URL should start with 'jdbc:'");
            assertTrue(dbUrl.contains("://"), "DB_URL should contain '://'");
        }

        @Test
        @DisplayName("Should have non-empty configuration values")
        void shouldHaveNonEmptyConfigurationValues() throws Exception {
            Field dbUrlField = ConnectionManager.class.getDeclaredField("DB_URL");
            Field usernameField = ConnectionManager.class.getDeclaredField("USERNAME");
            Field passwordField = ConnectionManager.class.getDeclaredField("PASSWORD");
            
            dbUrlField.setAccessible(true);
            usernameField.setAccessible(true);
            passwordField.setAccessible(true);

            String dbUrl = (String) dbUrlField.get(null);
            String username = (String) usernameField.get(null);
            String password = (String) passwordField.get(null);

            assertFalse(dbUrl.trim().isEmpty(), "DB_URL should not be empty");
            assertFalse(username.trim().isEmpty(), "USERNAME should not be empty");
            assertFalse(password.trim().isEmpty(), "PASSWORD should not be empty");
        }

        @Test
        @DisplayName("Should validate database configuration properties")
        void shouldValidateDatabaseConfigurationProperties() throws Exception {
            Field dbUrlField = ConnectionManager.class.getDeclaredField("DB_URL");
            Field usernameField = ConnectionManager.class.getDeclaredField("USERNAME");
            Field passwordField = ConnectionManager.class.getDeclaredField("PASSWORD");
            
            dbUrlField.setAccessible(true);
            usernameField.setAccessible(true);
            passwordField.setAccessible(true);

            String dbUrl = (String) dbUrlField.get(null);
            String username = (String) usernameField.get(null);
            String password = (String) passwordField.get(null);

            // Test that URL contains expected database components
            if (dbUrl.contains("mysql")) {
                assertTrue(dbUrl.contains("mysql://"), "MySQL URL should contain mysql://");
                assertTrue(dbUrl.contains(":") && (dbUrl.contains("localhost") || dbUrl.contains(".")), 
                    "MySQL URL should contain port and host information");
            }
            
            // Username should be reasonable length
            assertTrue(username.length() >= 2, "Username should be at least 2 characters");
            assertTrue(username.length() <= 64, "Username should not exceed 64 characters");
            
            // Password should meet minimum requirements
            assertTrue(password.length() >= 1, "Password should not be empty");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should establish actual database connection with valid credentials")
        void shouldEstablishActualDatabaseConnection() {
            // This is an integration test that requires actual database
            // It will only pass if the database is properly configured and running
            
            // Act & Assert
            assertDoesNotThrow(() -> {
                try (Connection conn = ConnectionManager.getConnection()) {
                    assertNotNull(conn, "Connection should not be null");
                    assertFalse(conn.isClosed(), "Connection should not be closed immediately");
                    
                    // Verify connection is functional
                    assertTrue(conn.isValid(5), "Connection should be valid within 5 seconds"); // 5 second timeout
                }
            }, "Should be able to establish a valid database connection");
        }

        @Test
        @DisplayName("Should handle multiple concurrent connections")
        void shouldHandleMultipleConcurrentConnections() {
            // Test that multiple connections can be established
            assertDoesNotThrow(() -> {
                try (Connection conn1 = ConnectionManager.getConnection();
                     Connection conn2 = ConnectionManager.getConnection()) {
                    
                    assertNotNull(conn1, "First connection should not be null");
                    assertNotNull(conn2, "Second connection should not be null");
                    assertNotSame(conn1, conn2, "Should be different connection instances");
                    assertFalse(conn1.isClosed(), "First connection should not be closed");
                    assertFalse(conn2.isClosed(), "Second connection should not be closed");
                    
                    // Both connections should be valid
                    assertTrue(conn1.isValid(5), "First connection should be valid");
                    assertTrue(conn2.isValid(5), "Second connection should be valid");
                }
            }, "Should be able to establish multiple concurrent connections");
        }

        @Test
        @DisplayName("Should properly close connections")
        void shouldProperlyCloseConnections() throws SQLException {
            // Test connection lifecycle
            Connection conn = ConnectionManager.getConnection();
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should not be closed initially");
            
            conn.close();
            assertTrue(conn.isClosed(), "Connection should be closed after calling close()");
        }

        @Test
        @DisplayName("Should handle database metadata retrieval")
        void shouldHandleDatabaseMetadataRetrieval() {
            assertDoesNotThrow(() -> {
                try (Connection conn = ConnectionManager.getConnection()) {
                    assertNotNull(conn.getMetaData(), "Database metadata should be available");
                    assertNotNull(conn.getMetaData().getDatabaseProductName(), 
                        "Database product name should be available");
                    assertNotNull(conn.getMetaData().getDriverName(), 
                        "Driver name should be available");
                }
            }, "Should be able to retrieve database metadata");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should establish connection within reasonable time")
        void shouldEstablishConnectionWithinReasonableTime() {
            long startTime = System.currentTimeMillis();
            
            assertDoesNotThrow(() -> {
                try (Connection conn = ConnectionManager.getConnection()) {
                    assertNotNull(conn, "Connection should not be null");
                }
            }, "Should be able to establish connection without throwing exceptions");
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            // Connection should be established within 10 seconds (adjust based on your requirements)
            assertTrue(duration < 10000, 
                "Connection should be established within 10 seconds, but took " + duration + "ms");
        }

        @Test
        @DisplayName("Should handle rapid connection requests")
        void shouldHandleRapidConnectionRequests() {
            assertDoesNotThrow(() -> {
                // Establish and close 5 connections rapidly
                for (int i = 0; i < 5; i++) {
                    try (Connection conn = ConnectionManager.getConnection()) {
                        assertNotNull(conn, "Connection " + (i + 1) + " should not be null");
                        assertTrue(conn.isValid(2), "Connection " + (i + 1) + " should be valid");
                    }
                }
            }, "Should handle rapid connection establishment and closure");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw SQLException for invalid operations on closed connection")
        void shouldThrowSQLExceptionForInvalidOperationsOnClosedConnection() throws SQLException {
            Connection conn = ConnectionManager.getConnection();
            conn.close();
            
            // Should throw SQLException when trying to use closed connection
            assertThrows(SQLException.class, () -> {
                conn.createStatement();
            }, "Should throw SQLException when creating statement on closed connection");
        }

        @Test
        @DisplayName("Should handle connection validation properly")
        void shouldHandleConnectionValidationProperly() throws SQLException {
            try (Connection conn = ConnectionManager.getConnection()) {
                // Test with valid timeout
                assertTrue(conn.isValid(5), "Connection should be valid with 5 second timeout");
                
                // Test with minimal timeout
                assertTrue(conn.isValid(1), "Connection should be valid with 1 second timeout");
                
                // Test with zero timeout (should not block)
                assertDoesNotThrow(() -> conn.isValid(0), 
                    "isValid(0) should not throw exception");
            }
        }
    }

    @Nested
    @DisplayName("Functional Tests")
    class FunctionalTests {

        @Test
        @DisplayName("Should support basic database operations")
        void shouldSupportBasicDatabaseOperations() {
            assertDoesNotThrow(() -> {
                try (Connection conn = ConnectionManager.getConnection()) {
                    // Test creating a statement
                    assertNotNull(conn.createStatement(), "Should be able to create statement");
                    
                    // Test transaction support (autoCommit should be available)
                    assertDoesNotThrow(() -> conn.getAutoCommit(), "Should be able to check autoCommit status");
                    
                    // Test catalog support
                    assertDoesNotThrow(() -> conn.getCatalog(), "Should be able to get catalog");
                }
            }, "Should support basic database operations");
        }

        @Test
        @DisplayName("Should provide connection information")
        void shouldProvideConnectionInformation() {
            assertDoesNotThrow(() -> {
                try (Connection conn = ConnectionManager.getConnection()) {
                    // Should be able to get connection URL (driver dependent)
                    assertDoesNotThrow(() -> conn.getMetaData().getURL(), 
                        "Should be able to get connection URL");
                    
                    // Should be able to get user name
                    assertDoesNotThrow(() -> conn.getMetaData().getUserName(), 
                        "Should be able to get user name");
                    
                    // Should be able to check if connection is read-only
                    assertDoesNotThrow(() -> conn.isReadOnly(), 
                        "Should be able to check read-only status");
                }
            }, "Should provide connection information");
        }
    }
}
