package org.ojx.service;

import java.util.List;
import java.util.Optional;

import org.ojx.model.Contest;

public interface ContestService {
    int create(int duration, long startedAt, String problemIds);
    List<Contest> getAll();
    Optional<Contest> getContestById(int contestId);
}