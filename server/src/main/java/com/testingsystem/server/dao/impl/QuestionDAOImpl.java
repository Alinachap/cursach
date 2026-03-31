package com.testingsystem.server.dao.impl;

import com.testingsystem.common.enums.QuestionType;
import com.testingsystem.server.dao.QuestionDAO;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of QuestionDAO using JDBC.
 */
public class QuestionDAOImpl implements QuestionDAO {

    private final ConnectionPool connectionPool;

    public QuestionDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Question save(Question question) throws SQLException {
        String sql = "INSERT INTO questions (test_id, question_text, question_type, order_num) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, question.getTestId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getQuestionType().getValue());
            stmt.setInt(4, question.getOrderNum());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating question failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    question.setId(rs.getLong(1));
                }
            }
            
            return question;
        }
    }

    @Override
    public Question update(Question question) throws SQLException {
        String sql = "UPDATE questions SET question_text = ?, question_type = ?, order_num = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getQuestionType().getValue());
            stmt.setInt(3, question.getOrderNum());
            stmt.setLong(4, question.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating question failed, no rows affected.");
            }
            
            return question;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Question> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return executeQuery(stmt);
        }
    }

    @Override
    public List<Question> findAll() throws SQLException {
        String sql = "SELECT * FROM questions ORDER BY test_id, order_num";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM questions";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            return 0;
        }
    }

    @Override
    public boolean exists(Long id) throws SQLException {
        String sql = "SELECT 1 FROM questions WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Question> findByTestId(Long testId) throws SQLException {
        String sql = "SELECT * FROM questions WHERE test_id = ? ORDER BY order_num";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, testId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<Question> findByTestIdOrdered(Long testId) throws SQLException {
        return findByTestId(testId);
    }

    @Override
    public int getMaxOrderNum(Long testId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(order_num), 0) FROM questions WHERE test_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    @Override
    public boolean updateOrderNum(Long questionId, int orderNum) throws SQLException {
        String sql = "UPDATE questions SET order_num = ? WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderNum);
            stmt.setLong(2, questionId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public int deleteByTestId(Long testId) throws SQLException {
        String sql = "DELETE FROM questions WHERE test_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, testId);
            return stmt.executeUpdate();
        }
    }

    private Optional<Question> executeQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToQuestion(rs));
            }
            return Optional.empty();
        }
    }

    private List<Question> executeQueryList(PreparedStatement stmt) throws SQLException {
        List<Question> questions = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        }
        return questions;
    }

    private Question mapResultSetToQuestion(ResultSet rs) throws SQLException {
        QuestionType type = QuestionType.fromValue(rs.getString("question_type"));
        Question question;
        
        if (type == QuestionType.SINGLE) {
            question = new SingleChoiceQuestion();
        } else {
            question = new MultipleChoiceQuestion();
        }
        
        question.setId(rs.getLong("id"));
        question.setTestId(rs.getLong("test_id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setQuestionType(type);
        question.setOrderNum(rs.getInt("order_num"));
        
        return question;
    }
}
