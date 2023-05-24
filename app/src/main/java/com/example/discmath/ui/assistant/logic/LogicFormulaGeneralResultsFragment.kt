package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.discmath.databinding.FragmentLogicFormulaGeneralResultsBinding


class LogicFormulaGeneralResultsFragment: Fragment() {

    // Data
    private lateinit var CNF: String
    private lateinit var DNF: String

    // Views
    private var _binding: FragmentLogicFormulaGeneralResultsBinding? = null
    private lateinit var CNFTextView: TextView
    private lateinit var DNFTextView: TextView

    private lateinit var tautologyTextView: TextView
    private lateinit var satisfiableTextView: TextView
    private lateinit var contradictionTextView: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            CNF = it.getString(CNFBundleKey)!!
            DNF = it.getString(DNFBundleKey)!!
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
        CNFTextView = binding.CNFText
        DNFTextView = binding.DNFText
        CNFTextView.text = CNF
        DNFTextView.text = DNF

        tautologyTextView = binding.tautologyText
        satisfiableTextView = binding.satisfiableText
        contradictionTextView = binding.contradictionText
    }

}