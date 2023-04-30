package com.example.discmath.ui.assistant.graph_theory.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.discmath.ui.assistant.graph_theory.GraphData

class GraphBuilderViewModel: ViewModel() {


    private val _graphs = MutableLiveData<MutableList<GraphData>>(mutableListOf())
    val graphs: LiveData<MutableList<GraphData>> = _graphs


    fun appendGraph(graphData: GraphData) {
        _graphs.value!!.add(graphData)
    }

    fun updateGraph(graphData: GraphData, index: Int) {
        _graphs.value!![index] = graphData
    }

}