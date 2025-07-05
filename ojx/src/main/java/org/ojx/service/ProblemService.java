package org.ojx.service;

import java.util.List;
import java.util.Optional;

import org.ojx.dto.CreateProblemDTO;
import org.ojx.dto.ProblemResDTO;
import org.ojx.model.Problem;

public interface ProblemService {
    Optional<Problem> getProblemById(int problemId);
    List<ProblemResDTO> getAllProblems();
    List<ProblemResDTO> getProblemsByDifficulty(String difficulty);
    int create(CreateProblemDTO problem);
}