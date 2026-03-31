package com.testingsystem.client.network;

import com.testingsystem.common.dto.*;
import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.common.enums.UserRole;
import com.testingsystem.common.network.Protocol;
import com.testingsystem.common.utils.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Request builder for constructing and sending requests to the server.
 * Provides type-safe methods for all available commands.
 */
public class RequestBuilder {
    private static final Logger logger = LogManager.getLogger(RequestBuilder.class);

    private final ServerConnection connection;

    /**
     * Constructs a RequestBuilder.
     */
    public RequestBuilder() {
        this.connection = ServerConnection.getInstance();
    }

    /**
     * Sends a login request.
     *
     * @param login the login
     * @param password the password
     * @return the response map containing user data
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> login(String login, String password) throws IOException {
        String response = connection.login(login, password);
        return parseResponse(response, Protocol.CMD_LOGIN);
    }

    /**
     * Sends a logout request.
     *
     * @return true if successful
     * @throws IOException if communication fails
     */
    public boolean logout() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_LOGOUT, "");
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    /**
     * Gets all tests.
     *
     * @param activeOnly if true, only active tests are returned
     * @return list of TestDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<TestDTO> getTests(boolean activeOnly) throws IOException {
        String data = activeOnly ? "active" : "";
        String response = connection.sendRequest(Protocol.CMD_GET_TESTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TESTS);
        return (List<TestDTO>) result.get("data");
    }

    /**
     * Gets a test by ID.
     *
     * @param testId the test ID
     * @return the TestDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestDTO getTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_TEST, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TEST);
        return (TestDTO) result.get("data");
    }

    /**
     * Gets questions for a test.
     *
     * @param testId the test ID
     * @param forTaking if true, correct answers are hidden
     * @return list of QuestionDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<QuestionDTO> getQuestions(Long testId, boolean forTaking) throws IOException {
        String data = testId + ":" + (forTaking ? "taking" : "full");
        String response = connection.sendRequest(Protocol.CMD_GET_QUESTIONS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_QUESTIONS);
        return (List<QuestionDTO>) result.get("data");
    }

    /**
     * Starts a test.
     *
     * @param testId the test ID
     * @return the TestResultDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestResultDTO startTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_START_TEST, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_START_TEST);
        return (TestResultDTO) result.get("data");
    }

    /**
     * Submits test answers.
     *
     * @param testId the test ID
     * @param answers list of user answers
     * @return the TestResultDTO with results
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestResultDTO submitTest(Long testId, List<UserAnswer> answers) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("testId", testId);
        data.put("answers", answers);
        String response = connection.sendRequest(Protocol.CMD_SUBMIT_TEST, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_SUBMIT_TEST);
        return (TestResultDTO) result.get("data");
    }

    /**
     * Gets user's assignments.
     *
     * @return list of TestAssignmentDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<TestAssignmentDTO> getMyAssignments() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_MY_ASSIGNMENTS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_MY_ASSIGNMENTS);
        return (List<TestAssignmentDTO>) result.get("data");
    }

    /**
     * Gets user's results.
     *
     * @return list of TestResultDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<TestResultDTO> getMyResults() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_MY_RESULTS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_MY_RESULTS);
        return (List<TestResultDTO>) result.get("data");
    }

    /**
     * Gets all users (admin only).
     *
     * @return list of UserDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<UserDTO> getUsers() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_USERS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_USERS);
        return (List<UserDTO>) result.get("data");
    }

    /**
     * Creates a new user (admin only).
     *
     * @param login the login
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @param role the role
     * @return the created UserDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public UserDTO createUser(String login, String password, String firstName, String lastName, UserRole role) 
            throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("login", login);
        data.put("password", password);
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("role", role.getValue());
        String response = connection.sendRequest(Protocol.CMD_CREATE_USER, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_CREATE_USER);
        return (UserDTO) result.get("data");
    }

    /**
     * Updates a user (admin only).
     *
     * @param user the user data
     * @return the updated UserDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public UserDTO updateUser(UserDTO user) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_UPDATE_USER, user);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_UPDATE_USER);
        return (UserDTO) result.get("data");
    }

    /**
     * Deletes a user (admin only).
     *
     * @param userId the user ID
     * @return true if successful
     * @throws IOException if communication fails
     */
    public boolean deleteUser(Long userId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_USER, userId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    /**
     * Blocks/unblocks a user (admin only).
     *
     * @param userId the user ID
     * @param isBlocked whether to block
     * @return the updated UserDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public UserDTO blockUser(Long userId, boolean isBlocked) throws IOException {
        String data = userId + ":" + isBlocked;
        String response = connection.sendRequest(Protocol.CMD_BLOCK_USER, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_BLOCK_USER);
        return (UserDTO) result.get("data");
    }

    /**
     * Creates a test (admin only).
     *
     * @param test the test data
     * @return the created TestDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestDTO createTest(TestDTO test) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_CREATE_TEST, test);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_CREATE_TEST);
        return (TestDTO) result.get("data");
    }

    /**
     * Updates a test (admin only).
     *
     * @param test the test data
     * @return the updated TestDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestDTO updateTest(TestDTO test) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_UPDATE_TEST, test);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_UPDATE_TEST);
        return (TestDTO) result.get("data");
    }

    /**
     * Deletes a test (admin only).
     *
     * @param testId the test ID
     * @return true if successful
     * @throws IOException if communication fails
     */
    public boolean deleteTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_TEST, testId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    /**
     * Creates a question (admin only).
     *
     * @param question the question data
     * @return the created QuestionDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public QuestionDTO createQuestion(QuestionDTO question) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_CREATE_QUESTION, question);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_CREATE_QUESTION);
        return (QuestionDTO) result.get("data");
    }

    /**
     * Deletes a question (admin only).
     *
     * @param questionId the question ID
     * @return true if successful
     * @throws IOException if communication fails
     */
    public boolean deleteQuestion(Long questionId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_QUESTION, questionId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    /**
     * Assigns a test to a user (admin only).
     *
     * @param userId the user ID
     * @param testId the test ID
     * @param attempts number of attempts
     * @return the created TestAssignmentDTO
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public TestAssignmentDTO assignTest(Long userId, Long testId, Integer attempts) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("testId", testId);
        data.put("attempts", attempts);
        String response = connection.sendRequest(Protocol.CMD_ASSIGN_TEST, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_ASSIGN_TEST);
        return (TestAssignmentDTO) result.get("data");
    }

    /**
     * Gets assignments (admin only).
     *
     * @param testId optional test ID filter
     * @return list of TestAssignmentDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<TestAssignmentDTO> getAssignments(Long testId) throws IOException {
        String data = testId != null ? testId.toString() : "";
        String response = connection.sendRequest(Protocol.CMD_GET_ASSIGNMENTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_ASSIGNMENTS);
        return (List<TestAssignmentDTO>) result.get("data");
    }

    /**
     * Gets results (admin only).
     *
     * @param testId optional test ID filter
     * @return list of TestResultDTOs
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public List<TestResultDTO> getResults(Long testId) throws IOException {
        String data = testId != null ? testId.toString() : "";
        String response = connection.sendRequest(Protocol.CMD_GET_RESULTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_RESULTS);
        return (List<TestResultDTO>) result.get("data");
    }

    /**
     * Gets overall statistics (admin only).
     *
     * @return statistics map
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getStatistics() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_STATISTICS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_STATISTICS);
        return (Map<String, Object>) result.get("data");
    }

    /**
     * Gets test statistics (admin only).
     *
     * @param testId the test ID
     * @return statistics map
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTestStatistics(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_TEST_STATS, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TEST_STATS);
        return (Map<String, Object>) result.get("data");
    }

    /**
     * Gets user statistics (admin only).
     *
     * @param userId the user ID
     * @return statistics map
     * @throws IOException if communication fails
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserStatistics(Long userId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_USER_STATS, userId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_USER_STATS);
        return (Map<String, Object>) result.get("data");
    }

    /**
     * Parses the response code from a response string.
     *
     * @param response the response string
     * @return the response code
     */
    private int parseResponseCode(String response) {
        String[] parts = response.split("\\|", 3);
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse response code", e);
            }
        }
        return Protocol.RESPONSE_ERROR;
    }

    /**
     * Parses a response and returns a map with the data.
     *
     * @param response the response string
     * @param expectedCommand the expected command
     * @return map containing the parsed data
     * @throws IOException if response indicates an error
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseResponse(String response, String expectedCommand) throws IOException {
        String[] parts = response.split("\\|", 3);
        if (parts.length < 2) {
            throw new IOException("Invalid response format");
        }

        String command = parts[0];
        int code = parseResponseCode(response);
        
        if (!expectedCommand.equals(command)) {
            logger.warn("Unexpected command in response: {} (expected {})", command, expectedCommand);
        }

        if (code != Protocol.RESPONSE_OK && code != Protocol.RESPONSE_CREATED) {
            String message = parts.length > 2 ? parts[2] : "Unknown error";
            throw new IOException("Server error (" + code + "): " + message);
        }

        Map<String, Object> result = new HashMap<>();
        if (parts.length > 2 && !parts[2].isEmpty()) {
            byte[] data = parts[2].getBytes();
            Object deserialized = SerializationUtils.deserialize(data);
            result.put("data", deserialized);
        }

        return result;
    }

    /**
     * Represents a user's answer to a question.
     */
    public static class UserAnswer {
        private Long questionId;
        private List<Long> selectedOptionIds;

        public UserAnswer() {
        }

        public UserAnswer(Long questionId, List<Long> selectedOptionIds) {
            this.questionId = questionId;
            this.selectedOptionIds = selectedOptionIds;
        }

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public List<Long> getSelectedOptionIds() {
            return selectedOptionIds;
        }

        public void setSelectedOptionIds(List<Long> selectedOptionIds) {
            this.selectedOptionIds = selectedOptionIds;
        }
    }
}
