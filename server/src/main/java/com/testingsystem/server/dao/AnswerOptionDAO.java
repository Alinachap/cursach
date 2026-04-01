package com.testingsystem.server.dao;

import com.testingsystem.server.model.AnswerOption;

import java.sql.SQLException;
import java.util.List;

public interface AnswerOptionDAO extends GenericDAO<AnswerOption, Long> {

    List<AnswerOption> findByQuestionId(Long questionId) throws SQLException;

    List<AnswerOption> findByQuestionIdOrdered(Long questionId) throws SQLException;

    int getMaxOrderNum(Long questionId) throws SQLException;

    int deleteByQuestionId(Long questionId) throws SQLException;
}
