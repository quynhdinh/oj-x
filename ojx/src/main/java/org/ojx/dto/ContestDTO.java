package org.ojx.dto;

public record ContestDTO(
    String contestName,
    int length,
    long startedAt,
    String problemIds,
    String points
) {
}