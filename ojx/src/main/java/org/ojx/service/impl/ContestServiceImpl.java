package org.ojx.service.impl;

import java.util.List;
import java.util.Optional;

import org.ojx.model.Contest;
import org.ojx.repository.ContestRepository;
import org.ojx.service.ContestService;

public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    public ContestServiceImpl(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }
    @Override
    public int create(int duration, long startedAt, String problemIds) {
        return contestRepository.create(duration, startedAt, problemIds);
    }
    @Override
    public List<Contest> getAll() {
        return contestRepository.getAll();
    }
    @Override
    public Optional<Contest> getContestById(int contestId) {
        return contestRepository.getContestById(contestId);
    }
    
}
