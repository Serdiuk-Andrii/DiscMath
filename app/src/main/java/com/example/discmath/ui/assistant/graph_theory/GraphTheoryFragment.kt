package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentGraphTheoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Math.PI
import kotlin.properties.Delegates


fun Float.convertToDegrees(): Float {
    return (this / PI * 180).toFloat()
}

class GraphTheoryFragment : Fragment() {

    enum class EditorState {
        MODIFY, EDGE, MOVE
    }

    // Views
    private var _binding: FragmentGraphTheoryBinding? = null
    private lateinit var layout: ConstraintLayout
    private lateinit var toolbox: LinearLayout
    private lateinit var removeButton: FloatingActionButton

    // Toolbox
    private lateinit var modifyOption: View
    private lateinit var edgeOption: View
    private lateinit var moveOption: View

    private val fixedViews: MutableList<View> = mutableListOf()

    // View data
    private var vertexWidth by Delegates.notNull<Int>()
    private var vertexHeight by Delegates.notNull<Int>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // State
    private var selectedVertex: Vertex? = null
    private var selectedEdge: Edge? = null
    private var vertexNextId: Int = 0
    private val vertices: MutableList<Vertex> = mutableListOf()

    private val nextId get() = vertexNextId++

    private var state: EditorState = EditorState.MODIFY

    // Drawables
    private lateinit var vertexStillBackground: Drawable
    private lateinit var vertexSelectedBackground: Drawable

