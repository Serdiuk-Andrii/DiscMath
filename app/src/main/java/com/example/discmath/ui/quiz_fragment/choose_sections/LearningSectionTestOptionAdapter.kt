package com.example.discmath.ui.quiz_fragment.choose_sections

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.entity.learning_section.LearningSection


const val OPACITY_ITEM_SELECTED = 0.7F
const val OPACITY_ITEM_NOT_SELECTED = 1F
class LearningSectionTestOptionAdapter(private val sections: Array<LearningSection>,
                                        private val itemClickedCallback:
                                            (LearningSection, Boolean) -> Unit):
        RecyclerView.Adapter<LearningSectionTestOptionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        var selected: Boolean = true

        init {
            name = view.findViewById(R.id.section_name)
            view.alpha = OPACITY_ITEM_SELECTED
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
                holder.itemView.alpha = OPACITY_ITEM_SELECTED
            } else {
                holder.itemView.alpha = OPACITY_ITEM_NOT_SELECTED
            }
            itemClickedCallback(section, holder.selected)
        }
        itemClickedCallback(section, holder.selected)
    }

    override fun getItemCount(): Int = sections.size
}