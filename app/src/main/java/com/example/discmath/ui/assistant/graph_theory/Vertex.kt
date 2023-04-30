package com.example.discmath.ui.assistant.graph_theory

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.pow
import kotlin.math.sqrt

class Vertex(currentContext: Context, var vertexId: Int,
             val edges: CopyOnWriteArrayList<Edge> = CopyOnWriteArrayList()):
    AppCompatButton(currentContext) {

    init {
        this.elevation = 15F
    }

    constructor(currentContext: Context): this(currentContext, -1)

    fun distance(other: Vertex): Float {
        return sqrt((this.x - other.x).pow(2) +
                (this.y - other.y).pow(2))
    }

    fun addEdge(edge: Edge) {
        edges.add(edge)
    }

    fun repositionEdges() {
        edges.forEach { edge -> edge.reposition() }
    }

    fun isAdjacent(other: Vertex): Boolean {
        return this.edges.find{ edge -> (edge.firstVertex == other ||
                edge.secondVertex == other) } != null
    }

    fun getNeighbours(): List<Vertex> = edges.map { edge -> edge.getOtherVertex(this) }

    fun getCorrespondingEdge(otherVertex: Int): Edge {
        return edges.filter { edge -> edge.getOtherVertex(this).vertexId == otherVertex }[0]
    }

}
