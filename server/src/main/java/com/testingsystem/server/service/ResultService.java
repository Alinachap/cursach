package com.testingsystem.server.service;

import com.testingsystem.common.dto.TestResultDTO;
import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for test result management.
 * Handles test taking, answer submission, and result calculation.
 */
public class ResultService {

    private final DAOFactory daoFactory;

    /**
     * Constructs a ResultService with the given DAO factory.
     *
     * @param daoFactory the DAO factory
     */
    public ResultService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Starts a test attempt for a user.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @return the created TestResultDTO
     * @throws ResultServiceException if test cannot be started
     */
    public TestResultDTO startTest(Long userId, Long testId) throws ResultServiceException {
        try {
            // Check assignment
            TestAssignment assignment = daoFactory.getTestAssignmentDAO()
                    .findByUserIdAndTestId(userId, testId);
            
            if (assignment == null) {
                throw new ResultServiceException("Test not assigned to user");
            }
            
            if (!assignment.canStart()) {
                if (assignment.isExpired()) {
                    throw new ResultServiceException("Test has expired");
                }
                if (assignment.getAttemptsLeft() <= 0) {
                    throw new ResultServiceException("No attempts left");
                }
                throw new ResultServiceException("Test cannot be started");
            }
            
            // Update assignment status
            daoFactory.getTestAssignmentDAO().updateStatus(assignment.getId(), AssignmentStatus.IN_PROGRESS);
            
            // Create test result
            TestResult result = new TestResult();
            result.setUserId(userId);
            result.setTestId(testId);
            result.setStartTime(LocalDateTime.now());
            result.setAttemptsUsed(1);
            
            TestResult savedResult = daoFactory.getTestResultDAO().save(result);
            
            // Decrement attempts
            daoFactory.getTestAssignmentDAO().decrementAttempts(assignment.getId());
            
            return toDTO(savedResult);
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to start test", e);
        }
    }

    /**
     * Submits test answers and calculates the result.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @param answers map of question ID to list of selected option IDs
     * @return the TestResultDTO with calculated score
     * @throws ResultServiceException if submission fails
     */
    public TestResultDTO submitTest(Long userId, Long testId, List<UserAnswer> answers) 
            throws ResultServiceException {
        try {
            // Get the latest result for this test
            TestResult result = daoFactory.getTestResultDAO()
                    .findLatestByUserIdAndTestId(userId, testId);
            
            if (result == null) {
                throw new ResultServiceException("No test result found");
            }
            
            // Get all questions for the test
            List<Question> questions = daoFactory.getQuestionDAO().findByTestId(testId);
            
            // Calculate score
            int correctCount = 0;
            int totalQuestions = questions.size();
            
            for (Question question : questions) {
                List<Long> selectedOptions = new ArrayList<>();
                for (UserAnswer answer : answers) {
                    if (answer.getQuestionId().equals(question.getId())) {
                        selectedOptions = answer.getSelectedOptionIds();
                        break;
                    }
                }
                
                if (question.isAnswerCorrect(selectedOptions)) {
                    correctCount++;
                }
            }
            
            // Calculate percentage
            BigDecimal scorePercent = totalQuestions > 0 
                    ? BigDecimal.valueOf(correctCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalQuestions), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            
            // Get test passing score
            Optional<Test> testOpt = daoFactory.getTestDAO().findById(testId);
            int passingScore = testOpt.map(Test::getPassingScore).orElse(70);
            boolean isPassed = scorePercent.compareTo(BigDecimal.valueOf(passingScore)) >= 0;
            
            // Update result
            result.setEndTime(LocalDateTime.now());
            result.setScorePercent(scorePercent);
            result.setIsPassed(isPassed);
            result.setAnswersData(answersToJson(answers));
            
            daoFactory.getTestResultDAO().update(result);
            
            // Update assignment status
            TestAssignment assignment = daoFactory.getTestAssignmentDAO()
                    .findByUserIdAndTestId(userId, testId);
            if (assignment != null) {
                daoFactory.getTestAssignmentDAO().updateStatus(assignment.getId(), AssignmentStatus.COMPLETED);
            }
            
            return toDTO(result);
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to submit test", e);
        }
    }

