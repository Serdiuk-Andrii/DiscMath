package com.example.discmath.ui.learning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R

class VideoTimestampAdapter(private val dataset: Array<Pair<String, String>>)
    : RecyclerView.Adapter<VideoTimestampAdapter.TimestampHolder>() {


      class TimestampHolder(view: View) : RecyclerView.ViewHolder(view) {

          val timestampTime: TextView
          val timestampTitle: TextView

          init {
              timestampTime = view.findViewById(R.id.timestamp_time)
              timestampTitle = view.findViewById(R.id.timestamp_title)
          }

      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimestampHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_timestamp, parent, false)
        return TimestampHolder(view)
    }

    override fun onBindViewHolder(holder: TimestampHolder, position: Int) {
        val timestamp = dataset[position]
        holder.timestampTime.text = timestamp.first
        holder.timestampTitle.text = timestamp.second
    }

    override fun getItemCount(): Int = dataset.size
}