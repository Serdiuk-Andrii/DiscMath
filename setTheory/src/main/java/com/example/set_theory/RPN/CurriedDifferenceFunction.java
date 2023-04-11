package com.example.set_theory.RPN;

import java.util.Set;
import java.util.stream.Collectors;

import kotlin.jvm.functions.Function1;

public class CurriedDifferenceFunction implements Function1<Set<Character>, Set<Character>> {

    private final Set<Character> universalSet;

    public CurriedDifferenceFunction(final Set<Character> universalSet) {
        this.universalSet = universalSet;
    }

    @Override
    public Set<Character> invoke(Set<Character> set) {
        return universalSet.stream().filter(element -> !set.contains(element)).
                collect(Collectors.toSet());
    }
}
