package com.testingsystem.common.enums;

import java.io.Serializable;

public enum UserRole implements Serializable {
    ADMIN("admin"),
    SPECIALIST("specialist");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid user role: " + value);
    }
}
