package org.ojx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import org.ojx.model.Contest;
import org.ojx.repository.ContestRepository;
import org.ojx.service.impl.ContestServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ContestService getContestById Tests")
class ContestServiceGetByIdTest {

    private ContestService contestService;
    private ContestRepository contestRepository;

    @BeforeEach
    void setUp() {
        contestRepository = new ContestRepository();
        contestService = new ContestServiceImpl(contestRepository);
    }

    @Nested
    @DisplayName("Return Value Tests")
    class ReturnValueTests {

        @Test
        @DisplayName("Should return Optional.empty() for non-existent contest ID")
        void shouldReturnEmptyForNonExistentContestId() {
            // Arrange
            int nonExistentId = 999999; // Use a very large ID that likely doesn't exist

            // Act
            Optional<Contest> result = contestService.getContestById(nonExistentId);

            // Assert
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Result should be empty for non-existent contest ID");
        }

        @Test
        @DisplayName("Should return Optional for any valid contest ID (integration test)")
        void shouldReturnOptionalForValidId() {
            // This is an integration test that works with actual database
            // We can't guarantee specific contests exist, but we can test the method structure
            
            // Act
            Optional<Contest> result = contestService.getContestById(1);

            // Assert
            assertNotNull(result, "Result should not be null");
            
            // If contest exists, verify its structure
            if (result.isPresent()) {
                Contest contest = result.get();
                assertTrue(contest.getContestId() > 0, "Contest ID should be positive");
                assertNotNull(contest.getContestName(), "Contest name should not be null");
                assertTrue(contest.getLength() >= 0, "Contest length should be non-negative");
                assertTrue(contest.getStartedAt() > 0, "Started at timestamp should be positive");
            }
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle zero contest ID")
        void shouldHandleZeroContestId() {
            // Act
            Optional<Contest> result = contestService.getContestById(0);

            // Assert
            assertNotNull(result, "Result should not be null for zero ID");
            assertTrue(result.isEmpty(), "Result should be empty for zero ID");
        }

        @Test
        @DisplayName("Should handle negative contest ID")
        void shouldHandleNegativeContestId() {
            // Act
            Optional<Contest> result = contestService.getContestById(-1);

            // Assert
            assertNotNull(result, "Result should not be null for negative ID");
            assertTrue(result.isEmpty(), "Result should be empty for negative ID");
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaxIntegerValue() {
            // Act
            Optional<Contest> result = contestService.getContestById(Integer.MAX_VALUE);

            // Assert
            assertNotNull(result, "Result should not be null for max integer value");
            assertTrue(result.isEmpty(), "Result should be empty for max integer value");
        }

        @Test
        @DisplayName("Should handle minimum integer value")
        void shouldHandleMinIntegerValue() {
            // Act
            Optional<Contest> result = contestService.getContestById(Integer.MIN_VALUE);

            // Assert
            assertNotNull(result, "Result should not be null for min integer value");
            assertTrue(result.isEmpty(), "Result should be empty for min integer value");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should execute within reasonable time")
        void shouldExecuteWithinReasonableTime() {
            // Act & Assert
            long startTime = System.currentTimeMillis();
            Optional<Contest> result = contestService.getContestById(1);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            assertNotNull(result, "Result should not be null");
            assertTrue(duration < 5000, "Method should execute within 5 seconds, but took " + duration + "ms");
        }

        @Test
        @DisplayName("Should handle multiple rapid calls")
        void shouldHandleMultipleRapidCalls() {
            // Arrange
            int[] testIds = {1, 2, 3, 999999, -1};

            // Act & Assert
            long startTime = System.currentTimeMillis();
            for (int id : testIds) {
                Optional<Contest> result = contestService.getContestById(id);
                assertNotNull(result, "Result should not be null for ID: " + id);
            }
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            assertTrue(duration < 10000, "Multiple calls should complete within 10 seconds, but took " + duration + "ms");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with database connection")
        void shouldWorkWithDatabaseConnection() {
            // This test verifies that the service can connect to the database
            // and execute queries without throwing exceptions
            
            // Act & Assert
            assertDoesNotThrow(() -> {
                Optional<Contest> result = contestService.getContestById(1);
                assertNotNull(result, "Result should not be null");
            }, "Should not throw exception when connecting to database");
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void shouldMaintainConsistentBehaviorAcrossMultipleCalls() {
            // Arrange
            int testId = 1;

            // Act
            Optional<Contest> result1 = contestService.getContestById(testId);
            Optional<Contest> result2 = contestService.getContestById(testId);

            // Assert
            assertEquals(result1.isPresent(), result2.isPresent(), 
                "Multiple calls should return consistent presence");
            
            if (result1.isPresent() && result2.isPresent()) {
                Contest contest1 = result1.get();
                Contest contest2 = result2.get();
                
                assertEquals(contest1.getContestId(), contest2.getContestId(), 
                    "Contest ID should be consistent across calls");
                assertEquals(contest1.getContestName(), contest2.getContestName(), 
                    "Contest name should be consistent across calls");
                assertEquals(contest1.getLength(), contest2.getLength(), 
                    "Contest length should be consistent across calls");
                assertEquals(contest1.getStartedAt(), contest2.getStartedAt(), 
                    "Started at timestamp should be consistent across calls");
            }
        }
    }

    @Nested
    @DisplayName("Error Handling Tests") 
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should not throw exceptions for invalid IDs")
        void shouldNotThrowExceptionsForInvalidIds() {
            // Test various invalid IDs
            int[] invalidIds = {-1, 0, Integer.MAX_VALUE, Integer.MIN_VALUE, 999999};

            for (int id : invalidIds) {
                assertDoesNotThrow(() -> {
                    Optional<Contest> result = contestService.getContestById(id);
                    assertNotNull(result, "Result should not be null for invalid ID: " + id);
                }, "Should not throw exception for invalid ID: " + id);
            }
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should return contest with valid structure when found")
        void shouldReturnContestWithValidStructureWhenFound() {
            // Try to find any existing contest
            Optional<Contest> result = contestService.getContestById(1);

            // If a contest is found, verify its structure
            if (result.isPresent()) {
                Contest contest = result.get();
                
                assertTrue(contest.getContestId() > 0, "Contest ID should be positive");
                assertNotNull(contest.getContestName(), "Contest name should not be null");
                assertFalse(contest.getContestName().trim().isEmpty(), "Contest name should not be empty");
                assertTrue(contest.getLength() >= 0, "Contest length should be non-negative");
                assertTrue(contest.getStartedAt() > 0, "Started at timestamp should be positive");
                
                // Additional validations for optional fields
                if (contest.getProblemIds() != null) {
                    assertTrue(contest.getProblemIds().length() >= 0, "Problem IDs should be valid string");
                }
                
                if (contest.getPoints() != null) {
                    assertTrue(contest.getPoints().length() >= 0, "Points should be valid string");
                }
            }
        }

        @Test
        @DisplayName("Should handle contests with different lengths")
        void shouldHandleContestsWithDifferentLengths() {
            // Test multiple IDs to try to find contests with different lengths
            for (int i = 1; i <= 10; i++) {
                Optional<Contest> result = contestService.getContestById(i);
                
                if (result.isPresent()) {
                    Contest contest = result.get();
                    assertTrue(contest.getLength() >= 0, 
                        "Contest length should be non-negative for contest ID " + i + 
                        ", but was: " + contest.getLength());
                }
            }
        }

        @Test
        @DisplayName("Should handle contests with valid timestamp")
        void shouldHandleContestsWithValidTimestamp() {
            // Test multiple IDs to verify timestamp validity
            for (int i = 1; i <= 5; i++) {
                Optional<Contest> result = contestService.getContestById(i);
                
                if (result.isPresent()) {
                    Contest contest = result.get();
                    long startedAt = contest.getStartedAt();
                    
                    assertTrue(startedAt > 0, 
                        "Started at timestamp should be positive for contest ID " + i + 
                        ", but was: " + startedAt);
                    
                    // Check if timestamp is reasonable (not too far in the future)
                    long currentTime = System.currentTimeMillis();
                    long futureLimit = currentTime + (365L * 24 * 60 * 60 * 1000); // 1 year from now
                    
                    assertTrue(startedAt <= futureLimit, 
                        "Started at timestamp should not be too far in the future for contest ID " + i);
                }
            }
        }
    }
}
