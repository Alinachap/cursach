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

public class RequestBuilder {
    private static final Logger logger = LogManager.getLogger(RequestBuilder.class);

    private final ServerConnection connection;

    public RequestBuilder() {
        this.connection = ServerConnection.getInstance();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> login(String login, String password) throws IOException {
        String response = connection.login(login, password);
        return parseResponse(response, Protocol.CMD_LOGIN);
    }

    public boolean logout() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_LOGOUT, "");
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    @SuppressWarnings("unchecked")
    public List<TestDTO> getTests(boolean activeOnly) throws IOException {
        String data = activeOnly ? "active" : "";
        String response = connection.sendRequest(Protocol.CMD_GET_TESTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TESTS);
        return (List<TestDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public TestDTO getTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_TEST, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TEST);
        return (TestDTO) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public List<QuestionDTO> getQuestions(Long testId, boolean forTaking) throws IOException {
        String data = testId + ":" + (forTaking ? "taking" : "full");
        String response = connection.sendRequest(Protocol.CMD_GET_QUESTIONS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_QUESTIONS);
        return (List<QuestionDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public TestResultDTO startTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_START_TEST, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_START_TEST);
        return (TestResultDTO) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public TestResultDTO submitTest(Long testId, List<UserAnswer> answers) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("testId", testId);
        data.put("answers", answers);
        String response = connection.sendRequest(Protocol.CMD_SUBMIT_TEST, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_SUBMIT_TEST);
        return (TestResultDTO) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public List<TestAssignmentDTO> getMyAssignments() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_MY_ASSIGNMENTS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_MY_ASSIGNMENTS);
        return (List<TestAssignmentDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public List<TestResultDTO> getMyResults() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_MY_RESULTS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_MY_RESULTS);
        return (List<TestResultDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public List<UserDTO> getUsers() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_USERS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_USERS);
        return (List<UserDTO>) result.get("data");
    }

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

    @SuppressWarnings("unchecked")
    public UserDTO updateUser(UserDTO user) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_UPDATE_USER, user);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_UPDATE_USER);
        return (UserDTO) result.get("data");
    }

    public boolean deleteUser(Long userId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_USER, userId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    @SuppressWarnings("unchecked")
    public UserDTO blockUser(Long userId, boolean isBlocked) throws IOException {
        String data = userId + ":" + isBlocked;
        String response = connection.sendRequest(Protocol.CMD_BLOCK_USER, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_BLOCK_USER);
        return (UserDTO) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public TestDTO createTest(TestDTO test) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_CREATE_TEST, test);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_CREATE_TEST);
        return (TestDTO) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public TestDTO updateTest(TestDTO test) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_UPDATE_TEST, test);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_UPDATE_TEST);
        return (TestDTO) result.get("data");
    }

    public boolean deleteTest(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_TEST, testId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

    @SuppressWarnings("unchecked")
    public QuestionDTO createQuestion(QuestionDTO question) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_CREATE_QUESTION, question);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_CREATE_QUESTION);
        return (QuestionDTO) result.get("data");
    }

    public boolean deleteQuestion(Long questionId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_DELETE_QUESTION, questionId.toString());
        return parseResponseCode(response) == Protocol.RESPONSE_OK;
    }

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

    @SuppressWarnings("unchecked")
    public List<TestAssignmentDTO> getAssignments(Long testId) throws IOException {
        String data = testId != null ? testId.toString() : "";
        String response = connection.sendRequest(Protocol.CMD_GET_ASSIGNMENTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_ASSIGNMENTS);
        return (List<TestAssignmentDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public List<TestResultDTO> getResults(Long testId) throws IOException {
        String data = testId != null ? testId.toString() : "";
        String response = connection.sendRequest(Protocol.CMD_GET_RESULTS, data);
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_RESULTS);
        return (List<TestResultDTO>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getStatistics() throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_STATISTICS, "");
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_STATISTICS);
        return (Map<String, Object>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getTestStatistics(Long testId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_TEST_STATS, testId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_TEST_STATS);
        return (Map<String, Object>) result.get("data");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getUserStatistics(Long userId) throws IOException {
        String response = connection.sendRequest(Protocol.CMD_GET_USER_STATS, userId.toString());
        Map<String, Object> result = parseResponse(response, Protocol.CMD_GET_USER_STATS);
        return (Map<String, Object>) result.get("data");
    }

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
