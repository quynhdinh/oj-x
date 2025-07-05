package org.ojx.service.impl;

import java.util.List;

import org.ojx.model.TestCase;
import org.ojx.repository.TestCaseRepository;
import org.ojx.service.TestCaseService;

public class TestCaseServiceImpl implements TestCaseService {
    
    private final TestCaseRepository testCaseRepository;

    public TestCaseServiceImpl(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    @Override
    public boolean createTestCase(int problemId, String input, String output, int isSample) {
        return testCaseRepository.createTestCase(problemId, input, output, isSample);
    }

    @Override
    public boolean createTestCaseInBatch(int problemId, List<TestCase> testCases) {
        return testCaseRepository.createTestCaseInBatch(problemId, testCases);
    }

    @Override
    public List<TestCase> getTestCasesByProblemId(int problemId) {
        return testCaseRepository.getTestCasesByProblemId(problemId);
    }

    @Override
    public List<TestCase> getSampleTestCasesByProblemId(int problemId) {
        return testCaseRepository.getSampleTestCasesByProblemId(problemId);
    }

}
