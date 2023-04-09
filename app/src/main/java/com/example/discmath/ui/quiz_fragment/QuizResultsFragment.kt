package com.example.discmath.ui.quiz_fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizResultsBinding
import com.example.discmath.toggleVisibility
import com.example.discmath.ui.quiz_fragment.view_models.QuizResultsViewModel

class QuizResultsFragment : Fragment() {

    private var _binding: FragmentQuizResultsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizzesResultsViewModel: QuizResultsViewModel

    // Views
    private lateinit var quizResultsTitle: TextView
    private lateinit var quizResultsSummaryText: TextView
    private lateinit var quizComeBackButton: Button
    private lateinit var quizResultsRecyclerView: RecyclerView
    private lateinit var quizExpandResultsButton: ImageButton

    // Values from Bundle
    private var correctAnswers: Int = -1
    private var totalAnswers: Int = -1
    private lateinit var resultsBySections: Array<Triple<String, Int, Int>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizResultsBinding.inflate(inflater, container, false)
        initializeViewModelAndReadData()
        initializeViews()
        bindDataToViews()
        return binding.root
    }

    private fun initializeViewModelAndReadData() {
        quizzesResultsViewModel = ViewModelProvider(requireActivity())[QuizResultsViewModel::class.java]
        correctAnswers = quizzesResultsViewModel.correctAnswers.value!!
        totalAnswers = quizzesResultsViewModel.totalAnswers.value!!
        resultsBySections = quizzesResultsViewModel.resultsBySection.value!!
    }

    private fun initializeViews() {
        quizResultsTitle = binding.quizResultsTitle
        quizResultsSummaryText = binding.quizResultsSummaryText
        quizComeBackButton = binding.quizResultsComeBackButton
        quizResultsRecyclerView = binding.quizResultsRecyclerView
        quizExpandResultsButton = binding.quizResultsExpandButton
        quizExpandResultsButton.setOnClickListener {
            quizResultsRecyclerView.toggleVisibility()
        }
    }

    private fun bindDataToViews() {
        val navController = findNavController()
        val res: Resources = resources
        quizResultsSummaryText.text = String.format(
            res.getString(R.string.quiz_results_summary_text),
            correctAnswers, totalAnswers
        )
        quizComeBackButton.setOnClickListener {
            navController.navigate(R.id.start_quizzes_fragment)
        }
        val dataset = quizzesResultsViewModel.resultsBySection.value!!.map { entry ->
            Pair(entry.first, String.format(
                res.getString(R.string.quiz_section_correct_answers),
                entry.second, entry.third))}.toTypedArray()
        quizResultsRecyclerView.adapter = ResultsBySectionAdapter(dataset = dataset)
    }

}