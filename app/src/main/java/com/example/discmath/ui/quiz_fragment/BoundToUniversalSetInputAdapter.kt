package com.example.discmath.ui.quiz_fragment

class BoundToUniversalSetInputAdapter(dataset: Array<Char>, private val
        watcher: UniversalSetObserver): SetInputAdapter(dataset) {

    override fun appendTextWatcher(holder: ViewHolder) {
        holder.textField.addTextChangedListener(
            SetEditTextWatcherBoundToUniversalSet(holder.textField, watcher))
    }
}