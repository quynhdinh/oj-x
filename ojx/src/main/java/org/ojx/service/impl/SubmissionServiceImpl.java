package org.ojx.service.impl;

import java.util.List;
import java.util.Optional;

import org.ojx.dto.SubmissionDetailDTO;
import org.ojx.dto.SubmissionResDTO;
import org.ojx.model.Submission;
import org.ojx.repository.SubmissionRepository;
import org.ojx.service.SubmissionService;

public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }


    @Override
    public boolean submitSolution(int userId, int problemId, String language, String sourceCode) {
        return submissionRepository.createSubmission(userId, problemId, language, sourceCode);
    }

    @Override
    public List<SubmissionResDTO> getSubmissionsByUserId(int userId) {
        return submissionRepository.getSubmissionsByUserId(userId);
    }

    @Override
    public List<SubmissionResDTO> getSubmissionsByProblemId(int problemId) {
        return submissionRepository.getSubmissionsByProblemId(problemId);
    }

    @Override
    public Optional<Submission> getSubmissionById(int submissionId) {
        return submissionRepository.getSubmissionById(submissionId);
    }

    

    @Override
    public List<SubmissionResDTO> getAllSubmissions(int page, int pageSize) {
        return submissionRepository.getAllSubmissions(page, pageSize);
    }

    @Override
    public int count() {
        return submissionRepository.count();
    }


    @Override
    public Optional<SubmissionDetailDTO> getSubmissionDetail(int submissionId) {
        return submissionRepository.getSubmissionDetail(submissionId);
    }
    
}
