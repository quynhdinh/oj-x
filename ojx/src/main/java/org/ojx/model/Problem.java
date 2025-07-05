package org.ojx.model;

import java.util.List;

public class Problem {
    // Problem(problem_id, problem_name, problem_statement, difficulty, List<String> Tags)
    private int problemId;
    private String problemName;
    private String problemStatement;
    private String difficulty;
    private List<String> tags;

    public Problem(int problemId, String problemName, String problemStatement, String difficulty, List<String> tags) {
        this.problemId = problemId;
        this.problemName = problemName;
        this.problemStatement = problemStatement;
        this.difficulty = difficulty;
        this.tags = tags;
    }

    // Getters
    public int getProblemId() {
        return problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public String getProblemStatement() {
        return problemStatement;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    // Setters
    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public void setProblemStatement(String problemStatement) {
        this.problemStatement = problemStatement;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}