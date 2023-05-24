package com.example.discmath.ui.assistant.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.set_theory.logic.TruthTable

class LogicFormulaViewModel: ViewModel() {

    private val _truthTable: MutableLiveData<TruthTable> = MutableLiveData(null)
    val truthTable: LiveData<TruthTable> = _truthTable

    fun setTruthTable(truthTable: TruthTable) {
        _truthTable.value = truthTable
    }

}