package com.example.discmath.ui.quiz_fragment.choose_time

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class TimeOptionAdapter(private val timesInMinutes: Array<Int>,
                        private val itemClickedCallback: (Int) -> Unit):
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
        val timeInMinutes = timesInMinutes[position]
        holder.timeTextView.text = timeInMinutes.toString()
        holder.itemView.setOnClickListener { itemClickedCallback(timeInMinutes) }
    }

    override fun getItemCount(): Int = timesInMinutes.size
}