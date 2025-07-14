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

    public ProblemServiceImpl(ProblemRepository problemRepository, TestCaseRepository testCaseRepository) {
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
        if (problem_id > 0) {
            testCaseRepository.createTestCaseInBatch(problem_id, problem.testCases());
            return problem_id;
        }
        return -1;
    }

    @Override
    public List<ProblemResDTO> getProblemsByName(String name) {
        return problemRepository.getProblemsByName(name);
    }

    @Override
    public List<ProblemResDTO> getProblemsByTags(String tags) {
        return problemRepository.getProblemsByTags(tags);
    }

    @Override
    public int setVisible(String problems, boolean isVisible) {
        return problemRepository.setVisible(problems, isVisible);
    }

    @Override
    public void delete(Integer problemIdObj) {
        testCaseRepository.deleteTestCasesByProblemId(problemIdObj);
        problemRepository.delete(problemIdObj);
    }
}
