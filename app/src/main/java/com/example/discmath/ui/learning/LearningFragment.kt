package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningBinding


class LearningFragment : Fragment() {

    private var _binding: FragmentLearningBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Views
    private lateinit var button: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningBinding.inflate(inflater, container, false)
        initializeViews()
        
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializeViews() {
        button = binding.button
        val navController = findNavController()
        button.setOnClickListener {
            navController.navigate(R.id.action_learningFragment_to_navigation_sections)
        }
    }

}