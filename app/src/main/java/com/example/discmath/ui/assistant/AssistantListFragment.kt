package com.example.discmath.ui.assistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentAssistantListBinding


class AssistantListFragment : Fragment() {

    // Views
    private var _binding: FragmentAssistantListBinding? = null
    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssistantListBinding.inflate(inflater, container, false)
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializeViews() {
        recyclerView = binding.assistantOptionsRecyclerView
        val options: Array<AssistantOption> = arrayOf(
            AssistantOption("Числення висловлювань", R.id.assistant_logic)
        )
        recyclerView.adapter = AssistantOptionsAdapter(options, ::navigateTo)
    }

    private fun navigateTo(destination: Int) {
        findNavController().navigate(destination)
    }

}