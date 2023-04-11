package com.example.set_theory.RPN;

import static com.example.set_theory.RPN.OperatorComparator.COMPLEMENT;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class FullSetEvaluator extends SimpleSetEvaluator {

    private final Set<Character> universalSet;


    public FullSetEvaluator(final Set<Character> universalSet) {
        super();
        this.universalSet = universalSet;
    }

    @Override
    public boolean isSupported(char symbol) {
        return symbol == COMPLEMENT || super.isSupported(symbol);
    }

    @SafeVarargs
    @Override
    protected final Set<Character> performEvaluation(char symbol, Set<Character>... values) {
        if (symbol == COMPLEMENT) {
            return getComplement(values[0]);
        }
        return super.performEvaluation(symbol, values);
    }

    private Set<Character> getComplement(@NotNull Set<Character> set) {
        return universalSet.stream().filter(element -> !set.contains(element)).
                collect(Collectors.toSet());
    }

    @Override
    public int getArity(char symbol) {
        if (symbol == COMPLEMENT) {
            return 1;
        }
        return super.getArity(symbol);
    }
}
