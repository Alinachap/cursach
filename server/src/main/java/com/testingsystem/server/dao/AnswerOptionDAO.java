package com.testingsystem.server.dao;

import com.testingsystem.server.model.AnswerOption;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object for AnswerOption entities.
 */
public interface AnswerOptionDAO extends GenericDAO<AnswerOption, Long> {

    /**
     * Finds answer options by question ID.
     *
     * @param questionId the question ID
     * @return list of answer options
     * @throws SQLException if a database error occurs
     */
    List<AnswerOption> findByQuestionId(Long questionId) throws SQLException;

    /**
     * Finds answer options by question ID ordered by order_num.
     *
     * @param questionId the question ID
     * @return list of answer options ordered
     * @throws SQLException if a database error occurs
     */
    List<AnswerOption> findByQuestionIdOrdered(Long questionId) throws SQLException;

    /**
     * Gets the maximum order number for a question.
     *
     * @param questionId the question ID
     * @return the maximum order number
     * @throws SQLException if a database error occurs
     */
    int getMaxOrderNum(Long questionId) throws SQLException;

    /**
     * Deletes all answer options for a question.
     *
     * @param questionId the question ID
     * @return number of deleted options
     * @throws SQLException if a database error occurs
     */
    int deleteByQuestionId(Long questionId) throws SQLException;
}
