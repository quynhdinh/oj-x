package org.ojx.dto;

public class SubmissionResDTO {
    private int submissionId;
    private int problemId;
    private String problemName;
    private String language;
    private int userName;
    private String judgeStatus;
    private long createdAt;
    
    public SubmissionResDTO(int submissionId, int problemId, String problemName, String language, int userName, String judgeStatus, long createdAt) {
        this.submissionId = submissionId;
        this.problemId = problemId;
        this.problemName = problemName;
        this.language = language;
        this.userName = userName;
        this.judgeStatus = judgeStatus;
        this.createdAt = createdAt;
    }
    
    // Getters
    public int getSubmissionId() {
        return submissionId;
    }
    
    public int getProblemId() {
        return problemId;
    }
    
    public String getProblemName() {
        return problemName;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public int getUserName() {
        return userName;
    }
    
    public String getJudgeStatus() {
        return judgeStatus;
    }
    
    public long getCreatedAt() {
        return createdAt;
    }
}