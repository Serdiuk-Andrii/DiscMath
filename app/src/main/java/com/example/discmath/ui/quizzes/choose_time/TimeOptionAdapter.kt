package com.example.discmath.ui.quizzes.choose_time

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class TimeOptionAdapter(private val times: Array<Pair<String, Drawable>>,
                        private val itemClickedCallback: (String) -> Unit):
        RecyclerView.Adapter<TimeOptionAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView
        val imageView: ImageView

        init {
            timeTextView = view.findViewById(R.id.timeOptionText)
            imageView = view.findViewById(R.id.time_option_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeOption = times[position]
        holder.timeTextView.text = timeOption.first
        holder.imageView.setImageDrawable(timeOption.second)
        holder.itemView.setOnClickListener { itemClickedCallback(timeOption.first) }
    }

    override fun getItemCount(): Int = times.size
}