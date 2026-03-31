package com.testingsystem.server.service;

import com.testingsystem.common.dto.TestAssignmentDTO;
import com.testingsystem.common.enums.AssignmentStatus;
import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.model.TestAssignment;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for administrative operations.
 * Handles test assignments and administrative tasks.
 */
public class AdminService {

    private final DAOFactory daoFactory;

    /**
     * Constructs an AdminService with the given DAO factory.
     *
     * @param daoFactory the DAO factory
     */
    public AdminService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Assigns a test to a user.
     *
     * @param userId the user ID
     * @param testId the test ID
     * @param deadline the deadline for completion
     * @param attempts number of attempts allowed
     * @return the created TestAssignmentDTO
     * @throws AdminServiceException if assignment fails
     */
    public TestAssignmentDTO assignTest(Long userId, Long testId, LocalDateTime deadline, Integer attempts) 
            throws AdminServiceException {
        try {
            // Check if assignment already exists
            TestAssignment existing = daoFactory.getTestAssignmentDAO()
                    .findByUserIdAndTestId(userId, testId);
            
            if (existing != null) {
                throw new AdminServiceException("Test already assigned to this user");
            }
            
            // Create new assignment
            TestAssignment assignment = new TestAssignment();
            assignment.setUserId(userId);
            assignment.setTestId(testId);
            assignment.setAssignedDate(LocalDateTime.now());
            assignment.setDeadline(deadline);
            assignment.setAttemptsLeft(attempts != null ? attempts : 1);
            assignment.setStatus(AssignmentStatus.ASSIGNED);
            
            TestAssignment saved = daoFactory.getTestAssignmentDAO().save(assignment);
            return toDTO(saved);
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to assign test", e);
        }
    }

    /**
     * Assigns a test to multiple users.
     *
     * @param userIds list of user IDs
     * @param testId the test ID
     * @param deadline the deadline
     * @param attempts number of attempts
     * @return list of created TestAssignmentDTOs
     * @throws AdminServiceException if assignment fails
     */
    public List<TestAssignmentDTO> assignTestToMultipleUsers(List<Long> userIds, Long testId, 
                                                              LocalDateTime deadline, Integer attempts) 
            throws AdminServiceException {
        List<TestAssignmentDTO> results = new ArrayList<>();
        for (Long userId : userIds) {
            try {
                TestAssignmentDTO dto = assignTest(userId, testId, deadline, attempts);
                results.add(dto);
            } catch (AdminServiceException e) {
                // Continue with other users
            }
        }
        return results;
    }

    /**
     * Gets assignments for a user.
     *
     * @param userId the user ID
     * @return list of TestAssignmentDTOs
     * @throws AdminServiceException if retrieval fails
     */
    public List<TestAssignmentDTO> getUserAssignments(Long userId) throws AdminServiceException {
        try {
            List<TestAssignment> assignments = daoFactory.getTestAssignmentDAO().findByUserId(userId);
            List<TestAssignmentDTO> dtos = new ArrayList<>();
            for (TestAssignment assignment : assignments) {
                dtos.add(toDTO(assignment));
            }
            return dtos;
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to get user assignments", e);
        }
    }

    /**
     * Gets assignments for a test.
     *
     * @param testId the test ID
     * @return list of TestAssignmentDTOs
     * @throws AdminServiceException if retrieval fails
     */
    public List<TestAssignmentDTO> getTestAssignments(Long testId) throws AdminServiceException {
        try {
            List<TestAssignment> assignments = daoFactory.getTestAssignmentDAO().findByTestId(testId);
            List<TestAssignmentDTO> dtos = new ArrayList<>();
            for (TestAssignment assignment : assignments) {
                dtos.add(toDTO(assignment));
            }
            return dtos;
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to get test assignments", e);
        }
    }

    /**
     * Gets all assignments.
     *
     * @return list of all TestAssignmentDTOs
     * @throws AdminServiceException if retrieval fails
     */
    public List<TestAssignmentDTO> getAllAssignments() throws AdminServiceException {
        try {
            List<TestAssignment> assignments = daoFactory.getTestAssignmentDAO().findAll();
            List<TestAssignmentDTO> dtos = new ArrayList<>();
            for (TestAssignment assignment : assignments) {
                dtos.add(toDTO(assignment));
            }
            return dtos;
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to get all assignments", e);
        }
    }

    /**
     * Gets assignments by status.
     *
     * @param status the assignment status
     * @return list of TestAssignmentDTOs
     * @throws AdminServiceException if retrieval fails
     */
    public List<TestAssignmentDTO> getAssignmentsByStatus(AssignmentStatus status)
            throws AdminServiceException {
        try {
            List<TestAssignment> assignments = daoFactory.getTestAssignmentDAO().findByStatus(status);
            List<TestAssignmentDTO> dtos = new ArrayList<>();
            for (TestAssignment assignment : assignments) {
                dtos.add(toDTO(assignment));
            }
            return dtos;
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to get assignments by status", e);
        }
    }

