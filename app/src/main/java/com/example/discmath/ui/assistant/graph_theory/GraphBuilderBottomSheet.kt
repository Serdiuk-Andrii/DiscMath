package com.example.discmath.ui.assistant.graph_theory

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
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
    private lateinit var actions: ViewPager2
    private lateinit var history: ViewPager2

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
        actions = binding.graphActions
        actions.adapter = GraphActionAdapter(
            arrayOf(
                NamedActionElement(resources.getString(R.string.graph_action_unary_operations),
                    ResourcesCompat.getDrawable(resources,
                        R.drawable.unary_operations,
                        null)!!
                )
                {
                    actions.adapter = GraphActionAdapter(
                        arrayOf(
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_connected_components),
                                ResourcesCompat.getDrawable(resources,
                                    R.drawable.connected_components,
                                    null)!!)
                            {
                                val graph = builder.getGraph()
                                builder.getGraphConnectedComponents(graph)
                                dismiss()
                            },
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_cut_vertices),
                                ResourcesCompat.getDrawable(resources,
                                    R.drawable.cut_vertex,
                                    null)!!
                                )
                            {
                                val graph = builder.getGraph()
                                builder.getGraphCutVertices(graph)
                                dismiss()
                            },
                            NamedActionElement(resources.getString(R.string.graph_unary_operation_bridges),
                                ResourcesCompat.getDrawable(resources,
                                    R.drawable.bridges,
                                    null)!!
                            )
                            {
                                val graph = builder.getGraph()
                                builder.getGraphBridges(graph)
                                dismiss()
                            },
                        )
                    )
                },
                NamedActionElement(resources.getString(R.string.graph_action_binary_operations),
                    ResourcesCompat.getDrawable(resources,
                        R.drawable.new_graph,
                        null)!!
                )
                {
                    saveCurrentGraphAndClear()
                    builder.currentGraphIndex = null
                    dismiss()
                },
                NamedActionElement(resources.getString(R.string.graph_builder_export_to_png),
                    ResourcesCompat.getDrawable(resources,
                        R.drawable.image_export,
                        null)!!
                    )
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