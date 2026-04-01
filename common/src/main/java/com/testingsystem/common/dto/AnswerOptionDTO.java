package com.testingsystem.common.dto;

import java.io.Serializable;
import java.util.Objects;

public class AnswerOptionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long questionId;
    private String optionText;
    private boolean isCorrect;
    private Integer orderNum;

    public AnswerOptionDTO() {
    }

    public AnswerOptionDTO(Long id, Long questionId, String optionText,
                           boolean isCorrect, Integer orderNum) {
        this.id = id;
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
        this.orderNum = orderNum;
    }

    public AnswerOptionDTO(Long questionId, String optionText, Integer orderNum) {
        this.questionId = questionId;
        this.optionText = optionText;
        this.isCorrect = false;
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerOptionDTO that = (AnswerOptionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AnswerOptionDTO{" +
               "id=" + id +
               ", optionText='" + (optionText != null ? optionText.substring(0, Math.min(30, optionText.length())) : "null") + "...'" +
               ", isCorrect=" + isCorrect +
               ", orderNum=" + orderNum +
               '}';
    }
}
