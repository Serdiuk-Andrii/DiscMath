package com.example.discmath.ui.learning.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.ui.learning.parseTimestampToTimeInSeconds
import kotlin.properties.Delegates

class VideoTimestampAdapter(private val dataset: Array<Pair<String, String>>,
    private val callback:  ((Float, Int) -> Unit))
    : RecyclerView.Adapter<VideoTimestampAdapter.TimestampHolder>() {

    private lateinit var currentTimestampView: TimestampHolder


      class TimestampHolder(view: View) : RecyclerView.ViewHolder(view) {

          val timestampTime: TextView
          val timestampTitle: TextView
          var timeInSeconds by Delegates.notNull<Float>()

          private val timestampTimeParentLayout: FrameLayout
          private val timestampTitleParentLayout: FrameLayout

          private val timestampTitleBackground: Drawable

          init {
              timestampTime = view.findViewById(R.id.timestamp_time)
              timestampTitle = view.findViewById(R.id.timestamp_title)

              timestampTimeParentLayout = view.findViewById(R.id.timestamp_time_layout)
              timestampTitleParentLayout = view.findViewById(R.id.timestamp_title_layout)

              timestampTitleBackground = timestampTitleParentLayout.background.constantState!!
                    .newDrawable()
          }

          fun switchToSelectedBackground() {
             timestampTitleParentLayout.background = timestampTimeParentLayout.background
          }

          fun switchToUnselectedBackground() {
              timestampTitleParentLayout.background = timestampTitleBackground
          }

      }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimestampHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_timestamp, parent, false)
        return TimestampHolder(view)
    }

    override fun onBindViewHolder(holder: TimestampHolder, position: Int) {
        if (position == 0) {
            currentTimestampView = holder
            currentTimestampView.switchToSelectedBackground()
        }
        val timestamp = dataset[position]
        holder.timestampTime.text = timestamp.first
        holder.timestampTitle.text = timestamp.second
        holder.timeInSeconds = timestamp.first.parseTimestampToTimeInSeconds().toFloat()
        holder.itemView.setOnClickListener {
            switchCurrentTimestamp(holder)
            callback(holder.timeInSeconds, position)
        }
    }

    fun switchCurrentTimestamp(holder: TimestampHolder) {
        currentTimestampView.switchToUnselectedBackground()
        holder.switchToSelectedBackground()
        currentTimestampView = holder
    }

    override fun getItemCount(): Int = dataset.size
}