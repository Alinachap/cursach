package com.testingsystem.server.model;

import com.testingsystem.common.enums.AssignmentStatus;

import java.time.LocalDateTime;

public class TestAssignment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long testId;
    private LocalDateTime assignedDate;
    private LocalDateTime deadline;
    private Integer attemptsLeft;
    private AssignmentStatus status;

    public TestAssignment() {
    }

    public TestAssignment(Long id, Long userId, Long testId, LocalDateTime assignedDate,
                          LocalDateTime deadline, Integer attemptsLeft, AssignmentStatus status) {
        super(id);
        this.userId = userId;
        this.testId = testId;
        this.assignedDate = assignedDate;
        this.deadline = deadline;
        this.attemptsLeft = attemptsLeft;
        this.status = status;
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

    public boolean canStart() {
        return (status == AssignmentStatus.ASSIGNED || status == AssignmentStatus.IN_PROGRESS)
               && attemptsLeft > 0
               && !isExpired();
    }

    public void useAttempt() {
        if (attemptsLeft > 0) {
            attemptsLeft--;
        }
    }

    @Override
    public String getEntityName() {
        return "TestAssignment";
    }

    @Override
    public String toString() {
        return "TestAssignment{" +
               "id=" + getId() +
               ", userId=" + userId +
               ", testId=" + testId +
               ", status=" + status +
               ", attemptsLeft=" + attemptsLeft +
               '}';
    }
}
