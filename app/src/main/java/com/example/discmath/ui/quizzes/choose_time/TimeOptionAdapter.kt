package com.example.discmath.ui.quizzes.choose_time

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class TimeOptionAdapter(private val times: Array<String>,
                        private val itemClickedCallback: (String) -> Unit):
        RecyclerView.Adapter<TimeOptionAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView

        init {
            timeTextView = view.findViewById(R.id.timeOptionText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.time_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeInMinutes = times[position]
        holder.timeTextView.text = timeInMinutes
        holder.itemView.setOnClickListener { itemClickedCallback(timeInMinutes) }
    }

    override fun getItemCount(): Int = times.size
}