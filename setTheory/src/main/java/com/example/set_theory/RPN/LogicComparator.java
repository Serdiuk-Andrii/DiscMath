package com.example.set_theory.RPN;

import static com.example.set_theory.RPN.LogicEvaluator.*;

import java.util.Comparator;

public class LogicComparator implements Comparator<Character> {

    @Override
    public int compare(Character first, Character second) {
        if (first == second) {
            return 0;
        }
        if (first == NOT) {
            return 1;
        }
        if (second == NOT) {
            return -1;
        }
        if (first == AND) {
            return 1;
        }
        if (second == AND) {
            return -1;
        }
        if (first == XOR) {
            return 1;
        }
        if (second == XOR) {
            return -1;
        }
        return 0;
    }

}
