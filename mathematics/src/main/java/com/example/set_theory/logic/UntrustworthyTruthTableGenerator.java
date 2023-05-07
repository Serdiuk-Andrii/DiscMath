package com.example.set_theory.logic;

import com.example.set_theory.exceptions.MissingArgumentException;
import com.example.set_theory.exceptions.UnknownOperatorException;

import java.util.List;
import java.util.Random;

import kotlin.Pair;

public class UntrustworthyTruthTableGenerator {

    public static final Random random = new Random();
    private final int errorProbability;

    public UntrustworthyTruthTableGenerator(int errorProbability) {
        this.errorProbability = errorProbability;
    }

    public UntrustworthyTruthTable getTableFromExpression(String expression) throws MissingArgumentException,
            UnknownOperatorException {
        TruthTable table = TruthTable.buildTruthTable(expression);
        int currentProbability = random.nextInt(100);
        Integer corruptedIndex = null;
        if (errorProbability < currentProbability) {
            corruptedIndex = random.nextInt(table.getRows().size());
            Pair<List<Boolean>, Boolean> row = table.getRows().get(corruptedIndex);
            table.getRows().set(corruptedIndex, new Pair<>(row.getFirst(), !row.getSecond()));
        }
        return new UntrustworthyTruthTable(table, corruptedIndex);
    }

}
