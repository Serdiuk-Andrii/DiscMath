package com.example.set_theory;

import com.example.set_theory.RPN.LogicComparator;
import com.example.set_theory.RPN.RPN;
import com.example.set_theory.RPN.SetEvaluator;
import com.example.set_theory.exceptions.MissingArgumentException;
import com.example.set_theory.exceptions.UnknownOperatorException;
import com.example.set_theory.logic.CNF;
import com.example.set_theory.logic.DNF;
import com.example.set_theory.logic.TruthTable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws Exception {
        testRPN();
    }

    private static void testTrustTableBuilder() throws UnknownOperatorException, MissingArgumentException {
        String expression = "((p ∨ q) → !r) ∨ (!p ^ (q ∨ r))";
        TruthTable table = TruthTable.buildTruthTable(expression);
        String cnf = CNF.buildCNFBasedOnTruthTable(table);
        String dnf = DNF.buildDNFBasedOnTruthTable(table);
        System.out.println("CNF: " + cnf);
        System.out.println("DNF: " + dnf);
        System.out.println(table);
    }

    private static void testRPN() throws Exception {
        /*
        String input = "((!A + !B))";
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('A', new HashSet<>(Set.of('a', 'b', 'c')));
        map.put('B', new HashSet<>(Set.of('d')));
        RPN<Set<Character>> converter = new RPN<>(input, new OperatorComparator());
        Map<Character, Function<Set<Character>>> evaluator = new SetEvaluator();
        Function<Set<Character>> complement = new CurriedDifferenceFunction(Set.of('a', 'b', 'c', 'd'));
        evaluator.put(COMPLEMENT, complement);
        Set<Character> result = converter.evaluate(evaluator, map);
         */

        String input = "!A\\B + (C △ A)";
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('A', new HashSet<>(Set.of('a', 'b', 'c')));
        map.put('B', new HashSet<>(Set.of('b', 'c')));
        RPN<Set<Character>> converter = new RPN<>(input, new LogicComparator());
        Set<Character> result = converter.evaluate(new SetEvaluator(), map);
        System.out.println(converter.getExpression());

    }

}
