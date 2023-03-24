package com.example.discmath.ui.quiz_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.entity.learning_section.LearningSection

class QuizzesViewModel : ViewModel() {

    private val _sectionRestrictions = MutableLiveData<Array<LearningSection>>()
    val sectionRestrictions: LiveData<Array<LearningSection>> = _sectionRestrictions

    fun setSectionRestrictions(valueToSet: Array<LearningSection>) {
        _sectionRestrictions.value = valueToSet
    }

}