package com.testingsystem.common.dto;

import com.testingsystem.common.enums.AssignmentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for TestAssignment entity.
 * Used for transferring test assignment data between client and server.
 */
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

    /**
     * Default constructor for serialization.
     */
    public TestAssignmentDTO() {
    }

    /**
     * Constructs a TestAssignmentDTO with all fields.
     *
     * @param id the assignment ID
     * @param userId the user ID
     * @param testId the test ID
     * @param userName the user's name
     * @param testName the test title
     * @param assignedDate the assignment date
     * @param deadline the deadline for completion
     * @param attemptsLeft remaining attempts
     * @param status the assignment status
     */
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

    /**
     * Constructs a TestAssignmentDTO without ID (for creation).
     *
     * @param userId the user ID
     * @param testId the test ID
     * @param deadline the deadline for completion
     * @param attemptsLeft remaining attempts
     */
    public TestAssignmentDTO(Long userId, Long testId, LocalDateTime deadline, Integer attemptsLeft) {
        this.userId = userId;
        this.testId = testId;
        this.deadline = deadline;
        this.attemptsLeft = attemptsLeft;
        this.status = AssignmentStatus.ASSIGNED;
        this.assignedDate = LocalDateTime.now();
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
