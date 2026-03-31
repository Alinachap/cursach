package com.testingsystem.server.dao;

import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.server.model.TestAssignment;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for TestAssignment entities.
 */
public interface TestAssignmentDAO extends GenericDAO<TestAssignment, Long> {

    /**
     * Finds assignments by user ID.
     *
     * @param userId the user ID
     * @return list of assignments for the user
     * @throws SQLException if a database error occurs
     */
    List<TestAssignment> findByUserId(Long userId) throws SQLException;

    /**
     * Finds assignments by test ID.
     *
     * @param testId the test ID
     * @return list of assignments for the test
     * @throws SQLException if a database error occurs
     */
    List<TestAssignment> findByTestId(Long testId) throws SQLException;

    /**
     * Finds assignment by user ID and test ID.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @return the assignment if found
     * @throws SQLException if a database error occurs
     */
    TestAssignment findByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    /**
     * Finds assignments by status.
     *
     * @param status the assignment status
     * @return list of assignments with the status
     * @throws SQLException if a database error occurs
     */
    List<TestAssignment> findByStatus(AssignmentStatus status) throws SQLException;

    /**
     * Finds assignments by user ID and status.
     *
     * @param userId the user ID
     * @param status the assignment status
     * @return list of assignments
     * @throws SQLException if a database error occurs
     */
    List<TestAssignment> findByUserIdAndStatus(Long userId, AssignmentStatus status) throws SQLException;

    /**
     * Updates assignment status.
     *
     * @param assignmentId the assignment ID
     * @param status the new status
     * @return true if updated
     * @throws SQLException if a database error occurs
     */
    boolean updateStatus(Long assignmentId, AssignmentStatus status) throws SQLException;

    /**
     * Decrements attempts left for an assignment.
     *
     * @param assignmentId the assignment ID
     * @return true if updated
     * @throws SQLException if a database error occurs
     */
    boolean decrementAttempts(Long assignmentId) throws SQLException;
}
