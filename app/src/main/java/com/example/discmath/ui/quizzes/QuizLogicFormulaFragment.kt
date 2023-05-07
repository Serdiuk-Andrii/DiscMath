package com.example.discmath.ui.quizzes

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizLogicFormulaBinding
import com.example.discmath.toggleVisibility
import com.example.discmath.ui.assistant.logic.TruthTableLayout
import com.example.set_theory.logic.LogicExpressionGenerator
import com.example.set_theory.logic.UntrustworthyTruthTable
import com.example.set_theory.logic.UntrustworthyTruthTableGenerator
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates


class QuizLogicFormulaFragment : Fragment() {

    // Views
    private var _binding: FragmentQuizLogicFormulaBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var logicFormula: TextView
    private lateinit var truthTableLayout: TruthTableLayout

    private lateinit var confirmButton: Button
    private lateinit var nextButton: Button
    private lateinit var clearButton: Button

    // Colors
    private var correctAnswerColor by Delegates.notNull<Int>()
    private var incorrectAnswerColor by Delegates.notNull<Int>()
    private var selectedRowBackground by Delegates.notNull<Int>()

    // Drawables
    private lateinit var rowDefaultDrawable: Drawable

    // Strings
    private lateinit var truthTableIsCorrectMessage: String
    private lateinit var truthTableIsCorrectButtonText: String
    private lateinit var truthTableHasIncorrectRowButtonText: String

    // State
    private lateinit var untrustworthyTruthTableGenerator: UntrustworthyTruthTableGenerator
    private lateinit var problemGenerator: LogicExpressionGenerator

    private var correctIndex: Int? = null
    private var selectedRow: TableRow? = null
    private var selectedRowIndex: Int? = null

    enum class State {
        SOLVING, REVIEWING
    }

    private var state: State = State.SOLVING


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizLogicFormulaBinding.inflate(inflater, container, false)
        initializeColorsAndDrawables()
        initializeStrings()
        initializeState()
        initializeViews()
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

    private fun initializeStrings() {
        truthTableIsCorrectMessage = resources.getString(R.string.truth_table_is_correct_error_message)
        truthTableIsCorrectButtonText = resources.getString(R.string.truth_table_is_correct_button_confirm_text)
        truthTableHasIncorrectRowButtonText = resources.getString(R.string.confirm)
    }

    private fun initializeColorsAndDrawables() {
        correctAnswerColor = ResourcesCompat.getColor(resources, R.color.correct_answer_color, null)
        incorrectAnswerColor = ResourcesCompat.getColor(resources, R.color.incorrect_answer_color, null)
        selectedRowBackground = ResourcesCompat.getColor(resources, R.color.selected_row_color, null)
        rowDefaultDrawable = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.row_background
        )!!
    }

    private fun initializeViews() {
        logicFormula = binding.logicFormula
        truthTableLayout = binding.logicTruthTable
        confirmButton = binding.confirmButton
        nextButton = binding.nextButton
        clearButton = binding.clearButton
        confirmButton.setOnClickListener {
            val corruptedIndex = correctIndex
            if (correctIndex == selectedRowIndex) {
                // Correct answer, generate next test
                generateNextTest()
                runColorAnimation(correctAnswerColor, confirmButton)
            } else {
                if (corruptedIndex != null) {
                    truthTableLayout[corruptedIndex + 1].setBackgroundColor(
                        ResourcesCompat.getColor(resources, R.color.incorrect_answer_color,
                            null))
                } else {
                    Snackbar.make(binding.root, truthTableIsCorrectMessage, Snackbar.LENGTH_SHORT)
                        .setAnchorView(truthTableLayout)
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
                confirmButton.toggleVisibility()
                nextButton.toggleVisibility()
                runColorAnimation(incorrectAnswerColor, nextButton)
                state = State.REVIEWING
            }
            clearButton.visibility = View.GONE
        }
        nextButton.setOnClickListener {
            generateNextTest()
            confirmButton.toggleVisibility()
            nextButton.toggleVisibility()
            state = State.SOLVING
        }
        clearButton.setOnClickListener {
            selectedRow?.background = rowDefaultDrawable
            selectedRow = null
            selectedRowIndex = null
            clearButton.toggleVisibility()
            confirmButton.text = truthTableIsCorrectButtonText
        }
    }

    private fun generateNextTest() {
        val formula: String = problemGenerator.generateExpression(5)
        selectedRowIndex = null
        logicFormula.text = formula
        val corruptedTruthTable: UntrustworthyTruthTable = untrustworthyTruthTableGenerator.
                getTableFromExpression(formula)
        correctIndex = corruptedTruthTable.corruptedIndex
        Toast.makeText(context, if(correctIndex == null) "Формула правильна"
            else "Правильний індекс $correctIndex", Toast.LENGTH_SHORT).show()
        truthTableLayout.clear()
        truthTableLayout.fillTable(corruptedTruthTable.table, 20) {
            row, index ->
            if (state == State.SOLVING) {
                selectedRow?.background = rowDefaultDrawable
                selectedRow = row
                row.setBackgroundColor(selectedRowBackground)
                selectedRowIndex = index
                clearButton.visibility = View.VISIBLE
                confirmButton.text = truthTableHasIncorrectRowButtonText
            }
        }
    }

    private fun runColorAnimation(targetColor: Int, view: View, duration: Long = 300L) {
        val initialColor = ResourcesCompat.getColor(resources, R.color.primary, null)
        val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), initialColor, targetColor)

        colorAnimator.duration = duration
        colorAnimator.addUpdateListener {
                animator -> view.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimator.start()

        val handler = view.handler
        handler.postDelayed(
            {
                view.setBackgroundColor(initialColor)
                val reverseColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(),
                    targetColor, initialColor)
                reverseColorAnimator.addUpdateListener {
                        animator -> view.setBackgroundColor(animator.animatedValue as Int)
                }
                reverseColorAnimator.duration = duration
                reverseColorAnimator.start()
            },
            duration
        )
    }

}
