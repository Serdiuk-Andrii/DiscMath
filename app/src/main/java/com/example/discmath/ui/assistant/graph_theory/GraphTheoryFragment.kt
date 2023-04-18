package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentGraphTheoryBinding

class GraphTheoryFragment : Fragment() {

    // Views
    private var _binding: FragmentGraphTheoryBinding? = null
    private lateinit var layout: ConstraintLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGraphTheoryBinding.inflate(inflater, container, false)
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeViews() {
        layout = binding.graphBuilderLayout

        val circleBackground = ResourcesCompat.getDrawable(resources,
            R.drawable.circle_background, null)
        val vertexWidth: Int = resources.getDimension(R.dimen.graph_vertex_width).toInt()
        val vertexHeight: Int = resources.getDimension(R.dimen.graph_vertex_height).toInt()

        layout.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN) {
                val newVertex = Button(context)
                newVertex.background = circleBackground
                newVertex.layoutParams = ConstraintLayout.LayoutParams(vertexWidth, vertexHeight)
                newVertex.x = event.x - vertexWidth / 2
                newVertex.y = event.y - vertexHeight / 2
                newVertex.setOnTouchListener(VertexTouchListener())
                layout.addView(newVertex)
                layout.performClick()
            }
            true
        }

    }

    private class VertexTouchListener: OnTouchListener {

        var dx: Float = 0F
        var dy: Float = 0F

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            view!!
            if (event!!.action == MotionEvent.ACTION_DOWN) {
                dx = view.x - event.rawX
                dy = view.y - event.rawY
                view.performClick()
            } else if (event.action == MotionEvent.ACTION_MOVE) {
                view.animate().x(event.rawX + dx)
                    .y(event.rawY + dy)
                    .setDuration(0)
                    .start()
            }
            return true
        }

    }

}