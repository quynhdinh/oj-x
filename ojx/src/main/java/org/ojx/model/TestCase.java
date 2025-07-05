package org.ojx.model;

public class TestCase {
    private int testCaseId;
    private int problemId;
    private int is_sample; // 1 is sample test case, shows in problem detail
    private String input;
    private String output;

    // Private constructor to be used by Builder
    private TestCase(Builder builder) {
        this.testCaseId = builder.testCaseId;
        this.problemId = builder.problemId;
        this.is_sample = builder.is_sample;
        this.input = builder.input;
        this.output = builder.output;
    }

    public int getTestCaseId() {
        return testCaseId;
    }

    public int getProblemId() {
        return problemId;
    }

    public int is_sample() {
        return is_sample;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public void setIs_sample(int is_sample) {
        this.is_sample = is_sample;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "TestCase{" +
                "testCaseId=" + testCaseId +
                ", problemId=" + problemId +
                ", is_sample=" + is_sample +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    // Builder class
    public static class Builder {
        private int testCaseId;
        private int problemId;
        private int is_sample = 0; // Default to 0 (not a sample)
        private String input;
        private String output;

        public Builder testCaseId(int testCaseId) {
            this.testCaseId = testCaseId;
            return this;
        }

        public Builder problemId(int problemId) {
            this.problemId = problemId;
            return this;
        }

        public Builder is_sample(int is_sample) {
            this.is_sample = is_sample;
            return this;
        }

        public Builder input(String input) {
            this.input = input;
            return this;
        }

        public Builder output(String output) {
            this.output = output;
            return this;
        }

        public TestCase build() {
            if (input == null || input.trim().isEmpty()) {
                throw new IllegalArgumentException("Input is required");
            }
            if (output == null || output.trim().isEmpty()) {
                throw new IllegalArgumentException("Output is required");
            }

            return new TestCase(this);
        }
    }
}
