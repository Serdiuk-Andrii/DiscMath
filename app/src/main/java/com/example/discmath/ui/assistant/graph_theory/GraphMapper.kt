package com.example.discmath.ui.assistant.graph_theory

import com.example.graph_theory.Graph

class GraphMapper(vertices: List<Vertex>) {

    val map: MutableMap<Int, Vertex> = mutableMapOf()
    private val reverseMap: MutableMap<Vertex, Int>
    val graph: Graph

    init {
        for((index, vertex) in vertices.withIndex()) {
            map[index] = vertex
        }
        reverseMap = map.entries.associateBy({ it.value }) { it.key }.toMutableMap()
        graph = Graph(vertices.map { vertex -> vertex.getNeighbours().map { reverseMap[it] }})
    }

    fun getCorrespondingVertices(indices: Collection<Int>): List<Vertex> {
        return indices.map { map[it]!! }
    }

    fun getCorrespondingEdges(edges: Collection<Pair<Int, Int>>): List<Edge> {
        return edges.map { map[it.first]!!.getCorrespondingEdge(map[it.second]!!) }
    }

}