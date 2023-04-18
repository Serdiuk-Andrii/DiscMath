package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.discmath.R

@SuppressLint("ViewConstructor")
class Edge(currentContext: Context, val firstVertex: Vertex, val secondVertex: Vertex) :
    View(currentContext) {

    init {
        this.setBackgroundColor(resources.getColor(R.color.black, null))
        this.elevation = 10F
        this.z = -1F
        this.pivotX = 0F
        this.pivotY = 0F
        firstVertex.addEdge(this)
        secondVertex.addEdge(this)
        this.reposition()
    }

    fun reposition() {
        this.x = firstVertex.x + firstVertex.width / 2
        this.y = firstVertex.y + firstVertex.height / 2
        val distance = firstVertex.distance(secondVertex)
        this.layoutParams = ConstraintLayout.LayoutParams(distance.toInt(), 5)
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
        this.rotation = rotation
    }


}