package com.testingsystem.server.model;

import com.testingsystem.common.enums.UserRole;

import java.time.LocalDateTime;

/**
 * Represents an administrator user with elevated privileges.
 * Extends User class with admin-specific functionality.
 */
public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for serialization.
     */
    public AdminUser() {
        super();
        setRole(UserRole.ADMIN);
    }

    /**
     * Constructs an AdminUser with all fields.
     *
     * @param id the user ID
     * @param login the user login
     * @param passwordHash the hashed password
     * @param firstName the first name
     * @param lastName the last name
     * @param isActive whether the user is active
     * @param createdAt the creation timestamp
     */
    public AdminUser(Long id, String login, String passwordHash, String firstName, String lastName,
                     boolean isActive, LocalDateTime createdAt) {
        super(id, login, passwordHash, firstName, lastName, UserRole.ADMIN, isActive, createdAt);
    }

    /**
     * Checks if this user can manage other users.
     *
     * @return true (admins can manage users)
     */
    public boolean canManageUsers() {
        return true;
    }

    /**
     * Checks if this user can manage tests.
     *
     * @return true (admins can manage tests)
     */
    public boolean canManageTests() {
        return true;
    }

    /**
     * Checks if this user can view statistics.
     *
     * @return true (admins can view statistics)
     */
    public boolean canViewStatistics() {
        return true;
    }

    @Override
    public String getEntityName() {
        return "AdminUser";
    }

    @Override
    public String toString() {
        return "AdminUser{" +
               "id=" + getId() +
               ", login='" + getLogin() + '\'' +
               ", fullName='" + getFullName() + '\'' +
               '}';
    }
}
