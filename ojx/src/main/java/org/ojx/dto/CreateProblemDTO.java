package org.ojx.dto;

import java.util.List;

import org.ojx.model.TestCase;

public record CreateProblemDTO(String problemName, 
                            String problemStatement,
                            String difficulty,
                            String tags,
                            List<TestCase> testCases) {
}