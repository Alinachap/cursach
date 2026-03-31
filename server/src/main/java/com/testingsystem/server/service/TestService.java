package com.testingsystem.server.service;

import com.testingsystem.common.dto.AnswerOptionDTO;
import com.testingsystem.common.dto.QuestionDTO;
import com.testingsystem.common.dto.TestDTO;
import com.testingsystem.common.enums.QuestionType;
import com.testingsystem.server.dao.DAOFactory;
import com.testingsystem.server.dao.TestDAO;
import com.testingsystem.server.model.AnswerOption;
import com.testingsystem.server.model.Question;
import com.testingsystem.server.model.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for test management.
 * Handles CRUD operations for tests and questions.
 */
public class TestService {

    private final DAOFactory daoFactory;
    private final TestDAO testDAO;

    /**
     * Constructs a TestService with the given DAO factory.
     *
     * @param daoFactory the DAO factory
     */
    public TestService(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.testDAO = daoFactory.getTestDAO();
    }

    /**
     * Gets all active tests.
     *
     * @return list of active TestDTOs
     * @throws TestServiceException if retrieval fails
     */
    public List<TestDTO> getActiveTests() throws TestServiceException {
        try {
            return testDAO.findActiveTests().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new TestServiceException("Failed to get active tests", e);
        }
    }

    /**
     * Gets all tests.
     *
     * @return list of all TestDTOs
     * @throws TestServiceException if retrieval fails
     */
    public List<TestDTO> getAllTests() throws TestServiceException {
        try {
            return testDAO.findAll().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new TestServiceException("Failed to get all tests", e);
        }
    }

    /**
     * Gets a test by ID.
     *
     * @param testId the test ID
     * @return the TestDTO if found
     * @throws TestServiceException if test not found
     */
    public TestDTO getTestById(Long testId) throws TestServiceException {
        try {
            Optional<Test> testOpt = testDAO.findById(testId);
            if (testOpt.isEmpty()) {
                throw new TestServiceException("Test not found");
            }
            return toDTO(testOpt.get());
        } catch (SQLException e) {
            throw new TestServiceException("Failed to get test", e);
        }
    }

    /**
     * Creates a new test.
     *
     * @param testDTO the test data
     * @return the created TestDTO
     * @throws TestServiceException if creation fails
     */
    public TestDTO createTest(TestDTO testDTO) throws TestServiceException {
        try {
            Test test = new Test();
            test.setTitle(testDTO.getTitle());
            test.setDescription(testDTO.getDescription());
            test.setTimeLimit(testDTO.getTimeLimit());
            test.setPassingScore(testDTO.getPassingScore());
            test.setActive(testDTO.isActive());
            
            Test savedTest = testDAO.save(test);
            return toDTO(savedTest);
        } catch (SQLException e) {
            throw new TestServiceException("Failed to create test", e);
        }
    }

    /**
     * Updates a test.
     *
     * @param testDTO the test data to update
     * @return the updated TestDTO
     * @throws TestServiceException if update fails
     */
    public TestDTO updateTest(TestDTO testDTO) throws TestServiceException {
        try {
            Optional<Test> testOpt = testDAO.findById(testDTO.getId());
            if (testOpt.isEmpty()) {
                throw new TestServiceException("Test not found");
            }
            
            Test test = testOpt.get();
            test.setTitle(testDTO.getTitle());
            test.setDescription(testDTO.getDescription());
            test.setTimeLimit(testDTO.getTimeLimit());
            test.setPassingScore(testDTO.getPassingScore());
            test.setActive(testDTO.isActive());
            
            Test updatedTest = testDAO.update(test);
            return toDTO(updatedTest);
        } catch (SQLException e) {
            throw new TestServiceException("Failed to update test", e);
        }
    }

    /**
     * Deletes a test.
     *
     * @param testId the test ID
     * @throws TestServiceException if deletion fails
     */
    public void deleteTest(Long testId) throws TestServiceException {
        try {
            if (!testDAO.exists(testId)) {
                throw new TestServiceException("Test not found");
            }
            testDAO.delete(testId);
        } catch (SQLException e) {
            throw new TestServiceException("Failed to delete test", e);
        }
    }

