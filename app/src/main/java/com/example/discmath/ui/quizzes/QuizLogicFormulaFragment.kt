package com.example.discmath.ui.quizzes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizLogicFormulaBinding
import com.example.discmath.ui.assistant.logic.TruthTableLayout
import com.example.set_theory.logic.LogicExpressionGenerator
import com.example.set_theory.logic.UntrustworthyTruthTable
import com.example.set_theory.logic.UntrustworthyTruthTableGenerator


class QuizLogicFormulaFragment : Fragment() {

    // Views
    private var _binding: FragmentQuizLogicFormulaBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var logicFormula: TextView
    private lateinit var truthTableLayout: TruthTableLayout
    private lateinit var confirmButton: Button

    // State
    private lateinit var untrustworthyTruthTableGenerator: UntrustworthyTruthTableGenerator
    private lateinit var problemGenerator: LogicExpressionGenerator

    private var correctIndex: Int? = null
    private var selectedRow: TableRow? = null
    private var selectedRowIndex: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizLogicFormulaBinding.inflate(inflater, container, false)
        initializeState()
        initializeViews()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateNextTest()
    }

    private fun initializeState() {
        problemGenerator = LogicExpressionGenerator(25)
        untrustworthyTruthTableGenerator = UntrustworthyTruthTableGenerator(60)
    }

    private fun initializeViews() {
        logicFormula = binding.logicFormula
        truthTableLayout = binding.logicTruthTable
        confirmButton = binding.confirmButton
        generateNextTest()
        confirmButton.setOnClickListener {
            Toast.makeText(context, "${correctIndex == selectedRowIndex}",
                Toast.LENGTH_SHORT).show()
            generateNextTest()
        }
    }

    private fun generateNextTest() {
        val formula: String = problemGenerator.generateExpression(5)
        selectedRowIndex = null
        logicFormula.text = formula
        val corruptedTruthTable: UntrustworthyTruthTable = untrustworthyTruthTableGenerator.
                getTableFromExpression(formula)
        correctIndex = corruptedTruthTable.corruptedIndex
        Toast.makeText(context, "$correctIndex", Toast.LENGTH_SHORT).show()
        truthTableLayout.clear()
        truthTableLayout.fillTable(corruptedTruthTable.table, 6) {
            row, index ->
                Toast.makeText(context, "$index", Toast.LENGTH_SHORT).show()
                selectedRow?.setBackgroundColor(
                    ResourcesCompat.getColor(resources, R.color.white, null)
                )
                selectedRow = row
                row.setBackgroundColor(
                    ResourcesCompat.getColor(resources, R.color.button_bright, null)
                )
                selectedRowIndex = index
        }
    }


}