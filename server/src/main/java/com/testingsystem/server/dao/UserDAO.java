package com.testingsystem.server.dao;

import com.testingsystem.common.enums.UserRole;
import com.testingsystem.server.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for User entities.
 * Provides database operations for user management.
 */
public interface UserDAO extends GenericDAO<User, Long> {

    /**
     * Finds a user by login.
     *
     * @param login the user login
     * @return an Optional containing the user if found
     * @throws SQLException if a database error occurs
     */
    Optional<User> findByLogin(String login) throws SQLException;

    /**
     * Finds users by role.
     *
     * @param role the user role
     * @return list of users with the specified role
     * @throws SQLException if a database error occurs
     */
    List<User> findByRole(UserRole role) throws SQLException;

    /**
     * Finds active users.
     *
     * @return list of active users
     * @throws SQLException if a database error occurs
     */
    List<User> findActiveUsers() throws SQLException;

    /**
     * Finds specialists (users with SPECIALIST role).
     *
     * @return list of specialist users
     * @throws SQLException if a database error occurs
     */
    List<User> findSpecialists() throws SQLException;

    /**
     * Finds admins (users with ADMIN role).
     *
     * @return list of admin users
     * @throws SQLException if a database error occurs
     */
    List<User> findAdmins() throws SQLException;

    /**
     * Updates user active status.
     *
     * @param userId the user ID
     * @param isActive the new active status
     * @return true if updated successfully
     * @throws SQLException if a database error occurs
     */
    boolean updateActiveStatus(Long userId, boolean isActive) throws SQLException;

    /**
     * Checks if a login already exists.
     *
     * @param login the login to check
     * @return true if login exists
     * @throws SQLException if a database error occurs
     */
    boolean loginExists(String login) throws SQLException;
}
