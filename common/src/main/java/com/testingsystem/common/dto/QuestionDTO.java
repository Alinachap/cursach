package com.testingsystem.common.dto;

import com.testingsystem.common.enums.QuestionType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class QuestionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long testId;
    private String questionText;
    private QuestionType questionType;
    private Integer orderNum;
    private List<AnswerOptionDTO> answerOptions;

    public QuestionDTO() {
    }

    public QuestionDTO(Long id, Long testId, String questionText, QuestionType questionType,
                       Integer orderNum, List<AnswerOptionDTO> answerOptions) {
        this.id = id;
        this.testId = testId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.orderNum = orderNum;
        this.answerOptions = answerOptions;
    }

    public QuestionDTO(Long testId, String questionText, QuestionType questionType, Integer orderNum) {
        this.testId = testId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.orderNum = orderNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<AnswerOptionDTO> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOptionDTO> answerOptions) {
        this.answerOptions = answerOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionDTO that = (QuestionDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
               "id=" + id +
               ", questionText='" + (questionText != null ? questionText.substring(0, Math.min(30, questionText.length())) : "null") + "...'" +
               ", questionType=" + questionType +
               ", orderNum=" + orderNum +
               '}';
    }
}
