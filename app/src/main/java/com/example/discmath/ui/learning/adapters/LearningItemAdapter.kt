package com.example.discmath.ui.learning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.entity.learning_item.LearningItem
import com.example.discmath.ui.util.navigation.getFormattedText

class LearningItemAdapter(private val dataSet: Array<LearningItem>,
                          private val itemClickedCallback:  ((TextView, LearningItem) -> Unit)):
    RecyclerView.Adapter<LearningItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val learningItemName: TextView
        val learningItemDescription: TextView
        val pdfIcon: ImageView
        val videoIcon: ImageView

        init {
            learningItemName = view.findViewById(R.id.learning_item_name)
            learningItemDescription = view.findViewById(R.id.learning_item_description)
            pdfIcon = view.findViewById(R.id.pdf)
            videoIcon = view.findViewById(R.id.video)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.learning_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val learningItem = dataSet[position]
        holder.learningItemName.text = learningItem.name
        if (learningItem.description != null) {
            holder.learningItemDescription.text = (learningItem.description.
            filterIsInstance<String>().toTypedArray()).getFormattedText()
        } else {
            holder.learningItemDescription.visibility = View.GONE
        }
        if (learningItem.urlPdf.isNotEmpty()) {
            holder.pdfIcon.setImageResource(R.drawable.pdf)
        } else {
            holder.pdfIcon.visibility = View.GONE
        }
        if (learningItem.urlVideo.isNotEmpty()) {
            holder.videoIcon.setImageResource(R.drawable.play)
        } else {
            holder.videoIcon.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            itemClickedCallback(holder.learningItemName, learningItem)
        }
    }

    override fun getItemCount(): Int  = dataSet.size

}