package com.example.set_theory.logic;

import static com.example.set_theory.RPN.LogicEvaluator.*;

import com.example.set_theory.exceptions.MissingArgumentException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.List;
import java.util.stream.Collectors;

import kotlin.Pair;

public class CNF {

    public static String buildCNFBasedOnTruthTable(final TruthTable table) {
        return table.getRows().stream().filter(row -> !row.getSecond()).map(pair ->
                getDisjunction(table.getSymbols(), pair.getFirst())).
                collect(Collectors.joining(CONJUNCTION_SEPARATOR));
    }

    public static String buildCNFBasedOnExpression(final String expression) throws UnknownOperatorException, MissingArgumentException {
        TruthTable table = TruthTable.buildTruthTable(expression);
        return buildCNFBasedOnTruthTable(table);
    }

    private static String getDisjunction(final List<Character> symbols, final List<Boolean> row) {
        assert symbols.size() == row.size();
        StringBuilder result = new StringBuilder(3 * symbols.size());
        result.append('(');
        for (int i = 0; i < symbols.size(); i++) {
            char symbol = symbols.get(i);
            if (row.get(i)) {
                result.append(NOT).append(symbol);
            } else {
                result.append(symbol);
            }
            result.append(DISJUNCTION_SEPARATOR);
        }
        result.delete(result.length() - 3, result.length());
        result.append(')');
        return result.toString();
    }

}
