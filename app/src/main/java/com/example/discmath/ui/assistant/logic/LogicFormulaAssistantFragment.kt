package com.example.discmath.ui.assistant.logic

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.discmath.databinding.FragmentLogicFormulaAssistantBinding
import com.example.set_theory.logic.CNF
import com.example.set_theory.logic.DNF
import com.example.set_theory.logic.TruthTable

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
    private lateinit var truthTableLayout: TableLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLogicFormulaAssistantBinding.inflate(inflater, container, false)
        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        formulaEditText = binding.logicFormulaEditText
        calculateButton = binding.logicCalculateButton
        CNFText = binding.CNFText
        DNFText = binding.DNFText
        truthTableLayout = binding.logicTruthTable

        calculateButton.setOnClickListener {
            val expression: String = formulaEditText.text.toString()
            // TODO: Reuse the truth table
            val truthTable: TruthTable = TruthTable.buildTruthTable(expression)
            val CNF: String = CNF.buildCNFBasedOnExpression(expression)
            CNFText.text = CNF
            CNFText.visibility = View.VISIBLE

            val DNF: String = DNF.buildDNFBasedOnExpression(expression)
            DNFText.text = DNF
            DNFText.visibility = View.VISIBLE
            updateTruthTable(truthTable)

        }
    }

    private fun updateTruthTable(truthTable: TruthTable) {
        truthTableLayout.removeAllViews()
        val titleRow = TableRow(context)
        for (value in truthTable.symbols) {
            val characterTextView = TextView(context)
            characterTextView.text = value.toString()
            characterTextView.setTextColor(Color.BLACK)
            titleRow.addView(characterTextView)
        }
        truthTableLayout.addView(titleRow)
        for (row in truthTable.rows) {
            val result = row.second
            val currentViewRow = TableRow(context)
            for (value in row.first) {
                val rowTextView = TextView(context)
                rowTextView.text = value.toInteger().toString()
                rowTextView.width = 70
                currentViewRow.addView(rowTextView)
            }
            val resultTextView = TextView(context)
            resultTextView.text = result.toInteger().toString()
            resultTextView.width = 70
            currentViewRow.addView(resultTextView)
            truthTableLayout.addView(currentViewRow)
        }
    }

}