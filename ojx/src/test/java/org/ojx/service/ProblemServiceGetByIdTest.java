package org.ojx.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import org.ojx.model.Problem;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.service.impl.ProblemServiceImpl;

import java.util.Optional;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProblemService getProblemById Tests")
class ProblemServiceGetByIdTest {

    private ProblemService problemService;
    private ProblemRepository problemRepository;
    private TestCaseRepository testCaseRepository;

    @BeforeEach
    void setUp() {
        problemRepository = new ProblemRepository();
        testCaseRepository = new TestCaseRepository();
        problemService = new ProblemServiceImpl(problemRepository, testCaseRepository);
    }

    @Nested
    @DisplayName("Return Value Tests")
    class ReturnValueTests {

        @Test
        @DisplayName("Should return Optional.empty() for non-existent problem ID")
        void shouldReturnEmptyForNonExistentProblemId() {
            // Arrange
            int nonExistentId = 999999; // Use a very large ID that likely doesn't exist

            // Act
            Optional<Problem> result = problemService.getProblemById(nonExistentId);

            // Assert
            assertNotNull(result, "Result should not be null");
            assertTrue(result.isEmpty(), "Result should be empty for non-existent problem ID");
        }

        @Test
        @DisplayName("Should return Optional for any valid problem ID (integration test)")
        void shouldReturnOptionalForValidId() {
            // This is an integration test that works with actual database
            // We can't guarantee specific problems exist, but we can test the method structure
            
            // Act
            Optional<Problem> result = problemService.getProblemById(1);

            // Assert
            assertNotNull(result, "Result should not be null");
            
            // If problem exists, verify its structure
            if (result.isPresent()) {
                Problem problem = result.get();
                assertTrue(problem.getProblemId() > 0, "Problem ID should be positive");
                assertNotNull(problem.getProblemName(), "Problem name should not be null");
                assertNotNull(problem.getProblemStatement(), "Problem statement should not be null");
                assertNotNull(problem.getDifficulty(), "Difficulty should not be null");
                assertNotNull(problem.getTags(), "Tags should not be null");
            }
        }
    }

    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should handle zero problem ID")
        void shouldHandleZeroProblemId() {
            // Act
            Optional<Problem> result = problemService.getProblemById(0);

            // Assert
            assertNotNull(result, "Result should not be null for zero ID");
            assertTrue(result.isEmpty(), "Result should be empty for zero ID");
        }

        @Test
        @DisplayName("Should handle negative problem ID")
        void shouldHandleNegativeProblemId() {
            // Act
            Optional<Problem> result = problemService.getProblemById(-1);

            // Assert
            assertNotNull(result, "Result should not be null for negative ID");
            assertTrue(result.isEmpty(), "Result should be empty for negative ID");
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaxIntegerValue() {
            // Act
            Optional<Problem> result = problemService.getProblemById(Integer.MAX_VALUE);

            // Assert
            assertNotNull(result, "Result should not be null for max integer value");
            assertTrue(result.isEmpty(), "Result should be empty for max integer value");
        }

        @Test
        @DisplayName("Should handle minimum integer value")
        void shouldHandleMinIntegerValue() {
            // Act
            Optional<Problem> result = problemService.getProblemById(Integer.MIN_VALUE);

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
            Optional<Problem> result = problemService.getProblemById(1);
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
                Optional<Problem> result = problemService.getProblemById(id);
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
                Optional<Problem> result = problemService.getProblemById(1);
                assertNotNull(result, "Result should not be null");
            }, "Should not throw exception when connecting to database");
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void shouldMaintainConsistentBehaviorAcrossMultipleCalls() {
            // Arrange
            int testId = 1;

            // Act
            Optional<Problem> result1 = problemService.getProblemById(testId);
            Optional<Problem> result2 = problemService.getProblemById(testId);

            // Assert
            assertEquals(result1.isPresent(), result2.isPresent(), 
                "Multiple calls should return consistent presence");
            
            if (result1.isPresent() && result2.isPresent()) {
                Problem problem1 = result1.get();
                Problem problem2 = result2.get();
                
                assertEquals(problem1.getProblemId(), problem2.getProblemId(), 
                    "Problem ID should be consistent across calls");
                assertEquals(problem1.getProblemName(), problem2.getProblemName(), 
                    "Problem name should be consistent across calls");
                assertEquals(problem1.getDifficulty(), problem2.getDifficulty(), 
                    "Difficulty should be consistent across calls");
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
                    Optional<Problem> result = problemService.getProblemById(id);
                    assertNotNull(result, "Result should not be null for invalid ID: " + id);
                }, "Should not throw exception for invalid ID: " + id);
            }
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should return problem with valid structure when found")
        void shouldReturnProblemWithValidStructureWhenFound() {
            // Try to find any existing problem
            Optional<Problem> result = problemService.getProblemById(1);

            // If a problem is found, verify its structure
            if (result.isPresent()) {
                Problem problem = result.get();
                
                assertTrue(problem.getProblemId() > 0, "Problem ID should be positive");
                assertNotNull(problem.getProblemName(), "Problem name should not be null");
                assertFalse(problem.getProblemName().trim().isEmpty(), "Problem name should not be empty");
                assertNotNull(problem.getProblemStatement(), "Problem statement should not be null");
                assertNotNull(problem.getDifficulty(), "Difficulty should not be null");
                assertNotNull(problem.getTags(), "Tags should not be null (but can be empty)");
                
                // Verify difficulty is one of expected values
                String difficulty = problem.getDifficulty();
                assertTrue(
                    "Easy".equals(difficulty) || "Medium".equals(difficulty) || "Hard".equals(difficulty),
                    "Difficulty should be Easy, Medium, or Hard, but was: " + difficulty
                );
            }
        }

        @Test
        @DisplayName("Should handle problems with different difficulties")
        void shouldHandleProblemsWithDifferentDifficulties() {
            // Test multiple IDs to try to find problems with different difficulties
            for (int i = 1; i <= 10; i++) {
                Optional<Problem> result = problemService.getProblemById(i);
                
                if (result.isPresent()) {
                    Problem problem = result.get();
                    String difficulty = problem.getDifficulty();
                    
                    assertTrue(
                        Arrays.asList("Easy", "Medium", "Hard").contains(difficulty),
                        "Difficulty should be valid for problem ID " + i + ", but was: " + difficulty
                    );
                }
            }
        }
    }
}
