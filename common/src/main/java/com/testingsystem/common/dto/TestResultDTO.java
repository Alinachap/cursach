package com.testingsystem.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for TestResult entity.
 * Used for transferring test result data between client and server.
 */
public class TestResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long testId;
    private String userName;
    private String testName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal scorePercent;
    private Boolean isPassed;
    private Integer attemptsUsed;

    /**
     * Default constructor for serialization.
     */
    public TestResultDTO() {
    }

    /**
     * Constructs a TestResultDTO with all fields.
     *
     * @param id the result ID
     * @param userId the user ID
     * @param testId the test ID
     * @param userName the user's name
     * @param testName the test title
     * @param startTime the test start time
     * @param endTime the test end time
     * @param scorePercent the score percentage
     * @param isPassed whether the test was passed
     * @param attemptsUsed number of attempts used
     */
    public TestResultDTO(Long id, Long userId, Long testId, String userName, String testName,
                         LocalDateTime startTime, LocalDateTime endTime,
                         BigDecimal scorePercent, Boolean isPassed, Integer attemptsUsed) {
        this.id = id;
        this.userId = userId;
        this.testId = testId;
        this.userName = userName;
        this.testName = testName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scorePercent = scorePercent;
        this.isPassed = isPassed;
        this.attemptsUsed = attemptsUsed;
    }

    /**
     * Constructs a TestResultDTO for a new test attempt.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @param startTime the start time
     */
    public TestResultDTO(Long userId, Long testId, LocalDateTime startTime) {
        this.userId = userId;
        this.testId = testId;
        this.startTime = startTime;
        this.attemptsUsed = 1;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getScorePercent() {
        return scorePercent;
    }

    public void setScorePercent(BigDecimal scorePercent) {
        this.scorePercent = scorePercent;
    }

    public Boolean getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(Boolean passed) {
        isPassed = passed;
    }

    public Integer getAttemptsUsed() {
        return attemptsUsed;
    }

    public void setAttemptsUsed(Integer attemptsUsed) {
        this.attemptsUsed = attemptsUsed;
    }

    /**
     * Gets the duration of the test in minutes.
     *
     * @return duration in minutes, or null if test hasn't ended
     */
    public Long getDurationMinutes() {
        if (startTime == null || endTime == null) {
            return null;
        }
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResultDTO that = (TestResultDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestResultDTO{" +
               "id=" + id +
               ", userId=" + userId +
               ", testId=" + testId +
               ", scorePercent=" + scorePercent +
               ", isPassed=" + isPassed +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               '}';
    }
}
