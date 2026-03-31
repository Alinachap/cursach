package com.testingsystem.server.model;

import java.time.LocalDateTime;

/**
 * Represents a test in the system.
 * Contains test configuration including time limit and passing score.
 */
public class Test extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private Integer timeLimit;
    private Integer passingScore;
    private boolean isActive;

    /**
     * Default constructor for serialization.
     */
    public Test() {
    }

    /**
     * Constructs a Test with all fields.
     *
     * @param id the test ID
     * @param title the test title
     * @param description the test description
     * @param timeLimit the time limit in minutes
     * @param passingScore the minimum passing score percentage
     * @param isActive whether the test is active
     * @param createdAt the creation timestamp
     */
    public Test(Long id, String title, String description, Integer timeLimit,
                Integer passingScore, boolean isActive, LocalDateTime createdAt) {
        super(id);
        this.title = title;
        this.description = description;
        this.timeLimit = timeLimit;
        this.passingScore = passingScore;
        this.isActive = isActive;
        this.setCreatedAt(createdAt);
    }

    // Getters and Setters

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

    @Override
    public String getEntityName() {
        return "Test";
    }

    @Override
    public String toString() {
        return "Test{" +
               "id=" + getId() +
               ", title='" + title + '\'' +
               ", timeLimit=" + timeLimit +
               ", passingScore=" + passingScore +
               ", isActive=" + isActive +
               '}';
    }
}
