package com.testingsystem.common.utils;

public final class Constants {

    public static final int DEFAULT_SERVER_PORT = 12345;

    public static final String DEFAULT_SERVER_HOST = "localhost";

    public static final int SOCKET_TIMEOUT_MS = 30000;

    public static final int SOCKET_READ_TIMEOUT_MS = 60000;

    public static final int MAX_CONNECTIONS = 50;

    public static final int DEFAULT_ATTEMPTS = 1;

    public static final int MAX_ATTEMPTS = 5;

    public static final int SESSION_TIMEOUT_MINUTES = 30;

    public static final int MIN_PASSWORD_LENGTH = 6;

    public static final int MAX_PASSWORD_LENGTH = 50;

    public static final int MIN_LOGIN_LENGTH = 3;

    public static final int MAX_LOGIN_LENGTH = 50;

    public static final int BCRYPT_WORK_FACTOR = 10;

    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
