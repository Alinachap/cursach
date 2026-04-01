package com.testingsystem.server.dao.impl;

import com.testingsystem.server.dao.TestDAO;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.model.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestDAOImpl implements TestDAO {

    private final ConnectionPool connectionPool;

    public TestDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Test save(Test test) throws SQLException {
        String sql = "INSERT INTO tests (title, description, time_limit, passing_score, is_active) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, test.getTitle());
            stmt.setString(2, test.getDescription());
            stmt.setInt(3, test.getTimeLimit());
            stmt.setInt(4, test.getPassingScore());
            stmt.setBoolean(5, test.isActive());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating test failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    test.setId(rs.getLong(1));
                    test.setCreatedAt(LocalDateTime.now());
                }
            }
            
            return test;
        }
    }

    @Override
    public Test update(Test test) throws SQLException {
        String sql = "UPDATE tests SET title = ?, description = ?, time_limit = ?, passing_score = ?, is_active = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, test.getTitle());
            stmt.setString(2, test.getDescription());
            stmt.setInt(3, test.getTimeLimit());
            stmt.setInt(4, test.getPassingScore());
            stmt.setBoolean(5, test.isActive());
            stmt.setLong(6, test.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating test failed, no rows affected.");
            }
            
            return test;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM tests WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Test> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM tests WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return executeQuery(stmt);
        }
    }

    @Override
    public List<Test> findAll() throws SQLException {
        String sql = "SELECT * FROM tests ORDER BY id";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tests";
        
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
        String sql = "SELECT 1 FROM tests WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Test> findActiveTests() throws SQLException {
        String sql = "SELECT * FROM tests WHERE is_active = TRUE ORDER BY id";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<Test> findByTitleContaining(String titlePart) throws SQLException {
        String sql = "SELECT * FROM tests WHERE title ILIKE ? ORDER BY id";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titlePart + "%");
            return executeQueryList(stmt);
        }
    }

    @Override
    public boolean updateActiveStatus(Long testId, boolean isActive) throws SQLException {
        String sql = "UPDATE tests SET is_active = ? WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBoolean(1, isActive);
            stmt.setLong(2, testId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Optional<Test> executeQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToTest(rs));
            }
            return Optional.empty();
        }
    }

    private List<Test> executeQueryList(PreparedStatement stmt) throws SQLException {
        List<Test> tests = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tests.add(mapResultSetToTest(rs));
            }
        }
        return tests;
    }

    private Test mapResultSetToTest(ResultSet rs) throws SQLException {
        Test test = new Test();
        test.setId(rs.getLong("id"));
        test.setTitle(rs.getString("title"));
        test.setDescription(rs.getString("description"));
        test.setTimeLimit(rs.getInt("time_limit"));
        test.setPassingScore(rs.getInt("passing_score"));
        test.setActive(rs.getBoolean("is_active"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        test.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);

        return test;
    }
}
