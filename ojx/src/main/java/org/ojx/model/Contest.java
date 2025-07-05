package org.ojx.model;

import java.util.List;

public class Contest {
    private int contestId;
    private int duration;
    private int length;
    private List<Integer> problemIds;
    public Contest(int contestId, int duration, int length, List<Integer> problemIds) {
        this.contestId = contestId;
        this.duration = duration;
        this.length = length;
        this.problemIds = problemIds;
    }
}
