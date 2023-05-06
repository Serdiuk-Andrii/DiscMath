package com.example.discmath.ui.learning.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.learning_section.SECTIONS_COLLECTION_STORAGE_PATH
import com.example.discmath.entity.quizzes.loadImageIntoViewFrom
import com.example.discmath.ui.learning.COLLECTION_PATH_KEY

const val SECTION_NAME_KEY = "name"
const val SECTION_DESCRIPTION_KEY = "description"
const val SECTION_IMAGE_URL_KEY = "image"

class LearningSectionObjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.learning_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loader: RequestManager = Glide.with(view)
        val card: CardView = view.findViewById(R.id.learning_section_card)
        val sectionName: TextView = view.findViewById(R.id.sectionTitle)
        val sectionDescription: TextView = view.findViewById(R.id.section_description)
        val backgroundImage: ImageView = view.findViewById(R.id.section_background_image)

        val sectionNameText = requireArguments().getString(SECTION_NAME_KEY)!!
        val sectionDescriptionText = requireArguments().getString(SECTION_DESCRIPTION_KEY)!!
        val sectionCollectionPath = requireArguments().getString(SECTIONS_COLLECTION_STORAGE_PATH)!!

        sectionName.text = sectionNameText
        sectionDescription.text = sectionDescriptionText
        val imageUrl = requireArguments().getString(SECTION_IMAGE_URL_KEY)
        if (imageUrl != null) {
            loadImageIntoViewFrom(
                imageUrl,
                backgroundImage, loader
            )
        }
        val section = LearningSection(sectionNameText, sectionDescriptionText,
            imageUrl, -1, sectionCollectionPath)
        card.setOnClickListener { navigateToLearningSection(sectionName, section) }
    }

    private fun navigateToLearningSection(view: View, learningSection: LearningSection) {
        val transitionName = getString(R.string.learning_section_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        val navController = findNavController()
        navController.navigate(
            R.id.specificLearningSectionFragment,
            Bundle().apply {
                putString(SECTION_NAME_KEY, learningSection.name)
                putString(COLLECTION_PATH_KEY, learningSection.getLearningElementsPath())
            },
            null,
            extras
        )
    }

}