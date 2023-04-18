package com.example.discmath.ui.assistant.graph_theory

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.discmath.databinding.FragmentGraphTheoryBinding

class GraphTheoryFragment : Fragment() {


    private class MyDragShadowBuilder(view: View): View.DragShadowBuilder(view) {
        private val shadow = view.background.constantState!!.newDrawable()

        override fun onProvideShadowMetrics(size: Point, touch: Point) {

            // Set the width of the shadow to half the width of the original
            // View.
            val width: Int = view.width

            // Set the height of the shadow to half the height of the original
            // View.
            val height: Int = view.height

            // The drag shadow is a ColorDrawable. Set its dimensions to
            // be the same as the Canvas that the system provides. As a result,
            // the drag shadow fills the Canvas.
            shadow.setBounds(0, 0, width, height)

            // Set the size parameter's width and height values. These get back
            // to the system through the size parameter.
            size.set(width, height)

            // Set the touch point's position to be in the middle of the drag
            // shadow.
            touch.set(width / 2, height / 2)
        }

        // Define a callback that draws the drag shadow in a Canvas that the system
        // constructs from the dimensions passed to onProvideShadowMetrics().
        override fun onDrawShadow(canvas: Canvas) {

            // Draw the ColorDrawable on the Canvas passed in from the system.
            shadow.draw(canvas)
        }

        

    }


    // Views
    private var _binding: FragmentGraphTheoryBinding? = null
    private lateinit var layout: ConstraintLayout
    private lateinit var vertex: View

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

    private fun initializeViews() {
        layout = binding.graphBuilderLayout
        vertex = binding.button2
        vertex.setOnDragListener { view, dragEvent ->

            when(dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    Toast.makeText(context, "Dragging started", Toast.LENGTH_SHORT).show()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Toast.makeText(context, "Dragging entered", Toast.LENGTH_SHORT).show()
                    view.invalidate()
                    false
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    Toast.makeText(context, "Dragging location", Toast.LENGTH_SHORT).show()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Toast.makeText(context, "Dragging exited", Toast.LENGTH_SHORT).show()
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    Toast.makeText(context, "Dragging dropped", Toast.LENGTH_SHORT).show()

                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Toast.makeText(context, "Dragging ended", Toast.LENGTH_SHORT).show()
                    val draggedView = dragEvent.localState as View
                    draggedView.x = dragEvent.x - draggedView.width / 2
                    draggedView.y = dragEvent.y - draggedView.height
                    draggedView.alpha = 1F
                    draggedView.invalidate()
                    view.invalidate()
                    true
                }
                else -> false
            }
        }
        vertex.setOnLongClickListener {
            Toast.makeText(context, "Long clicked", Toast.LENGTH_SHORT).show()
            val clipText = "test"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val dragData = ClipData(clipText, mimeTypes, item)
            val dragShadow =  View.DragShadowBuilder(it)
            it.startDragAndDrop(
                dragData,
                dragShadow,
                it,
                0
            )
            vertex.alpha = 0F
            true
        }

    }



}