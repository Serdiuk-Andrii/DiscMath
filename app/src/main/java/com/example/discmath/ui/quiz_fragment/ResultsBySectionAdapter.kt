package com.example.discmath.ui.quiz_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class ResultsBySectionAdapter(private val dataset: Array<Pair<String, String>>):
    RecyclerView.Adapter<ResultsBySectionAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val sectionName: TextView
        val sectionResults: TextView

        init {
            sectionName = view.findViewById(R.id.quiz_section_result_name)
            sectionResults = view.findViewById(R.id.quiz_section_result_correct_answers_value)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.quiz_section_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataset[position]
        holder.sectionName.text = data.first
        holder.sectionResults.text = data.second
    }

    override fun getItemCount(): Int = dataset.size
}