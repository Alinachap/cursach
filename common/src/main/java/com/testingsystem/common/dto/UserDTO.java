package com.testingsystem.common.dto;

import com.testingsystem.common.enums.UserRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for User entity.
 * Used for transferring user data between client and server.
 */
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private UserRole role;
    private boolean isActive;
    private LocalDateTime createdAt;

    /**
     * Default constructor for serialization.
     */
    public UserDTO() {
    }

    /**
     * Constructs a UserDTO with all fields.
     *
     * @param id the user ID
     * @param login the user login
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param role the user's role
     * @param isActive whether the user is active
     * @param createdAt the creation timestamp
     */
    public UserDTO(Long id, String login, String firstName, String lastName, 
                   UserRole role, boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    /**
     * Constructs a UserDTO without ID and timestamps (for creation).
     *
     * @param login the user login
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param role the user's role
     */
    public UserDTO(String login, String firstName, String lastName, UserRole role) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isActive = true;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) &&
               Objects.equals(login, userDTO.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
               "id=" + id +
               ", login='" + login + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", role=" + role +
               ", isActive=" + isActive +
               '}';
    }
}
