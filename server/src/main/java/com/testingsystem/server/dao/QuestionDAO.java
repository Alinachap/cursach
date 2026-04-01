package com.testingsystem.server.dao;

import com.testingsystem.server.model.Question;

import java.sql.SQLException;
import java.util.List;

public interface QuestionDAO extends GenericDAO<Question, Long> {

    List<Question> findByTestId(Long testId) throws SQLException;

    List<Question> findByTestIdOrdered(Long testId) throws SQLException;

    int getMaxOrderNum(Long testId) throws SQLException;

    boolean updateOrderNum(Long questionId, int orderNum) throws SQLException;

    int deleteByTestId(Long testId) throws SQLException;
}
