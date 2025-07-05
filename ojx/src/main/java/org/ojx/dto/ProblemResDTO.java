package org.ojx.dto;

import org.ojx.model.Problem;

public record ProblemResDTO(int problemId, String problemName, String tags, String difficulty) {
    public ProblemResDTO(Problem problem) {
        this(problem.getProblemId(), problem.getProblemName(), String.join(",", problem.getTags()), problem.getDifficulty());
    }
}