    // Colors
    private lateinit var selectedToolboxOptionColor: ColorStateList
    private lateinit var unselectedToolboxOptionColor: ColorStateList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGraphTheoryBinding.inflate(inflater, container, false)
        initializeViewData()
        initializeDrawables()
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }


    private fun initializeViewData() {
        vertexWidth = resources.getDimension(R.dimen.graph_vertex_width).toInt()
        vertexHeight = resources.getDimension(R.dimen.graph_vertex_height).toInt()

        selectedToolboxOptionColor = requireContext().
            getColorStateList(R.color.graph_builder_toolbox_option_background)
        unselectedToolboxOptionColor = requireContext().getColorStateList(R.color.white)
    }

    private fun initializeDrawables() {
        vertexStillBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.circle_background, null
        )!!
        vertexSelectedBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.circle_with_border_background, null
        )!!
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeViews() {
        layout = binding.graphBuilderLayout
        val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            private val minScale = 0.5F
            private val maxScale = 2F

            private var scaleFactor: Float = 1F

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                if (state == EditorState.MOVE) {
                    scaleFactor =
                        maxOf(minScale, minOf(scaleFactor * detector.scaleFactor, maxScale))
                    //val width = layout.width
                    //val height = layout.height
                    //layout.layoutParams = FrameLayout.LayoutParams(width, height)
                    layout.allViews.forEach {
                        if (it !is Edge && it !in toolbox.allViews) {
                            /*
                            val newX = layout.width / 2F -
                                    scaleFactor * (layout.width / 2F - it.x)
                            val newY = layout.height / 2F -
                                    scaleFactor * (layout.height / 2F - it.y)
                            */
                            it.animate()
                                .setDuration(1000)
                                .setInterpolator { 0.85f }
                                .scaleX(scaleFactor)
                                .scaleY(scaleFactor)
                                .start()
                        }
                    }

                }
                return true
            }
        }

        val scaleDetector = ScaleGestureDetector(requireContext(), scaleListener)

        // Initialize toolbox
        toolbox = binding.graphBuilderToolbox.toolboxLayout
        moveOption = binding.graphBuilderToolbox.graphBuilderMoveOption
        edgeOption = binding.graphBuilderToolbox.graphBuilderEdgeModeOption
        modifyOption = binding.graphBuilderToolbox.graphBuilderModifyModeOption
        removeButton = binding.removeButton

        fixedViews.add(removeButton)

        modifyOption.backgroundTintList = selectedToolboxOptionColor

        moveOption.setOnClickListener {
            selectGivenToolboxOption(moveOption)
            state = EditorState.MOVE
        }
        edgeOption.setOnClickListener {
            selectGivenToolboxOption(edgeOption)
            state = EditorState.EDGE
        }
        modifyOption.setOnClickListener {
            selectGivenToolboxOption(modifyOption)
            state = EditorState.MODIFY
        }

        removeButton.setOnClickListener {
            when(state) {
                EditorState.MODIFY -> {
                    vertices.remove(selectedVertex)
                    layout.removeView(selectedVertex)
                    for (edge in selectedVertex?.edges!!) {
                        layout.removeView(edge.parent)
                        edge.destruct()
                    }
                }
                EditorState.EDGE -> {
                    selectedEdge?.destruct()
                    layout.removeView(selectedEdge?.parent)
                }
                else -> {}
            }
            removeButton.visibility = View.GONE
        }

        fixedViews.add(layout)
        fixedViews.addAll(toolbox.allViews)
        layout.setOnTouchListener(object : OnTouchListener {

            var lastX: Float = 0F
            var lastY: Float = 0F

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                val action = event.action
                if (action == MotionEvent.ACTION_DOWN) {
                    if (state == EditorState.MODIFY) {
                        val vertex = Vertex(requireContext(), nextId)
                        vertex.background = vertexStillBackground
                        vertex.layoutParams =
                            ConstraintLayout.LayoutParams(vertexWidth, vertexHeight)
                        vertex.x = event.x - vertexWidth / 2
                        vertex.y = event.y - vertexHeight / 2
                        vertex.setOnTouchListener(VertexTouchListener(this@GraphTheoryFragment))
                        vertices.add(vertex)
                        layout.addView(vertex)
                        layout.performClick()
                    }

                    lastX = event.rawX
                    lastY = event.rawY
                } else if (action == MotionEvent.ACTION_MOVE) {

                    layout.allViews.forEach {
                        if (it !in fixedViews && it !is Edge) {
                            it.animate()
                                .x(it.x - (lastX - event.rawX))
                                .y(it.y - (lastY - event.rawY))
                                .setDuration(0)
                                .setInterpolator { 0.95f }

                                .start()
                        }
                    }
                    lastX = event.rawX
                    lastY = event.rawY
                }
                scaleDetector.onTouchEvent(event)
                return true
            }
        })

        /*
                layout.allViews.forEach { view ->
                    if (view != layout) {
                        run {
                            view.scaleX = view.scaleX * 0.5F
                            view.scaleY = view.scaleY * 0.5F
                        }
                    }
         }
         */
    }

    private fun selectGivenToolboxOption(option: View) {
        disableFocusOnCurrentVertex()
        disableFocusOnCurrentEdge()
        removeButton.visibility = View.GONE
        updateToolboxOnCurrentOption(option)
    }

    fun notifyVertexSelected(vertex: Vertex) {
        if (state != EditorState.MODIFY && state != EditorState.EDGE) {
            return
        }
        if (state == EditorState.EDGE) {
            if (selectedVertex != null && vertex != selectedVertex &&
                !vertex.isAdjacent(selectedVertex!!)
            ) {
                val edge = Edge(requireContext(), selectedVertex!!, vertex)
                edge.setOnClickListener {
                    switchSelectedEdge(edge)
                    removeButton.visibility = View.VISIBLE
                }
                layout.addView(edge.parent)
                //layout.addView(edge)
                disableFocusOnCurrentVertex()
            } else {
                switchSelectedVertex(vertex)
            }
        } else if (state == EditorState.MODIFY) {
            removeButton.visibility = View.VISIBLE
            switchSelectedVertex(vertex)
        }
    }

    private fun updateToolboxOnCurrentOption(option: View) {
        toolbox.allViews.forEach {
            if (it is FrameLayout && it != option) {
                it.backgroundTintList = unselectedToolboxOptionColor
            }
        }
        option.backgroundTintList = selectedToolboxOptionColor
    }

    private fun disableFocusOnCurrentVertex() = switchSelectedVertex(null)
    private fun disableFocusOnCurrentEdge() = switchSelectedEdge(null)

    private fun switchSelectedVertex(vertex: Vertex?) {
        selectedVertex?.background = vertexStillBackground
        selectedVertex = vertex
        selectedVertex?.background = vertexSelectedBackground
    }

    private fun switchSelectedEdge(edge: Edge?) {
        selectedEdge?.setBackgroundColor(requireContext().getColor(R.color.black))
        selectedEdge = edge
        selectedEdge?.setBackgroundColor(requireContext().getColor(R.color.edge_selected_color))
    }

    private class VertexTouchListener(val fragment: GraphTheoryFragment) : OnTouchListener {

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

}