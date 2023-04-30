package com.example.discmath.ui.assistant

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentAssistantListBinding
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel


class AssistantListFragment : Fragment() {

    // Views
    private var _binding: FragmentAssistantListBinding? = null
    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // ViewModels
    private lateinit var graphBuilderViewModel: GraphBuilderViewModel

    // Navigation
    private lateinit var navController: NavController

    // Resources
    private lateinit var resources: Resources

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssistantListBinding.inflate(inflater, container, false)
        navController = findNavController()
        resources = requireActivity().resources
        initializeViewModels()
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializeViewModels() {
        graphBuilderViewModel = ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
    }

    private fun initializeViews() {
        recyclerView = binding.assistantOptionsRecyclerView
        val options: Array<NamedNavigationElement> = arrayOf(
            NamedNavigationElement(resources.getString(R.string.assistant_option_logic_title),
                R.id.assistant_logic),
            NamedNavigationElement(resources.getString(R.string.assistant_option_graph_theory_title),
                R.id.assistant_graph_theory)
        )
        recyclerView.adapter = AssistantOptionsAdapter(options, ::navigateTo)
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

}