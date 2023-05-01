package com.example.discmath.ui.quizzes.set_quizzes.text_watchers

import com.example.discmath.ui.quizzes.set_quizzes.adapter.SetInputAdapter

class BoundToUniversalSetInputAdapter(dataset: Array<Char>, private val
        watcher: UniversalSetObserver
): SetInputAdapter(dataset) {

    override fun appendTextWatcher(holder: ViewHolder) {
        holder.textField.addTextChangedListener(
            SetEditTextWatcherBoundToUniversalSet(holder.textField, watcher)
        )
    }
}