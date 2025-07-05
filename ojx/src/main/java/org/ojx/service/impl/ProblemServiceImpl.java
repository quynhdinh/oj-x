package org.ojx.service.impl;

import java.util.List;
import java.util.Optional;

import org.ojx.dto.CreateProblemDTO;
import org.ojx.dto.ProblemResDTO;
import org.ojx.model.Problem;
import org.ojx.repository.ProblemRepository;
import org.ojx.repository.TestCaseRepository;
import org.ojx.service.ProblemService;

public class ProblemServiceImpl implements ProblemService {

    private ProblemRepository problemRepository;
    private TestCaseRepository testCaseRepository;

    ProblemServiceImpl(ProblemRepository problemRepository, TestCaseRepository testCaseRepository) {
        this.problemRepository = problemRepository;
        this.testCaseRepository = testCaseRepository;
    }

    @Override
    public Optional<Problem> getProblemById(int problemId) {
        return problemRepository.getProblemById(problemId);
    }

    @Override
    public List<ProblemResDTO> getAllProblems() {
        return problemRepository.getAllProblems();
    }

    @Override
    public List<ProblemResDTO> getProblemsByDifficulty(String difficulty) {
        return problemRepository.getProblemsByDifficulty(difficulty);
    }

    @Override
    public int create(CreateProblemDTO problem) {
        int problem_id = problemRepository.create(problem);
        for (var testCase : problem.testCases()) {
            boolean ok = testCaseRepository.createTestCase(problem_id, testCase.getInput(), testCase.getOutput(),
                    testCase.is_sample());
            if (ok) {
                return -1;
            }
        }
        return 1;
    }

}
