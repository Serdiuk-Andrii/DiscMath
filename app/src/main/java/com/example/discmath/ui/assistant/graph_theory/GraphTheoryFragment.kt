package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentGraphTheoryBinding
import java.lang.Math.PI
import kotlin.properties.Delegates


fun Float.convertToDegrees(): Float {
    return (this / PI * 180).toFloat()
}

class GraphTheoryFragment : Fragment() {

    // Views
    private var _binding: FragmentGraphTheoryBinding? = null
    private lateinit var layout: ConstraintLayout

    // View data
    private var vertexWidth by Delegates.notNull<Int>()
    private var vertexHeight by Delegates.notNull<Int>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // State
    private var selectedVertex: Vertex? = null
    private var vertexNextId: Int = 0

    private val nextId get() = vertexNextId++


    // Drawables
    private lateinit var vertexStillBackground: Drawable
    private lateinit var vertexSelectedBackground: Drawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGraphTheoryBinding.inflate(inflater, container, false)
        initializeViewData()
        initializeDrawables()
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun initializeViewData() {
        vertexWidth = resources.getDimension(R.dimen.graph_vertex_width).toInt()
        vertexHeight= resources.getDimension(R.dimen.graph_vertex_height).toInt()
    }

    private fun initializeDrawables() {
        vertexStillBackground = ResourcesCompat.getDrawable(resources,
            R.drawable.circle_background, null)!!
        vertexSelectedBackground = ResourcesCompat.getDrawable(resources,
            R.drawable.circle_with_border_background, null)!!
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeViews() {
        layout = binding.graphBuilderLayout

        layout.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                val newVertex = Vertex(requireContext(), nextId)
                newVertex.background = vertexStillBackground
                newVertex.layoutParams = ConstraintLayout.LayoutParams(vertexWidth, vertexHeight)
                newVertex.elevation = 15F
                newVertex.x = event.x - vertexWidth / 2
                newVertex.y = event.y - vertexHeight / 2
                newVertex.setOnTouchListener(VertexTouchListener(this))
                layout.addView(newVertex)
                layout.performClick()
            }
            true
        }

    }

    fun notifyVertexSelected(vertex: Vertex) {
        if (selectedVertex != null && vertex != selectedVertex) {
            val edge = Edge(requireContext(), selectedVertex!!, vertex)
            layout.addView(edge)
            selectedVertex!!.background = vertexStillBackground
            selectedVertex = null
        } else {
            selectedVertex?.background = vertexStillBackground
            selectedVertex = vertex
            selectedVertex!!.background = vertexSelectedBackground
        }
    }

    private class VertexTouchListener(val fragment: GraphTheoryFragment): OnTouchListener {

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
            }
            return true
        }

    }

}