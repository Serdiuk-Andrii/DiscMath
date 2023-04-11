package com.example.set_theory.RPN;

import kotlin.Function;
import kotlin.jvm.functions.Function2;

public class BinaryOperation<T> extends Operator<T> {

    public BinaryOperation(final char symbol, final Function2<T, T, T> operation) {
        super(symbol, operation);
    }

}
