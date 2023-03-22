package com.example.discmath.ui.quiz_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.quizzes.quiz_preferences.QuizPreference

class QuizzesViewModel : ViewModel() {

    val quizPreferences: MutableLiveData<QuizPreference> = MutableLiveData(
        QuizPreference(arrayOf(), -1)
    )
}