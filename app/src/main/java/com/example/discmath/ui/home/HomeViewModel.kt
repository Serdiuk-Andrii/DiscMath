package com.example.discmath.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    val items = MutableLiveData<List<LearningItem>>().apply {
        value = mutableListOf()
    }

    val currentLearningItem: MutableLiveData<LearningItem> by lazy {
        MutableLiveData<LearningItem>()
    }

    fun updateCurrentLearningItem(learningItem: LearningItem) {
        currentLearningItem.value = learningItem
    }

}