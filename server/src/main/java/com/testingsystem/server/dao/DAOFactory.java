package com.testingsystem.server.dao;

import com.testingsystem.server.dao.impl.*;
import com.testingsystem.server.db.ConnectionPool;

public class DAOFactory {

    private final ConnectionPool connectionPool;

    private UserDAO userDAO;
    private TestDAO testDAO;
    private QuestionDAO questionDAO;
    private AnswerOptionDAO answerOptionDAO;
    private TestAssignmentDAO testAssignmentDAO;
    private TestResultDAO testResultDAO;

    public DAOFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAOImpl(connectionPool);
        }
        return userDAO;
    }

    public TestDAO getTestDAO() {
        if (testDAO == null) {
            testDAO = new TestDAOImpl(connectionPool);
        }
        return testDAO;
    }

    public QuestionDAO getQuestionDAO() {
        if (questionDAO == null) {
            questionDAO = new QuestionDAOImpl(connectionPool);
        }
        return questionDAO;
    }

    public AnswerOptionDAO getAnswerOptionDAO() {
        if (answerOptionDAO == null) {
            answerOptionDAO = new AnswerOptionDAOImpl(connectionPool);
        }
        return answerOptionDAO;
    }

    public TestAssignmentDAO getTestAssignmentDAO() {
        if (testAssignmentDAO == null) {
            testAssignmentDAO = new TestAssignmentDAOImpl(connectionPool);
        }
        return testAssignmentDAO;
    }

    public TestResultDAO getTestResultDAO() {
        if (testResultDAO == null) {
            testResultDAO = new TestResultDAOImpl(connectionPool);
        }
        return testResultDAO;
    }

    public void close() {
        userDAO = null;
        testDAO = null;
        questionDAO = null;
        answerOptionDAO = null;
        testAssignmentDAO = null;
        testResultDAO = null;
    }
}
