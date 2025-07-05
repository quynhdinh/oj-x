package org.ojx.service;

import java.util.List;
import java.util.Optional;

import org.ojx.dto.SubmissionDetailDTO;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.model.Submission;

public interface SubmissionService {
    boolean submitSolution(int userId, int problemId, String language, String sourceCode);
    List<SubmissionResDTO> getSubmissionsByUserId(int userId);
    List<SubmissionResDTO> getSubmissionsByProblemId(int problemId);
    int count();
    List<SubmissionResDTO> getAllSubmissions(int page, int pageSize);
    Optional<Submission> getSubmissionById(int submissionId);
    Optional<SubmissionDetailDTO> getSubmissionDetail(int submissionId);
}
