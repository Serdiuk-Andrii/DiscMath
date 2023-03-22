package com.example.discmath.ui.quiz_fragment.choose_sections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.entity.learning_section.LearningSection

class LearningSectionTestOptionAdapter(private val sections: Array<LearningSection>,
                                        private val itemClickedCallback:
                                            (LearningSection, Boolean) -> Unit):
        RecyclerView.Adapter<LearningSectionTestOptionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        var selected: Boolean = true

        init {
            name = view.findViewById(R.id.section_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.section_quiz_option, parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        holder.name.text = section.name
        holder.itemView.setOnClickListener {
            holder.selected = !holder.selected
            if (holder.selected) {
                holder.itemView.alpha = 1F
            } else {
                holder.itemView.alpha = 0.5F
            }
            itemClickedCallback(section, holder.selected)
        }
        itemClickedCallback(section, holder.selected)
    }

    override fun getItemCount(): Int = sections.size
}