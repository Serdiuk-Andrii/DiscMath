package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentSpecificLearningSectionBinding
import com.example.discmath.entity.learning_item.LearningItem
import com.example.discmath.entity.learning_section.NAME_FIELD_KEY
import com.example.discmath.entity.learning_section.PDF_URL_FIELD_KEY
import com.example.discmath.entity.learning_section.TYPE_FIELD_KEY
import com.example.discmath.entity.learning_section.VIDEO_URL_FIELD_KEY
import com.example.discmath.ui.learning.adapters.LearningItemAdapter
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

    // TODO: This viewModel should probably be used for caching the data
    private lateinit var learningItemViewModel: LearningItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionName = it.getString(SECTION_NAME_KEY)!!
            collectionPath = it.getString(COLLECTION_PATH_KEY)!!
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificLearningSectionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //learningItemViewModel = ViewModelProvider(this)[LearningItemViewModel::class.java]

        // Initialize the RecyclerView and download the information
        val recyclerView: RecyclerView = binding.learningItemsRecyclerView
        val adapter = LearningItemAdapter(arrayOf()) {
            navigateToLearningItem(it)
        }

        recyclerView.adapter = adapter
        db.collection(collectionPath).get().addOnSuccessListener { querySnapshot ->
            val learningItems: List<LearningItem> =
                querySnapshot.documents.map { documentSnapshot ->
                    LearningItem(
                        typeString = documentSnapshot.get(TYPE_FIELD_KEY) as String,
                        name = documentSnapshot.get(NAME_FIELD_KEY) as String,
                        urlVideo = documentSnapshot.get(VIDEO_URL_FIELD_KEY) as String,
                        urlPdf = documentSnapshot.get(PDF_URL_FIELD_KEY) as String
                    )
                }
            recyclerView.adapter = LearningItemAdapter(
                learningItems.toTypedArray()
            ) {
                navigateToLearningItem(it)
                Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
            }
        }
        return root
    }

    private fun navigateToLearningItem(learningItem: LearningItem) {
        //learningItemViewModel.updateCurrentLearningItem(learningItem)
        val navController = findNavController()
        if (learningItem.urlVideo.isEmpty() && learningItem.urlPdf.isEmpty()) {
            Toast.makeText(context, "Both urls are absent!", Toast.LENGTH_SHORT).show()
        } else if (learningItem.urlPdf.isEmpty()) {
            navController.navigate(R.id.video_learning_fragment, Bundle().apply {
                putString(NAME_KEY, learningItem.name)
                putString(URL_VIDEO_KEY, learningItem.urlVideo)
            })
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