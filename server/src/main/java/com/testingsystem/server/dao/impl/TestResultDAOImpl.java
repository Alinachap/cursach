package com.testingsystem.server.dao.impl;

import com.testingsystem.server.dao.TestResultDAO;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.model.TestResult;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TestResultDAO using JDBC.
 */
public class TestResultDAOImpl implements TestResultDAO {

    private final ConnectionPool connectionPool;

    public TestResultDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public TestResult save(TestResult result) throws SQLException {
        String sql = "INSERT INTO test_results (user_id, test_id, start_time, end_time, score_percent, is_passed, attempts_used, answers_data) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, result.getUserId());
            stmt.setLong(2, result.getTestId());
            stmt.setObject(3, result.getStartTime());
            stmt.setObject(4, result.getEndTime());
            stmt.setBigDecimal(5, result.getScorePercent());
            stmt.setBoolean(6, result.getIsPassed() != null ? result.getIsPassed() : false);
            stmt.setInt(7, result.getAttemptsUsed() != null ? result.getAttemptsUsed() : 1);
            stmt.setString(8, result.getAnswersData());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating test result failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    result.setId(rs.getLong(1));
                }
            }
            
            return result;
        }
    }

    @Override
    public TestResult update(TestResult result) throws SQLException {
        String sql = "UPDATE test_results SET end_time = ?, score_percent = ?, is_passed = ?, attempts_used = ?, answers_data = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, result.getEndTime());
            stmt.setBigDecimal(2, result.getScorePercent());
            stmt.setBoolean(3, result.getIsPassed() != null ? result.getIsPassed() : false);
            stmt.setInt(4, result.getAttemptsUsed() != null ? result.getAttemptsUsed() : 1);
            stmt.setString(5, result.getAnswersData());
            stmt.setLong(6, result.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating test result failed, no rows affected.");
            }
            
            return result;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM test_results WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<TestResult> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return executeQuery(stmt);
        }
    }

    @Override
    public List<TestResult> findAll() throws SQLException {
        String sql = "SELECT * FROM test_results ORDER BY start_time DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM test_results";
        
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
        String sql = "SELECT 1 FROM test_results WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<TestResult> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE user_id = ? ORDER BY start_time DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<TestResult> findByTestId(Long testId) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE test_id = ? ORDER BY start_time DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, testId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<TestResult> findByUserIdAndTestId(Long userId, Long testId) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE user_id = ? AND test_id = ? ORDER BY start_time DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, testId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public TestResult findLatestByUserIdAndTestId(Long userId, Long testId) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE user_id = ? AND test_id = ? ORDER BY start_time DESC LIMIT 1";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToResult(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<TestResult> findPassedByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM test_results WHERE user_id = ? AND is_passed = TRUE ORDER BY start_time DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public int countAttemptsByUserIdAndTestId(Long userId, Long testId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM test_results WHERE user_id = ? AND test_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    private Optional<TestResult> executeQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToResult(rs));
            }
            return Optional.empty();
        }
    }

    private List<TestResult> executeQueryList(PreparedStatement stmt) throws SQLException {
        List<TestResult> results = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(mapResultSetToResult(rs));
            }
        }
        return results;
    }

    private TestResult mapResultSetToResult(ResultSet rs) throws SQLException {
        TestResult result = new TestResult();
        result.setId(rs.getLong("id"));
        result.setUserId(rs.getLong("user_id"));
        result.setTestId(rs.getLong("test_id"));
        
        Timestamp startTime = rs.getTimestamp("start_time");
        result.setStartTime(startTime != null ? startTime.toLocalDateTime() : null);
        
        Timestamp endTime = rs.getTimestamp("end_time");
        result.setEndTime(endTime != null ? endTime.toLocalDateTime() : null);
        
        result.setScorePercent(rs.getBigDecimal("score_percent"));
        result.setIsPassed(rs.getBoolean("is_passed"));
        result.setAttemptsUsed(rs.getInt("attempts_used"));
        result.setAnswersData(rs.getString("answers_data"));
        
        return result;
    }
}
