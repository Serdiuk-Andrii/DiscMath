package com.example.set_theory.exceptions;

public class IncorrectSetElementException extends Exception {
    private final char operator;

    public IncorrectSetElementException(final char operator) {
        super("Unknown operator passed in the constructor: " + operator);
        this.operator = operator;
    }

    public char getOperator() {
        return operator;
    }

}
