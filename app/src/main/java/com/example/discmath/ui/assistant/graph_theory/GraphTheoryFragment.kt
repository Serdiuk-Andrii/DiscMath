package com.example.discmath.ui.assistant.graph_theory

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.allViews
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentGraphTheoryBinding
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.example.discmath.ui.util.color.getColor
import com.example.graph_theory.Graph
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.PI
import java.util.*
import kotlin.properties.Delegates


fun Float.convertToDegrees(): Float {
    return (this / PI * 180).toFloat()
}

fun Collection<Vertex>.getEdges(): Set<Edge> = this.map { it.edges }.flatMap { it.toSet() }.toSet()


fun List<Vertex>.getGraph(): Graph = Graph(this.map { vertex -> vertex.getNeighbours().map { it.vertexId } })

class GraphTheoryFragment : Fragment() {

    enum class EditorState {
        MODIFY, EDGE, MOVE
    }

    // Views
    private var _binding: FragmentGraphTheoryBinding? = null
    private lateinit var layout: ConstraintLayout
    private lateinit var toolbox: LinearLayout
    private lateinit var finishButton: ImageView
    private lateinit var removeButton: FloatingActionButton

    // ViewModels
    private lateinit var graphBuilderViewModel: GraphBuilderViewModel

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
    val vertices: MutableList<Vertex> = mutableListOf()

    private val nextId get() = vertexNextId++

    private var state: EditorState = EditorState.MODIFY
    var currentGraphIndex: Int? = null

    // Navigation
    private lateinit var navController: NavController

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
        navController = findNavController()
        initializeViewModels()
        initializeViewData()
        initializeDrawables()
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializeViewModels() {
        graphBuilderViewModel = ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
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
        fixedViews.add(layout)
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
        initializeToolbox()
        val scaleDetector = ScaleGestureDetector(requireContext(), scaleListener)
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

        finishButton.setOnClickListener {
            it.isEnabled = false
            finish()
            it.postDelayed({ it.isEnabled = true }, 1000)
        }

        layout.setOnTouchListener(object : OnTouchListener {

            var lastX: Float = 0F
            var lastY: Float = 0F

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                val action = event.action
                if (action == MotionEvent.ACTION_DOWN) {
                    if (state == EditorState.MODIFY) {
                        val vertex = createVertex(event.x, event.y)
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

    }

    private fun initializeToolbox() {
        // Initialize toolbox
        toolbox = binding.graphBuilderToolbox.toolboxLayout
        moveOption = binding.graphBuilderToolbox.graphBuilderMoveOption
        edgeOption = binding.graphBuilderToolbox.graphBuilderEdgeModeOption
        modifyOption = binding.graphBuilderToolbox.graphBuilderModifyModeOption
        removeButton = binding.removeButton
        finishButton = binding.finishButton

        fixedViews.add(removeButton)
        fixedViews.add(finishButton)

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
        fixedViews.addAll(toolbox.allViews)
    }

    private fun finish() {
        val bottomSheet = GraphBuilderBottomSheet(this)
        bottomSheet.show(requireActivity().supportFragmentManager, GraphBuilderBottomSheet.TAG)
    }

    fun getGraph(): Graph {
        return this.vertices.getGraph()
    }

    fun createPng() {
        val bitmap = createGraphBitmap()
        val fileName = "mygraph.png"
        // Files created in the cache directory are removed automatically when necessary
        val file = File(requireActivity().cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri = FileProvider.getUriForFile(requireContext(),
            "${requireContext().packageName}.provider", file)

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/png"
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(Intent.createChooser(shareIntent,
                resources.getString(R.string.graph_builder_export_to_png_intent_title)
            ))

    }

    fun createGraphBitmap(): Bitmap {
        /*
        vertices.sortWith { first, second ->
            when {
                first.x < second.x -> -1
                first.x > second.x -> 1
                else -> 0
            }
        }

        val smallest = vertices[0].x
        vertices.sortWith { first, second ->
            when {
                first.x < second.x -> 1
                first.x > second.x -> -1
                else -> 0
            }
        }
        val largest = vertices[0].x
         */

        // Get the dimensions of the layout
        val width = layout.width //(largest - smallest).toInt() + 50
        val height = layout.height

        // Create a bitmap to draw the graph onto
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        // Create a canvas to draw onto the bitmap
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 5F

        vertices.getEdges().forEach {
            val startX = it.firstVertex.x + vertexWidth / 2F
            val startY = it.firstVertex.y + vertexHeight / 2F
            val endX = it.secondVertex.x + vertexWidth / 2F
            val endY = it.secondVertex.y + vertexHeight / 2F
            canvas.drawLine(startX, startY, endX, endY, paint)
        }

        // val translateX: Float = if(smallest < 0) -smallest else 0F

        vertices.forEach {

            val drawable = it.background.constantState!!.newDrawable().mutate()
            val left = it.x.toInt()
            val top = it.y.toInt()
            val right = left + vertexWidth
            val bottom = top + vertexHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }

        return bitmap
    }

    fun clear() {
        vertices.getEdges().forEach { layout.removeView(it.parent) }
        vertices.forEach { layout.removeView(it) }
        vertices.clear()
        selectedVertex = null
        selectedEdge = null
        state = EditorState.MOVE
        updateToolboxOnCurrentOption(moveOption)
    }

    fun getGraphConnectedComponents(graph: Graph) {
        val connectedComponents = graph.connectedComponents
        for ((index, component) in connectedComponents.withIndex()) {
            val componentDrawable = vertexStillBackground.constantState!!.newDrawable()
            val colorList = ColorStateList.valueOf(getColor(index).toColorInt())
            componentDrawable.setTintList(colorList)
            vertices.filter { vertex -> component.contains(vertex.vertexId) }.forEach {
                it.background = componentDrawable
            }
        }

    }

    fun getGraphCutVertices(graph: Graph) {
        val cutVertices = graph.cutVertices
        vertices.filter { cutVertices.contains(it.vertexId) }.forEach {
            it.alpha = 0.75F
        }
        Toast.makeText(context, "Time: ${cutVertices.size}", Toast.LENGTH_SHORT).show()
    }

    fun getGraphBridges(graph: Graph) {
        val bridges = graph.bridges
        bridges.map { pair -> vertices.find { it.vertexId == pair.first }!!
            .getCorrespondingEdge(pair.second) }
            .forEach { it.background.alpha = (0.5 * 255).toInt() }
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
                    if (state == EditorState.EDGE) {
                        switchSelectedEdge(edge)
                        removeButton.visibility = View.VISIBLE
                    }
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
        removeButton.visibility = View.GONE
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

    @SuppressLint("ClickableViewAccessibility")
    private fun createVertex(x: Float, y: Float): Vertex {
        val vertex = Vertex(requireContext(), nextId)
        vertex.background = vertexStillBackground
        vertex.layoutParams = ConstraintLayout.LayoutParams(vertexWidth, vertexHeight)
        vertex.x = x - vertexWidth / 2
        vertex.y = y - vertexHeight / 2
        vertex.setOnTouchListener(VertexTouchListener(this@GraphTheoryFragment))
        return vertex
    }

    fun resetVertices(newVertices: Collection<Vertex>) {
        vertices.clear()
        vertices.addAll(newVertices)
        val edges = newVertices.getEdges()
        // TODO: Understand why this is necessary
        val newBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.circle_background, null
        )!!
        vertices.forEach {
            it.background = newBackground
            layout.addView(it)
        }
        edges.forEach { layout.addView(it.parent) }
    }

}
