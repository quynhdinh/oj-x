package org.ojx.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.ojx.dto.ContestDTO;
import org.ojx.model.Contest;
import org.ojx.repository.ContestRepository;
import org.ojx.service.ContestService;

public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    public ContestServiceImpl(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }
    @Override
    public int create(ContestDTO contest) {
        return contestRepository.create(contest);
    }
    @Override
    public List<Contest> getAll() throws SQLException {
        return contestRepository.getAll();
    }
    @Override
    public Optional<Contest> getContestById(int contestId) {
        return contestRepository.getContestById(contestId);
    }
    
}
