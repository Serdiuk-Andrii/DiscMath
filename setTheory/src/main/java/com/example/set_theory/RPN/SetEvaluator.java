package com.example.set_theory.RPN;

import static com.example.set_theory.RPN.SetTheoryOperatorComparator.COMPLEMENT;
import static com.example.set_theory.RPN.SetTheoryOperatorComparator.DIFFERENCE;
import static com.example.set_theory.RPN.SetTheoryOperatorComparator.INTERSECTION;
import static com.example.set_theory.RPN.SetTheoryOperatorComparator.SYMMETRIC_DIFFERENCE;
import static com.example.set_theory.RPN.SetTheoryOperatorComparator.UNION;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import kotlin.Function;
import kotlin.jvm.functions.Function2;

public class SetEvaluator extends HashMap<Character, Function<Set<Character>>> {


    public SetEvaluator() {
        super();
        Function<Set<Character>> union =
                (Function2<Set<Character>, Set<Character>, Set<Character>>)
                        (first, second) -> {
                            second.addAll(first);
                            return second;
                        };
        Function<Set<Character>> intersection =
                (Function2<Set<Character>, Set<Character>, Set<Character>>)
                        (first, second) -> {
                            second.retainAll(first);
                            return second;
                        };
        Function<Set<Character>> difference =
                (Function2<Set<Character>, Set<Character>, Set<Character>>)
                        (first, second) -> {
                            first.removeAll(second);
                            return first;
                        };
        Function<Set<Character>> symmetricDifference =
                (Function2<Set<Character>, Set<Character>, Set<Character>>)
                        (first, second) -> {
                            // Calculating the intersection
                            Set<Character> copy = new HashSet<>(first);
                            copy.retainAll(second);
                            // Calculating the difference
                            second.addAll(first);
                            second.removeAll(copy);
                            return second;
                        };
        put(UNION, union);
        put(INTERSECTION, intersection);
        put(DIFFERENCE, difference);
        put(SYMMETRIC_DIFFERENCE, symmetricDifference);
    }

    public void addComplementToUniversalSet(final Set<Character> set) {
        put(COMPLEMENT, new CurriedDifferenceFunction(set));
    }

}
