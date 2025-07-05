package org.ojx.model;

class JudgeStatus {
    public static final String ACCEPTED = "AC";
    public static final String WRONG_ANSWER = "WA";
    public static final String TIME_LIMIT_EXCEEDED = "TLE";
    public static final String MEMORY_LIMIT_EXCEEDED = "MLE";
    public static final String RUNTIME_ERROR = "RE";
    public static final String COMPILATION_ERROR = "CE";
}
public class Submission {
    private int submissionId;
    private String language;
    private int userId;
    private String sourceCode;
    private String judgeStatus;
    private long createdAt;
    
    public Submission(int submissionId, String language, int userId, String sourceCode, String judgeStatus, long createdAt) {
        this.submissionId = submissionId;
        this.language = language;
        this.userId = userId;
        this.sourceCode = sourceCode;
        this.judgeStatus = judgeStatus;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getSubmissionId() {
        return submissionId;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public String getSourceCode() {
        return sourceCode;
    }
    
    public String getJudgeStatus() {
        return judgeStatus;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
    
    // Setters
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    
    public void setJudgeStatus(String judgeStatus) {
        this.judgeStatus = judgeStatus;
    }
    
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
