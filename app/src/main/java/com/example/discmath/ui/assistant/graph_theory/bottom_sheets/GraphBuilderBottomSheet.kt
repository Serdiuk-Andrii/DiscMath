package com.example.discmath.ui.assistant.graph_theory.bottom_sheets

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.GraphBuilderOptionsBinding
import com.example.discmath.ui.util.navigation.NamedActionElement
import com.example.discmath.ui.assistant.graph_theory.GraphData
import com.example.discmath.ui.assistant.graph_theory.GraphMapper
import com.example.discmath.ui.assistant.graph_theory.GraphTheoryFragment
import com.example.discmath.ui.assistant.graph_theory.Vertex
import com.example.discmath.ui.assistant.graph_theory.adapters.GraphActionAdapter
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.example.graph_theory.Graph
import com.example.graph_theory.GraphUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GraphBuilderBottomSheet(private val builder: GraphTheoryFragment) :
    BottomSheetDialogFragment() {

    // Views
    private var _binding: GraphBuilderOptionsBinding? = null
    private lateinit var actions: ViewPager2
    private lateinit var historyButton: Button

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
        graphBuilderViewModel =
            ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
    }

    private fun initializeViews() {
        historyButton = binding.openGraphHistoryButton
        historyButton.setOnClickListener {
            dismiss()
            builder.openHistory()
        }
        actions = binding.graphActions
        actions.adapter = GraphActionAdapter(
            arrayOf(
                NamedActionElement(
                    resources.getString(R.string.graph_action_unary_operations),
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.unary_operations,
                        null
                    )!!
                )
                {
                    actions.adapter = GraphActionAdapter(
                        arrayOf(
                            NamedActionElement(
                                resources.getString(R.string.graph_unary_operation_connected_components),
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.connected_components,
                                    null
                                )!!
                            )
                            {
                                builder.getGraphConnectedComponents()
                                dismiss()
                            },
                            NamedActionElement(
                                resources.getString(R.string.graph_unary_operation_cut_vertices),
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.cut_vertex,
                                    null
                                )!!
                            )
                            {
                                builder.getGraphCutVertices()
                                dismiss()
                            },
                            NamedActionElement(
                                resources.getString(R.string.graph_unary_operation_bridges),
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.bridges,
                                    null
                                )!!
                            )
                            {
                                builder.getGraphBridges()
                                dismiss()
                            },
                        )
                    )
                },
                NamedActionElement(
                    resources.getString(R.string.graph_action_binary_operations),
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.binary_operations,
                        null
                    )!!
                )
                {
                    if (graphBuilderViewModel.graphs.value!!.size > 1) {
                        actions.adapter = GraphActionAdapter(
                            arrayOf(
                                NamedActionElement(
                                    resources.getString(R.string.graph_binary_operation_union),
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.graph_union,
                                        null
                                    )!!)
                                    {
                                        dismiss()
                                        builder.openHistorySelector(::calculateUnion)
                                    },
                                NamedActionElement(
                                    resources.getString(R.string.graph_binary_operation_join),
                                    ResourcesCompat.getDrawable(
                                        resources,
                                        R.drawable.graph_join,
                                        null
                                    )!!
                                )
                                {
                                    dismiss()
                                    builder.openHistorySelector(::calculateJoin)
                                }
                            )
                        )
                    } else {
                        showBinaryOperationRequiresTwoGraphs()
                    }
                },
                NamedActionElement(
                    resources.getString(R.string.graph_action_new_graph),
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.new_graph,
                        null
                    )!!
                )
                {
                    builder.clear()
                    dismiss()
                },
                NamedActionElement(
                    resources.getString(R.string.graph_builder_export_to_png),
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.image_export,
                        null
                    )!!
                )
                {
                    builder.createPng()
                    dismiss()
                }
            )
        )
    }

    private fun showBinaryOperationRequiresTwoGraphs() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.graph_binary_operations_require_at_least_two_graphs))
            .setPositiveButton(R.string.graph_extreme_size_confirm)
            { _, _ ->
            }
            .show()
    }

    private fun placeResultOnCanvas(result: Graph) {
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

    private fun calculateUnion(selectedGraphs: Collection<GraphData>) {
        val result: Graph =
            GraphUtil.calculateUnion(selectedGraphs.map { GraphMapper(it.vertices).graph })
        placeResultOnCanvas(result)
    }

    private fun calculateJoin(selectedGraphs: Collection<GraphData>) {
        val result: Graph =
            GraphUtil.calculateJoin(selectedGraphs.map { GraphMapper(it.vertices).graph })
        placeResultOnCanvas(result)
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

    companion object {
        const val TAG = "GraphBuilderBottomSheet"
    }

}