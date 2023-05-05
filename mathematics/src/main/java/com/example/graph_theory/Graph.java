package com.example.graph_theory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import kotlin.Pair;

public class Graph {

    private final List<List<Integer>> incidenceList;
    private List<Set<Integer>> connectedComponents = null;

    public Graph() {
        incidenceList = new ArrayList<>();
    }

    public Graph(List<List<Integer>> list) {
        incidenceList = list;
    }

    public List<Pair<Integer, Integer>> getBridges() {
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        List<Set<Integer>> components = getConnectedComponents();
        for (Set<Integer> component: components) {
            if (component.size() == 1) {
                continue;
            }
            for(Integer vertex: component) {
                List<Integer> neighbours = new ArrayList<>(incidenceList.get(vertex));
                for (Integer neighbour : neighbours) {
                    if (result.contains(new Pair<>(neighbour, vertex))) {
                        continue;
                    }
                    incidenceList.get(vertex).remove(neighbour);
                    List<Integer> neighbourIncidenceList = incidenceList.get(neighbour);
                    neighbourIncidenceList.remove(vertex);
                    int newSize = findConnectedComponentForVertex(vertex).size();
                    if (newSize != component.size()) {
                        result.add(new Pair<>(vertex, neighbour));
                    }
                    incidenceList.get(vertex).add(neighbour);
                    neighbourIncidenceList.add(vertex);
                }
            }
        }
        return result;
    }


    public List<Integer> getCutVertices() {
        List<Integer> result = new ArrayList<>(incidenceList.size());
        List<Set<Integer>> components = getConnectedComponents();
        for (Set<Integer> component: components) {
            if (component.size() <= 2) {
                continue;
            }
            for (int vertex: component) {
                Collection<Integer> neighbours = new ArrayList<>(incidenceList.get(vertex));
                if (neighbours.size() == 1) {
                    continue;
                }
                removeVertexEdges(vertex);
                int root = component.stream().filter(index -> index != vertex).
                        collect(Collectors.toList()).get(0);
                int newComponentSize = findConnectedComponentForVertex(root).size();
                if (component.size() != newComponentSize + 1) {
                    result.add(vertex);
                }
                addVertexEdges(vertex, neighbours);
            }
        }
        return result;
    }

    public List<Set<Integer>> getConnectedComponents() {
        if (connectedComponents != null) {
            return connectedComponents;
        }
        connectedComponents = calculateConnectedComponents();
        return connectedComponents;
    }

    public void removeVertexEdges(final Integer index) {
        List<Integer> neighboursList = new ArrayList<>(incidenceList.get(index));
        for (Integer neighbour: neighboursList) {
            incidenceList.get(neighbour).remove(index);
        }
        incidenceList.get(index).clear();
    }

    public void addVertexEdges(final int index, final Collection<Integer> vertices) {
        incidenceList.get(index).addAll(vertices);
        for (int vertex: vertices) {
            incidenceList.get(vertex).add(index);
        }
    }

    public Graph join(Graph other) {
        int size = this.incidenceList.size();
        Graph union = this.union(other);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < other.incidenceList.size(); j++) {
                union.addEdge(i, size + j);
            }
        }
        return union;
    }

    public Graph union(Graph other) {
        int size = this.incidenceList.size();
        int resultGraphSize = size + other.incidenceList.size();
        List<List<Integer>> resultIncidenceList = new ArrayList<>(resultGraphSize);
        for (int i = 0; i < resultGraphSize; i++) {
            List<Integer> neighbours;
            if (i < size) {
                neighbours = new ArrayList<>(this.incidenceList.get(i));
            } else {
                neighbours = other.incidenceList.get(i - size).stream()
                        .map(vertex -> vertex + size).collect(Collectors.toList());
            }
            resultIncidenceList.add(neighbours);
        }
        return new Graph(resultIncidenceList);
    }

    private void addEdge(int first, int second) {
        incidenceList.get(first).add(second);
        incidenceList.get(second).add(first);
    }

    private List<Set<Integer>> calculateConnectedComponents() {
        List<Set<Integer>> components = new ArrayList<>(incidenceList.size());
        Set<Integer> vertices = new HashSet<>();
        for (int i = 0; i < incidenceList.size(); i++) {
            vertices.add(i);
        }
        while (!vertices.isEmpty()) {
            int root = vertices.stream().findAny().get();
            Set<Integer> component = findConnectedComponentForVertex(root);
            components.add(component);
            vertices.removeAll(component);
        }
        return components;
    }

    private Set<Integer> findConnectedComponentForVertex(int root) {
        Set<Integer> component = new HashSet<>();
        Set<Integer> verticesToExplore = new HashSet<>(incidenceList.size());
        verticesToExplore.add(root);
        while (!verticesToExplore.isEmpty()) {
            component.addAll(verticesToExplore);
            verticesToExplore = verticesToExplore.stream().map(incidenceList::get).
                    flatMap(Collection::stream).filter(vertex -> !component.contains(vertex)).
                    collect(Collectors.toSet());
        }
        return component;
    }

    public int getNumberOfVertices() {
        return incidenceList.size();
    }

    public int getNumberOfEdges() {
        return incidenceList.stream().map(List::size).reduce(Integer::sum).get() / 2;
    }

    public List<Integer> getNeighboursOf(int vertex) {
        return incidenceList.get(vertex);
    }

}
