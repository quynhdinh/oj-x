package org.ojx.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.ojx.connection.ConnectionManager;
import org.ojx.dto.SubmissionDetailDTO;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.model.Submission;
import org.ojx.utils.Time;

public class SubmissionRepository {

    private static final String TABLE_NAME = "submission";
    private static final Logger log = Logger.getLogger(SubmissionRepository.class.getName());

    public boolean createSubmission(Submission submission) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "INSERT INTO " + TABLE_NAME
                    + " (user_id, problem_id, language, source_code, judge_status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, submission.getUserId());
                preparedStatement.setInt(2, submission.getProblemId());
                preparedStatement.setString(3, submission.getLanguage());
                preparedStatement.setString(4, submission.getSourceCode());
                preparedStatement.setString(5, "Queued"); // TODO: Remove this
                preparedStatement.setLong(6, Time.now());
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
                        submissions.add(new SubmissionResDTO(submissionId, submissionId, sql, language, userId, judgeStatus, createdAt));
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
                        submissions.add(new SubmissionResDTO(submissionId, problemId, sql, language, userId, judgeStatus, createdAt));
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
                        Submission submission = Submission.builder()
                                .submissionId(id)
                                .userId(userId)
                                .language(language)
                                .sourceCode(sourceCode)
                                .judgeStatus(judgeStatus)
                                .createdAt(createdAt)
                                .build();
                        return Optional.of(submission);
                    }
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching submission by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    // join problem table to get problem name
    // and order by created_at DESC with pagination
    public List<SubmissionResDTO> getAllSubmissions(int page, int pageSize) {
        try(Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT s.*, p.problem_name FROM " + TABLE_NAME + " s JOIN problem p ON s.problem_id = p.problem_id ORDER BY s.created_at DESC LIMIT ? OFFSET ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, pageSize);
                preparedStatement.setInt(2, (page - 1) * pageSize);
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<SubmissionResDTO> submissions = new ArrayList<>();
                    while (resultSet.next()) {
                        int submissionId = resultSet.getInt("submission_id");
                        int problemId = resultSet.getInt("problem_id");
                        String problemName = resultSet.getString("problem_name");
                        String language = resultSet.getString("language");
                        int userId = resultSet.getInt("user_id");
                        String judgeStatus = resultSet.getString("judge_status");
                        long createdAt = resultSet.getLong("created_at");
                        submissions.add(new SubmissionResDTO(submissionId, problemId, problemName, language, userId, judgeStatus, createdAt));
                    }
                    return submissions;
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching all submissions: " + e.getMessage());
        }
        return List.of();
    }

    public int count() {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
            try (var preparedStatement = conn.prepareStatement(sql);
                 var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (Exception e) {
            log.warning("Error counting submissions: " + e.getMessage());
        }
        return 0;
    }

    public Optional<SubmissionDetailDTO> getSubmissionDetail(int submissionId) {
        try (Connection conn = ConnectionManager.getConnection()) {
            String sql = "SELECT s.*, u.user_name, p.problem_name FROM " + TABLE_NAME + " s JOIN user u ON s.user_id = u.user_id JOIN problem p ON s.problem_id = p.problem_id WHERE s.submission_id = ?";
            try (var preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setInt(1, submissionId);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int submission_id = resultSet.getInt("submission_id");
                        String language = resultSet.getString("language");
                        int problemId = resultSet.getInt("problem_id");
                        int userId = resultSet.getInt("user_id");
                        String userName = resultSet.getString("user_name");
                        String sourceCode = resultSet.getString("source_code");
                        String judgeStatus = resultSet.getString("judge_status");
                        String problem_name = resultSet.getString("problem_name");
                        long createdAt = resultSet.getLong("created_at");
                        return Optional.of(new SubmissionDetailDTO(
                            submission_id, problemId, problem_name, language, userId, userName, sourceCode, judgeStatus, createdAt));
                    }
                }
            }
        } catch (Exception e) {
            log.warning("Error fetching submission detail: " + e.getMessage());
        }
        return Optional.empty();
    }
}