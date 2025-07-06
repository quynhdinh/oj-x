package org.ojx.model;

public class Contest {
    private int contestId;
    private String contestName;
    private int length;
    private long startedAt;
    private String problemIds;
    private String points;

    public Contest(int contestId, String contestName, int length, long startedAt, String problemIds, String points) {
        this.contestId = contestId;
        this.contestName = contestName;
        this.length = length;
        this.startedAt = startedAt;
        this.problemIds = problemIds;
        this.points = points;
    }

    public int getContestId() {
        return contestId;
    }

    public String getContestName() {
        return contestName;
    }

    public long getStartedAt() {
        return startedAt;
    }

    public int getLength() {
        return length;
    }

    public String getProblemIds() {
        return problemIds;
    }

    public String getPoints() {
        return points;
    }
    @Override
    public String toString() {
        return "Contest{" +
                "contestId=" + contestId +
                ", contestName='" + contestName + '\'' +
                ", length=" + length +
                ", startedAt=" + startedAt +
                ", problemIds='" + problemIds + '\'' +
                ", points='" + points + '\'' +
                '}';
    }
}
