package com.example.discmath.ui.assistant.graph_theory.bottom_sheets

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.GraphHistoryBinding
import com.example.discmath.ui.assistant.graph_theory.GraphData
import com.example.discmath.ui.assistant.graph_theory.GraphTheoryFragment
import com.example.discmath.ui.assistant.graph_theory.adapters.GraphHistoryAdapter
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GraphHistoryBottomSheet(private val builder: GraphTheoryFragment): BottomSheetDialogFragment() {

    // Views
    private var _binding: GraphHistoryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var historyRecyclerView: ViewPager2
    private lateinit var historyAdapter: GraphHistoryAdapter
    private lateinit var historyCounter: TextView

    // Navigation
    private lateinit var navController: NavController

    // ViewModels
    private lateinit var graphBuilderViewModel: GraphBuilderViewModel

    // Resources
    private lateinit var resources: Resources

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GraphHistoryBinding.inflate(inflater, container, false)
        navController = findNavController()
        resources = requireActivity().resources
        initializeViewModels()
        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        historyRecyclerView.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    historyCounter.text = String.format(
                        resources.getString(R.string.graph_history_counter),
                        position + 1, historyAdapter.itemCount)
                }
            }
        )
    }

    private fun initializeViews() {
        historyRecyclerView = binding.graphHistoryRecyclerView
        historyAdapter = GraphHistoryAdapter(graphBuilderViewModel.graphs.value!!,
            ::graphSelected)
        historyRecyclerView.adapter = historyAdapter
        historyCounter = binding.historyCounter
    }

    private fun initializeViewModels() {
        graphBuilderViewModel = ViewModelProvider(requireActivity())[GraphBuilderViewModel::class.java]
    }

    private fun graphSelected(graphData: GraphData, index: Int) {
        builder.clear()
        builder.resetVertices(graphData.vertices.toList(), index)
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
        const val TAG = "GraphHistoryBottomSheet"
    }

}
