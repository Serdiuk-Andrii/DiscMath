package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLogicFormulaAssistantBinding
import com.example.discmath.ui.util.keyboard.LogicKeyboard
import com.example.set_theory.lexical_analysis.Lexer
import com.example.set_theory.logic.CNF
import com.example.set_theory.logic.DNF
import com.example.set_theory.logic.TruthTable
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

fun Boolean.toInteger(): Int {
    return if (this) 1 else 0
}

val tabs = arrayOf(
    "Загальне",
    "Таблиця істинності"
)

class LogicFormulaAssistantFragment : Fragment() {

    // Views
    private var _binding: FragmentLogicFormulaAssistantBinding? = null

    private lateinit var formulaEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var keyboard: LogicKeyboard

    private lateinit var tabLayout: TabLayout
    private lateinit var resultsViewPager: ViewPager2

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // ViewModels
    private lateinit var logicFormulaViewModel: LogicFormulaViewModel

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
        initializeViewModels()
        initializeViews()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }

    private fun initializeViewModels() {
        logicFormulaViewModel =
            ViewModelProvider(requireActivity())[LogicFormulaViewModel::class.java]
    }

    private fun initializeViews() {
        formulaEditText = binding.logicFormulaEditText
        calculateButton = binding.logicCalculateButton
        resultsViewPager = binding.logicViewPagerResults
        tabLayout = binding.logicViewResultsTabLayout
        keyboard = binding.testKeyboard

        resultsViewPager.isUserInputEnabled = false

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
            val expression: String = formulaEditText.text.toString().replace("!!", "")
            if (!Lexer.isCorrectLogicalExpression(expression)) {
                showError("Синтаксична помилка в формулі")
            } else {
                try {
                    val truthTable: TruthTable = TruthTable.buildTruthTable(expression)
                    var CNF: String = CNF.buildCNFBasedOnTruthTable(truthTable)
                    CNF = CNF.replace('!', '¬')

                    var DNF: String = DNF.buildDNFBasedOnTruthTable(truthTable)
                    DNF = DNF.replace('!', '¬')

                    val formulaClassResourceId =
                        if (truthTable.isTautology) {
                            R.string.logic_tautology_formula
                        } else if (truthTable.isSatisfiable) {
                            R.string.logic_satisfiable_formula
                        } else {
                            R.string.logic_contradiction_formula
                        }

                    logicFormulaViewModel.setTruthTable(truthTable)
                    resultsViewPager.adapter = LogicFormulaResultsAdapter(
                        this, CNF, DNF, formulaClassResourceId)
                    TabLayoutMediator(tabLayout, resultsViewPager) { tab, position ->
                        tab.text = tabs[position]
                    }.attach()
                    resultsViewPager.visibility = View.VISIBLE
                    tabLayout.visibility = View.VISIBLE
                } catch (error: java.lang.Exception) {
                    showError(error.message ?: "Помилка")
                }
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(
                ResourcesCompat.getColor(
                    resources,
                    R.color.edge_selected_color,
                    null
                )
            )
            .setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.white,
                    null
                )
            )
            .show()
    }

}
