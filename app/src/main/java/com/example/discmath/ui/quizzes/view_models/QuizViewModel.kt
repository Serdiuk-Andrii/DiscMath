package com.example.discmath.ui.quizzes.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.quizzes.Quiz

class QuizViewModel: ViewModel() {

    private val _quizzes = MutableLiveData<MutableList<Quiz>>(mutableListOf())
    val quizzes: LiveData<MutableList<Quiz>> = _quizzes

    fun addQuizzes(valueToSet: Collection<Quiz>) {
        _quizzes.value!!.addAll(valueToSet)
    }

    fun shuffleQuizzes() {
        _quizzes.value!!.shuffle()
    }

    fun clearQuizzes() {
        _quizzes.value!!.clear()
    }

}