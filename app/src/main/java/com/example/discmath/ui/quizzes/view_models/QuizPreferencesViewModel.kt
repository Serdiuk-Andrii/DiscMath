package com.example.discmath.ui.quizzes.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.learning_section.LearningSection

class QuizPreferencesViewModel : ViewModel() {

    private val _sectionRestrictions = MutableLiveData<Array<LearningSection>>()
    val sectionRestrictions: LiveData<Array<LearningSection>> = _sectionRestrictions

    private val _time = MutableLiveData<Int>()
    val time: LiveData<Int> = _time

    fun setSectionRestrictions(valueToSet: Array<LearningSection>) {
        _sectionRestrictions.value = valueToSet
    }

    fun setTime(valueToSet: Int) {
        _time.value = valueToSet
    }

}