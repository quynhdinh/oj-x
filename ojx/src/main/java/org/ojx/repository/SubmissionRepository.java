package org.ojx.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.ojx.connection.ConnectionManager;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.model.Submission;

public class SubmissionRepository {

    private static final String TABLE_NAME = "submission";
    private static final Logger log = Logger.getLogger(SubmissionRepository.class.getName());

    public boolean createSubmission(int userId, int problemId, String language, String sourceCode) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (user_id, problem_id, language, source_code) VALUES (?, ?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, problemId);
                preparedStatement.setString(3, language);
                preparedStatement.setString(4, sourceCode);
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            log.warning("Error creating submission: " + e.getMessage());
            return false;
        }
    }

    public List<SubmissionResDTO> getSubmissionsByUserId(int userId) {
        try(Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE user_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<SubmissionResDTO> submissions = new ArrayList<>();
                    while (resultSet.next()) {
                        int submissionId = resultSet.getInt("submission_id");
                        String language = resultSet.getString("language");
                        String judgeStatus = resultSet.getString("judge_status");
                        long createdAt = resultSet.getTimestamp("created_at").getTime();
                        submissions.add(new SubmissionResDTO(submissionId, language, userId, judgeStatus, createdAt));
                    }
                    return submissions;
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching submissions by user ID: " + e.getMessage());
        }
        return List.of();
    }

    public List<SubmissionResDTO> getSubmissionsByProblemId(int problemId) {
        try(Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE problem_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, problemId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<SubmissionResDTO> submissions = new ArrayList<>();
                    while (resultSet.next()) {
                        int submissionId = resultSet.getInt("submission_id");
                        String language = resultSet.getString("language");
                        int userId = resultSet.getInt("user_id");
                        String judgeStatus = resultSet.getString("judge_status");
                        long createdAt = resultSet.getTimestamp("created_at").getTime();
                        submissions.add(new SubmissionResDTO(submissionId, language, userId, judgeStatus, createdAt));
                    }
                    return submissions;
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching submissions by problem ID: " + e.getMessage());
        }
        return List.of();
    }

    public Optional<Submission> getSubmissionById(int submissionId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT * FROM " + TABLE_NAME + " WHERE submission_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, submissionId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("submission_id");
                        String language = resultSet.getString("language");
                        int userId = resultSet.getInt("user_id");
                        String sourceCode = resultSet.getString("source_code");
                        String judgeStatus = resultSet.getString("judge_status");
                        long createdAt = resultSet.getLong("created_at");
                        return Optional.of(new Submission(id, language, userId, sourceCode, judgeStatus, createdAt));
                    }
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching submission by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

}
