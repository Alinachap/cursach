package com.testingsystem.server.dao;

import com.testingsystem.server.model.Test;

import java.sql.SQLException;
import java.util.List;

public interface TestDAO extends GenericDAO<Test, Long> {

    List<Test> findActiveTests() throws SQLException;

    List<Test> findByTitleContaining(String titlePart) throws SQLException;

    boolean updateActiveStatus(Long testId, boolean isActive) throws SQLException;
}
