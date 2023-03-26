package com.example.discmath.ui.quiz_fragment

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizResultsBinding
import com.example.discmath.ui.quiz_fragment.view_models.QuizViewModel

class QuizResultsFragment : Fragment() {

    private var _binding: FragmentQuizResultsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizzesViewModel: QuizViewModel

    // Views
    private lateinit var quizResultsTitle: TextView
    private lateinit var quizResultsSummaryText: TextView
    private lateinit var quizComeBackButton: Button

    // Values from Bundle
    private var correctAnswers: Int = -1
    private var totalAnswers: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctAnswers = it.getInt(CORRECT_ANSWERS_INTENT_KEY)
            totalAnswers = it.getInt(TOTAL_ANSWERS_INTENT_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        val navController = findNavController()
        val res: Resources = resources

        _binding = FragmentQuizResultsBinding.inflate(inflater, container, false)
        val root = binding.root
        initializeViews()
        quizResultsSummaryText.text = String.format(
            res.getString(R.string.quiz_results_summary_text),
            correctAnswers, totalAnswers
        )
        quizComeBackButton.setOnClickListener {
            navController.navigate(R.id.startQuizzesFragment)
        }
        return root
    }

    private fun initializeViews() {
        quizResultsTitle = binding.quizResultsTitle
        quizResultsSummaryText = binding.quizResultsSummaryText
        quizComeBackButton = binding.quizResultsComeBackButton
    }

}