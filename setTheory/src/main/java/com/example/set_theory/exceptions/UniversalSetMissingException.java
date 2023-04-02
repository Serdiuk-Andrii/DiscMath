package com.example.set_theory.exceptions;

public class UniversalSetMissingException extends Exception {

    public UniversalSetMissingException() {
        super("The expression contains the complement operation, " +
                "but the universal set has not been provided");
    }

}