    /**
     * Gets results for a user.
     *
     * @param userId the user ID
     * @return list of TestResultDTOs
     * @throws ResultServiceException if retrieval fails
     */
    public List<TestResultDTO> getUserResults(Long userId) throws ResultServiceException {
        try {
            List<TestResult> results = daoFactory.getTestResultDAO().findByUserId(userId);
            List<TestResultDTO> dtos = new ArrayList<>();
            for (TestResult result : results) {
                dtos.add(toDTO(result));
            }
            return dtos;
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to get user results", e);
        }
    }

    /**
     * Gets results for a test.
     *
     * @param testId the test ID
     * @return list of TestResultDTOs
     * @throws ResultServiceException if retrieval fails
     */
    public List<TestResultDTO> getTestResults(Long testId) throws ResultServiceException {
        try {
            List<TestResult> results = daoFactory.getTestResultDAO().findByTestId(testId);
            List<TestResultDTO> dtos = new ArrayList<>();
            for (TestResult result : results) {
                dtos.add(toDTO(result));
            }
            return dtos;
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to get test results", e);
        }
    }

    /**
     * Gets all results.
     *
     * @return list of all TestResultDTOs
     * @throws ResultServiceException if retrieval fails
     */
    public List<TestResultDTO> getAllResults() throws ResultServiceException {
        try {
            List<TestResult> results = daoFactory.getTestResultDAO().findAll();
            List<TestResultDTO> dtos = new ArrayList<>();
            for (TestResult result : results) {
                dtos.add(toDTO(result));
            }
            return dtos;
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to get all results", e);
        }
    }

    /**
     * Gets a result by ID.
     *
     * @param resultId the result ID
     * @return the TestResultDTO
     * @throws ResultServiceException if not found
     */
    public TestResultDTO getResultById(Long resultId) throws ResultServiceException {
        try {
            Optional<TestResult> resultOpt = daoFactory.getTestResultDAO().findById(resultId);
            if (resultOpt.isEmpty()) {
                throw new ResultServiceException("Result not found");
            }
            return toDTO(resultOpt.get());
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to get result", e);
        }
    }

    /**
     * Converts TestResult entity to DTO with user and test names.
     */
    private TestResultDTO toDTO(TestResult result) throws ResultServiceException {
        try {
            TestResultDTO dto = new TestResultDTO();
            dto.setId(result.getId());
            dto.setUserId(result.getUserId());
            dto.setTestId(result.getTestId());
            dto.setStartTime(result.getStartTime());
            dto.setEndTime(result.getEndTime());
            dto.setScorePercent(result.getScorePercent());
            dto.setIsPassed(result.getIsPassed());
            dto.setAttemptsUsed(result.getAttemptsUsed());
            
            // Get user name
            Optional<User> userOpt = daoFactory.getUserDAO().findById(result.getUserId());
            if (userOpt.isPresent()) {
                dto.setUserName(userOpt.get().getFullName());
            }
            
            // Get test name
            Optional<Test> testOpt = daoFactory.getTestDAO().findById(result.getTestId());
            if (testOpt.isPresent()) {
                dto.setTestName(testOpt.get().getTitle());
            }
            
            return dto;
        } catch (SQLException e) {
            throw new ResultServiceException("Failed to convert result to DTO", e);
        }
    }

    /**
     * Converts answers to JSON string.
     */
    private String answersToJson(List<UserAnswer> answers) {
        StringBuilder json = new StringBuilder("{\"answers\":{");
        for (int i = 0; i < answers.size(); i++) {
            UserAnswer answer = answers.get(i);
            json.append("\"").append(answer.getQuestionId()).append("\":[");
            for (int j = 0; j < answer.getSelectedOptionIds().size(); j++) {
                json.append(answer.getSelectedOptionIds().get(j));
                if (j < answer.getSelectedOptionIds().size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");
            if (i < answers.size() - 1) {
                json.append(",");
            }
        }
        json.append("}}");
        return json.toString();
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

    /**
     * Custom exception for result service errors.
     */
    public static class ResultServiceException extends Exception {
        public ResultServiceException(String message) {
            super(message);
        }

        public ResultServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
