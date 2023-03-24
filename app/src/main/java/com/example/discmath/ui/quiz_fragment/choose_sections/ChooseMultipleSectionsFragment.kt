package com.example.discmath.ui.quiz_fragment.choose_sections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentChooseMultipleSectionsBinding
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.ui.learning.SECTIONS_COLLECTION_STORAGE_PATH
import com.example.discmath.ui.learning.SECTION_ELEMENTS_STORAGE_PATH
import com.example.discmath.ui.quiz_fragment.QuizzesViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChooseMultipleSectionsFragment : Fragment() {

    // Firebase
    private var db = Firebase.firestore

    private var _binding: FragmentChooseMultipleSectionsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var chosenSections: MutableList<LearningSection>
    private lateinit var nextButton: Button

    private lateinit var quizzesViewModel: QuizzesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        chosenSections = mutableListOf()
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizzesViewModel::class.java]
        _binding = FragmentChooseMultipleSectionsBinding.inflate(inflater, container, false)
        val root:View = binding.root
        // Recycler view
        val sectionsRecyclerView: RecyclerView = binding.recyclerViewChooseMultipleSections
        nextButton = binding.sectionsChosenConfirmButton
        val navController = findNavController()
        sectionsRecyclerView.adapter = LearningSectionTestOptionAdapter(arrayOf(), ::itemClicked)
        db.collection(SECTIONS_COLLECTION_STORAGE_PATH).get().addOnSuccessListener {
                querySnapshot ->
            val sections: List<LearningSection> = querySnapshot.documents.map {
                    documentSnapshot ->
                LearningSection(name = documentSnapshot.get("name") as String,
                    collectionPath = "${documentSnapshot.reference.path}/$SECTION_ELEMENTS_STORAGE_PATH"
                )
            }
            sectionsRecyclerView.adapter = LearningSectionTestOptionAdapter(sections.toTypedArray(),
                ::itemClicked)
        }
        nextButton.setOnClickListener {
            quizzesViewModel.setSectionRestrictions(chosenSections.toTypedArray())
            navController.navigate(R.id.chooseTimeTypeFragment)
        }
        return root
    }

    private fun itemClicked(section: LearningSection, hasBeenSelected: Boolean) {
        if (hasBeenSelected) {
            chosenSections.add(section)
        } else {
            chosenSections.remove(section)
        }
        nextButton.isEnabled = chosenSections.isNotEmpty()
    }

}