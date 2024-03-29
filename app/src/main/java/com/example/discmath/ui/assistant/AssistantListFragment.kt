package com.example.discmath.ui.assistant

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentAssistantListBinding
import com.example.discmath.ui.assistant.graph_theory.view_model.GraphBuilderViewModel
import com.example.discmath.ui.util.navigation.FunctionElement
import com.example.discmath.ui.util.navigation.FunctionElementAdapter


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
        val options: Array<FunctionElement> = arrayOf(
            FunctionElement(resources.getString(R.string.assistant_option_logic_title),
                R.id.action_assistant_options_fragment_to_assistant_logic,
                ResourcesCompat.getDrawable(resources, R.drawable.zero_order_logic, null)!!,
                arrayOf("Побудова КНФ та ДНФ", "Таблиця істинності")),
            FunctionElement(resources.getString(R.string.assistant_option_graph_theory_title),
                R.id.assistant_graph_theory_navigation,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, null)!!,
                arrayOf("Побудова графів", "Унарні та бінарні операції", "Експорт в PNG")
            )
        )
        recyclerView.adapter = FunctionElementAdapter(options, ::navigateTo)
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

}