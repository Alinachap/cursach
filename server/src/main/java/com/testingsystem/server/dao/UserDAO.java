package com.testingsystem.server.dao;

import com.testingsystem.common.enums.UserRole;
import com.testingsystem.server.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO extends GenericDAO<User, Long> {

    Optional<User> findByLogin(String login) throws SQLException;

    List<User> findByRole(UserRole role) throws SQLException;

    List<User> findActiveUsers() throws SQLException;

    List<User> findSpecialists() throws SQLException;

    List<User> findAdmins() throws SQLException;

    boolean updateActiveStatus(Long userId, boolean isActive) throws SQLException;

    boolean loginExists(String login) throws SQLException;
}
