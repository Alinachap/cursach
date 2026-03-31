package com.testingsystem.common.enums;

import java.io.Serializable;

/**
 * Enumeration of test assignment statuses.
 * Tracks the current state of a test assignment to a user.
 */
public enum AssignmentStatus implements Serializable {
    /**
     * Test has been assigned but not yet started.
     */
    ASSIGNED("assigned"),
    
    /**
     * Test is currently being taken by the user.
     */
    IN_PROGRESS("in_progress"),
    
    /**
     * Test has been completed by the user.
     */
    COMPLETED("completed"),
    
    /**
     * Test deadline has passed without completion.
     */
    EXPIRED("expired");

    private final String value;

    AssignmentStatus(String value) {
        this.value = value;
    }

    /**
     * Gets the string representation of the status.
     * @return the status value as string
     */
    public String getValue() {
        return value;
    }

    /**
     * Parses a string value to AssignmentStatus enum.
     * @param value the string value to parse
     * @return the corresponding AssignmentStatus
     * @throws IllegalArgumentException if the value is not valid
     */
    public static AssignmentStatus fromValue(String value) {
        for (AssignmentStatus status : AssignmentStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid assignment status: " + value);
    }
}
