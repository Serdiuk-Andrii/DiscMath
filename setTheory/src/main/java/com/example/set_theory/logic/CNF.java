package com.example.set_theory.logic;

import static com.example.set_theory.RPN.LogicEvaluator.*;

import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.List;
import java.util.stream.Collectors;

import kotlin.Pair;

public class CNF {

    public static String buildCNFBasedOnTruthTable(final List<Character> symbols,
                                                   final List<Pair<List<Boolean>, Boolean>> rows) {
        return rows.stream().filter(row -> !row.getSecond()).map(pair ->
                getDisjunction(symbols, pair.getFirst())).
                collect(Collectors.joining(CONJUNCTION_SEPARATOR));
    }

    public static String buildCNFBasedOnExpression(final String expression) throws UnknownOperatorException {
        TruthTable table = TruthTable.buildTruthTable(expression);
        return buildCNFBasedOnTruthTable(table.getSymbols(), table.getRows());
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
