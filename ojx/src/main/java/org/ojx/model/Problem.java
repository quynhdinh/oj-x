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

}