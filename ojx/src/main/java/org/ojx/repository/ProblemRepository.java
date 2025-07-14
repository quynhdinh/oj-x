package org.ojx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.ojx.connection.ConnectionManager;
import org.ojx.dto.CreateProblemDTO;
import org.ojx.dto.ProblemResDTO;
import org.ojx.dto.ProblemWithTestCaseResDTO;
import org.ojx.model.Problem;
import org.ojx.model.TestCase;

public class ProblemRepository {

    private static final String TABLE_NAME = "problem";

    private static final Logger log = Logger.getLogger(UserRepository.class.getName());

    public Optional<Problem> getProblemById(int problemId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE problem_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, problemId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String problemName = rs.getString("problem_name");
                        String problemStatement = rs.getString("problem_statement");
                        String difficulty = rs.getString("difficulty");
                        String tagsStr = rs.getString("tags");
                        List<String> tags = List.of(tagsStr.split(","));
                        return Optional.of(new Problem(problemId, problemName, problemStatement, difficulty, tags));
                    }
                }
            }
        } catch (SQLException e) {
            log.severe("Error fetching problem by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<ProblemWithTestCaseResDTO> getProblemWithTestCasesById(int problemId) {
        ProblemWithTestCaseResDTO problemWithTestCase = new ProblemWithTestCaseResDTO();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "select p.problem_id, p.problem_name, p.problem_statement, "
                    + "p.difficulty, p.tags, t.input, t.output, t.is_sample "
                    + "from " + TABLE_NAME + " p "
                    + "left join test_case t on p.problem_id = t.problem_id "
                    + "where p.problem_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, problemId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        if (problemWithTestCase.getProblem() == null) {
                            String problemName = rs.getString("problem_name");
                            String problemStatement = rs.getString("problem_statement");
                            String difficulty = rs.getString("difficulty");
                            String tagsStr = rs.getString("tags");
                            List<String> tags = List.of(tagsStr.split(","));
                            problemWithTestCase.setProblem(
                                    new Problem(problemId, problemName, problemStatement, difficulty, tags));
                        }
                        String input = rs.getString("input");
                        String output = rs.getString("output");
                        int isSample = rs.getInt("is_sample");
                        if (input != null && output != null) {
                            TestCase testCase = TestCase.builder()
                                    .input(input)
                                    .output(output)
                                    .is_sample(isSample)
                                    .build();
                            problemWithTestCase.addTestCase(testCase);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.severe("Error fetching problem with test cases by ID: " + e.getMessage());
        }
        return Optional.ofNullable(problemWithTestCase.getProblem() != null ? problemWithTestCase : null);
    }

    public List<ProblemResDTO> getAllProblems() {
        List<ProblemResDTO> problems = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT problem_id, problem_name, tags, difficulty FROM " + TABLE_NAME;
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                    ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int problemId = rs.getInt("problem_id");
                    String problemName = rs.getString("problem_name");
                    String tags = rs.getString("tags");
                    String difficulty = rs.getString("difficulty");
                    problems.add(new ProblemResDTO(problemId, problemName, tags, difficulty));
                }
                return problems;
            }
        } catch (SQLException e) {
            log.severe("Error fetching all problems: " + e.getMessage());
        }
        return problems;
    }

    public List<ProblemResDTO> getProblemsByDifficulty(String difficulty) {
        List<ProblemResDTO> problems = new ArrayList<>();
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT problem_id, problem_name, tags FROM " + TABLE_NAME + " WHERE difficulty = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, difficulty);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int problemId = rs.getInt("problem_id");
                        String problemName = rs.getString("problem_name");
                        String tags = rs.getString("tags");
                        problems.add(new ProblemResDTO(problemId, problemName, tags, difficulty));
                    }
                    return problems;
                }
            }
        } catch (SQLException e) {
            log.severe("Error fetching problems by difficulty: " + e.getMessage());
        }
        return problems;
    }

    public int create(CreateProblemDTO problem) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (problem_name, problem_statement, difficulty, tags, isVisible) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, problem.problemName());
                pstmt.setString(2, problem.problemStatement());
                pstmt.setString(3, problem.difficulty());
                pstmt.setString(4, problem.tags());
                pstmt.setBoolean(5, problem.isVisible());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating problem failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating problem failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<ProblemResDTO> getProblemsByName(String name) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT problem_id, problem_name, tags, difficulty FROM " + TABLE_NAME
                    + " WHERE isVisible = 1 AND LOWER(problem_name) LIKE LOWER(?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + name + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    List<ProblemResDTO> problems = new ArrayList<>();
                    while (rs.next()) {
                        int problemId = rs.getInt("problem_id");
                        String problemName = rs.getString("problem_name");
                        String tags = rs.getString("tags");
                        String difficulty = rs.getString("difficulty");
                        problems.add(new ProblemResDTO(problemId, problemName, tags, difficulty));
                    }
                    return problems;
                }
            }
        } catch (SQLException e) {
            log.severe("Error fetching problems by name: " + e.getMessage());
        }
        return List.of();
    }

    public List<ProblemResDTO> getProblemsByTags(String tags) {
        String[] splits = tags.split(",");
        try (Connection conn = ConnectionManager.getConnection()) {
            StringBuilder sql = new StringBuilder("SELECT problem_id, problem_name, tags, difficulty FROM " + TABLE_NAME + " WHERE isVisible = 1");
            if (splits.length > 0) {
                // Using Stream API to generate placeholder conditions  
                String conditions = Arrays.stream(splits)
                    .map(tag -> "LOWER(tags) LIKE LOWER(?)")
                    .collect(Collectors.joining(" OR "));
                sql.append(" AND ").append(conditions);
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                // Using Stream API for parameter setting with IntStream
                IntStream.range(0, splits.length)
                    .forEach(i -> {
                        try {
                            pstmt.setString(i + 1, "%" + splits[i].trim() + "%");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                String string = pstmt.toString();
                System.out.println(string);
                try (ResultSet rs = pstmt.executeQuery()) {
                    List<ProblemResDTO> problems = new ArrayList<>();
                    while (rs.next()) {
                        int problemId = rs.getInt("problem_id");
                        String problemName = rs.getString("problem_name");
                        String tagsStr = rs.getString("tags");
                        String difficulty = rs.getString("difficulty");
                        problems.add(new ProblemResDTO(problemId, problemName, tagsStr, difficulty));
                    }
                    return problems;
                }
            }
        } catch (SQLException e) {
            log.severe("Error fetching problems by tags: " + e.getMessage());
        }
        return List.of();
    }

    public int setVisible(String problems, boolean isVisible) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "UPDATE " + TABLE_NAME + " SET isVisible = ? WHERE problem_id IN (" + problems + ")";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, isVisible ? 1 : 0);
                return pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.severe("Error setting problem visibility: " + e.getMessage());
        }
        return 0;
    }

    public void delete(Integer problemIdObj) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE problem_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, problemIdObj);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            log.severe("Error deleting problem: " + e.getMessage());
        }
    }
}
