package com.testingsystem.server.model;

import com.testingsystem.common.enums.UserRole;

import java.time.LocalDateTime;

public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    public AdminUser() {
        super();
        setRole(UserRole.ADMIN);
    }

    public AdminUser(Long id, String login, String passwordHash, String firstName, String lastName,
                     boolean isActive, LocalDateTime createdAt) {
        super(id, login, passwordHash, firstName, lastName, UserRole.ADMIN, isActive, createdAt);
    }

    public boolean canManageUsers() {
        return true;
    }

    public boolean canManageTests() {
        return true;
    }

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
