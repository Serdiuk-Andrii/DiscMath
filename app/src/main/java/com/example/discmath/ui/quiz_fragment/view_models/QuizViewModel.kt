package com.example.discmath.ui.quiz_fragment.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.quizzes.Quiz
import java.util.*

class QuizViewModel: ViewModel() {

    private val _quizzes = MutableLiveData<Queue<Quiz>>(LinkedList())
    val quizzes: LiveData<Queue<Quiz>> = _quizzes

    fun addQuizzes(valueToSet: Collection<Quiz>) {
        _quizzes.value!!.addAll(valueToSet)
    }

}