package com.example.discmath.ui.assistant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class AssistantOptionsAdapter(private val dataset: Array<AssistantFunctionElement>,
                              private val itemClickedCallback:  ((Int) -> Unit)):
    RecyclerView.Adapter<AssistantOptionsAdapter.OptionViewHolder>() {

    class OptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val optionTitle: TextView
        val image: ImageView
        val functionsText: TextView

        init {
            optionTitle = view.findViewById(R.id.option_title)
            image = view.findViewById(R.id.option_image)
            functionsText = view.findViewById(R.id.functions_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.assistant_option, parent, false)

        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = dataset[position]
        holder.optionTitle.text = item.title
        holder.itemView.setOnClickListener {
            itemClickedCallback(dataset[position].destinationFragmentId)
        }
        holder.image.setImageDrawable(item.drawable)
        holder.functionsText.text = item.getFunctionsPrettyText()
    }

    override fun getItemCount(): Int = dataset.size
}