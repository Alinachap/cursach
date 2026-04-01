package com.testingsystem.server.model;

public class AnswerOption extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long questionId;
    private String optionText;
    private boolean isCorrect;
    private Integer orderNum;

    public AnswerOption() {
    }

    public AnswerOption(Long id, Long questionId, String optionText, boolean isCorrect, Integer orderNum) {
        super(id);
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.orderNum = orderNum;
    }

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
