package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.discmath.R

@SuppressLint("ViewConstructor")
class Edge(currentContext: Context, val firstVertex: Vertex, val secondVertex: Vertex) :
    View(currentContext) {

    private val edgeHeight =
        currentContext.resources.getDimension(R.dimen.graph_edge_height).toInt()
    val parent: ConstraintLayout

    init {
        this.setBackgroundColor(resources.getColor(R.color.black, null))
        this.elevation = 10F
        this.z = firstVertex.z - 1
        this.pivotX = 0F
        this.pivotY = 0F
        this.x = 0F
        this.y = 0F
        firstVertex.addEdge(this)
        secondVertex.addEdge(this)
        parent = ConstraintLayout(currentContext)
        parent.pivotX = 0F
        parent.pivotY = 0F
        parent.addView(this)
        parent.setOnClickListener { this.performClick() }
        this.reposition()
    }

    fun reposition() {
        parent.x = firstVertex.x + firstVertex.width / 2
        parent.y = firstVertex.y + firstVertex.height / 2
        val distance = firstVertex.distance(secondVertex)
        parent.layoutParams = ConstraintLayout.LayoutParams(distance.toInt(),
           10 * edgeHeight)
        val leg = kotlin.math.abs(firstVertex.x - secondVertex.x)
        val angle = kotlin.math.acos(leg / distance).convertToDegrees()
        val rotation =
            if (firstVertex.x <= secondVertex.x && firstVertex.y >= secondVertex.y) {
                360F - angle
            } else if (firstVertex.x >= secondVertex.x && firstVertex.y >= secondVertex.y) {
                180F + angle
            } else if (firstVertex.x >= secondVertex.x) {
                180F - angle
            } else {
                angle
            }
        parent.rotation = rotation
        this.layoutParams = ConstraintLayout.LayoutParams(distance.toInt(),
             edgeHeight)
    }

    fun destruct() {
        firstVertex.edges.remove(this)
        secondVertex.edges.remove(this)
    }

    fun getOtherVertex(vertex: Vertex): Vertex = if (vertex == firstVertex) secondVertex else firstVertex

}
