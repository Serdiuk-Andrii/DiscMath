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
import com.example.discmath.ui.assistant.graph_theory.adapters.GraphHistoryAdapter
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GraphBuilderBottomSheet(private val builder: GraphTheoryFragment): BottomSheetDialogFragment() {

    // Views
    private var _binding: GraphBuilderOptionsBinding? = null
    private lateinit var actions: RecyclerView
    private lateinit var history: RecyclerView

    private lateinit var historyAdapter: GraphHistoryAdapter

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
        historyAdapter = GraphHistoryAdapter(graphBuilderViewModel.graphs.value!!, ::graphSelected)
        history = binding.graphHistoryRecyclerView
        history.adapter = historyAdapter
        actions = binding.graphActionsRecyclerView
        actions.adapter = GraphActionAdapter(
            arrayOf(
                NamedActionElement(resources.getString(R.string.graph_action_unary_operations)
                )
                {
                    actions.adapter = GraphActionAdapter(
                        arrayOf(
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_connected_components))
                            {
                                val graph = builder.getGraph()
                                builder.getGraphConnectedComponents(graph)
                                dismiss()
                            },
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_cut_vertices))
                            {
                                val graph = builder.getGraph()
                                builder.getGraphCutVertices(graph)
                                dismiss()
                            },
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_bridges))
                            {
                                val graph = builder.getGraph()
                                builder.getGraphBridges(graph)
                                dismiss()
                            },
                        )
                    )
                },
                NamedActionElement(resources.getString(R.string.graph_action_binary_operations)
                )
                {
                    saveCurrentGraphAndClear()
                    builder.currentGraphIndex = null
                    dismiss()
                },
                NamedActionElement(resources.getString(R.string.graph_builder_export_to_png))
                {
                    builder.createPng()
                    dismiss()
                }
            )
        )
    }

    private fun saveCurrentGraphAndClear(): GraphData {
        val graphData = getCurrentGraph()
        val index = builder.currentGraphIndex
        if (index == null) {
            graphBuilderViewModel.appendGraph(graphData)
        } else {
            graphBuilderViewModel.updateGraph(graphData, index)
        }
        builder.clear()
        return graphData
    }

    private fun getCurrentGraph(): GraphData {
        val vertices = builder.vertices
        val bitmap = builder.createGraphBitmap()
        return GraphData(vertices.toList(), bitmap)
    }

    private fun graphSelected(graphData: GraphData, index: Int) {
        saveCurrentGraphAndClear()
        builder.currentGraphIndex = index
        builder.resetVertices(graphData.vertices.toList())
        dismiss()
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