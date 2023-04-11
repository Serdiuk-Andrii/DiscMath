package com.example.set_theory.exceptions;

public class IllegalNumberOfArgumentsException extends Exception {

    public IllegalNumberOfArgumentsException(final char operator, final int expectedArity,
                                             final int givenArity) {
        super("Incorrect number of arguments passed for the operator: " + operator
                + ". Expected " + expectedArity +
                "parameters, but found: " + givenArity );
    }
}
