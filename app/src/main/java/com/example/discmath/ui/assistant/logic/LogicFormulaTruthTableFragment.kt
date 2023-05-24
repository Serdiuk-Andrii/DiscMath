package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.databinding.FragmentLogicFormulaTruthTableBinding
import com.example.set_theory.logic.TruthTable


class LogicFormulaTruthTableFragment() : Fragment() {

    // Views
    private var _binding: FragmentLogicFormulaTruthTableBinding? = null
    private lateinit var truthTableLayout: TruthTableLayout
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // ViewModels
    private lateinit var logicFormulaViewModel: LogicFormulaViewModel

    // Data
    private lateinit var truthTable: TruthTable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogicFormulaTruthTableBinding.inflate(inflater,
            container, false)
        initializeViewModels()
        initializeData()
        initializeViews()
        return binding.root
    }

    private fun initializeData() {
        truthTable = logicFormulaViewModel.truthTable.value!!
    }

    private fun initializeViewModels() {
        logicFormulaViewModel =
            ViewModelProvider(requireActivity())[LogicFormulaViewModel::class.java]
    }

    private fun initializeViews() {
        truthTableLayout = binding.logicTruthTable
        truthTableLayout.fillTable(truthTable, 20)
    }

}