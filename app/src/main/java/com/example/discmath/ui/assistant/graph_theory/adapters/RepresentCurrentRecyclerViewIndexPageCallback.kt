package com.example.discmath.ui.assistant.graph_theory.adapters

import android.content.res.Resources
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R

class RepresentCurrentRecyclerViewIndexPageCallback(
    private val counter: TextView,
    val adapter: RecyclerView.Adapter<*>,
    val resources: Resources):
    ViewPager2.OnPageChangeCallback() {


    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        counter.text = String.format(
            resources.getString(R.string.graph_history_counter),
            position + 1, adapter.itemCount)
    }
}