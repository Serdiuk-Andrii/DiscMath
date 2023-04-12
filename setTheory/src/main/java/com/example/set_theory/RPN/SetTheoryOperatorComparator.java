package com.example.set_theory.RPN;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SetTheoryOperatorComparator implements Comparator<Character> {

    public static final char UNION = '+';
    public static final char INTERSECTION = '*';
    public static final char DIFFERENCE = '\\';
    public static final char COMPLEMENT = '!';
    public static final char SYMMETRIC_DIFFERENCE = '△';

    public static final Set<Character> AVAILABLE_CHARACTERS =
            new HashSet<>(Arrays.asList(UNION, INTERSECTION, DIFFERENCE,
                    SYMMETRIC_DIFFERENCE, COMPLEMENT));

    @Override
    public int compare(@NotNull Character first, @NotNull Character second) {
        if (first == second) {
            return 0;
        }
        if (first == COMPLEMENT) {
            return 1;
        }
        if (second ==  COMPLEMENT) {
            return -1;
        }
        // The intersection and the difference operators have the same precedence, but
        // it is higher than the precedence of the union operator
        if (first == INTERSECTION || first == DIFFERENCE || first == SYMMETRIC_DIFFERENCE) {
            if (second == INTERSECTION || second == DIFFERENCE || second == SYMMETRIC_DIFFERENCE) {
                return 0;
            } else {
                return 1;
            }
        }
        return -1;
    }
}
