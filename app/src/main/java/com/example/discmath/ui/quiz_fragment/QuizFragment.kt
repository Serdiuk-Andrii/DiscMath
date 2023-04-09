package com.example.discmath.ui.quiz_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizBinding
import com.example.discmath.entity.quizzes.Quiz
import com.example.discmath.ui.quiz_fragment.view_models.QuizResultsViewModel
import com.example.discmath.ui.quiz_fragment.view_models.QuizViewModel

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizzesViewModel: QuizViewModel
    private lateinit var quizzesResultsViewModel: QuizResultsViewModel

    // Views
    private lateinit var skipButton: Button
    private lateinit var quizRecyclerView: RecyclerView

    // Adapter
    private lateinit var adapter: QuizAdapter

    // NavController
    private lateinit var navController: NavController

    private var correctAnswers: Int = 0
    private var totalAnswers: Int = 0

    private lateinit var currentQuiz: Quiz

    private val resultsMap: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        initializeViewModels()
        initializeViews()
        adapter = QuizAdapter(
            dataSet = quizzesViewModel.quizzes.value!!,
            correctAnswerCallback = ::onCorrectAnswer,
            incorrectAnswerCallback = ::onIncorrectAnswer)
        currentQuiz = quizzesViewModel.quizzes.value!![0]
        quizRecyclerView.adapter = adapter
        navController = findNavController()
        return binding.root
    }

    private fun initializeViewModels() {
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        quizzesResultsViewModel = ViewModelProvider(requireActivity())[QuizResultsViewModel::class.java]
    }

    private fun initializeViews() {
        skipButton = binding.skipButton
        skipButton.setOnClickListener {onIncorrectAnswer()}
        quizRecyclerView = binding.quizzesRecyclerView
        quizRecyclerView.layoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false)
            { override fun canScrollHorizontally() = false }
    }

    private fun navigateFurther() {
        if (!adapter.isTheLastQuiz()) {
            currentQuiz = adapter.nextQuiz()
        } else {
            putDataIntoViewModel()
            navController.navigate(R.id.quizResultsFragment)
        }
    }

    private fun putDataIntoViewModel() {
        quizzesResultsViewModel.setCorrectAnswers(correctAnswers)
        quizzesResultsViewModel.setTotalAnswers(totalAnswers)
        val resultsBySection = resultsMap.map{entry ->  Triple(entry.key,
            entry.value.first, entry.value.second)}.toTypedArray()
        quizzesResultsViewModel.setResultsBySection(resultsBySection)
    }

    private fun onCorrectAnswer() {
        val quizSectionName: String = currentQuiz.learningSectionName
        val currentPair = resultsMap.getOrPut(quizSectionName) {Pair(0, 0)}
        resultsMap[quizSectionName] = Pair(currentPair.first + 1, currentPair.second + 1)
        correctAnswers++
        totalAnswers++
        navigateFurther()
    }

    private fun onIncorrectAnswer() {
        val quizSectionName: String = currentQuiz.learningSectionName
        val currentPair = resultsMap.getOrPut(quizSectionName) {Pair(0, 0)}
        resultsMap[quizSectionName] = Pair(currentPair.first, currentPair.second + 1)
        totalAnswers++
        navigateFurther()
    }

}