package com.testingsystem.server.network;

import com.testingsystem.common.dto.*;
import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.common.enums.UserRole;
import com.testingsystem.common.network.Protocol;
import com.testingsystem.common.utils.SerializationUtils;
import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request processor for handling client commands.
 * Routes commands to appropriate service methods.
 */
public class RequestProcessor {
    private static final Logger logger = LogManager.getLogger(RequestProcessor.class);

    private final AuthService authService;
    private final TestService testService;
    private final ResultService resultService;
    private final AdminService adminService;
    private final StatisticsService statisticsService;

    private final Map<Long, UserDTO> authenticatedUsers;

    /**
     * Constructs a RequestProcessor.
     *
     * @param connectionPool the database connection pool
     * @param authenticatedUsers map of authenticated users
     */
    public RequestProcessor(ConnectionPool connectionPool, Map<Long, UserDTO> authenticatedUsers) {
        DAOFactory daoFactory = new DAOFactory(connectionPool);
        
        this.authService = new AuthService(daoFactory);
        this.testService = new TestService(daoFactory);
        this.resultService = new ResultService(daoFactory);
        this.adminService = new AdminService(daoFactory);
        this.statisticsService = new StatisticsService(daoFactory);
        this.authenticatedUsers = authenticatedUsers;
    }

    /**
     * Processes a client request.
     *
     * @param request the request string
     * @param clientId the client ID
     * @return the response string
     */
    public String processRequest(String request, Long clientId) {
        logger.debug("Processing request: {}", request);
        
        String[] parts = request.split(Protocol.SEPARATOR, 3);
        if (parts.length < 2) {
            return Protocol.createErrorResponse("UNKNOWN", Protocol.RESPONSE_BAD_REQUEST, "Invalid request format");
        }

        String command = parts[0];
        String data = parts.length > 2 ? parts[2] : "";

        try {
            switch (command) {
                case Protocol.CMD_LOGIN:
                    return handleLogin(data, clientId);
                case Protocol.CMD_LOGOUT:
                    return handleLogout(clientId);
                case Protocol.CMD_GET_TESTS:
                    return handleGetTests(data, clientId);
                case Protocol.CMD_GET_TEST:
                    return handleGetTest(data, clientId);
                case Protocol.CMD_GET_QUESTIONS:
                    return handleGetQuestions(data, clientId);
                case Protocol.CMD_START_TEST:
                    return handleStartTest(data, clientId);
                case Protocol.CMD_SUBMIT_TEST:
                    handleSubmitTest(data, clientId);
                case Protocol.CMD_GET_MY_ASSIGNMENTS:
                    return handleGetMyAssignments(clientId);
                case Protocol.CMD_GET_MY_RESULTS:
                    return handleGetMyResults(clientId);
                case Protocol.CMD_GET_USERS:
                    return handleGetUsers(clientId);
                case Protocol.CMD_CREATE_USER:
                    return handleCreateUser(data, clientId);
                case Protocol.CMD_UPDATE_USER:
                    return handleUpdateUser(data, clientId);
                case Protocol.CMD_DELETE_USER:
                    return handleDeleteUser(data, clientId);
                case Protocol.CMD_BLOCK_USER:
                    return handleBlockUser(data, clientId);
                case Protocol.CMD_CREATE_TEST:
                    return handleCreateTest(data, clientId);
                case Protocol.CMD_UPDATE_TEST:
                    return handleUpdateTest(data, clientId);
                case Protocol.CMD_DELETE_TEST:
                    return handleDeleteTest(data, clientId);
                case Protocol.CMD_CREATE_QUESTION:
                    return handleCreateQuestion(data, clientId);
                case Protocol.CMD_DELETE_QUESTION:
                    return handleDeleteQuestion(data, clientId);
                case Protocol.CMD_ASSIGN_TEST:
                    return handleAssignTest(data, clientId);
                case Protocol.CMD_GET_ASSIGNMENTS:
                    return handleGetAssignments(data, clientId);
                case Protocol.CMD_GET_RESULTS:
                    return handleGetResults(data, clientId);
                case Protocol.CMD_GET_STATISTICS:
                    return handleGetStatistics(clientId);
                case Protocol.CMD_GET_TEST_STATS:
                    return handleGetTestStats(data, clientId);
                case Protocol.CMD_GET_USER_STATS:
                    return handleGetUserStats(data, clientId);
                default:
                    return Protocol.createErrorResponse(command, Protocol.RESPONSE_BAD_REQUEST, 
                            "Unknown command: " + command);
            }
        } catch (Exception e) {
            logger.error("Error processing request: {}", command, e);
            return Protocol.createErrorResponse(command, Protocol.RESPONSE_ERROR, 
                    e.getMessage() != null ? e.getMessage() : "Internal server error");
        }
    }

