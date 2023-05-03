package com.example.discmath.ui.assistant.graph_theory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.ui.assistant.graph_theory.GraphData

class GraphHistoryAdapter(private val dataset: MutableList<GraphData> = mutableListOf(),
                            private val graphSelectedCallback: ((GraphData, Int) -> Unit)):
    RecyclerView.Adapter<GraphHistoryAdapter.GraphViewHolder>() {

    class GraphViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val graphSnapshot: ImageView

        init {
            graphSnapshot = view.findViewById(R.id.graph_image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GraphViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.graph_history_item, parent, false)

        return GraphViewHolder(view)
    }

    override fun onBindViewHolder(holder: GraphViewHolder, position: Int) {
        val graph = dataset[position]
        holder.graphSnapshot.setImageBitmap(graph.snapshot)
        holder.graphSnapshot.setOnClickListener { graphSelectedCallback(graph, position) }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}