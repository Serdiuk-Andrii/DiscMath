package com.example.discmath.ui.assistant.graph_theory

import android.view.MotionEvent
import android.view.View

class VertexTouchListener(val fragment: GraphTheoryFragment) : View.OnTouchListener {

    var dx: Float = 0F
    var dy: Float = 0F

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (view == null || event == null || view !is Vertex) {
            return false
        }
        val action = event.action
        if (action == MotionEvent.ACTION_DOWN) {
            dx = view.x - event.rawX
            dy = view.y - event.rawY
            fragment.notifyVertexSelected(view)
            view.performClick()
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            view.animate()
                .x(event.rawX + dx)
                .y(event.rawY + dy)
                .setDuration(0)
                .start()
            view.repositionEdges()
        }
        return true
    }

}