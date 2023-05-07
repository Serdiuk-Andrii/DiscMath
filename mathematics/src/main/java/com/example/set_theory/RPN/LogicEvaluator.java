package com.example.set_theory.RPN;

import java.util.HashMap;

import kotlin.Function;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class LogicEvaluator extends HashMap<Character, Function<Boolean>> {

    public static final char AND = '∧';
    public static final char OR = '∨';
    public static final char IMPLICATION = '→';
    public static final char EQUIVALENCE = '↔';
    public static final char NOT = '!';
    public static final char XOR = '⊕';

    public static final String DISJUNCTION_SEPARATOR = " " + OR + " ";
    public static final String CONJUNCTION_SEPARATOR = " " + AND + " ";


    public LogicEvaluator() {
        super();

        Function<Boolean> and = (Function2<Boolean, Boolean, Boolean>)
                (first, second) -> first && second;
        Function<Boolean> or = (Function2<Boolean, Boolean, Boolean>)
                (first, second) -> first || second;
        Function<Boolean> implication = (Function2<Boolean, Boolean, Boolean>)
                (first, second) -> !first || second;
        Function<Boolean> xor = (Function2<Boolean, Boolean, Boolean>)
                (first, second) -> first ^ second;
        Function<Boolean> equivalence = (Function2<Boolean, Boolean, Boolean>)
                (first, second) -> first == second;
        Function<Boolean> not = (Function1<Boolean, Boolean>) (element) -> !element;
        put(AND, and);
        put(OR, or);
        put(IMPLICATION, implication);
        put(NOT, not);
        put(XOR, xor);
        put(EQUIVALENCE, equivalence);
    }

}
