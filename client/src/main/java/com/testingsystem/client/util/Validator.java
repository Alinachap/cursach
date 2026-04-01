package com.testingsystem.client.util;

public class Validator {

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

    public static String validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return fieldName + " cannot be empty";
        }
        if (name.length() > 100) {
            return fieldName + " must not exceed 100 characters";
        }
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty";
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return "Invalid email format";
        }
        return null;
    }

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

    private Validator() {
        throw new UnsupportedOperationException("Validator class cannot be instantiated");
    }
}
