package com.testingsystem.server.dao.impl;

import com.testingsystem.server.dao.AnswerOptionDAO;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.model.AnswerOption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of AnswerOptionDAO using JDBC.
 */
public class AnswerOptionDAOImpl implements AnswerOptionDAO {

    private final ConnectionPool connectionPool;

    public AnswerOptionDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public AnswerOption save(AnswerOption option) throws SQLException {
        String sql = "INSERT INTO answer_options (question_id, option_text, is_correct, order_num) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, option.getQuestionId());
            stmt.setString(2, option.getOptionText());
            stmt.setBoolean(3, option.isCorrect());
            stmt.setInt(4, option.getOrderNum());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating answer option failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    option.setId(rs.getLong(1));
                }
            }
            
            return option;
        }
    }

    @Override
    public AnswerOption update(AnswerOption option) throws SQLException {
        String sql = "UPDATE answer_options SET option_text = ?, is_correct = ?, order_num = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, option.getOptionText());
            stmt.setBoolean(2, option.isCorrect());
            stmt.setInt(3, option.getOrderNum());
            stmt.setLong(4, option.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating answer option failed, no rows affected.");
            }
            
            return option;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM answer_options WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<AnswerOption> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM answer_options WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return executeQuery(stmt);
        }
    }

    @Override
    public List<AnswerOption> findAll() throws SQLException {
        String sql = "SELECT * FROM answer_options ORDER BY question_id, order_num";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM answer_options";
        
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
        String sql = "SELECT 1 FROM answer_options WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<AnswerOption> findByQuestionId(Long questionId) throws SQLException {
        String sql = "SELECT * FROM answer_options WHERE question_id = ? ORDER BY order_num";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, questionId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<AnswerOption> findByQuestionIdOrdered(Long questionId) throws SQLException {
        return findByQuestionId(questionId);
    }

    @Override
    public int getMaxOrderNum(Long questionId) throws SQLException {
        String sql = "SELECT COALESCE(MAX(order_num), 0) FROM answer_options WHERE question_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    @Override
    public int deleteByQuestionId(Long questionId) throws SQLException {
        String sql = "DELETE FROM answer_options WHERE question_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, questionId);
            return stmt.executeUpdate();
        }
    }

    private Optional<AnswerOption> executeQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToAnswerOption(rs));
            }
            return Optional.empty();
        }
    }

    private List<AnswerOption> executeQueryList(PreparedStatement stmt) throws SQLException {
        List<AnswerOption> options = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                options.add(mapResultSetToAnswerOption(rs));
            }
        }
        return options;
    }

    private AnswerOption mapResultSetToAnswerOption(ResultSet rs) throws SQLException {
        AnswerOption option = new AnswerOption();
        option.setId(rs.getLong("id"));
        option.setQuestionId(rs.getLong("question_id"));
        option.setOptionText(rs.getString("option_text"));
        option.setCorrect(rs.getBoolean("is_correct"));
        option.setOrderNum(rs.getInt("order_num"));
        
        return option;
    }
}
