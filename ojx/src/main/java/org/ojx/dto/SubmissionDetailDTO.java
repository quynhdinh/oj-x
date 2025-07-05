package org.ojx.dto;

public record SubmissionDetailDTO(int submissionId,
                                   int problemId,
                                   String problem_name,
                                   String language, 
                                   int userId, 
                                   String user_name, 
                                   String sourceCode,
                                   String judgeStatus, 
                                   long createdAt) {

}
