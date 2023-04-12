package com.example.set_theory;

import static com.example.set_theory.RPN.OperatorComparator.COMPLEMENT;

import com.example.set_theory.RPN.CurriedDifferenceFunction;
import com.example.set_theory.RPN.LogicComparator;
import com.example.set_theory.RPN.LogicEvaluator;
import com.example.set_theory.RPN.OperatorComparator;
import com.example.set_theory.RPN.RPN;
import com.example.set_theory.RPN.SetEvaluator;
import com.example.set_theory.exceptions.IllegalNumberOfArgumentsException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kotlin.Function;

public class Main {

    public static void main(String[] args) throws UnknownOperatorException {
        long start = System.nanoTime();
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
        String input = "A\\B";
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('A', new HashSet<>(Set.of('a', 'b', 'c')));
        map.put('B', new HashSet<>(Set.of('b', 'c')));
        RPN<Set<Character>> converter = new RPN<>(input, new LogicComparator());
        Set<Character> result = converter.evaluate(new SetEvaluator(), map);
        long finish = System.nanoTime();
        System.out.println("Time in milliseconds: " + (finish - start) / 1_000_000);

    }

}
