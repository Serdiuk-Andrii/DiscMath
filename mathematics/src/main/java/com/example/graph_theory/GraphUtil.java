package com.example.graph_theory;

import java.util.List;

public class GraphUtil {

    public static Graph calculateJoin(List<Graph> graphs) {
        Graph current = graphs.get(0);
        for (int i = 1; i < graphs.size(); i++) {
            Graph next = graphs.get(i);
            current = current.join(next);
        }
        return current;
    }

    public static Graph calculateUnion(List<Graph> graphs) {
        Graph current = graphs.get(0);
        for (int i = 1; i < graphs.size(); i++) {
            Graph next = graphs.get(i);
            current = current.union(next);
        }
        return current;
    }

}
