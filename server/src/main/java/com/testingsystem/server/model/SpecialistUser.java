package com.testingsystem.server.model;

import com.testingsystem.common.enums.UserRole;

import java.time.LocalDateTime;

public class SpecialistUser extends User {
    private static final long serialVersionUID = 1L;

    public SpecialistUser() {
        super();
        setRole(UserRole.SPECIALIST);
    }

    public SpecialistUser(Long id, String login, String passwordHash, String firstName, String lastName,
                          boolean isActive, LocalDateTime createdAt) {
        super(id, login, passwordHash, firstName, lastName, UserRole.SPECIALIST, isActive, createdAt);
    }

    public boolean canTakeTests() {
        return isActive();
    }

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
