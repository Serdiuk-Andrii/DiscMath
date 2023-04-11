package com.example.set_theory.RPN;

import com.example.set_theory.exceptions.IllegalNumberOfArgumentsException;

import java.util.Collection;

public abstract class Evaluator<T> {

    private final Collection<Character> symbols;

    public Evaluator(final Collection<Character> symbols) {
        this.symbols = symbols;
    }

    protected abstract T performEvaluation(final char symbol, T... values);


    public final T evaluate(final char symbol, T... values) throws IllegalNumberOfArgumentsException {
        int arity = getArity(symbol);
        if (arity != values.length) {
            throw new IllegalNumberOfArgumentsException(symbol, arity, values.length);
        }
        return performEvaluation(symbol, values);
    }
    public abstract int getArity(final char symbol);
    public boolean isSupported(final char symbol) {
        return symbols.contains(symbol);
    }


}
