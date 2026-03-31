package com.testingsystem.client.util;

/**
 * Utility class for input validation.
 */
public class Validator {

    /**
     * Validates a login string.
     *
     * @param login the login to validate
     * @return error message if invalid, null if valid
     */
    public static String validateLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return "Login cannot be empty";
        }
        if (login.length() < 3) {
            return "Login must be at least 3 characters";
        }
        if (login.length() > 50) {
            return "Login must not exceed 50 characters";
        }
        if (!login.matches("^[a-zA-Z0-9_]+$")) {
            return "Login can only contain letters, numbers, and underscores";
        }
        return null;
    }

    /**
     * Validates a password string.
     *
     * @param password the password to validate
     * @return error message if invalid, null if valid
     */
    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters";
        }
        if (password.length() > 50) {
            return "Password must not exceed 50 characters";
        }
        return null;
    }

    /**
     * Validates a name string.
     *
     * @param name the name to validate
     * @param fieldName the field name for error message
     * @return error message if invalid, null if valid
     */
    public static String validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return fieldName + " cannot be empty";
        }
        if (name.length() > 100) {
            return fieldName + " must not exceed 100 characters";
        }
        return null;
    }

    /**
     * Validates an email string.
     *
     * @param email the email to validate
     * @return error message if invalid, null if valid
     */
    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format";
        }
        return null;
    }

    /**
     * Validates a positive integer.
     *
     * @param value the value to validate
     * @param fieldName the field name for error message
     * @param min minimum allowed value
     * @param max maximum allowed value
     * @return error message if invalid, null if valid
     */
    public static String validatePositiveInteger(Integer value, String fieldName, int min, int max) {
        if (value == null) {
            return fieldName + " is required";
        }
        if (value < min) {
            return fieldName + " must be at least " + min;
        }
        if (value > max) {
            return fieldName + " must not exceed " + max;
        }
        return null;
    }

    /**
     * Validates a string length.
     *
     * @param value the string to validate
     * @param fieldName the field name
     * @param minLength minimum length
     * @param maxLength maximum length
     * @return error message if invalid, null if valid
     */
    public static String validateStringLength(String value, String fieldName, int minLength, int maxLength) {
        if (value == null) {
            return fieldName + " is required";
        }
        if (value.length() < minLength) {
            return fieldName + " must be at least " + minLength + " characters";
        }
        if (value.length() > maxLength) {
            return fieldName + " must not exceed " + maxLength + " characters";
        }
        return null;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private Validator() {
        throw new UnsupportedOperationException("Validator class cannot be instantiated");
    }
}
