package com.testingsystem.server.model;

import com.testingsystem.common.enums.AssignmentStatus;

import java.time.LocalDateTime;

/**
 * Represents a test assignment to a user.
 * Tracks the status, deadline, and remaining attempts.
 */
public class TestAssignment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long testId;
    private LocalDateTime assignedDate;
    private LocalDateTime deadline;
    private Integer attemptsLeft;
    private AssignmentStatus status;

    /**
     * Default constructor for serialization.
     */
    public TestAssignment() {
    }

    /**
     * Constructs a TestAssignment with all fields.
     *
     * @param id the assignment ID
     * @param userId the user ID
     * @param testId the test ID
     * @param assignedDate the assignment date
     * @param deadline the deadline
     * @param attemptsLeft remaining attempts
     * @param status the assignment status
     */
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

    // Getters and Setters

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

    /**
     * Checks if the assignment is expired.
     *
     * @return true if deadline has passed and status is not completed
     */
    public boolean isExpired() {
        return deadline != null && 
               LocalDateTime.now().isAfter(deadline) && 
               status != AssignmentStatus.COMPLETED;
    }

    /**
     * Checks if the assignment can be started.
     *
     * @return true if status is assigned or in_progress and attempts are available
     */
    public boolean canStart() {
        return (status == AssignmentStatus.ASSIGNED || status == AssignmentStatus.IN_PROGRESS) 
               && attemptsLeft > 0 
               && !isExpired();
    }

    /**
     * Decrements the attempts left counter.
     */
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
