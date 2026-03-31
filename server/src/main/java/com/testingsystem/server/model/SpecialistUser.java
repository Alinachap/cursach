package com.testingsystem.server.model;

import com.testingsystem.common.enums.UserRole;

import java.time.LocalDateTime;

/**
 * Represents a specialist user who can take tests.
 * Extends User class with specialist-specific functionality.
 */
public class SpecialistUser extends User {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for serialization.
     */
    public SpecialistUser() {
        super();
        setRole(UserRole.SPECIALIST);
    }

    /**
     * Constructs a SpecialistUser with all fields.
     *
     * @param id the user ID
     * @param login the user login
     * @param passwordHash the hashed password
     * @param firstName the first name
     * @param lastName the last name
     * @param isActive whether the user is active
     * @param createdAt the creation timestamp
     */
    public SpecialistUser(Long id, String login, String passwordHash, String firstName, String lastName,
                          boolean isActive, LocalDateTime createdAt) {
        super(id, login, passwordHash, firstName, lastName, UserRole.SPECIALIST, isActive, createdAt);
    }

    /**
     * Checks if this user can take tests.
     *
     * @return true if user is active
     */
    public boolean canTakeTests() {
        return isActive();
    }

    /**
     * Checks if this user can view their own results.
     *
     * @return true (specialists can view their results)
     */
    public boolean canViewOwnResults() {
        return true;
    }

    @Override
    public String getEntityName() {
        return "SpecialistUser";
    }

    @Override
    public String toString() {
        return "SpecialistUser{" +
               "id=" + getId() +
               ", login='" + getLogin() + '\'' +
               ", fullName='" + getFullName() + '\'' +
               '}';
    }
}
