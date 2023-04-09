package com.example.discmath.ui.quiz_fragment.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizResultsViewModel: ViewModel() {

    private val _correctAnswers = MutableLiveData<Int>()
    val correctAnswers: LiveData<Int> = _correctAnswers

    fun setCorrectAnswers(value: Int) {
        _correctAnswers.value =  value
    }

    private val _totalAnswers = MutableLiveData<Int>()
    val totalAnswers: LiveData<Int> = _totalAnswers

    fun setTotalAnswers(value: Int) {
        _totalAnswers.value =  value
    }

    private val _resultsBySection = MutableLiveData<Array<Triple<String, Int, Int>>>()
    val resultsBySection: LiveData<Array<Triple<String, Int, Int>>> = _resultsBySection

    fun setResultsBySection(value: Array<Triple<String, Int, Int>>) {
        _resultsBySection.value =  value
    }


}