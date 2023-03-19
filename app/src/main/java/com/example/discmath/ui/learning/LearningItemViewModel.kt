package com.example.discmath.ui.learning

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.ui.entity.LearningItem

class LearningItemViewModel : ViewModel() {

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