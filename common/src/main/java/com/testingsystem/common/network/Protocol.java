package com.testingsystem.common.network;

/**
 * Network protocol constants.
 * Defines command types and response codes for client-server communication.
 */
public final class Protocol {

    // Command types
    public static final String CMD_LOGIN = "LOGIN";
    public static final String CMD_LOGOUT = "LOGOUT";
    public static final String CMD_GET_TESTS = "GET_TESTS";
    public static final String CMD_GET_TEST = "GET_TEST";
    public static final String CMD_CREATE_TEST = "CREATE_TEST";
    public static final String CMD_UPDATE_TEST = "UPDATE_TEST";
    public static final String CMD_DELETE_TEST = "DELETE_TEST";
    public static final String CMD_GET_QUESTIONS = "GET_QUESTIONS";
    public static final String CMD_CREATE_QUESTION = "CREATE_QUESTION";
    public static final String CMD_UPDATE_QUESTION = "UPDATE_QUESTION";
    public static final String CMD_DELETE_QUESTION = "DELETE_QUESTION";
    public static final String CMD_START_TEST = "START_TEST";
    public static final String CMD_SUBMIT_TEST = "SUBMIT_TEST";
    public static final String CMD_GET_RESULTS = "GET_RESULTS";
    public static final String CMD_GET_RESULT = "GET_RESULT";
    public static final String CMD_MANAGE_USERS = "MANAGE_USERS";
    public static final String CMD_GET_USERS = "GET_USERS";
    public static final String CMD_CREATE_USER = "CREATE_USER";
    public static final String CMD_UPDATE_USER = "UPDATE_USER";
    public static final String CMD_DELETE_USER = "DELETE_USER";
    public static final String CMD_BLOCK_USER = "BLOCK_USER";
    public static final String CMD_ASSIGN_TEST = "ASSIGN_TEST";
    public static final String CMD_GET_ASSIGNMENTS = "GET_ASSIGNMENTS";
    public static final String CMD_UPDATE_ASSIGNMENT = "UPDATE_ASSIGNMENT";
    public static final String CMD_DELETE_ASSIGNMENT = "DELETE_ASSIGNMENT";
    public static final String CMD_GET_STATISTICS = "GET_STATISTICS";
    public static final String CMD_GET_TEST_STATS = "GET_TEST_STATS";
    public static final String CMD_GET_USER_STATS = "GET_USER_STATS";
    public static final String CMD_GET_QUESTION_STATS = "GET_QUESTION_STATS";
    public static final String CMD_GET_MY_ASSIGNMENTS = "GET_MY_ASSIGNMENTS";
    public static final String CMD_GET_MY_RESULTS = "GET_MY_RESULTS";

    // Response codes
    public static final int RESPONSE_OK = 200;
    public static final int RESPONSE_CREATED = 201;
    public static final int RESPONSE_BAD_REQUEST = 400;
    public static final int RESPONSE_UNAUTHORIZED = 401;
    public static final int RESPONSE_FORBIDDEN = 403;
    public static final int RESPONSE_NOT_FOUND = 404;
    public static final int RESPONSE_ERROR = 500;

    // Message separators
    public static final String SEPARATOR = "|";
    public static final String DATA_SEPARATOR = "||";

    /**
     * Private constructor to prevent instantiation.
     */
    private Protocol() {
        throw new UnsupportedOperationException("Protocol class cannot be instantiated");
    }

    /**
     * Creates a success response message.
     *
     * @param command the command
     * @param data the response data
     * @return the formatted response
     */
    public static String createSuccessResponse(String command, String data) {
        return command + SEPARATOR + RESPONSE_OK + SEPARATOR + data;
    }

    /**
     * Creates an error response message.
     *
     * @param command the command
     * @param errorCode the error code
     * @param message the error message
     * @return the formatted response
     */
    public static String createErrorResponse(String command, int errorCode, String message) {
        return command + SEPARATOR + errorCode + SEPARATOR + message;
    }

    /**
     * Creates a request message.
     *
     * @param command the command
     * @param data the request data
     * @return the formatted request
     */
    public static String createRequest(String command, String data) {
        return command + SEPARATOR + data;
    }
}
