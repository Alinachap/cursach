package com.testingsystem.server.service;

import com.testingsystem.common.dto.UserDTO;
import com.testingsystem.common.enums.UserRole;
import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.dao.UserDAO;
import com.testingsystem.server.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthService {

    private final DAOFactory daoFactory;
    private final UserDAO userDAO;

    public AuthService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.userDAO = daoFactory.getUserDAO();
    }

    public UserDTO authenticate(String login, String password) throws AuthenticationException {
        try {
            Optional<User> userOpt = userDAO.findByLogin(login);

            if (userOpt.isEmpty()) {
                throw new AuthenticationException("Invalid login or password");
            }

            User user = userOpt.get();

            if (!user.isActive()) {
                throw new AuthenticationException("Account is blocked");
            }

            String hashedPassword = hashPassword(password);
            if (!hashedPassword.equals(user.getPasswordHash())) {
                throw new AuthenticationException("Invalid login or password");
            }

            return toDTO(user);
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during authentication", e);
        }
    }

    public UserDTO register(String login, String password, String firstName, String lastName, UserRole role)
            throws AuthenticationException {
        try {
            if (userDAO.loginExists(login)) {
                throw new AuthenticationException("Login already exists");
            }

            if (login.length() < 3 || login.length() > 50) {
                throw new AuthenticationException("Login must be between 3 and 50 characters");
            }

            if (password.length() < 6) {
                throw new AuthenticationException("Password must be at least 6 characters");
            }

            User user = new User();
            user.setLogin(login);
            user.setPasswordHash(hashPassword(password));
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
            user.setActive(true);

            User savedUser = userDAO.save(user);
            return toDTO(savedUser);
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during registration", e);
        }
    }

    public UserDTO getUserById(Long userId) throws AuthenticationException {
        try {
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) {
                throw new AuthenticationException("User not found");
            }
            return toDTO(userOpt.get());
        } catch (SQLException e) {
            throw new AuthenticationException("Database error", e);
        }
    }

    public List<UserDTO> getAllUsers() throws AuthenticationException {
        try {
            return userDAO.findAll().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new AuthenticationException("Database error", e);
        }
    }

    public List<UserDTO> getAllSpecialists() throws AuthenticationException {
        try {
            return userDAO.findSpecialists().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new AuthenticationException("Database error", e);
        }
    }

    public UserDTO updateUser(UserDTO userDTO) throws AuthenticationException {
        try {
            Optional<User> userOpt = userDAO.findById(userDTO.getId());
            if (userOpt.isEmpty()) {
                throw new AuthenticationException("User not found");
            }

            User user = userOpt.get();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setActive(userDTO.isActive());

            if (userDTO.getRole() != null) {
                user.setRole(userDTO.getRole());
            }

            User updatedUser = userDAO.update(user);
            return toDTO(updatedUser);
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during update", e);
        }
    }

    public void deleteUser(Long userId) throws AuthenticationException {
        try {
            if (!userDAO.exists(userId)) {
                throw new AuthenticationException("User not found");
            }
            userDAO.delete(userId);
        } catch (SQLException e) {
            throw new AuthenticationException("Database error during deletion", e);
        }
    }

    public UserDTO blockUser(Long userId, boolean isBlocked) throws AuthenticationException {
        try {
            if (!userDAO.updateActiveStatus(userId, !isBlocked)) {
                throw new AuthenticationException("User not found");
            }
            return getUserById(userId);
        } catch (SQLException e) {
            throw new AuthenticationException("Database error", e);
        }
    }

    private String hashPassword(String password) throws AuthenticationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("Password hashing algorithm not available", e);
        }
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
