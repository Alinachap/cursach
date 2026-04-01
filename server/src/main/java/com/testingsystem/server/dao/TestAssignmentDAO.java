package com.testingsystem.server.dao;

import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.server.model.TestAssignment;

import java.sql.SQLException;
import java.util.List;

public interface TestAssignmentDAO extends GenericDAO<TestAssignment, Long> {

    List<TestAssignment> findByUserId(Long userId) throws SQLException;

    List<TestAssignment> findByTestId(Long testId) throws SQLException;

    TestAssignment findByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    List<TestAssignment> findByStatus(AssignmentStatus status) throws SQLException;

    List<TestAssignment> findByUserIdAndStatus(Long userId, AssignmentStatus status) throws SQLException;

    boolean updateStatus(Long assignmentId, AssignmentStatus status) throws SQLException;

    boolean decrementAttempts(Long assignmentId) throws SQLException;
}
