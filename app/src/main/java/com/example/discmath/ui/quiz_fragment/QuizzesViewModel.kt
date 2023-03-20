package com.example.discmath.ui.quiz_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizzesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is quiz_fragment Fragment"
    }
    val text: LiveData<String> = _text
}