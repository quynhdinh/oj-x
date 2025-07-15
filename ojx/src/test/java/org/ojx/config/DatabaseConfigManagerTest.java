package org.ojx.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DatabaseConfigManager Singleton Tests")
class DatabaseConfigManagerTest {

    @Nested
    @DisplayName("Singleton Pattern Tests")
    class SingletonPatternTests {

        @Test
        @DisplayName("Should return the same instance on multiple calls")
        void shouldReturnSameInstance() {
            DatabaseConfigManager instance1 = DatabaseConfigManager.getInstance();
            DatabaseConfigManager instance2 = DatabaseConfigManager.getInstance();
            
            assertSame(instance1, instance2, "getInstance() should return the same instance");
        }

        @Test
        @DisplayName("Should prevent cloning")
        void shouldPreventCloning() {
            DatabaseConfigManager instance = DatabaseConfigManager.getInstance();
            
            assertThrows(CloneNotSupportedException.class, () -> {
                instance.clone();
            }, "Cloning should throw CloneNotSupportedException");
        }
    }

    @Nested
    @DisplayName("Configuration Tests")
    class ConfigurationTests {

        @Test
        @DisplayName("Should load database configuration")
        void shouldLoadDatabaseConfiguration() {
            DatabaseConfigManager config = DatabaseConfigManager.getInstance();
            
            assertNotNull(config.getDbUrl(), "DB URL should not be null");
            assertNotNull(config.getUsername(), "Username should not be null");
            assertNotNull(config.getPassword(), "Password should not be null");
            
            assertFalse(config.getDbUrl().trim().isEmpty(), "DB URL should not be empty");
            assertFalse(config.getUsername().trim().isEmpty(), "Username should not be empty");
            assertFalse(config.getPassword().trim().isEmpty(), "Password should not be empty");
        }

        @Test
        @DisplayName("Should validate database URL format")
        void shouldValidateDatabaseUrlFormat() {
            DatabaseConfigManager config = DatabaseConfigManager.getInstance();
            String dbUrl = config.getDbUrl();
            
            assertTrue(dbUrl.startsWith("jdbc:"), "DB URL should start with 'jdbc:'");
            assertTrue(dbUrl.contains("://"), "DB URL should contain '://'");
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should be thread-safe")
        void shouldBeThreadSafe() throws InterruptedException {
            final DatabaseConfigManager[] instances = new DatabaseConfigManager[10];
            Thread[] threads = new Thread[10];
            
            // Create multiple threads that get the instance
            for (int i = 0; i < 10; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    instances[index] = DatabaseConfigManager.getInstance();
                });
            }
            
            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            
            // Verify all instances are the same
            DatabaseConfigManager firstInstance = instances[0];
            for (int i = 1; i < instances.length; i++) {
                assertSame(firstInstance, instances[i], 
                    "All instances should be the same in multi-threaded environment");
            }
        }
    }
}
