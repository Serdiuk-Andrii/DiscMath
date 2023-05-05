package com.example.discmath.ui.assistant.graph_theory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.toggleVisibility
import com.example.discmath.ui.assistant.graph_theory.GraphData

const val GRAPH_SELECTED_ELEVATION_FACTOR = 100F
const val GRAPH_SELECTED_SCALE_FACTOR = 0.9F

class GraphHistorySelectionAdapter(
    private val dataset: MutableList<GraphData>,
    private val graphSelectedCallback: ((GraphData) -> Unit)
) :
    RecyclerView.Adapter<GraphHistorySelectionAdapter.GraphSelectorViewHolder>() {


    class GraphSelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val graphSnapshot: ImageView
        private val graphSelector: ImageView

        init {
            graphSnapshot = view.findViewById(R.id.graph_image)
            graphSelector = view.findViewById(R.id.graph_image_selector)
        }

        fun toggleSelection() {
            val result = this.graphSelector.toggleVisibility()
            val view = this.itemView
            view.elevation = if (result) GRAPH_SELECTED_ELEVATION_FACTOR else 0F
            view.scaleX = if (result) GRAPH_SELECTED_SCALE_FACTOR else 1F
            view.scaleY = view.scaleX
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphSelectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.graph_history_selector_item, parent, false)

        return GraphSelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: GraphSelectorViewHolder, position: Int) {
        val graph = dataset[position]
        holder.graphSnapshot.setImageBitmap(graph.snapshot)
        holder.graphSnapshot.setOnClickListener {
            holder.toggleSelection()
            graphSelectedCallback(graph)
        }
    }

    override fun getItemCount(): Int = dataset.size

}