package com.example.discmath.ui.learning.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.quizzes.loadImageIntoViewFrom

// const val SECTION_NAME_KEY = "section_name"

class LearningSectionObjectFragment(private val section: LearningSection,
                                    val clickCallback: ((View,
                                                         LearningSection) -> Unit)): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.learning_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*
        arguments?.takeIf { it.containsKey(SECTION_NAME_KEY) }?.apply {
            val textView: TextView = view.findViewById(R.id.sectionTitle)
            textView.text = getString(SECTION_NAME_KEY)
        }
        */
        val loader: RequestManager = Glide.with(view)
        val card: CardView = view.findViewById(R.id.learning_section_card)
        val sectionName: TextView = view.findViewById(R.id.sectionTitle)
        val sectionDescription: TextView = view.findViewById(R.id.section_description)
        val backgroundImage: ImageView = view.findViewById(R.id.section_background_image)
        sectionName.text = section.name
        sectionDescription.text = section.description
        if (section.backgroundImageUrl != null) {
            loadImageIntoViewFrom(
                section.backgroundImageUrl,
                backgroundImage, loader
            )
        }
        card.setOnClickListener { clickCallback(sectionName, section) }
    }

}