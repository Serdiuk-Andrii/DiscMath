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
import com.example.discmath.ui.quiz_fragment.view_models.QuizViewModel
import com.example.set_theory.RPN.SetRPN


// Keys for intents
const val TOTAL_ANSWERS_INTENT_KEY = "total"
const val CORRECT_ANSWERS_INTENT_KEY = "correct"

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizzesViewModel: QuizViewModel

    // Views
    private lateinit var skipButton: Button
    private lateinit var quizRecyclerView: RecyclerView

    // Adapter
    private lateinit var adapter: QuizAdapter

    // NavController
    private lateinit var navController: NavController

    private var correctAnswers: Int = 0
    private var totalAnswers: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        correctAnswers = 0
        totalAnswers = 0
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        navController = findNavController()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        val root = binding.root
        skipButton = binding.skipButton
        quizRecyclerView = binding.quizzesRecyclerView
        quizRecyclerView.layoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false)
            { override fun canScrollHorizontally() = false }
        adapter = QuizAdapter(
            dataSet = quizzesViewModel.quizzes.value!!,
            correctAnswerCallback = ::onCorrectAnswer,
            incorrectAnswerCallback = ::onIncorrectAnswer)
        quizRecyclerView.adapter = adapter
        skipButton.setOnClickListener {navigateFurther()}
        return root
    }

    private fun navigateFurther() {
        if (!adapter.isTheLastQuiz()) {
            adapter.nextQuiz()
        } else {
            navController.navigate(R.id.quizResultsFragment, Bundle().apply {
                putInt(TOTAL_ANSWERS_INTENT_KEY, totalAnswers)
                putInt(CORRECT_ANSWERS_INTENT_KEY, correctAnswers)
            })
        }
    }

    private fun onCorrectAnswer() {
        correctAnswers++
        totalAnswers++
        navigateFurther()
    }

    private fun onIncorrectAnswer() {
        totalAnswers++
        navigateFurther()
    }

}