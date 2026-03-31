package com.testingsystem.server.model;

import com.testingsystem.common.enums.UserRole;

import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * Base class for AdminUser and SpecialistUser.
 * Implements the inheritance pattern for user types.
 */
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String login;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private UserRole role;
    private boolean isActive;

    /**
     * Default constructor for serialization.
     */
    public User() {
    }

    /**
     * Constructs a User with all fields.
     *
     * @param id the user ID
     * @param login the user login
     * @param passwordHash the hashed password
     * @param firstName the first name
     * @param lastName the last name
     * @param role the user role
     * @param isActive whether the user is active
     * @param createdAt the creation timestamp
     */
    public User(Long id, String login, String passwordHash, String firstName, String lastName,
                UserRole role, boolean isActive, LocalDateTime createdAt) {
        super(id);
        this.login = login;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isActive = isActive;
        this.setCreatedAt(createdAt);
    }

    // Getters and Setters

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets the full name of the user.
     *
     * @return first name and last name combined
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String getEntityName() {
        return "User";
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + getId() +
               ", login='" + login + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", role=" + role +
               ", isActive=" + isActive +
               '}';
    }
}
