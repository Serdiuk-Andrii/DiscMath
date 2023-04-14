package com.example.set_theory.logic;

import static com.example.set_theory.RPN.LogicEvaluator.*;

import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.List;
import java.util.stream.Collectors;

import kotlin.Pair;

public class DNF {

    public static String buildDNFBasedOnTruthTable(final List<Character> symbols,
                                                   final List<Pair<List<Boolean>, Boolean>> rows) {
        return rows.stream().filter(Pair::getSecond).map(pair -> getConjunction(symbols,
                        pair.getFirst())).collect(Collectors.joining(DISJUNCTION_SEPARATOR));
    }

    public static String buildDNFBasedOnExpression(final String expression) throws UnknownOperatorException {
        TruthTable table = TruthTable.buildTruthTable(expression);
        return buildDNFBasedOnTruthTable(table.getSymbols(), table.getRows());
    }

    private static String getConjunction(final List<Character> symbols, final List<Boolean> row) {
        assert symbols.size() == row.size();
        StringBuilder result = new StringBuilder(3 * symbols.size());
        result.append('(');
        for (int i = 0; i < symbols.size(); i++) {
            char symbol = symbols.get(i);
            if (row.get(i)) {
                result.append(symbol);
            } else {
                result.append(NOT).append(symbol);
            }
            result.append(CONJUNCTION_SEPARATOR);
        }
        result.delete(result.length() - 3, result.length());
        result.append(')');
        return result.toString();
    }

}
