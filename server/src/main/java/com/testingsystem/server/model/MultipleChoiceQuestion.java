package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultipleChoiceQuestion extends Question {
    private static final long serialVersionUID = 1L;

    public MultipleChoiceQuestion() {
        setQuestionType(QuestionType.MULTIPLE);
    }

    public MultipleChoiceQuestion(Long id, Long testId, String questionText, Integer orderNum) {
        super(id, testId, questionText, QuestionType.MULTIPLE, orderNum);
    }

    @Override
    public boolean isAnswerCorrect(List<Long> selectedOptionIds) {
        if (selectedOptionIds == null || selectedOptionIds.isEmpty()) {
            return false;
        }

        Set<Long> selectedSet = new HashSet<>(selectedOptionIds);
        Set<Long> correctIds = new HashSet<>();

        for (AnswerOption option : answerOptions) {
            if (option.isCorrect()) {
                correctIds.add(option.getId());
            }
        }

        return selectedSet.equals(correctIds);
    }

    public double getPartialScore(List<Long> selectedOptionIds) {
        if (selectedOptionIds == null || selectedOptionIds.isEmpty()) {
            return 0.0;
        }

        int correctSelected = 0;
        int incorrectSelected = 0;

        for (AnswerOption option : answerOptions) {
            if (selectedOptionIds.contains(option.getId())) {
                if (option.isCorrect()) {
                    correctSelected++;
                } else {
                    incorrectSelected++;
                }
            }
        }

        int totalCorrect = 0;
        for (AnswerOption option : answerOptions) {
            if (option.isCorrect()) {
                totalCorrect++;
            }
        }

        if (totalCorrect == 0) {
            return 0.0;
        }

        double score = (double) correctSelected / totalCorrect;
        score -= (double) incorrectSelected / totalCorrect;

        return Math.max(0.0, score);
    }

    @Override
    public String getEntityName() {
        return "MultipleChoiceQuestion";
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
               "id=" + getId() +
               ", questionText='" + (questionText != null ? questionText.substring(0, Math.min(30, questionText.length())) : "null") + "...'" +
               '}';
    }
}
