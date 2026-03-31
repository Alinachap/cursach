package com.testingsystem.server.dao;

import com.testingsystem.server.dao.impl.*;
import com.testingsystem.server.db.ConnectionPool;

/**
 * Factory for creating DAO instances.
 * Implements the Factory Method pattern.
 * Provides a centralized way to create DAO objects.
 */
public class DAOFactory {

    private final ConnectionPool connectionPool;

    private UserDAO userDAO;
    private TestDAO testDAO;
    private QuestionDAO questionDAO;
    private AnswerOptionDAO answerOptionDAO;
    private TestAssignmentDAO testAssignmentDAO;
    private TestResultDAO testResultDAO;

    /**
     * Constructs a DAOFactory with the given connection pool.
     *
     * @param connectionPool the database connection pool
     */
    public DAOFactory(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Gets the UserDAO instance (lazy initialization).
     *
     * @return the UserDAO instance
     */
    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAOImpl(connectionPool);
        }
        return userDAO;
    }

    /**
     * Gets the TestDAO instance (lazy initialization).
     *
     * @return the TestDAO instance
     */
    public TestDAO getTestDAO() {
        if (testDAO == null) {
            testDAO = new TestDAOImpl(connectionPool);
        }
        return testDAO;
    }

    /**
     * Gets the QuestionDAO instance (lazy initialization).
     *
     * @return the QuestionDAO instance
     */
    public QuestionDAO getQuestionDAO() {
        if (questionDAO == null) {
            questionDAO = new QuestionDAOImpl(connectionPool);
        }
        return questionDAO;
    }

    /**
     * Gets the AnswerOptionDAO instance (lazy initialization).
     *
     * @return the AnswerOptionDAO instance
     */
    public AnswerOptionDAO getAnswerOptionDAO() {
        if (answerOptionDAO == null) {
            answerOptionDAO = new AnswerOptionDAOImpl(connectionPool);
        }
        return answerOptionDAO;
    }

    /**
     * Gets the TestAssignmentDAO instance (lazy initialization).
     *
     * @return the TestAssignmentDAO instance
     */
    public TestAssignmentDAO getTestAssignmentDAO() {
        if (testAssignmentDAO == null) {
            testAssignmentDAO = new TestAssignmentDAOImpl(connectionPool);
        }
        return testAssignmentDAO;
    }

    /**
     * Gets the TestResultDAO instance (lazy initialization).
     *
     * @return the TestResultDAO instance
     */
    public TestResultDAO getTestResultDAO() {
        if (testResultDAO == null) {
            testResultDAO = new TestResultDAOImpl(connectionPool);
        }
        return testResultDAO;
    }

    /**
     * Closes all DAO resources.
     */
    public void close() {
        userDAO = null;
        testDAO = null;
        questionDAO = null;
        answerOptionDAO = null;
        testAssignmentDAO = null;
        testResultDAO = null;
    }
}
