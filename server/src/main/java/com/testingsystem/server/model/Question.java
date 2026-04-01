package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

public abstract class Question extends BaseEntity {
    private static final long serialVersionUID = 1L;

    protected Long testId;
    protected String questionText;
    protected QuestionType questionType;
    protected Integer orderNum;
    protected List<AnswerOption> answerOptions;

    protected Question() {
        this.answerOptions = new ArrayList<>();
    }

    protected Question(Long id, Long testId, String questionText, QuestionType questionType, Integer orderNum) {
        super(id);
        this.testId = testId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.orderNum = orderNum;
        this.answerOptions = new ArrayList<>();
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

    public List<AnswerOption> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOption> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public void addAnswerOption(AnswerOption option) {
        if (this.answerOptions == null) {
            this.answerOptions = new ArrayList<>();
        }
        this.answerOptions.add(option);
    }

    public abstract boolean isAnswerCorrect(List<Long> selectedOptionIds);

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
