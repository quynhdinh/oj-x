package org.ojx.dto;

public class SubmissionResDTO {
    private int submissionId;
    private String language;
    private int userName;
    private String judgeStatus;
    private long createdAt;
    public SubmissionResDTO(int submissionId, String language, int userName, String judgeStatus, long createdAt) {
        this.submissionId = submissionId;
        this.language = language;
        this.userName = userName;
        this.judgeStatus = judgeStatus;
        this.createdAt = createdAt;
    }
}