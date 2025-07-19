package org.ojx.service.impl;

import org.ojx.model.Submission;
import org.ojx.service.JudgeService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Logger;

public class JudgeServiceImpl implements JudgeService {
    
    private static final Logger logger = Logger.getLogger(JudgeServiceImpl.class.getName());
    private final HttpClient httpClient;
    private final String judgeServerUrl;
    
    public JudgeServiceImpl() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        this.judgeServerUrl = getJudgeServerUrl();
    }
    
    private String getJudgeServerUrl() {
        String url = System.getenv("JUDGE_SERVER_URL");
        if (url != null && !url.trim().isEmpty()) {
            return url.trim();
        }
        // fallback to localhost if not set
        return "http://localhost:8080";
    }

    @Override
    public boolean submitSolution(Submission submission) {
        try {
            // Create JSON request body
            String jsonBody = createSubmissionJson(submission);

            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(judgeServerUrl + "/submissions"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            logger.info("Submitting solution to judge server: " + judgeServerUrl);
            logger.info("Request body: " + jsonBody);
            
            // Send HTTP request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // Log response details
            logger.info("Judge server response status: " + response.statusCode());
            logger.info("Judge server response body: " + response.body());
            
            // Check if submission was successful (HTTP 200 or 201)
            boolean success = response.statusCode() >= 200 && response.statusCode() < 300;
            
            if (success) {
                logger.info("Solution submitted successfully for user: " + submission.getUserId() + ", problem: " + submission.getProblemId());
            } else {
                logger.warning("Failed to submit solution. Status: " + response.statusCode() + ", Response: " + response.body());
            }
            
            return success;
            
        } catch (URISyntaxException e) {
            logger.severe("Invalid judge server URL: " + judgeServerUrl + ". Error: " + e.getMessage());
            return false;
        } catch (IOException e) {
            logger.severe("Network error while submitting to judge server: " + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            logger.severe("Request interrupted while submitting to judge server: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupted status
            return false;
        } catch (Exception e) {
            logger.severe("Unexpected error while submitting to judge server: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Creates JSON request body for submission
     */
    private String createSubmissionJson(Submission submission) {
        // Escape special characters in source code for JSON
        String escapedSourceCode = escapeJsonString(submission.getSourceCode());
    
        // May use jackson or gson here
        return String.format(
            "{\n" +
            "  \"submission_id\": %d,\n" +
            "  \"user_id\": %d,\n" +
            "  \"problem_id\": %d,\n" +
            "  \"language\": \"%s\",\n" +
            "  \"source_code\": \"%s\",\n" +
            "  \"created_at\": %d\n" +
            "}",
            submission.getSubmissionId(),
            submission.getUserId(),
            submission.getProblemId(),
            escapeJsonString(submission.getLanguage()),
            escapedSourceCode,
            System.currentTimeMillis()
        );
    }
    
    /**
     * Escapes special characters for JSON string
     */
    private String escapeJsonString(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("\\", "\\\\")  // Escape backslash
                .replace("\"", "\\\"")  // Escape quotes
                .replace("\b", "\\b")   // Escape backspace
                .replace("\f", "\\f")   // Escape form feed
                .replace("\n", "\\n")   // Escape newline
                .replace("\r", "\\r")   // Escape carriage return
                .replace("\t", "\\t");  // Escape tab
    }
}
