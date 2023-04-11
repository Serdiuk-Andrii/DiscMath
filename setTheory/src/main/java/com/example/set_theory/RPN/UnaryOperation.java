package com.example.set_theory.RPN;

import kotlin.Function;

public class UnaryOperation<T> extends Operator<T>{

    public UnaryOperation(final char symbol, final Function<T> operation) {
        super(symbol, operation);
    }

}
