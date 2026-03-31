package com.testingsystem.server.dao;

import com.testingsystem.server.model.TestResult;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for TestResult entities.
 */
public interface TestResultDAO extends GenericDAO<TestResult, Long> {

    /**
     * Finds results by user ID.
     *
     * @param userId the user ID
     * @return list of results for the user
     * @throws SQLException if a database error occurs
     */
    List<TestResult> findByUserId(Long userId) throws SQLException;

    /**
     * Finds results by test ID.
     *
     * @param testId the test ID
     * @return list of results for the test
     * @throws SQLException if a database error occurs
     */
    List<TestResult> findByTestId(Long testId) throws SQLException;

    /**
     * Finds results by user ID and test ID.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @return list of results
     * @throws SQLException if a database error occurs
     */
    List<TestResult> findByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    /**
     * Finds the latest result for a user and test.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @return the latest result if found
     * @throws SQLException if a database error occurs
     */
    TestResult findLatestByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    /**
     * Finds passed results by user ID.
     *
     * @param userId the user ID
     * @return list of passed results
     * @throws SQLException if a database error occurs
     */
    List<TestResult> findPassedByUserId(Long userId) throws SQLException;

    /**
     * Counts attempts by user ID and test ID.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @return the number of attempts
     * @throws SQLException if a database error occurs
     */
    int countAttemptsByUserIdAndTestId(Long userId, Long testId) throws SQLException;
}
