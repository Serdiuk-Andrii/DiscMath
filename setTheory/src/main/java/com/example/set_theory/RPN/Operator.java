package com.example.set_theory.RPN;

import kotlin.Function;

abstract public class Operator<T> {

    private final char symbol;
    private final Function<T> operation;


    public Operator(final char symbol, final Function<T> operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    public char getSymbol() {
        return symbol;
    }

    public Function<T> getOperation() {
        return operation;
    }
}
