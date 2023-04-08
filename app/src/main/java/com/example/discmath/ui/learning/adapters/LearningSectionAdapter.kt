package com.example.discmath.ui.learning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.quizzes.loadImageIntoViewFrom

class LearningSectionAdapter(private val dataSet: Array<LearningSection>,
                             private val itemClickedCallback:  ((LearningSection) -> Unit))
    :RecyclerView.Adapter<LearningSectionAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val backgroundImage: ImageView
        val learningSectionTitle: TextView

        init {
           learningSectionTitle = view.findViewById(R.id.sectionTitle)
           backgroundImage = view.findViewById(R.id.section_background_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.learning_section, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO: Consider this line carefully
        val loader: RequestManager = Glide.with(holder.itemView)
        val learningSection = dataSet[position]
        holder.learningSectionTitle.text = learningSection.name

        if (learningSection.backgroundImageUrl != null) {
            loadImageIntoViewFrom(
                learningSection.backgroundImageUrl,
                holder.backgroundImage, loader
            )
        }
        holder.itemView.setOnClickListener {
            itemClickedCallback(learningSection)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}