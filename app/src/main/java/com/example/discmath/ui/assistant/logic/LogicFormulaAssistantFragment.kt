package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLogicFormulaAssistantBinding
import com.example.discmath.ui.util.keyboard.LogicKeyboard
import com.example.set_theory.lexical_analysis.Lexer
import com.example.set_theory.logic.CNF
import com.example.set_theory.logic.DNF
import com.example.set_theory.logic.TruthTable
import com.google.android.material.snackbar.Snackbar

fun Boolean.toInteger(): Int {
    if (this) {
        return 1
    }
    return 0
}

class LogicFormulaAssistantFragment : Fragment() {

    // Views
    private var _binding: FragmentLogicFormulaAssistantBinding? = null

    private lateinit var formulaEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var CNFText: TextView
    private lateinit var DNFText: TextView
    private lateinit var truthTableLayout: TruthTableLayout
    private lateinit var calculationResults: View
    private lateinit var keyboard: LogicKeyboard

    private lateinit var tautologyTextView: TextView
    private lateinit var satisfiableTextView: TextView
    private lateinit var contradictionTextView: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (keyboard.visibility == View.VISIBLE) {
                keyboard.visibility = View.GONE
                formulaEditText.clearFocus()
            } else {
                this.isEnabled = false
                findNavController().popBackStack()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogicFormulaAssistantBinding.inflate(inflater, container, false)
        initializeViews()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }

    private fun initializeViews() {
        formulaEditText = binding.logicFormulaEditText
        calculateButton = binding.logicCalculateButton
        CNFText = binding.CNFText
        DNFText = binding.DNFText
        truthTableLayout = binding.logicTruthTable
        calculationResults = binding.logicCalculationResults
        keyboard = binding.testKeyboard

        tautologyTextView = binding.tautologyText
        satisfiableTextView = binding.satisfiableText
        contradictionTextView = binding.contradictionText

        formulaEditText.showSoftInputOnFocus = false
        val ic: InputConnection = formulaEditText.onCreateInputConnection(EditorInfo())
        keyboard.setInputConnection(ic)

        formulaEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                keyboard.visibility = View.VISIBLE
            } else {
                keyboard.visibility = View.GONE
            }
        }

        calculateButton.setOnClickListener {
            formulaEditText.clearFocus()
            // Remove two consecutive negations for simplicity
            val expression: String = formulaEditText.text.toString().
                replace("!!", "")
            if (!Lexer.isCorrectLogicalExpression(expression)) {
                showError("Синтаксична помилка в формулі")
            } else {
                try {
                    val truthTable: TruthTable = TruthTable.buildTruthTable(expression)
                    var CNF: String = CNF.buildCNFBasedOnTruthTable(truthTable)
                    CNF = CNF.replace('!', '¬')
                    CNFText.text = CNF

                    var DNF: String = DNF.buildDNFBasedOnTruthTable(truthTable)
                    DNF = DNF.replace('!', '¬')
                    DNFText.text = DNF
                    updateTruthTable(truthTable)
                    calculationResults.visibility = View.VISIBLE
                } catch (error: java.lang.Exception) {
                    showError(error.message ?: "Помилка")
                }
            }
        }
    }

    private fun updateTruthTable(truthTable: TruthTable) {
        truthTableLayout.clear()

        tautologyTextView.visibility = View.GONE
        satisfiableTextView.visibility = View.GONE
        contradictionTextView.visibility = View.GONE

        truthTableLayout.fillTable(truthTable,2)

        if (truthTable.isSatisfiable) {
            satisfiableTextView.visibility = View.VISIBLE
            if (truthTable.isTautology) {
                tautologyTextView.visibility = View.VISIBLE
            }
        } else {
            contradictionTextView.visibility = View.VISIBLE
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ResourcesCompat.getColor(
                resources,
                R.color.edge_selected_color,
            null))
            .setTextColor(ResourcesCompat.getColor(
                resources,
                R.color.white,
                null))
            .show()
    }

}