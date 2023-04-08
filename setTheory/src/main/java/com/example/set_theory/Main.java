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
        String input = "((A + B))";
        Map<Character, Set<Character>> map = new HashMap<>();
        map.put('A', new HashSet<>(Set.of('a', 'b', 'c')));
        map.put('B', new HashSet<>(Set.of('d')));
        SetRPN converter = new SetRPN(input);
        Set<Character> result = converter.evaluate(map);
        long finish = System.nanoTime();
        System.out.println("Time in milliseconds: " + (finish - start) / 1_000_000);
        for (char element: result) {
            System.out.print(element + ", ");
        }
    }

}
