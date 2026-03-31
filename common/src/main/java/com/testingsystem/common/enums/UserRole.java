package com.testingsystem.common.enums;

import java.io.Serializable;

/**
 * Enumeration of user roles in the system.
 * Defines the two types of users: administrators and specialists.
 */
public enum UserRole implements Serializable {
    /**
     * Administrator role with full system access.
     */
    ADMIN("admin"),
    
    /**
     * Specialist role with limited access to take tests.
     */
    SPECIALIST("specialist");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    /**
     * Gets the string representation of the role.
     * @return the role value as string
     */
    public String getValue() {
        return value;
    }

    /**
     * Parses a string value to UserRole enum.
     * @param value the string value to parse
     * @return the corresponding UserRole
     * @throws IllegalArgumentException if the value is not valid
     */
    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid user role: " + value);
    }
}
