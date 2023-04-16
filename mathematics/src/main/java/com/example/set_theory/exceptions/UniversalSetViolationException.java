package com.example.set_theory.exceptions;

import java.util.Collection;
import java.util.Set;

public class UniversalSetViolationException extends Exception {

    private final Set<Character> universalSet;
    private final Collection<Set<Character>> sets;

    public UniversalSetViolationException(Set<Character> universalSet,
                                          Collection<Set<Character>> sets) {
        super("The universal set must contain all the elements provided in the other sets");
        this.universalSet = universalSet;
        this.sets = sets;
    }

    public Set<Character> getUniversalSet() {
        return universalSet;
    }

    public Collection<Set<Character>> getSets() {
        return sets;
    }
}
