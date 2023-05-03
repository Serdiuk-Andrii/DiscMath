package com.example.set_theory.logic;

import com.example.set_theory.RPN.LogicComparator;
import com.example.set_theory.RPN.LogicEvaluator;
import com.example.set_theory.RPN.RPN;
import com.example.set_theory.exceptions.MissingArgumentException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Function;
import kotlin.Pair;

public class TruthTable {

    private final static Comparator<Character> comparator = new LogicComparator();
    private final static Map<Character, Function<Boolean>> evaluator = new LogicEvaluator();

    private final List<Character> symbols;
    private final List<Pair<List<Boolean>, Boolean>> rows;

    private TruthTable(List<Character> symbols, List<Pair<List<Boolean>, Boolean>> rows) {
        this.symbols = symbols;
        this.rows = rows;
    }

    public static TruthTable buildTruthTable(final String expression) throws UnknownOperatorException, MissingArgumentException {
        RPN<Boolean> postfixNotation = new RPN<>(expression, comparator);
        List<Character> operands = new ArrayList<>(postfixNotation.getOperandsNames());
        int n = operands.size();
        List<Pair<List<Boolean>, Boolean>> rows = new ArrayList<>(n);
        List<List<Boolean>> combinations = getAllCombinations(n);
        for (List<Boolean> evaluation: combinations) {
            Map<Character, Boolean> map = new HashMap<>(evaluation.size());
            for (int i = 0; i < evaluation.size(); i++) {
                map.put(operands.get(i), evaluation.get(i));
            }
            boolean result = postfixNotation.evaluate(evaluator, map);
            rows.add(new Pair<>(evaluation, result));
        }
        operands.sort(Comparator.naturalOrder());
        return new TruthTable(operands, rows);
    }

    private static List<List<Boolean>> getAllCombinations(final int n) throws MissingArgumentException {
        if (n <= 0) {
            throw new MissingArgumentException();
        }
        if (n == 1) {
            List<Boolean> startTrue = new ArrayList<>(List.of(true));
            List<Boolean> startFalse = new ArrayList<>(List.of(false));
            return List.of(startTrue, startFalse);
        }
        List<List<Boolean>> smallTable = getAllCombinations(n - 1);
        List<List<Boolean>> result = new ArrayList<>();
        for (List<Boolean> combination: smallTable) {
            List<Boolean> combinationCopy = new ArrayList<>(combination);
            combinationCopy.add(true);
            result.add(combinationCopy);
            combination.add(false);
            result.add(combination);
        }
        return result;
    }

    public boolean isTautology() {
        return this.rows.stream().allMatch(Pair::getSecond);
    }

    public boolean isSatisfiable() {
        return this.rows.stream().anyMatch(Pair::getSecond);
    }

    public boolean isContradiction() {
        return this.rows.stream().noneMatch(Pair::getSecond);
    }

    public List<Character> getSymbols() {
        return symbols;
    }

    public List<Pair<List<Boolean>, Boolean>> getRows() {
        return rows;
    }
}