    /**
     * Gets questions for a test.
     *
     * @param testId the test ID
     * @return list of QuestionDTOs with answer options
     * @throws TestServiceException if retrieval fails
     */
    public List<QuestionDTO> getQuestionsForTest(Long testId) throws TestServiceException {
        try {
            List<Question> questions = daoFactory.getQuestionDAO().findByTestIdOrdered(testId);
            List<QuestionDTO> dtos = new ArrayList<>();
            
            for (Question question : questions) {
                QuestionDTO dto = toDTO(question);
                List<AnswerOption> options = daoFactory.getAnswerOptionDAO()
                        .findByQuestionIdOrdered(question.getId());
                dto.setAnswerOptions(options.stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
                dtos.add(dto);
            }
            
            return dtos;
        } catch (SQLException e) {
            throw new TestServiceException("Failed to get questions", e);
        }
    }

    /**
     * Gets questions without correct answers (for test taking).
     *
     * @param testId the test ID
     * @return list of QuestionDTOs without correct answer indicators
     * @throws TestServiceException if retrieval fails
     */
    public List<QuestionDTO> getQuestionsForTaking(Long testId) throws TestServiceException {
        try {
            List<QuestionDTO> allQuestions = getQuestionsForTest(testId);
            
            // Remove correct answer indicators
            for (QuestionDTO dto : allQuestions) {
                if (dto.getAnswerOptions() != null) {
                    for (AnswerOptionDTO option : dto.getAnswerOptions()) {
                        option.setCorrect(false);
                    }
                }
            }
            
            return allQuestions;
        } catch (TestServiceException e) {
            throw e;
        }
    }

    /**
     * Creates a question for a test.
     *
     * @param questionDTO the question data
     * @return the created QuestionDTO
     * @throws TestServiceException if creation fails
     */
    public QuestionDTO createQuestion(QuestionDTO questionDTO) throws TestServiceException {
        try {
            Question question = createQuestionEntity(questionDTO);
            
            int maxOrder = daoFactory.getQuestionDAO().getMaxOrderNum(questionDTO.getTestId());
            question.setOrderNum(maxOrder + 1);
            
            Question savedQuestion = daoFactory.getQuestionDAO().save(question);
            
            // Save answer options
            if (questionDTO.getAnswerOptions() != null) {
                for (AnswerOptionDTO optionDTO : questionDTO.getAnswerOptions()) {
                    AnswerOption option = new AnswerOption();
                    option.setQuestionId(savedQuestion.getId());
                    option.setOptionText(optionDTO.getOptionText());
                    option.setCorrect(optionDTO.isCorrect());
                    
                    int maxOptionOrder = daoFactory.getAnswerOptionDAO()
                            .getMaxOrderNum(savedQuestion.getId());
                    option.setOrderNum(maxOptionOrder + 1);
                    
                    daoFactory.getAnswerOptionDAO().save(option);
                }
            }
            
            return getQuestionById(savedQuestion.getId());
        } catch (SQLException e) {
            throw new TestServiceException("Failed to create question", e);
        }
    }

    /**
     * Updates a question.
     *
     * @param questionDTO the question data
     * @return the updated QuestionDTO
     * @throws TestServiceException if update fails
     */
    public QuestionDTO updateQuestion(QuestionDTO questionDTO) throws TestServiceException {
        try {
            Optional<Question> questionOpt = daoFactory.getQuestionDAO().findById(questionDTO.getId());
            if (questionOpt.isEmpty()) {
                throw new TestServiceException("Question not found");
            }
            
            Question question = questionOpt.get();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setQuestionType(questionDTO.getQuestionType());
            
            daoFactory.getQuestionDAO().update(question);
            
            return getQuestionById(question.getId());
        } catch (SQLException e) {
            throw new TestServiceException("Failed to update question", e);
        }
    }

    /**
     * Deletes a question.
     *
     * @param questionId the question ID
     * @throws TestServiceException if deletion fails
     */
    public void deleteQuestion(Long questionId) throws TestServiceException {
        try {
            if (!daoFactory.getQuestionDAO().exists(questionId)) {
                throw new TestServiceException("Question not found");
            }
            daoFactory.getQuestionDAO().delete(questionId);
        } catch (SQLException e) {
            throw new TestServiceException("Failed to delete question", e);
        }
    }

    /**
     * Gets a question by ID with answer options.
     *
     * @param questionId the question ID
     * @return the QuestionDTO
     * @throws TestServiceException if not found
     */
    public QuestionDTO getQuestionById(Long questionId) throws TestServiceException {
        try {
            Optional<Question> questionOpt = daoFactory.getQuestionDAO().findById(questionId);
            if (questionOpt.isEmpty()) {
                throw new TestServiceException("Question not found");
            }
            
            QuestionDTO dto = toDTO(questionOpt.get());
            List<AnswerOption> options = daoFactory.getAnswerOptionDAO()
                    .findByQuestionIdOrdered(questionId);
            dto.setAnswerOptions(options.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList()));
            
            return dto;
        } catch (SQLException e) {
            throw new TestServiceException("Failed to get question", e);
        }
    }

    /**
     * Creates a Question entity from DTO.
     */
    private Question createQuestionEntity(QuestionDTO dto) {
        Question question;
        if (dto.getQuestionType() == QuestionType.SINGLE) {
            question = new com.testingsystem.server.model.SingleChoiceQuestion();
        } else {
            question = new com.testingsystem.server.model.MultipleChoiceQuestion();
        }
        question.setTestId(dto.getTestId());
        question.setQuestionText(dto.getQuestionText());
        question.setQuestionType(dto.getQuestionType());
        return question;
    }

    /**
     * Converts Test entity to DTO.
     */
    private TestDTO toDTO(Test test) {
        TestDTO dto = new TestDTO();
        dto.setId(test.getId());
        dto.setTitle(test.getTitle());
        dto.setDescription(test.getDescription());
        dto.setTimeLimit(test.getTimeLimit());
        dto.setPassingScore(test.getPassingScore());
        dto.setActive(test.isActive());
        dto.setCreatedAt(test.getCreatedAt());
        return dto;
    }

    /**
     * Converts Question entity to DTO.
     */
    private QuestionDTO toDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setTestId(question.getTestId());
        dto.setQuestionText(question.getQuestionText());
        dto.setQuestionType(question.getQuestionType());
        dto.setOrderNum(question.getOrderNum());
        return dto;
    }

    /**
     * Converts AnswerOption entity to DTO.
     */
    private AnswerOptionDTO toDTO(AnswerOption option) {
        AnswerOptionDTO dto = new AnswerOptionDTO();
        dto.setId(option.getId());
        dto.setQuestionId(option.getQuestionId());
        dto.setOptionText(option.getOptionText());
        dto.setCorrect(option.isCorrect());
        dto.setOrderNum(option.getOrderNum());
        return dto;
    }

    /**
     * Custom exception for test service errors.
     */
    public static class TestServiceException extends Exception {
        public TestServiceException(String message) {
            super(message);
        }

        public TestServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
