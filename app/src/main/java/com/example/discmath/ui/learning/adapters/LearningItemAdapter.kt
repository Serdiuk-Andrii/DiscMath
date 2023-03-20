package com.example.discmath.ui.learning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.ui.entity.learning_item.LearningItem

class LearningItemAdapter(private val dataSet: Array<LearningItem>,
                          private val itemClickedCallback:  ((LearningItem) -> Unit)):
    RecyclerView.Adapter<LearningItemAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val learningItemName: TextView
        val pdfIcon: ImageView
        val videoIcon: ImageView

        init {
            // Define click listener for the ViewHolder's View
            learningItemName = view.findViewById(R.id.itemName)
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
        holder.itemView.setOnClickListener { itemClickedCallback(learningItem) }
    }

    override fun getItemCount(): Int  = dataSet.size

}