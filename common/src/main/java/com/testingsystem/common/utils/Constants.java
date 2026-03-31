package com.testingsystem.common.utils;

/**
 * Utility class containing system-wide constants.
 * This class cannot be instantiated and provides only static constants.
 */
public final class Constants {

    /**
     * Default server port for client-server communication.
     */
    public static final int DEFAULT_SERVER_PORT = 12345;

    /**
     * Default host for server connection.
     */
    public static final String DEFAULT_SERVER_HOST = "localhost";

    /**
     * Socket connection timeout in milliseconds.
     */
    public static final int SOCKET_TIMEOUT_MS = 30000;

    /**
     * Socket read timeout in milliseconds.
     */
    public static final int SOCKET_READ_TIMEOUT_MS = 60000;

    /**
     * Maximum number of concurrent connections.
     */
    public static final int MAX_CONNECTIONS = 50;

    /**
     * Default number of attempts for test assignments.
     */
    public static final int DEFAULT_ATTEMPTS = 1;

    /**
     * Maximum number of attempts allowed.
     */
    public static final int MAX_ATTEMPTS = 5;

    /**
     * Session timeout in minutes.
     */
    public static final int SESSION_TIMEOUT_MINUTES = 30;

    /**
     * Minimum password length.
     */
    public static final int MIN_PASSWORD_LENGTH = 6;

    /**
     * Maximum password length.
     */
    public static final int MAX_PASSWORD_LENGTH = 50;

    /**
     * Minimum login length.
     */
    public static final int MIN_LOGIN_LENGTH = 3;

    /**
     * Maximum login length.
     */
    public static final int MAX_LOGIN_LENGTH = 50;

    /**
     * BCrypt work factor for password hashing.
     */
    public static final int BCRYPT_WORK_FACTOR = 10;

    /**
     * Private constructor to prevent instantiation.
     */
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
