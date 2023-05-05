package com.example.discmath.ui.assistant.graph_theory.bottom_sheets

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.GraphHistorySelectorBinding
import com.example.discmath.ui.assistant.graph_theory.GraphData
import com.example.discmath.ui.assistant.graph_theory.GraphMapper
import com.example.discmath.ui.assistant.graph_theory.GraphTheoryFragment
import com.example.discmath.ui.assistant.graph_theory.Vertex
import com.example.discmath.ui.assistant.graph_theory.adapters.GraphHistorySelectionAdapter
import com.example.discmath.ui.assistant.graph_theory.adapters.RepresentCurrentRecyclerViewIndexPageCallback
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.example.graph_theory.Graph
import com.example.graph_theory.GraphUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


const val JOIN_THRESHOLD_EDGES = 1000

class GraphHistorySelectionBottomSheet(private val builder: GraphTheoryFragment) :
    BottomSheetDialogFragment() {

    // Views
    private var _binding: GraphHistorySelectorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var historySelectorTitle: TextView
    private lateinit var historySelectorRecyclerView: ViewPager2
    private lateinit var historySelectorAdapter: GraphHistorySelectionAdapter
    private lateinit var historySelectorCounter: TextView
    private lateinit var historyFinishSelectionButton: Button

    // Navigation
    private lateinit var navController: NavController

    // ViewModels
    private lateinit var graphBuilderViewModel: GraphBuilderViewModel

    // Resources
    private lateinit var resources: Resources

    // State
    private val selectedGraphs: MutableList<GraphData> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GraphHistorySelectorBinding.inflate(inflater, container, false)
        navController = findNavController()
        resources = requireActivity().resources
        initializeViewModels()
        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historySelectorRecyclerView.registerOnPageChangeCallback(
            RepresentCurrentRecyclerViewIndexPageCallback(
                historySelectorCounter,
                historySelectorAdapter,
                resources
            )
        )
    }

    private fun initializeViews() {
        historySelectorTitle = binding.graphHistoryTitleSelector
        updateNumberOfSelectedGraphs()
        historySelectorRecyclerView = binding.graphHistorySelectorRecyclerView
        historySelectorAdapter = GraphHistorySelectionAdapter(
            graphBuilderViewModel.graphs.value!!,
            ::graphSelected
        )
        historySelectorRecyclerView.adapter = historySelectorAdapter
        historySelectorCounter = binding.historyCounterSelector
        historyFinishSelectionButton = binding.historyFinishSelectionButton
        historyFinishSelectionButton.setOnClickListener {
            // Calculate the result and display it
            val resultSize =
                selectedGraphs.map { it.vertices.size }.reduce { accumulator: Int, next: Int ->
                    accumulator * next
                }
            if (resultSize > JOIN_THRESHOLD_EDGES) {
                showExtremeGraphResultDialog()
            } else {
                val result: Graph =
                    GraphUtil.calculateJoin(selectedGraphs.map { GraphMapper(it.vertices).graph })
                builder.clear()
                val resultVertices: MutableList<Vertex> = mutableListOf()
                for (i in 0 until result.numberOfVertices) {
                    resultVertices.add(builder.createVertex(0F, 0F))
                }
                for (i in 0 until result.numberOfVertices) {
                    for (vertex in result.getNeighboursOf(i)) {
                        if (vertex > i) {
                            builder.createEdge(
                                resultVertices[i],
                                resultVertices[vertex]
                            )
                        }
                    }
                }
                builder.resetVertices(resultVertices)
            }
            dismiss()
        }
    }

    private fun initializeViewModels() {
        graphBuilderViewModel =
            ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
    }

    private fun graphSelected(graphData: GraphData) {
        if (selectedGraphs.contains(graphData)) {
            selectedGraphs.remove(graphData)
        } else {
            selectedGraphs.add(graphData)
        }
        updateNumberOfSelectedGraphs()
        historyFinishSelectionButton.isEnabled = selectedGraphs.size > 1
    }

    private fun updateNumberOfSelectedGraphs() {
        historySelectorTitle.text = String.format(
            resources.getString(R.string.graph_history_selector_title),
            selectedGraphs.size
        )
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet: View = dialog.findViewById(
                com.google.android.material.R.id.design_bottom_sheet
            )
            bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    private fun showExtremeGraphResultDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.graph_extreme_size_message))
            .setPositiveButton(R.string.graph_extreme_size_confirm)
            { _, _ ->
            }
            .show()
    }

    companion object {
        const val TAG = "GraphHistorySelectionBottomSheet"
    }

}
