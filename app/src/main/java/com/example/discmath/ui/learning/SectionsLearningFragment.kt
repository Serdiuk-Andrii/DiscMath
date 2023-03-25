package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningSectionsBinding
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.learning_section.SECTIONS_COLLECTION_STORAGE_PATH
import com.example.discmath.entity.learning_section.SECTION_NAME_STORAGE_PATH
import com.example.discmath.ui.learning.adapters.LearningSectionAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Keys for intents
const val COLLECTION_PATH_KEY = "collection"
const val SECTION_NAME_KEY = "name"

class SectionsLearningFragment : Fragment() {

    // Firebase
    private var db = Firebase.firestore

    private var _binding: FragmentLearningSectionsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningSectionsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Initialize the RecyclerView and download the information
        val learningSections = binding.learningSectionsRecyclerView
        val adapter = LearningSectionAdapter(arrayOf()) {
            navigateToLearningSection(it)
        }
        learningSections.adapter = adapter
        db.collection(SECTIONS_COLLECTION_STORAGE_PATH).get().addOnSuccessListener {
                querySnapshot ->
            val sections: List<LearningSection> = querySnapshot.documents.map {
                documentSnapshot ->
                LearningSection(name = documentSnapshot.get(SECTION_NAME_STORAGE_PATH) as String,
                    collectionPath = documentSnapshot.reference.path
                                )
            }
            learningSections.adapter = LearningSectionAdapter(sections.toTypedArray()) {
                navigateToLearningSection(it)
            }
        }
        return root
    }

    private fun navigateToLearningSection(learningSection: LearningSection) {
        val navController = findNavController()
        navController.navigate(
            R.id.specificLearningSectionFragment,
            Bundle().apply {
                putString(SECTION_NAME_KEY, learningSection.name)
                putString(COLLECTION_PATH_KEY, learningSection.getLearningElementsPath())
            }
        )
    }

    /*override fun onResume() {
        Toast.makeText(context, "Resuming", Toast.LENGTH_SHORT).show()
        val adapter = LearningItemAdapter(
            viewModel.items.value!!.toTypedArray()
        ) {
            navigateToLearningItem(it)
        }
        val learningItems: RecyclerView = binding.learningSectionsRecyclerView
        learningItems.adapter = adapter
        super.onResume()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}