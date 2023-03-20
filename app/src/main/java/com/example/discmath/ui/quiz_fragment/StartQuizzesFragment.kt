package com.example.discmath.ui.quiz_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentStartQuizzesBinding


class StartQuizzesFragment : Fragment() {

    private var _binding: FragmentStartQuizzesBinding? = null

    private lateinit var image: ImageView
    private lateinit var encouragingText: TextView
    private lateinit var startButton: Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartQuizzesBinding.inflate(inflater, container, false)
        // Views
        val root: View = binding.root
        image = binding.imageTraining
        encouragingText = binding.textTrainingEncouragement
        startButton = binding.buttonStartTraining
        // Navigation
        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()
        startButton.setOnClickListener {
            navController.navigate(R.id.navigation_quiz_multiple_choice)
        }
        return root
    }


}