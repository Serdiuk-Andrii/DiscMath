package com.example.discmath.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    //private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private var db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val learningItems = binding.learningItemsRecyclerView
        val root: View = binding.root

        if (viewModel.items.value.isNullOrEmpty()) {
            Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show()
            val adapter: LearningItemAdapter = LearningItemAdapter(arrayOf()) {
                navigateToLearningItem(it)
                Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
            }
            learningItems.adapter = adapter

            db.collection("sections").document("set_theory")
                .collection("items").get().addOnSuccessListener { querySnapshot ->
                    val names: List<LearningItem> =
                        querySnapshot.documents.map { documentSnapshot ->
                            LearningItem(
                                typeString = documentSnapshot.get("type") as String,
                                name = documentSnapshot.get("name") as String,
                                urlVideo = documentSnapshot.get("url_video") as String,
                                urlPdf = documentSnapshot.get("url_pdf") as String
                            )
                        }
                    viewModel.items.value = names
                    learningItems.adapter = LearningItemAdapter(names.toTypedArray()
                    ) {
                        navigateToLearningItem(it)
                        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return root
    }

    private fun navigateToLearningItem(learningItem: LearningItem) {
        viewModel.updateCurrentLearningItem(learningItem)
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()
        navController.navigate(R.id.learning_fragment, Bundle().apply {
            putString("name", learningItem.name)
            putString("url", learningItem.urlVideo)
        })
    }

    override fun onResume() {
        Toast.makeText(context, "Resuming", Toast.LENGTH_SHORT).show()
        val adapter: LearningItemAdapter = LearningItemAdapter(viewModel.items.value!!.toTypedArray()
        ) {
            navigateToLearningItem(it)
            Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
        }
        val learningItems: RecyclerView = binding.learningItemsRecyclerView
        learningItems.adapter = adapter
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}