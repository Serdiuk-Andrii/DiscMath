package com.example.discmath.ui.assistant.graph_theory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graph_theory.Graph

class GraphBuilderViewModel: ViewModel() {

    private val _firstGraph = MutableLiveData<Graph?>(null)
    val firstGraph: LiveData<Graph?> = _firstGraph

    private val _secondGraph = MutableLiveData<Graph?>(null)
    val secondGraph: LiveData<Graph?> = _secondGraph

    private fun currentGraph(): MutableLiveData<Graph?> {
       return if (firstGraph.value == null) _firstGraph else _secondGraph
    }

    fun setCurrentGraph(graph: Graph) {
        currentGraph().value = graph
    }

    private val _nextFragmentId = MutableLiveData<Int>()
    val nextFragmentId: LiveData<Int> = _nextFragmentId

    fun setNextFragmentId(fragmentId: Int) {
        _nextFragmentId.value = fragmentId
    }
}