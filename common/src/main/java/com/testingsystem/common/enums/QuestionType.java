package com.testingsystem.common.enums;

import java.io.Serializable;

public enum QuestionType implements Serializable {
    SINGLE("single"),
    MULTIPLE("multiple");

    private final String value;

    QuestionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static QuestionType fromValue(String value) {
        for (QuestionType type : QuestionType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid question type: " + value);
    }
}
