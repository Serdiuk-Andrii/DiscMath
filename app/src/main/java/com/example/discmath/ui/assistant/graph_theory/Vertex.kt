package com.example.discmath.ui.assistant.graph_theory

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.pow
import kotlin.math.sqrt

const val VERTEX_ELEVATION = 15F

class Vertex(currentContext: Context, val edges: CopyOnWriteArrayList<Edge> = CopyOnWriteArrayList()):
    AppCompatButton(currentContext), java.io.Serializable {

    init {
        this.elevation = VERTEX_ELEVATION
    }

    constructor(currentContext: Context): this(currentContext, CopyOnWriteArrayList())

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

    fun getCorrespondingEdge(otherVertex: Vertex): Edge {
        return edges.filter { edge -> edge.getOtherVertex(this) == otherVertex }[0]
    }

}
