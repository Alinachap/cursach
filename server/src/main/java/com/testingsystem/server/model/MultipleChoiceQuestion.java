package com.testingsystem.server.model;

import com.testingsystem.common.enums.QuestionType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a multiple-choice question.
 * One or more answer options can be correct.
 */
public class MultipleChoiceQuestion extends Question {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor for serialization.
     */
    public MultipleChoiceQuestion() {
        setQuestionType(QuestionType.MULTIPLE);
    }

    /**
     * Constructs a MultipleChoiceQuestion.
     *
     * @param id the question ID
     * @param testId the test ID
     * @param questionText the question text
     * @param orderNum the display order
     */
    public MultipleChoiceQuestion(Long id, Long testId, String questionText, Integer orderNum) {
        super(id, testId, questionText, QuestionType.MULTIPLE, orderNum);
    }

    /**
     * Evaluates if the selected answers are correct.
     * For multiple choice, all correct options must be selected and no incorrect options.
     *
     * @param selectedOptionIds the IDs of selected options
     * @return true if all correct options are selected and no incorrect options
     */
    @Override
    public boolean isAnswerCorrect(List<Long> selectedOptionIds) {
        if (selectedOptionIds == null || selectedOptionIds.isEmpty()) {
            return false;
        }
        
        Set<Long> selectedSet = new HashSet<>(selectedOptionIds);
        Set<Long> correctIds = new HashSet<>();
        
        // Collect all correct option IDs
        for (AnswerOption option : answerOptions) {
            if (option.isCorrect()) {
                correctIds.add(option.getId());
            }
        }
        
        // Check if selected exactly matches correct answers
        return selectedSet.equals(correctIds);
    }

    /**
     * Gets partial score for partially correct answers.
     *
     * @param selectedOptionIds the IDs of selected options
     * @return score between 0 and 1
     */
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
