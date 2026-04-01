package com.testingsystem.server.dao;

import com.testingsystem.server.model.TestResult;

import java.sql.SQLException;
import java.util.List;

public interface TestResultDAO extends GenericDAO<TestResult, Long> {

    List<TestResult> findByUserId(Long userId) throws SQLException;

    List<TestResult> findByTestId(Long testId) throws SQLException;

    List<TestResult> findByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    TestResult findLatestByUserIdAndTestId(Long userId, Long testId) throws SQLException;

    List<TestResult> findPassedByUserId(Long userId) throws SQLException;

    int countAttemptsByUserIdAndTestId(Long userId, Long testId) throws SQLException;
}
