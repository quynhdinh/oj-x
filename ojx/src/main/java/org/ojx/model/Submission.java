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
    private int problemId;
    
    // Private constructor to be used by Builder
    private Submission(Builder builder) {
        this.submissionId = builder.submissionId;
        this.language = builder.language;
        this.userId = builder.userId;
        this.sourceCode = builder.sourceCode;
        this.judgeStatus = builder.judgeStatus;
        this.createdAt = builder.createdAt;
        this.problemId = builder.problemId;
    }
    
    // Keep existing constructor for backward compatibility
    public Submission(int submissionId, String language, int userId, int problemId, String sourceCode, String judgeStatus, long createdAt) {
        this.submissionId = submissionId;
        this.language = language;
        this.userId = userId;
        this.problemId = problemId;
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

    public int getProblemId() {
        return problemId;
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

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public static Builder builder() {
        return new Builder();
    }
    
    // Builder class
    public static class Builder {
        private int problemId;
        private int submissionId;
        private String language;
        private int userId;
        private String sourceCode;
        private String judgeStatus = "PENDING"; // Default status
        private long createdAt = System.currentTimeMillis(); // Default to current time
        
        public Builder submissionId(int submissionId) {
            this.submissionId = submissionId;
            return this;
        }

        public Builder problemId(int problemId) {
            this.problemId = problemId;
            return this;
        }
    
        public Builder language(String language) {
            this.language = language;
            return this;
        }
        
        public Builder userId(int userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder sourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
            return this;
        }
        
        public Builder judgeStatus(String judgeStatus) {
            this.judgeStatus = judgeStatus;
            return this;
        }
        
        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public Submission build() {
            if (language == null || language.trim().isEmpty()) {
                throw new IllegalArgumentException("Language is required");
            }
            if (sourceCode == null || sourceCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Source code is required");
            }
            if (userId <= 0) {
                throw new IllegalArgumentException("Valid user ID is required");
            }
            if (problemId <= 0) {
                throw new IllegalArgumentException("Valid problem ID is required");
            }

            return new Submission(this);
        }
    }
}
