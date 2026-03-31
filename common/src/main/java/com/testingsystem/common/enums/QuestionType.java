package com.testingsystem.common.enums;

import java.io.Serializable;

/**
 * Enumeration of question types in tests.
 * Defines whether a question has a single correct answer or multiple correct answers.
 */
public enum QuestionType implements Serializable {
    /**
     * Single choice question - only one correct answer.
     */
    SINGLE("single"),
    
    /**
     * Multiple choice question - one or more correct answers.
     */
    MULTIPLE("multiple");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    /**
     * Gets the string representation of the question type.
     * @return the question type value as string
     */
    public String getValue() {
        return value;
    }

    /**
     * Parses a string value to QuestionType enum.
     * @param value the string value to parse
     * @return the corresponding QuestionType
     * @throws IllegalArgumentException if the value is not valid
     */
    public static QuestionType fromValue(String value) {
        for (QuestionType type : QuestionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid question type: " + value);
    }
}
