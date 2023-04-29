package com.example.discmath.ui.assistant.graph_theory.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.ui.assistant.NamedActionElement

class GraphActionAdapter(private val dataset: Array<NamedActionElement>):
    RecyclerView.Adapter<GraphActionAdapter.ActionViewHolder>(){


    class ActionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val actionTitle: TextView

        init {
            actionTitle = view.findViewById(R.id.graph_action_title)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.graph_action, parent, false)

        return ActionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val actionElement = dataset[position]
        holder.actionTitle.text = actionElement.title
        holder.itemView.setOnClickListener {
            actionElement.itemClickedCallback()
        }
    }

    override fun getItemCount(): Int = dataset.size
}