    /**
     * Updates an assignment.
     *
     * @param assignmentDTO the assignment data
     * @return the updated TestAssignmentDTO
     * @throws AdminServiceException if update fails
     */
    public TestAssignmentDTO updateAssignment(TestAssignmentDTO assignmentDTO) 
            throws AdminServiceException {
        try {
            Optional<TestAssignment> assignmentOpt = daoFactory.getTestAssignmentDAO()
                    .findById(assignmentDTO.getId());
            if (assignmentOpt.isEmpty()) {
                throw new AdminServiceException("Assignment not found");
            }
            
            TestAssignment assignment = assignmentOpt.get();
            assignment.setDeadline(assignmentDTO.getDeadline());
            assignment.setAttemptsLeft(assignmentDTO.getAttemptsLeft());
            if (assignmentDTO.getStatus() != null) {
                assignment.setStatus(assignmentDTO.getStatus());
            }
            
            daoFactory.getTestAssignmentDAO().update(assignment);
            return toDTO(assignment);
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to update assignment", e);
        }
    }

    /**
     * Deletes an assignment.
     *
     * @param assignmentId the assignment ID
     * @throws AdminServiceException if deletion fails
     */
    public void deleteAssignment(Long assignmentId) throws AdminServiceException {
        try {
            if (!daoFactory.getTestAssignmentDAO().exists(assignmentId)) {
                throw new AdminServiceException("Assignment not found");
            }
            daoFactory.getTestAssignmentDAO().delete(assignmentId);
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to delete assignment", e);
        }
    }

    /**
     * Extends deadline for an assignment.
     *
     * @param assignmentId the assignment ID
     * @param newDeadline the new deadline
     * @return the updated TestAssignmentDTO
     * @throws AdminServiceException if update fails
     */
    public TestAssignmentDTO extendDeadline(Long assignmentId, LocalDateTime newDeadline) 
            throws AdminServiceException {
        try {
            Optional<TestAssignment> assignmentOpt = daoFactory.getTestAssignmentDAO()
                    .findById(assignmentId);
            if (assignmentOpt.isEmpty()) {
                throw new AdminServiceException("Assignment not found");
            }
            
            TestAssignment assignment = assignmentOpt.get();
            assignment.setDeadline(newDeadline);
            
            daoFactory.getTestAssignmentDAO().update(assignment);
            return toDTO(assignment);
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to extend deadline", e);
        }
    }

    /**
     * Resets an assignment to allow re-taking.
     *
     * @param assignmentId the assignment ID
     * @param newAttempts new number of attempts
     * @return the updated TestAssignmentDTO
     * @throws AdminServiceException if update fails
     */
    public TestAssignmentDTO resetAssignment(Long assignmentId, Integer newAttempts) 
            throws AdminServiceException {
        try {
            Optional<TestAssignment> assignmentOpt = daoFactory.getTestAssignmentDAO()
                    .findById(assignmentId);
            if (assignmentOpt.isEmpty()) {
                throw new AdminServiceException("Assignment not found");
            }
            
            TestAssignment assignment = assignmentOpt.get();
            assignment.setStatus(AssignmentStatus.ASSIGNED);
            assignment.setAttemptsLeft(newAttempts != null ? newAttempts : 1);
            
            daoFactory.getTestAssignmentDAO().update(assignment);
            return toDTO(assignment);
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to reset assignment", e);
        }
    }

    /**
     * Converts TestAssignment entity to DTO.
     */
    private TestAssignmentDTO toDTO(TestAssignment assignment) throws AdminServiceException {
        try {
            TestAssignmentDTO dto = new TestAssignmentDTO();
            dto.setId(assignment.getId());
            dto.setUserId(assignment.getUserId());
            dto.setTestId(assignment.getTestId());
            dto.setAssignedDate(assignment.getAssignedDate());
            dto.setDeadline(assignment.getDeadline());
            dto.setAttemptsLeft(assignment.getAttemptsLeft());
            dto.setStatus(assignment.getStatus());
            
            // Get user name
            Optional<com.testingsystem.server.model.User> userOpt = 
                    daoFactory.getUserDAO().findById(assignment.getUserId());
            if (userOpt.isPresent()) {
                dto.setUserName(userOpt.get().getFullName());
            }
            
            // Get test name
            Optional<com.testingsystem.server.model.Test> testOpt = 
                    daoFactory.getTestDAO().findById(assignment.getTestId());
            if (testOpt.isPresent()) {
                dto.setTestName(testOpt.get().getTitle());
            }
            
            return dto;
        } catch (SQLException e) {
            throw new AdminServiceException("Failed to convert assignment to DTO", e);
        }
    }

    /**
     * Custom exception for admin service errors.
     */
    public static class AdminServiceException extends Exception {
        public AdminServiceException(String message) {
            super(message);
        }

        public AdminServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
