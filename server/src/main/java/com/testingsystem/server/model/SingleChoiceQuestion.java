package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.List;

/**
 * Represents a single-choice question.
 * Only one answer option is correct.
 */
public class SingleChoiceQuestion extends Question {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for serialization.
     */
    public SingleChoiceQuestion() {
        setQuestionType(QuestionType.SINGLE);
    }

    /**
     * Constructs a SingleChoiceQuestion.
     *
     * @param id the question ID
     * @param testId the test ID
     * @param questionText the question text
     * @param orderNum the display order
     */
    public SingleChoiceQuestion(Long id, Long testId, String questionText, Integer orderNum) {
        super(id, testId, questionText, QuestionType.SINGLE, orderNum);
    }

    /**
     * Evaluates if the selected answer is correct.
     * For single choice, exactly one option must be selected and it must be correct.
     *
     * @param selectedOptionIds the IDs of selected options
     * @return true if exactly one correct option is selected
     */
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
