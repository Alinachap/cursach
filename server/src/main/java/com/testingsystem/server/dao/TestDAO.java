package com.testingsystem.server.dao;

import com.testingsystem.server.model.Test;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for Test entities.
 * Provides database operations for test management.
 */
public interface TestDAO extends GenericDAO<Test, Long> {

    /**
     * Finds active tests.
     *
     * @return list of active tests
     * @throws SQLException if a database error occurs
     */
    List<Test> findActiveTests() throws SQLException;

    /**
     * Finds tests by title containing the given text.
     *
     * @param titlePart the text to search for in title
     * @return list of matching tests
     * @throws SQLException if a database error occurs
     */
    List<Test> findByTitleContaining(String titlePart) throws SQLException;

    /**
     * Updates test active status.
     *
     * @param testId the test ID
     * @param isActive the new active status
     * @return true if updated successfully
     * @throws SQLException if a database error occurs
     */
    boolean updateActiveStatus(Long testId, boolean isActive) throws SQLException;
}
