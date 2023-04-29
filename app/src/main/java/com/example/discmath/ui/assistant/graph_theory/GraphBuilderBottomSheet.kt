package com.example.discmath.ui.assistant.graph_theory

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.GraphBuilderOptionsBinding
import com.example.discmath.ui.assistant.NamedActionElement
import com.example.discmath.ui.assistant.graph_theory.adapters.GraphActionAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GraphBuilderBottomSheet(private val builder: GraphTheoryFragment): BottomSheetDialogFragment() {

    // Views
    private var _binding: GraphBuilderOptionsBinding? = null
    private lateinit var actions: RecyclerView

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GraphBuilderOptionsBinding.inflate(inflater, container, false)
        navController = findNavController()
        resources = requireActivity().resources
        initializeViewModels()
        initializeViews()
        return binding.root
    }

    private fun initializeViewModels() {
        graphBuilderViewModel = ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
    }

    private fun initializeViews() {
        actions = binding.graphActionsRecyclerView
        actions.adapter = GraphActionAdapter(
            arrayOf(
                NamedActionElement(resources.getString(R.string.graph_action_unary_operations)
                )
                {
                    actions.adapter = GraphActionAdapter(
                        arrayOf(
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_cut_vertices))
                            {
                                val graph = builder.getGraph()
                                builder.getGraphConnectedComponents(graph)
                                builder.getGraphCutVertices(graph)
                                builder.createPng()
                                dismiss()
                            },
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_bridges))
                            {
                                val graph = builder.getGraph()
                                builder.getGraphConnectedComponents(graph)
                                builder.getGraphBridges(graph)
                                builder.createPng()
                                dismiss()
                            },
                        )
                    )
                },
                NamedActionElement(resources.getString(R.string.graph_action_binary_operations)
                )
                {
                    val graph = builder.getGraph()
                    graphBuilderViewModel.setCurrentGraph(graph)
                    builder.clear()
                    dismiss()
                }
            )
        )
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet: View = dialog.findViewById(com.google.android.material.
                R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    companion object {
        const val TAG = "GraphBuilderBottomSheet"
    }

}