package com.testingsystem.server.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class for all entity classes.
 * Provides common fields: id and createdAt.
 * Implements the Template Method pattern for entity comparison.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Long id;
    protected LocalDateTime createdAt;

    /**
     * Default constructor for serialization.
     */
    protected BaseEntity() {
    }

    /**
     * Constructs a BaseEntity with ID.
     *
     * @param id the entity ID
     */
    protected BaseEntity(Long id) {
        this.id = id;
    }

    /**
     * Gets the entity ID.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity ID.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the creation timestamp.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the timestamp to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Abstract method to get entity name for logging.
     *
     * @return the entity name
     */
    public abstract String getEntityName();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getEntityName() + "{id=" + id + '}';
    }
}
