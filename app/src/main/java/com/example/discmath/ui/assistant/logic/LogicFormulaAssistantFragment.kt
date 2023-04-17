package com.example.discmath.ui.assistant.logic

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.discmath.R
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
    private lateinit var calculationResults: View

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
        calculationResults = binding.logicCalculationResults

        calculateButton.setOnClickListener {
            val expression: String = formulaEditText.text.toString()
            val truthTable: TruthTable = TruthTable.buildTruthTable(expression)

            calculationResults.visibility = View.VISIBLE

            var CNF: String = CNF.buildCNFBasedOnTruthTable(truthTable)
            CNF = CNF.replace('!', '¬').replace('^', '∧')
            CNFText.text = CNF

            var DNF: String = DNF.buildDNFBasedOnTruthTable(truthTable)
            DNF = DNF.replace('!', '¬').replace('^', '∧')
            DNFText.text = DNF
            updateTruthTable(truthTable)

        }
    }

    private fun updateTruthTable(truthTable: TruthTable) {
        val rowBackground = ContextCompat.getDrawable(requireContext(),
            R.drawable.row_background)
        val rowGravity = Gravity.CENTER
        truthTableLayout.removeAllViews()

        val titleRow = TableRow(context)
        titleRow.background = rowBackground
        for (value in truthTable.symbols) {
            val characterTextView = TextView(context)
            characterTextView.text = value.toString()
            characterTextView.setTextColor(Color.BLACK)
            characterTextView.setTypeface(null, Typeface.BOLD)
            characterTextView.gravity = rowGravity
            titleRow.addView(characterTextView)
        }
        truthTableLayout.addView(titleRow)

        for (row in truthTable.rows) {
            val result = row.second
            val currentViewRow = TableRow(context)
            currentViewRow.background = rowBackground
            for (value in row.first) {
                val rowTextView = TextView(context)
                rowTextView.text = value.toInteger().toString()
                rowTextView.setTextColor(Color.BLACK)
                rowTextView.gravity = rowGravity
                currentViewRow.addView(rowTextView)
            }
            val resultTextView = TextView(context)
            resultTextView.setTextColor(Color.BLACK)
            resultTextView.gravity = rowGravity
            resultTextView.text = result.toInteger().toString()
            currentViewRow.addView(resultTextView)
            truthTableLayout.addView(currentViewRow)
        }
    }

}