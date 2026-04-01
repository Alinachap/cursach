package com.testingsystem.common.dto;

import com.testingsystem.common.enums.AssignmentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TestAssignmentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long testId;
    private String userName;
    private String testName;
    private LocalDateTime assignedDate;
    private LocalDateTime deadline;
    private Integer attemptsLeft;
    private AssignmentStatus status;

    public TestAssignmentDTO() {
    }

    public TestAssignmentDTO(Long id, Long userId, Long testId, String userName, String testName,
                             LocalDateTime assignedDate, LocalDateTime deadline,
                             Integer attemptsLeft, AssignmentStatus status) {
        this.id = id;
        this.userId = userId;
        this.testId = testId;
        this.userName = userName;
        this.testName = testName;
        this.assignedDate = assignedDate;
        this.deadline = deadline;
        this.attemptsLeft = attemptsLeft;
        this.status = status;
    }

    public TestAssignmentDTO(Long userId, Long testId, LocalDateTime deadline, Integer attemptsLeft) {
        this.userId = userId;
        this.testId = testId;
        this.deadline = deadline;
        this.attemptsLeft = attemptsLeft;
        this.status = AssignmentStatus.ASSIGNED;
        this.assignedDate = LocalDateTime.now();
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

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Integer getAttemptsLeft() {
        return attemptsLeft;
    }

    public void setAttemptsLeft(Integer attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public boolean isExpired() {
        return deadline != null &&
               LocalDateTime.now().isAfter(deadline) &&
               status != AssignmentStatus.COMPLETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestAssignmentDTO that = (TestAssignmentDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestAssignmentDTO{" +
               "id=" + id +
               ", userId=" + userId +
               ", testId=" + testId +
               ", status=" + status +
               ", attemptsLeft=" + attemptsLeft +
               '}';
    }
}
