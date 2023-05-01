package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.databinding.FragmentLearningItemBinding

class LearningItemFragment : Fragment() {

    private var _binding: FragmentLearningItemBinding? = null

    // private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    // private var db = Firebase.firestore

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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