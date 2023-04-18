package com.example.discmath.ui.assistant.graph_theory

import android.content.Context
import kotlin.math.pow
import kotlin.math.sqrt

class Vertex(currentContext: Context, val vertexId: Int):
    androidx.appcompat.widget.AppCompatButton(currentContext) {

    constructor(currentContext: Context): this(currentContext, -1)

    fun distance(other: Vertex): Float {
        return sqrt((this.x - other.x).pow(2) +
                (this.y - other.y).pow(2))
    }

}