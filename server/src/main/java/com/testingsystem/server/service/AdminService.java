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

public class AdminService {

    private final DAOFactory daoFactory;

    public AdminService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public TestAssignmentDTO assignTest(Long userId, Long testId, LocalDateTime deadline, Integer attempts)
            throws AdminServiceException {
        try {
            TestAssignment existing = daoFactory.getTestAssignmentDAO()
                    .findByUserIdAndTestId(userId, testId);

            if (existing != null) {
                throw new AdminServiceException("Test already assigned to this user");
            }

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

    public List<TestAssignmentDTO> assignTestToMultipleUsers(List<Long> userIds, Long testId,
                                                              LocalDateTime deadline, Integer attempts)
            throws AdminServiceException {
        List<TestAssignmentDTO> results = new ArrayList<>();
        for (Long userId : userIds) {
            try {
                TestAssignmentDTO dto = assignTest(userId, testId, deadline, attempts);
                results.add(dto);
            } catch (AdminServiceException e) {
            }
        }
        return results;
    }

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

            Optional<com.testingsystem.server.model.User> userOpt =
                    daoFactory.getUserDAO().findById(assignment.getUserId());
            if (userOpt.isPresent()) {
                dto.setUserName(userOpt.get().getFullName());
            }

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

    public static class AdminServiceException extends Exception {
        public AdminServiceException(String message) {
            super(message);
        }

        public AdminServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
