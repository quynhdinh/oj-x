package org.ojx.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.ojx.dto.ContestDTO;
import org.ojx.model.Contest;

public interface ContestService {
    int create(ContestDTO contest);
    List<Contest> getAll() throws SQLException;
    Optional<Contest> getContestById(int contestId);
}