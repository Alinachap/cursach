package com.testingsystem.server.model;

import java.time.LocalDateTime;

public class Test extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private Integer timeLimit;
    private Integer passingScore;
    private boolean isActive;

    public Test() {
    }

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
