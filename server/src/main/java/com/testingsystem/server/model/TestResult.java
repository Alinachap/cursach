package com.testingsystem.server.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestResult extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long testId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal scorePercent;
    private Boolean isPassed;
    private Integer attemptsUsed;
    private String answersData;

    public TestResult() {
    }

    public TestResult(Long id, Long userId, Long testId, LocalDateTime startTime, LocalDateTime endTime,
                      BigDecimal scorePercent, Boolean isPassed, Integer attemptsUsed, String answersData) {
        super(id);
        this.userId = userId;
        this.testId = testId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scorePercent = scorePercent;
        this.isPassed = isPassed;
        this.attemptsUsed = attemptsUsed;
        this.answersData = answersData;
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

    public String getAnswersData() {
        return answersData;
    }

    public void setAnswersData(String answersData) {
        this.answersData = answersData;
    }

    public Long getDurationMinutes() {
        if (startTime == null || endTime == null) {
            return null;
        }
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String getEntityName() {
        return "TestResult";
    }

    @Override
    public String toString() {
        return "TestResult{" +
               "id=" + getId() +
               ", userId=" + userId +
               ", testId=" + testId +
               ", scorePercent=" + scorePercent +
               ", isPassed=" + isPassed +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               '}';
    }
}
