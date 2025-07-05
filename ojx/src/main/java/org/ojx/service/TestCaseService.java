package org.ojx.service;

import java.util.List;

import org.ojx.model.TestCase;

public interface TestCaseService {

    boolean createTestCase(int problemId, String input, String output, int isSample);

    boolean createTestCaseInBatch(int problemId, List<TestCase> testCases);

    List<TestCase> getTestCasesByProblemId(int problemId);

    List<TestCase> getSampleTestCasesByProblemId(int problemId);
}
