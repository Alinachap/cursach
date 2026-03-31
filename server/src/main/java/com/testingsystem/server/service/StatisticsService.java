package com.testingsystem.server.service;

import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.model.Test;
import com.testingsystem.server.model.TestResult;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for statistics and reporting.
 * Provides analytics on tests, users, and results.
 */
public class StatisticsService {

    private final DAOFactory daoFactory;

    /**
     * Constructs a StatisticsService with the given DAO factory.
     *
     * @param daoFactory the DAO factory
     */
    public StatisticsService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Gets overall statistics.
     *
     * @return map containing overall statistics
     * @throws StatisticsException if retrieval fails
     */
    public Map<String, Object> getOverallStatistics() throws StatisticsException {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            stats.put("totalUsers", daoFactory.getUserDAO().count());
            stats.put("totalTests", daoFactory.getTestDAO().count());
            stats.put("totalQuestions", daoFactory.getQuestionDAO().count());
            stats.put("totalAssignments", daoFactory.getTestAssignmentDAO().count());
            stats.put("totalResults", daoFactory.getTestResultDAO().count());
            
            // Calculate pass rate
            List<TestResult> allResults = daoFactory.getTestResultDAO().findAll();
            long passedCount = allResults.stream().filter(r -> Boolean.TRUE.equals(r.getIsPassed())).count();
            double passRate = allResults.isEmpty() ? 0.0 : 
                    (double) passedCount / allResults.size() * 100;
            stats.put("overallPassRate", String.format("%.2f", passRate) + "%");
            
            return stats;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get overall statistics", e);
        }
    }

    /**
     * Gets statistics for a specific test.
     *
     * @param testId the test ID
     * @return map containing test statistics
     * @throws StatisticsException if retrieval fails
     */
    public Map<String, Object> getTestStatistics(Long testId) throws StatisticsException {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Get test info
            Optional<Test> testOpt = daoFactory.getTestDAO().findById(testId);
            if (testOpt.isEmpty()) {
                throw new StatisticsException("Test not found");
            }
            Test test = testOpt.get();
            
            stats.put("testId", testId);
            stats.put("testTitle", test.getTitle());
            stats.put("passingScore", test.getPassingScore());
            stats.put("timeLimit", test.getTimeLimit());
            
            // Get results
            List<TestResult> results = daoFactory.getTestResultDAO().findByTestId(testId);
            
            stats.put("totalAttempts", results.size());
            
            long passedCount = results.stream().filter(r -> Boolean.TRUE.equals(r.getIsPassed())).count();
            stats.put("passedCount", passedCount);
            stats.put("failedCount", results.size() - passedCount);
            
            double passRate = results.isEmpty() ? 0.0 : 
                    (double) passedCount / results.size() * 100;
            stats.put("passRate", String.format("%.2f", passRate) + "%");
            
            // Calculate average score
            if (!results.isEmpty()) {
                BigDecimal avgScore = results.stream()
                        .filter(r -> r.getScorePercent() != null)
                        .map(TestResult::getScorePercent)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(results.size()), 2, RoundingMode.HALF_UP);
                stats.put("averageScore", avgScore);
            }
            
            // Get unique users count
            Set<Long> uniqueUsers = new HashSet<>();
            for (TestResult result : results) {
                uniqueUsers.add(result.getUserId());
            }
            stats.put("uniqueUsers", uniqueUsers.size());
            
            return stats;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get test statistics", e);
        }
    }

    /**
     * Gets statistics for a specific user.
     *
     * @param userId the user ID
     * @return map containing user statistics
     * @throws StatisticsException if retrieval fails
     */
    public Map<String, Object> getUserStatistics(Long userId) throws StatisticsException {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // Get user info
            Optional<com.testingsystem.server.model.User> userOpt = 
                    daoFactory.getUserDAO().findById(userId);
            if (userOpt.isEmpty()) {
                throw new StatisticsException("User not found");
            }
            stats.put("userId", userId);
            stats.put("userName", userOpt.get().getFullName());
            
            // Get results
            List<TestResult> results = daoFactory.getTestResultDAO().findByUserId(userId);
            
            stats.put("totalTestsTaken", results.size());
            
            long passedCount = results.stream().filter(r -> Boolean.TRUE.equals(r.getIsPassed())).count();
            stats.put("testsPassed", passedCount);
            stats.put("testsFailed", results.size() - passedCount);
            
            double passRate = results.isEmpty() ? 0.0 : 
                    (double) passedCount / results.size() * 100;
            stats.put("passRate", String.format("%.2f", passRate) + "%");
            
            // Calculate average score
            if (!results.isEmpty()) {
                BigDecimal avgScore = results.stream()
                        .filter(r -> r.getScorePercent() != null)
                        .map(TestResult::getScorePercent)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(results.size()), 2, RoundingMode.HALF_UP);
                stats.put("averageScore", avgScore);
            }
            
            // Get assignments
            long assignedCount = daoFactory.getTestAssignmentDAO()
                    .findByUserId(userId).size();
            stats.put("testsAssigned", assignedCount);
            
            return stats;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get user statistics", e);
        }
    }

    /**
     * Gets question statistics (percentage of correct answers).
     *
     * @param testId the test ID
     * @return list of question statistics
     * @throws StatisticsException if retrieval fails
     */
    public List<Map<String, Object>> getQuestionStatistics(Long testId) throws StatisticsException {
        try {
            List<Map<String, Object>> questionStats = new ArrayList<>();
            
            // Get questions
            List<com.testingsystem.server.model.Question> questions = 
                    daoFactory.getQuestionDAO().findByTestId(testId);
            
            // Get all results for this test
            List<TestResult> results = daoFactory.getTestResultDAO().findByTestId(testId);
            
            for (com.testingsystem.server.model.Question question : questions) {
                Map<String, Object> qStats = new HashMap<>();
                qStats.put("questionId", question.getId());
                qStats.put("questionText", truncateText(question.getQuestionText(), 100));
                qStats.put("questionType", question.getQuestionType().getValue());
                
                // Count correct answers for this question
                int correctCount = 0;
                int totalAnswers = 0;
                
                for (TestResult result : results) {
                    // Parse answers and check if this question was answered correctly
                    // This is simplified - in production, you'd parse the answers_data JSON
                    totalAnswers++;
                    // For now, we'll estimate based on overall pass rate
                    if (Boolean.TRUE.equals(result.getIsPassed())) {
                        correctCount++;
                    }
                }
                
                double correctPercent = totalAnswers == 0 ? 0.0 : 
                        (double) correctCount / totalAnswers * 100;
                qStats.put("correctPercent", String.format("%.2f", correctPercent) + "%");
                qStats.put("totalAnswers", totalAnswers);
                
                questionStats.add(qStats);
            }
            
            return questionStats;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get question statistics", e);
        }
    }

    /**
     * Gets results for a date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of results in the date range
     * @throws StatisticsException if retrieval fails
     */
    public List<Map<String, Object>> getResultsByDateRange(LocalDate startDate, LocalDate endDate) 
            throws StatisticsException {
        try {
            List<Map<String, Object>> resultsData = new ArrayList<>();
            
            List<TestResult> allResults = daoFactory.getTestResultDAO().findAll();
            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
            
            for (TestResult result : allResults) {
                if (result.getStartTime() != null && 
                    !result.getStartTime().isBefore(startDateTime) && 
                    !result.getStartTime().isAfter(endDateTime)) {
                    
                    Map<String, Object> rData = new HashMap<>();
                    rData.put("resultId", result.getId());
                    rData.put("userId", result.getUserId());
                    rData.put("testId", result.getTestId());
                    rData.put("startTime", result.getStartTime());
                    rData.put("scorePercent", result.getScorePercent());
                    rData.put("isPassed", result.getIsPassed());
                    
                    // Get user name
                    Optional<com.testingsystem.server.model.User> userOpt = 
                            daoFactory.getUserDAO().findById(result.getUserId());
                    if (userOpt.isPresent()) {
                        rData.put("userName", userOpt.get().getFullName());
                    }
                    
                    // Get test name
                    Optional<Test> testOpt = daoFactory.getTestDAO().findById(result.getTestId());
                    if (testOpt.isPresent()) {
                        rData.put("testName", testOpt.get().getTitle());
                    }
                    
                    resultsData.add(rData);
                }
            }
            
            return resultsData;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get results by date range", e);
        }
    }

    /**
     * Gets top performers for a test.
     *
     * @param testId the test ID
     * @param limit maximum number of results
     * @return list of top performers
     * @throws StatisticsException if retrieval fails
     */
    public List<Map<String, Object>> getTopPerformers(Long testId, int limit) throws StatisticsException {
        try {
            List<Map<String, Object>> topPerformers = new ArrayList<>();
            
            List<TestResult> results = daoFactory.getTestResultDAO().findByTestId(testId);
            
            // Sort by score descending
            results.sort((a, b) -> {
                if (a.getScorePercent() == null) return 1;
                if (b.getScorePercent() == null) return -1;
                return b.getScorePercent().compareTo(a.getScorePercent());
            });
            
            int count = 0;
            Set<Long> addedUsers = new HashSet<>();
            
            for (TestResult result : results) {
                if (count >= limit) break;
                if (addedUsers.contains(result.getUserId())) continue;
                
                Map<String, Object> performer = new HashMap<>();
                performer.put("userId", result.getUserId());
                performer.put("scorePercent", result.getScorePercent());
                performer.put("isPassed", result.getIsPassed());
                
                Optional<com.testingsystem.server.model.User> userOpt = 
                        daoFactory.getUserDAO().findById(result.getUserId());
                if (userOpt.isPresent()) {
                    performer.put("userName", userOpt.get().getFullName());
                }
                
                topPerformers.add(performer);
                addedUsers.add(result.getUserId());
                count++;
            }
            
            return topPerformers;
        } catch (SQLException e) {
            throw new StatisticsException("Failed to get top performers", e);
        }
    }

    /**
     * Truncates text to specified length.
     */
    private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "...";
    }

    /**
     * Custom exception for statistics service errors.
     */
    public static class StatisticsException extends Exception {
        public StatisticsException(String message) {
            super(message);
        }

        public StatisticsException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
