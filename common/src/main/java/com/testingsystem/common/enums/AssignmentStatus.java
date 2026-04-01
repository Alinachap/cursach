package com.testingsystem.common.enums;

import java.io.Serializable;

public enum AssignmentStatus implements Serializable {
    ASSIGNED("assigned"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    EXPIRED("expired");

    private final String value;

    AssignmentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AssignmentStatus fromValue(String value) {
        for (AssignmentStatus status : AssignmentStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid assignment status: " + value);
    }
}