    private String handleLogin(String data, Long clientId) throws AuthService.AuthenticationException {
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        if (bytes == null || bytes.length == 0) {
            throw new AuthService.AuthenticationException("Invalid login data");
        }

        Map<String, String> loginData = SerializationUtils.deserialize(bytes);
        if (loginData == null) {
            throw new AuthService.AuthenticationException("Invalid login data format");
        }
        
        String login = loginData.get("login");
        String password = loginData.get("password");

        UserDTO user = authService.authenticate(login, password);
        authenticatedUsers.put(clientId, user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("message", "Login successful");

        return Protocol.createSuccessResponse(Protocol.CMD_LOGIN,
                new String(SerializationUtils.serialize(response), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleLogout(Long clientId) {
        authenticatedUsers.remove(clientId);
        return Protocol.createSuccessResponse(Protocol.CMD_LOGOUT, "Logout successful");
    }

    private String handleGetTests(String data, Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        boolean activeOnly = data.equals("active");
        
        List<TestDTO> tests = activeOnly ? testService.getActiveTests() : testService.getAllTests();
        return Protocol.createSuccessResponse(Protocol.CMD_GET_TESTS,
                new String(SerializationUtils.serialize(tests), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetTest(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long testId = Long.parseLong(data);
        TestDTO test = testService.getTestById(testId);
        return Protocol.createSuccessResponse(Protocol.CMD_GET_TEST,
                new String(SerializationUtils.serialize(test), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetQuestions(String data, Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        String[] parts = data.split(":");
        Long testId = Long.parseLong(parts[0]);
        boolean forTaking = parts.length > 1 && parts[1].equals("taking");

        List<QuestionDTO> questions = forTaking ?
                testService.getQuestionsForTaking(testId) : testService.getQuestionsForTest(testId);
        return Protocol.createSuccessResponse(Protocol.CMD_GET_QUESTIONS,
                new String(SerializationUtils.serialize(questions), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleStartTest(String data, Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        Long testId = Long.parseLong(data);
        TestResultDTO result = resultService.startTest(user.getId(), testId);
        return Protocol.createSuccessResponse(Protocol.CMD_START_TEST,
                new String(SerializationUtils.serialize(result), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleSubmitTest(String data, Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Map<String, Object> submitData = SerializationUtils.deserialize(bytes);

        Long testId = (Long) submitData.get("testId");
        List<ResultService.UserAnswer> answers =
                (List<ResultService.UserAnswer>) submitData.get("answers");

        TestResultDTO result = resultService.submitTest(user.getId(), testId, answers);
        return Protocol.createSuccessResponse(Protocol.CMD_SUBMIT_TEST,
                new String(SerializationUtils.serialize(result), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetMyAssignments(Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        List<TestAssignmentDTO> assignments = adminService.getUserAssignments(user.getId());
        return Protocol.createSuccessResponse(Protocol.CMD_GET_MY_ASSIGNMENTS,
                new String(SerializationUtils.serialize(assignments), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetMyResults(Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        List<TestResultDTO> results = resultService.getUserResults(user.getId());
        return Protocol.createSuccessResponse(Protocol.CMD_GET_MY_RESULTS,
                new String(SerializationUtils.serialize(results), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetUsers(Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("Only admins can get users");
        }
        List<UserDTO> users = authService.getAllUsers();
        return Protocol.createSuccessResponse(Protocol.CMD_GET_USERS,
                new String(SerializationUtils.serialize(users), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleCreateUser(String data, Long clientId) throws Exception {
        UserDTO user = getAuthenticatedUser(clientId);
        if (user.getRole() != UserRole.ADMIN) {
            throw new SecurityException("Only admins can create users");
        }

        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Map<String, Object> userData = SerializationUtils.deserialize(bytes);

        UserDTO newUser = authService.register(
                (String) userData.get("login"),
                (String) userData.get("password"),
                (String) userData.get("firstName"),
                (String) userData.get("lastName"),
                UserRole.fromValue((String) userData.get("role"))
        );

        return Protocol.createSuccessResponse(Protocol.CMD_CREATE_USER,
                new String(SerializationUtils.serialize(newUser), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleUpdateUser(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        UserDTO userDTO = SerializationUtils.deserialize(bytes);
        UserDTO updated = authService.updateUser(userDTO);
        return Protocol.createSuccessResponse(Protocol.CMD_UPDATE_USER,
                new String(SerializationUtils.serialize(updated), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleDeleteUser(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long userId = Long.parseLong(data);
        authService.deleteUser(userId);
        return Protocol.createSuccessResponse(Protocol.CMD_DELETE_USER, "User deleted");
    }

    private String handleBlockUser(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        String[] parts = data.split(":");
        Long userId = Long.parseLong(parts[0]);
        boolean isBlocked = Boolean.parseBoolean(parts[1]);
        UserDTO updated = authService.blockUser(userId, isBlocked);
        return Protocol.createSuccessResponse(Protocol.CMD_BLOCK_USER,
                new String(SerializationUtils.serialize(updated), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleCreateTest(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        TestDTO testDTO = SerializationUtils.deserialize(bytes);
        TestDTO created = testService.createTest(testDTO);
        return Protocol.createSuccessResponse(Protocol.CMD_CREATE_TEST,
                new String(SerializationUtils.serialize(created), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleUpdateTest(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        TestDTO testDTO = SerializationUtils.deserialize(bytes);
        TestDTO updated = testService.updateTest(testDTO);
        return Protocol.createSuccessResponse(Protocol.CMD_UPDATE_TEST,
                new String(SerializationUtils.serialize(updated), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleDeleteTest(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long testId = Long.parseLong(data);
        testService.deleteTest(testId);
        return Protocol.createSuccessResponse(Protocol.CMD_DELETE_TEST, "Test deleted");
    }

    private String handleCreateQuestion(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        QuestionDTO questionDTO = SerializationUtils.deserialize(bytes);
        QuestionDTO created = testService.createQuestion(questionDTO);
        return Protocol.createSuccessResponse(Protocol.CMD_CREATE_QUESTION,
                new String(SerializationUtils.serialize(created), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleDeleteQuestion(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long questionId = Long.parseLong(data);
        testService.deleteQuestion(questionId);
        return Protocol.createSuccessResponse(Protocol.CMD_DELETE_QUESTION, "Question deleted");
    }

    private String handleAssignTest(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        Map<String, Object> assignData = SerializationUtils.deserialize(bytes);

        Long userId = (Long) assignData.get("userId");
        Long testId = (Long) assignData.get("testId");
        Integer attempts = (Integer) assignData.get("attempts");

        TestAssignmentDTO assignment = adminService.assignTest(userId, testId, null, attempts);
        return Protocol.createSuccessResponse(Protocol.CMD_ASSIGN_TEST,
                new String(SerializationUtils.serialize(assignment), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetAssignments(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        List<TestAssignmentDTO> assignments;
        
        if (data.isEmpty()) {
            assignments = adminService.getAllAssignments();
        } else {
            Long filterId = Long.parseLong(data);
            assignments = adminService.getTestAssignments(filterId);
        }
        
        return Protocol.createSuccessResponse(Protocol.CMD_GET_ASSIGNMENTS,
                new String(SerializationUtils.serialize(assignments), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetResults(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        List<TestResultDTO> results;

        if (data.isEmpty()) {
            results = resultService.getAllResults();
        } else {
            Long filterId = Long.parseLong(data);
            results = resultService.getTestResults(filterId);
        }

        return Protocol.createSuccessResponse(Protocol.CMD_GET_RESULTS,
                new String(SerializationUtils.serialize(results), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetStatistics(Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Map<String, Object> stats = statisticsService.getOverallStatistics();
        return Protocol.createSuccessResponse(Protocol.CMD_GET_STATISTICS,
                new String(SerializationUtils.serialize(stats), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetTestStats(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long testId = Long.parseLong(data);
        Map<String, Object> stats = statisticsService.getTestStatistics(testId);
        return Protocol.createSuccessResponse(Protocol.CMD_GET_TEST_STATS,
                new String(SerializationUtils.serialize(stats), java.nio.charset.StandardCharsets.UTF_8));
    }

    private String handleGetUserStats(String data, Long clientId) throws Exception {
        getAuthenticatedUser(clientId);
        Long userId = Long.parseLong(data);
        Map<String, Object> stats = statisticsService.getUserStatistics(userId);
        return Protocol.createSuccessResponse(Protocol.CMD_GET_USER_STATS,
                new String(SerializationUtils.serialize(stats), java.nio.charset.StandardCharsets.UTF_8));
    }

    private UserDTO getAuthenticatedUser(Long clientId) throws SecurityException {
        UserDTO user = authenticatedUsers.get(clientId);
        if (user == null) {
            throw new SecurityException("User not authenticated");
        }
        return user;
    }
}
