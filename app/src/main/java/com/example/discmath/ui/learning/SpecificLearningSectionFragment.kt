package com.example.discmath.ui.learning

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentSpecificLearningSectionBinding
import com.example.discmath.entity.learning_item.LearningItem
import com.example.discmath.ui.learning.adapters.LearningItemAdapter
import com.example.discmath.ui.learning.adapters.SECTION_NAME_KEY
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Keys for intents
const val NAME_KEY = "name"
const val URL_VIDEO_KEY = "urlVideo"
const val URL_PDF_KEY = "urlPdf"

class SpecificLearningSectionFragment : Fragment() {

    private var db = Firebase.firestore

    // Section information
    private lateinit var sectionName: String
    private lateinit var collectionPath: String

    private var _binding: FragmentSpecificLearningSectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var sectionNameTextView: TextView

    private lateinit var learningItemViewModel: LearningItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionName = it.getString(SECTION_NAME_KEY)!!
            collectionPath = it.getString(COLLECTION_PATH_KEY)!!
        }
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_activity_main
            duration = 1000L
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificLearningSectionBinding.inflate(inflater, container, false)
        //learningItemViewModel = ViewModelProvider(this)[LearningItemViewModel::class.java]

        // Initialize the RecyclerView and download the information

        initializeViews()
        bindDataToViews()

        return binding.root
    }

    private fun initializeViews() {
        recyclerView = binding.learningItemsRecyclerView
        val adapter = LearningItemAdapter(arrayOf(), ::navigateToLearningItem)
        recyclerView.adapter = adapter
        sectionNameTextView = binding.sectionName
    }

    private fun bindDataToViews() {
        sectionNameTextView.text = sectionName
        db.collection(collectionPath).get().addOnSuccessListener { querySnapshot ->
            val learningItems: List<LearningItem> =
                querySnapshot.documents.map { 
                        documentSnapshot -> LearningItem(documentSnapshot)
                }.sortedBy {  it.order  }
            recyclerView.adapter = LearningItemAdapter(
                learningItems.toTypedArray(),
                ::navigateToLearningItem
            )
        }
    }

    private fun navigateToLearningItem(learningItemTextView: TextView, learningItem: LearningItem) {
        //learningItemViewModel.updateCurrentLearningItem(learningItem)
        val navController = findNavController()
        if (learningItem.urlVideo.isEmpty() && learningItem.urlPdf.isEmpty()) {
            Toast.makeText(context, "Both urls are absent!", Toast.LENGTH_SHORT).show()
        } else if (learningItem.urlPdf.isEmpty()) {
            val lectureTitleTransitionName = getString(R.string.lecture_title_transition_name)
            val extras = FragmentNavigatorExtras(learningItemTextView to lectureTitleTransitionName)
            navController.navigate(R.id.video_learning_fragment, Bundle().apply {
                putString(NAME_KEY, learningItem.name)
                putString(URL_VIDEO_KEY, learningItem.urlVideo)
            }, null, extras)
        } else if (learningItem.urlVideo.isEmpty()) {
            // Navigate to pdf
            navController.navigate(R.id.pdfLearningFragment, Bundle().apply {
                putString(NAME_KEY, learningItem.name)
                putString(URL_PDF_KEY, learningItem.urlPdf)
            })
        } else {
            // Show the transition fragment
            navController.navigate(R.id.chooseLearningTypeFragment, Bundle().apply {
                putString(NAME_KEY, learningItem.name)
                putString(URL_VIDEO_KEY, learningItem.urlVideo)
                putString(URL_PDF_KEY, learningItem.urlPdf)
            })
        }
    }

}