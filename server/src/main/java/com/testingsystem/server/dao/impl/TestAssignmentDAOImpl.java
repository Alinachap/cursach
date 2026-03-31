package com.testingsystem.server.dao.impl;

import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.server.dao.TestAssignmentDAO;
import com.testingsystem.server.db.ConnectionPool;
import com.testingsystem.server.model.TestAssignment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TestAssignmentDAO using JDBC.
 */
public class TestAssignmentDAOImpl implements TestAssignmentDAO {

    private final ConnectionPool connectionPool;

    public TestAssignmentDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public TestAssignment save(TestAssignment assignment) throws SQLException {
        String sql = "INSERT INTO test_assignments (user_id, test_id, assigned_date, deadline, attempts_left, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, assignment.getUserId());
            stmt.setLong(2, assignment.getTestId());
            stmt.setObject(3, assignment.getAssignedDate());
            stmt.setObject(4, assignment.getDeadline());
            stmt.setInt(5, assignment.getAttemptsLeft() != null ? assignment.getAttemptsLeft() : 1);
            stmt.setString(6, assignment.getStatus() != null ? assignment.getStatus().getValue() : "assigned");
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating assignment failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    assignment.setId(rs.getLong(1));
                }
            }
            
            return assignment;
        }
    }

    @Override
    public TestAssignment update(TestAssignment assignment) throws SQLException {
        String sql = "UPDATE test_assignments SET deadline = ?, attempts_left = ?, status = ? WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, assignment.getDeadline());
            stmt.setInt(2, assignment.getAttemptsLeft() != null ? assignment.getAttemptsLeft() : 0);
            stmt.setString(3, assignment.getStatus() != null ? assignment.getStatus().getValue() : "assigned");
            stmt.setLong(4, assignment.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating assignment failed, no rows affected.");
            }
            
            return assignment;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM test_assignments WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<TestAssignment> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return executeQuery(stmt);
        }
    }

    @Override
    public List<TestAssignment> findAll() throws SQLException {
        String sql = "SELECT * FROM test_assignments ORDER BY id";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            return executeQueryList(stmt);
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM test_assignments";
        
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
        String sql = "SELECT 1 FROM test_assignments WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<TestAssignment> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE user_id = ? ORDER BY assigned_date DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<TestAssignment> findByTestId(Long testId) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE test_id = ? ORDER BY assigned_date DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, testId);
            return executeQueryList(stmt);
        }
    }

    @Override
    public TestAssignment findByUserIdAndTestId(Long userId, Long testId) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE user_id = ? AND test_id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setLong(2, testId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAssignment(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<TestAssignment> findByStatus(AssignmentStatus status) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE status = ? ORDER BY assigned_date DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.getValue());
            return executeQueryList(stmt);
        }
    }

    @Override
    public List<TestAssignment> findByUserIdAndStatus(Long userId, AssignmentStatus status) throws SQLException {
        String sql = "SELECT * FROM test_assignments WHERE user_id = ? AND status = ? ORDER BY assigned_date DESC";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            stmt.setString(2, status.getValue());
            return executeQueryList(stmt);
        }
    }

    @Override
    public boolean updateStatus(Long assignmentId, AssignmentStatus status) throws SQLException {
        String sql = "UPDATE test_assignments SET status = ? WHERE id = ?";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.getValue());
            stmt.setLong(2, assignmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean decrementAttempts(Long assignmentId) throws SQLException {
        String sql = "UPDATE test_assignments SET attempts_left = attempts_left - 1 WHERE id = ? AND attempts_left > 0";
        
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, assignmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Optional<TestAssignment> executeQuery(PreparedStatement stmt) throws SQLException {
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(mapResultSetToAssignment(rs));
            }
            return Optional.empty();
        }
    }

    private List<TestAssignment> executeQueryList(PreparedStatement stmt) throws SQLException {
        List<TestAssignment> assignments = new ArrayList<>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                assignments.add(mapResultSetToAssignment(rs));
            }
        }
        return assignments;
    }

    private TestAssignment mapResultSetToAssignment(ResultSet rs) throws SQLException {
        TestAssignment assignment = new TestAssignment();
        assignment.setId(rs.getLong("id"));
        assignment.setUserId(rs.getLong("user_id"));
        assignment.setTestId(rs.getLong("test_id"));
        
        Timestamp assignedDate = rs.getTimestamp("assigned_date");
        assignment.setAssignedDate(assignedDate != null ? assignedDate.toLocalDateTime() : LocalDateTime.now());
        
        Timestamp deadline = rs.getTimestamp("deadline");
        assignment.setDeadline(deadline != null ? deadline.toLocalDateTime() : null);
        
        assignment.setAttemptsLeft(rs.getInt("attempts_left"));
        
        String statusStr = rs.getString("status");
        assignment.setStatus(statusStr != null ? AssignmentStatus.fromValue(statusStr) : AssignmentStatus.ASSIGNED);
        
        return assignment;
    }
}
