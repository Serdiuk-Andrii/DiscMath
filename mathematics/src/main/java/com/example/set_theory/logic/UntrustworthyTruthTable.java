package com.example.set_theory.logic;

public class UntrustworthyTruthTable {

    private final TruthTable table;
    private final Integer corruptedIndex;

    public UntrustworthyTruthTable(TruthTable table, Integer corruptedIndex) {
        this.table = table;
        this.corruptedIndex = corruptedIndex;
    }


    public TruthTable getTable() {
        return table;
    }

    public Integer getCorruptedIndex() {
        return corruptedIndex;
    }
}
