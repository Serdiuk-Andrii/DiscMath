package com.example.discmath.ui.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.databinding.FragmentAssistantBinding

class AssistantFragment : Fragment() {

    private var _binding: FragmentAssistantBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val assistantViewModel =
            ViewModelProvider(this)[AssistantViewModel::class.java]

        _binding = FragmentAssistantBinding.inflate(inflater, container, false)
        return binding.root
    }

}