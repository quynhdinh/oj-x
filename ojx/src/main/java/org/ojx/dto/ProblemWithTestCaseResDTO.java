package org.ojx.dto;

import java.util.ArrayList;
import java.util.List;

import org.ojx.model.Problem;
import org.ojx.model.TestCase;

public class ProblemWithTestCaseResDTO {
    Problem problem;
    List<TestCase> sampleTestCases;

    public ProblemWithTestCaseResDTO() {
        this.sampleTestCases = new ArrayList<>();
    }
    public Problem getProblem() {
        return problem;
    }

    public void addTestCase(TestCase testCase) {
        this.sampleTestCases.add(testCase);
    }
    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}