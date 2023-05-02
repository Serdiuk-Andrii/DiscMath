package com.example.discmath.ui.learning

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningItemBinding
import com.google.android.material.transition.MaterialContainerTransform

class LearningItemFragment : Fragment() {

    private var _binding: FragmentLearningItemBinding? = null

    // private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    // private var db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_activity_main
            duration = 1000L
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val learningItemViewModel =
            ViewModelProvider(this)[LearningItemViewModel::class.java]

        _binding = FragmentLearningItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}