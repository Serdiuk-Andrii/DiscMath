package com.example.discmath.ui.learning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.ui.entity.LearningSection

class LearningSectionAdapter(private val dataSet: Array<LearningSection>,
                             private val itemClickedCallback:  ((LearningSection) -> Unit))
    :RecyclerView.Adapter<LearningSectionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val learningSectionTitle: TextView

        init {
            // Define click listener for the ViewHolder's View
           learningSectionTitle = view.findViewById(R.id.sectionTitle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.learning_section, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val learningSection = dataSet[position]
        holder.learningSectionTitle.text = learningSection.name
        holder.itemView.setOnClickListener {
            itemClickedCallback(learningSection)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}