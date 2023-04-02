package com.example.set_theory;

import com.example.set_theory.RPN.SetRPN;
import com.example.set_theory.exceptions.UniversalSetMissingException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws UniversalSetMissingException,
            UnknownOperatorException {
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //String input = reader.readLine();
        long start = System.nanoTime();
        String input = "((A + B) * (C △ D) + !E)";
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('A', new HashSet<>(Set.of('a', 'b', 'c')));
        map.put('B', new HashSet<>(Set.of('d')));
        map.put('C', new HashSet<>(Set.of('d', 'e', 'y')));
        map.put('D', new HashSet<>(Set.of('e', 'j', 'x')));
        map.put('E', new HashSet<>(Set.of('y', 'x')));
        Set<Character> universalSet = Set.of('a', 'b', 'c', 'd', 'e', 'j', 'y', 'x');
        SetRPN converter = new SetRPN(input, universalSet);
        Set<Character> result = converter.evaluate(map);
        long finish = System.nanoTime();
        System.out.println("Time in milliseconds: " + (finish - start) / 1_000_000);
        for (char element: result) {
            System.out.print(element + ", ");
        }
    }

}
