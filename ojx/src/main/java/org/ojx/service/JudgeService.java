package org.ojx.service;

import org.ojx.model.Submission;

public interface JudgeService {
    boolean submitSolution(Submission submission);
}
