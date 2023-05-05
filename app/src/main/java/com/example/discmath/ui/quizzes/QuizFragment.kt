package com.example.discmath.ui.quizzes

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizBinding
import com.example.discmath.entity.quizzes.Quiz
import com.example.discmath.ui.learning.parseTimestampToTimeInSeconds
import com.example.discmath.ui.quizzes.view_models.QuizPreferencesViewModel
import com.example.discmath.ui.quizzes.view_models.QuizResultsViewModel
import com.example.discmath.ui.quizzes.view_models.QuizViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


fun Int.getTimeSectionRepresentation(): String {
    return if (this < 10) "0$this" else "$this"
}

fun Int.parseIntoTime(): String {
   if (this > 3599) {
       return ""
   }
   val minutes = (this / 60).getTimeSectionRepresentation()
   val seconds = (this % 60).getTimeSectionRepresentation()
   return "$minutes:$seconds"
}

const val BUTTON_DEBOUNCING_DELAY = 300L

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizPreferencesViewModel: QuizPreferencesViewModel
    private lateinit var quizzesViewModel: QuizViewModel
    private lateinit var quizzesResultsViewModel: QuizResultsViewModel

    // Views
    private lateinit var timer: TextView
    private lateinit var skipButton: Button
    private lateinit var quizRecyclerView: RecyclerView

    // Adapter
    private lateinit var adapter: QuizAdapter

    // Navigation
    private lateinit var navController: NavController
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            showConfirmModal()
        }
    }

    // State
    private var correctAnswers: Int = 0
    private var totalAnswers: Int = 0
    private var timeLeftInSeconds: Int = -1

    private lateinit var currentQuiz: Quiz
    private var isVerifying = false

    private lateinit var handler: Handler
    private lateinit var timerRunnable: Runnable

    private val resultsMap: MutableMap<String, Pair<Int, Int>> = mutableMapOf()

    // Media
    private lateinit var correctMediaPlayer: MediaPlayer
    private lateinit var timeIsUpMediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        navController = findNavController()
        initializeViewModels()
        initializeViews()
        initializeMedia()
        adapter = QuizAdapter(
            dataSet = quizzesViewModel.quizzes.value!!,
            correctAnswerCallback = ::onCorrectAnswer,
            incorrectAnswerCallback = ::onIncorrectAnswer)
        currentQuiz = quizzesViewModel.quizzes.value!![0]
        quizRecyclerView.adapter = adapter
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return binding.root
    }

    private fun initializeViewModels() {
        quizPreferencesViewModel = ViewModelProvider(requireActivity())[QuizPreferencesViewModel::class.java]
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizViewModel::class.java]
        quizzesResultsViewModel = ViewModelProvider(requireActivity())[QuizResultsViewModel::class.java]
    }

    private fun initializeViews() {
        timer = binding.quizTimer
        val quizTime = quizPreferencesViewModel.time.value.toString()
        timer.text = quizTime
        timeLeftInSeconds = quizTime.parseTimestampToTimeInSeconds()
        handler = Handler(Looper.getMainLooper())
        timerRunnable = Runnable {
            timeLeftInSeconds--
            timer.text = timeLeftInSeconds.parseIntoTime()
            if (timeLeftInSeconds > 0) {
                handler.postDelayed(timerRunnable, 1000)
            } else {
                playTimeIsUpSound()
            }
        }
        skipButton = binding.skipButton
        skipButton.setOnClickListener {
            onIncorrectAnswer()
        }
        quizRecyclerView = binding.quizzesRecyclerView
        quizRecyclerView.layoutManager =
            object : LinearLayoutManager(context, HORIZONTAL, false)
            { override fun canScrollHorizontally() = false }
    }

    private fun initializeMedia() {
        correctMediaPlayer = MediaPlayer.create(this.context, R.raw.correct)
        timeIsUpMediaPlayer = MediaPlayer.create(this.context, R.raw.correct)
    }

    private fun navigateFurther() {
        if (!adapter.isTheLastQuiz() && timeLeftInSeconds != 0) {
            currentQuiz = adapter.nextQuiz()
        } else {
            putDataIntoViewModelAndNavigate()
        }
    }

    private fun putDataIntoViewModelAndNavigate() {
        this.onBackPressedCallback.isEnabled = false
        quizzesResultsViewModel.setCorrectAnswers(correctAnswers)
        quizzesResultsViewModel.setTotalAnswers(totalAnswers)
        val resultsBySection = resultsMap.map{entry ->  Triple(entry.key,
            entry.value.first, entry.value.second)}.toTypedArray()
        quizzesResultsViewModel.setResultsBySection(resultsBySection)
        navController.navigate(R.id.navigation_to_quiz_results)
    }

    private fun onCorrectAnswer() {
        if (!isVerifying) {
            isVerifying = true
            playCorrectSound()
            val quizSectionName: String = currentQuiz.learningSectionName
            val currentPair = resultsMap.getOrPut(quizSectionName) { Pair(0, 0) }
            resultsMap[quizSectionName] = Pair(currentPair.first + 1, currentPair.second + 1)
            correctAnswers++
            totalAnswers++
            navigateFurther()
            this.view?.postDelayed({ isVerifying = false }, BUTTON_DEBOUNCING_DELAY)
        }
    }

    private fun onIncorrectAnswer() {
        if (!isVerifying) {
            isVerifying = true
            playIncorrectSound()
            val quizSectionName: String = currentQuiz.learningSectionName
            val currentPair = resultsMap.getOrPut(quizSectionName) { Pair(0, 0) }
            resultsMap[quizSectionName] = Pair(currentPair.first, currentPair.second + 1)
            totalAnswers++
            navigateFurther()
            this.view?.postDelayed({ isVerifying = false }, BUTTON_DEBOUNCING_DELAY)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(timerRunnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(timerRunnable, 1000)
    }

    private fun playCorrectSound() {
        correctMediaPlayer.start()
    }

    private fun playTimeIsUpSound() {

    }

    private fun playIncorrectSound() {

    }

    private fun showConfirmModal() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.finish_quiz_dialog_title))
            .setNegativeButton(R.string.finish_quiz_dialog_reject_text)
            {
                    _, _ ->

            }
            .setPositiveButton(R.string.finish_quiz_dialog_confirm_text)
            {
                _, _ ->
                putDataIntoViewModelAndNavigate()
            }
            .show()
    }

}