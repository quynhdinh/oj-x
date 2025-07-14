package org.ojx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ojx.connection.ConnectionManager;
import org.ojx.model.TestCase;

public class TestCaseRepository {
    private static final String TABLE_NAME = "test_case";

    public boolean createTestCase(int problemId, String input, String output, int is_sample) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME + " (problem_id, input, output, is_sample) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, problemId);
            ps.setString(2, input);
            ps.setString(3, output);
            ps.setInt(4, is_sample);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createTestCaseInBatch(int problemId, List<TestCase> testCases) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME + " (problem_id, input, output, is_sample) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            // Using Stream API to add batch parameters
            testCases.stream().forEach(testCase -> {
                try {
                    ps.setInt(1, problemId);
                    ps.setString(2, testCase.getInput());
                    ps.setString(3, testCase.getOutput());
                    ps.setInt(4, testCase.is_sample());
                    ps.addBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            int[] rowsAffected = ps.executeBatch();
            return rowsAffected.length == testCases.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<TestCase> getTestCasesByProblemId(int problemId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE problem_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, problemId);
            ResultSet rs = ps.executeQuery();
            List<TestCase> testCases = new ArrayList<>();
            while (rs.next()) {
                TestCase testCase = TestCase.builder()
                        .testCaseId(rs.getInt("id"))
                        .problemId(rs.getInt("problem_id"))
                        .input(rs.getString("input"))
                        .output(rs.getString("output"))
                        .is_sample(rs.getInt("is_sample"))
                        .build();
                testCases.add(testCase);
            }
            return testCases;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public List<TestCase> getSampleTestCasesByProblemId(int problemId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE problem_id = ? AND is_sample = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, problemId);
            ResultSet rs = ps.executeQuery();
            List<TestCase> testCases = new ArrayList<>();
            while (rs.next()) {
                TestCase testCase = TestCase.builder()
                        .testCaseId(rs.getInt("id"))
                        .problemId(rs.getInt("problem_id"))
                        .input(rs.getString("input"))
                        .output(rs.getString("output"))
                        .is_sample(rs.getInt("is_sample"))
                        .build();
                testCases.add(testCase);
            }
            return testCases;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public void deleteTestCasesByProblemId(Integer problemIdObj) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE problem_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, problemIdObj);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}