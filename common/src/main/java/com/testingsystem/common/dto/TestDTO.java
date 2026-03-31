package com.testingsystem.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for Test entity.
 * Used for transferring test data between client and server.
 */
public class TestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String description;
    private Integer timeLimit;
    private Integer passingScore;
    private boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Default constructor for serialization.
     */
    public TestDTO() {
    }

    /**
     * Constructs a TestDTO with all fields.
     *
     * @param id the test ID
     * @param title the test title
     * @param description the test description
     * @param timeLimit the time limit in minutes
     * @param passingScore the minimum passing score percentage
     * @param isActive whether the test is active
     * @param createdAt the creation timestamp
     */
    public TestDTO(Long id, String title, String description, Integer timeLimit,
                   Integer passingScore, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Constructs a TestDTO without ID and timestamps (for creation).
     *
     * @param title the test title
     * @param description the test description
     * @param timeLimit the time limit in minutes
     * @param passingScore the minimum passing score percentage
     */
    public TestDTO(String title, String description, Integer timeLimit, Integer passingScore) {
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
        this.isActive = true;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestDTO testDTO = (TestDTO) o;
        return Objects.equals(id, testDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestDTO{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", timeLimit=" + timeLimit +
               ", passingScore=" + passingScore +
               ", isActive=" + isActive +
               '}';
    }
}
