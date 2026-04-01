package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.List;

public class SingleChoiceQuestion extends Question {
    private static final long serialVersionUID = 1L;

    public SingleChoiceQuestion() {
        setQuestionType(QuestionType.SINGLE);
    }

    public SingleChoiceQuestion(Long id, Long testId, String questionText, Integer orderNum) {
        super(id, testId, questionText, QuestionType.SINGLE, orderNum);
    }

    @Override
    public boolean isAnswerCorrect(List<Long> selectedOptionIds) {
        if (selectedOptionIds == null || selectedOptionIds.size() != 1) {
            return false;
        }

        Long selectedId = selectedOptionIds.get(0);
        for (AnswerOption option : answerOptions) {
            if (option.getId().equals(selectedId)) {
                return option.isCorrect();
            }
        }
        return false;
    }

    @Override
    public String getEntityName() {
        return "SingleChoiceQuestion";
    }

    @Override
    public String toString() {
        return "SingleChoiceQuestion{" +
               "id=" + getId() +
               ", questionText='" + (questionText != null ? questionText.substring(0, Math.min(30, questionText.length())) : "null") + "...'" +
               '}';
    }
}
