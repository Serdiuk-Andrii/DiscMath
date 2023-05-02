package com.example.discmath.ui.quizzes.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.learning_section.LearningSection

class QuizPreferencesViewModel : ViewModel() {

    private val _sectionRestrictions = MutableLiveData<Array<LearningSection>>()
    val sectionRestrictions: LiveData<Array<LearningSection>> = _sectionRestrictions

    private val _time = MutableLiveData<String>()
    val time: LiveData<String> = _time

    fun setSectionRestrictions(valueToSet: Array<LearningSection>) {
        _sectionRestrictions.value = valueToSet
    }

    fun setTime(valueToSet: String) {
        _time.value = valueToSet
    }

}