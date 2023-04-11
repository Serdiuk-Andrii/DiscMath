package com.example.set_theory.RPN;

import static com.example.set_theory.RPN.OperatorComparator.DIFFERENCE;
import static com.example.set_theory.RPN.OperatorComparator.INTERSECTION;
import static com.example.set_theory.RPN.OperatorComparator.SYMMETRIC_DIFFERENCE;
import static com.example.set_theory.RPN.OperatorComparator.UNION;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.jvm.functions.Function2;

public class SimpleSetEvaluator extends Evaluator<Set<Character>>{

    public static Collection<Character> supportedSymbols =
            List.of(UNION, INTERSECTION, DIFFERENCE, SYMMETRIC_DIFFERENCE);
    private static final Map<Character, Function2<Set<Character>, Set<Character>, Set<Character>>>
            binarySetOperators = new HashMap<>();

    static {
        Function2<Set<Character>, Set<Character>, Set<Character>> union = (first, second) ->
        {
            second.addAll(first);
            return second;
        };
        Function2<Set<Character>, Set<Character>, Set<Character>> intersection = (first, second) ->
        {
            second.retainAll(first);
            return second;
        };
        Function2<Set<Character>, Set<Character>, Set<Character>> difference = (first, second) ->
        {
            second.removeAll(first);
            return second;
        };
        Function2<Set<Character>, Set<Character>, Set<Character>> symmetricalDifference =
                (first, second) ->
                {
                    // Calculating the intersection
                    Set<Character> copy = new HashSet<>(first);
                    copy.retainAll(second);
                    // Calculating the difference
                    second.addAll(first);
                    second.removeAll(copy);
                    return second;
                };
        binarySetOperators.put(UNION, union);
        binarySetOperators.put(INTERSECTION, intersection);
        binarySetOperators.put(DIFFERENCE, difference);
        binarySetOperators.put(SYMMETRIC_DIFFERENCE, symmetricalDifference);
    }

    public SimpleSetEvaluator() {
        super(supportedSymbols);
    }

    @Override
    protected Set<Character> performEvaluation(char symbol, Set<Character>... values) {
        Function2<Set<Character>, Set<Character>, Set<Character>> function = binarySetOperators.get(symbol);
        return function.invoke(values[0], values[1]);
    }

    @Override
    public int getArity(char symbol) {
        return 2;
    }
}
