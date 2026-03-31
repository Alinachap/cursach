package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for questions.
 * Implements the Template Method pattern for question evaluation.
 * Subclasses define specific evaluation logic based on question type.
 */
public abstract class Question extends BaseEntity {
    private static final long serialVersionUID = 1L;

    protected Long testId;
    protected String questionText;
    protected QuestionType questionType;
    protected Integer orderNum;
    protected List<AnswerOption> answerOptions;

    /**
     * Default constructor for serialization.
     */
    protected Question() {
        this.answerOptions = new ArrayList<>();
    }

    /**
     * Constructs a Question with fields.
     *
     * @param id the question ID
     * @param testId the test ID
     * @param questionText the question text
     * @param questionType the question type
     * @param orderNum the display order
     */
    protected Question(Long id, Long testId, String questionText, QuestionType questionType, Integer orderNum) {
        super(id);
        this.testId = testId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.orderNum = orderNum;
        this.answerOptions = new ArrayList<>();
    }

    // Getters and Setters

    public Long getTestId() {
        return testId;
    }

    public void setTestId(Long testId) {
        this.testId = testId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    /**
     * Adds an answer option to this question.
     *
     * @param option the option to add
     */
    public void addAnswerOption(AnswerOption option) {
        if (this.answerOptions == null) {
            this.answerOptions = new ArrayList<>();
        }
        this.answerOptions.add(option);
    }

    /**
     * Abstract method to evaluate if given answers are correct.
     * Subclasses implement specific evaluation logic.
     *
     * @param selectedOptionIds the IDs of selected options
     * @return true if the answer is correct
     */
    public abstract boolean isAnswerCorrect(List<Long> selectedOptionIds);

    /**
     * Gets the maximum possible score for this question.
     *
     * @return 1 for all question types
     */
    public int getMaxScore() {
        return 1;
    }

    @Override
    public String getEntityName() {
        return "Question";
    }

    @Override
    public String toString() {
        return "Question{" +
               "id=" + getId() +
               ", testId=" + testId +
               ", questionType=" + questionType +
               ", orderNum=" + orderNum +
               '}';
    }
}
