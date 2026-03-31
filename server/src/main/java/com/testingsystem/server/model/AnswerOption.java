package com.testingsystem.server.model;

/**
 * Represents an answer option for a question.
 * Each option has text and a flag indicating if it's correct.
 */
public class AnswerOption extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long questionId;
    private String optionText;
    private boolean isCorrect;
    private Integer orderNum;

    /**
     * Default constructor for serialization.
     */
    public AnswerOption() {
    }

    /**
     * Constructs an AnswerOption with all fields.
     *
     * @param id the option ID
     * @param questionId the question ID
     * @param optionText the option text
     * @param isCorrect whether this is a correct answer
     * @param orderNum the display order
     */
    public AnswerOption(Long id, Long questionId, String optionText, boolean isCorrect, Integer orderNum) {
        super(id);
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.orderNum = orderNum;
    }

    // Getters and Setters

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String getEntityName() {
        return "AnswerOption";
    }

    @Override
    public String toString() {
        return "AnswerOption{" +
               "id=" + getId() +
               ", questionId=" + questionId +
               ", isCorrect=" + isCorrect +
               ", orderNum=" + orderNum +
               '}';
    }
}
