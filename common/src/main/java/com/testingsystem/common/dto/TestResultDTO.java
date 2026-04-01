package com.testingsystem.common.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public TestResultDTO() {
    }

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

    public TestResultDTO(Long userId, Long testId, LocalDateTime startTime) {
        this.userId = userId;
        this.testId = testId;
        this.startTime = startTime;
        this.attemptsUsed = 1;
    }

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
