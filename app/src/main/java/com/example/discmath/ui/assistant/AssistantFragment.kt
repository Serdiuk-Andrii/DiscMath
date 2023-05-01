package com.example.discmath.ui.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentAssistantBinding

class AssistantFragment : Fragment() {

    // Views
    private lateinit var navigateButton: Button
    private var _binding: FragmentAssistantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val assistantViewModel = ViewModelProvider(this)[AssistantViewModel::class.java]
        _binding = FragmentAssistantBinding.inflate(inflater, container, false)
        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        val navigation = findNavController()

        navigateButton = binding.navigateToAssistantButton
        navigateButton.setOnClickListener {
            navigation.navigate(R.id.assistant_list_fragment_navigation)
        }
    }

}