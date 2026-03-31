package com.testingsystem.server.dao;

import com.testingsystem.server.model.Question;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for Question entities.
 */
public interface QuestionDAO extends GenericDAO<Question, Long> {

    /**
     * Finds questions by test ID.
     *
     * @param testId the test ID
     * @return list of questions for the test
     * @throws SQLException if a database error occurs
     */
    List<Question> findByTestId(Long testId) throws SQLException;

    /**
     * Finds questions by test ID ordered by order_num.
     *
     * @param testId the test ID
     * @return list of questions ordered
     * @throws SQLException if a database error occurs
     */
    List<Question> findByTestIdOrdered(Long testId) throws SQLException;

    /**
     * Gets the maximum order number for a test.
     *
     * @param testId the test ID
     * @return the maximum order number
     * @throws SQLException if a database error occurs
     */
    int getMaxOrderNum(Long testId) throws SQLException;

    /**
     * Updates the order number of a question.
     *
     * @param questionId the question ID
     * @param orderNum the new order number
     * @return true if updated
     * @throws SQLException if a database error occurs
     */
    boolean updateOrderNum(Long questionId, int orderNum) throws SQLException;

    /**
     * Deletes all questions for a test.
     *
     * @param testId the test ID
     * @return number of deleted questions
     * @throws SQLException if a database error occurs
     */
    int deleteByTestId(Long testId) throws SQLException;
}
