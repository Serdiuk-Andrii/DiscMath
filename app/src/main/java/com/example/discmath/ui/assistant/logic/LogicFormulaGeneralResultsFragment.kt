package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLogicFormulaGeneralResultsBinding
import kotlin.properties.Delegates


class LogicFormulaGeneralResultsFragment: Fragment() {

    // Data
    private lateinit var CNF: String
    private lateinit var DNF: String
    private var formulaClassStringResourceId by Delegates.notNull<Int>()

    // Views
    private var _binding: FragmentLogicFormulaGeneralResultsBinding? = null
    private lateinit var CNFTextView: TextView
    private lateinit var DNFTextView: TextView

    private lateinit var logicFormulaClassTextView: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            CNF = it.getString(CNF_BUNDLE_KEY)!!
            DNF = it.getString(DNF_BUNDLE_KEY)!!
            formulaClassStringResourceId = it.
                getInt(LOGIC_FORMULA_CLASS_STRING_RESOURCE_ID_BUNDLE_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogicFormulaGeneralResultsBinding.inflate(inflater, container, false)
        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        logicFormulaClassTextView = binding.logicFormulaClass
        CNFTextView = binding.CNFText
        DNFTextView = binding.DNFText
        logicFormulaClassTextView.text = String.format(
            resources.getString(R.string.logic_formula_property_title),
            resources.getString(formulaClassStringResourceId))
        CNFTextView.text = CNF
        DNFTextView.text = DNF


    }

}