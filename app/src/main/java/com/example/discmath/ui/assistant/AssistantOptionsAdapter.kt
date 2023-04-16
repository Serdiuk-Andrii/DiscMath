package com.example.discmath.ui.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class AssistantOptionsAdapter(private val dataset: Array<AssistantOption>,
                              private val itemClickedCallback:  ((Int) -> Unit)):
    RecyclerView.Adapter<AssistantOptionsAdapter.OptionViewHolder>() {

    class OptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val optionTitle: TextView

        init {
            optionTitle = view.findViewById(R.id.option_title)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assistant_option, parent, false)

        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionTitle.text = dataset[position].title
        holder.itemView.setOnClickListener {
            itemClickedCallback(dataset[position].destinationFragmentId)
        }
    }

    override fun getItemCount(): Int = dataset.size
}