package com.example.discmath.ui.quiz_fragment.set_quizzes.adapter

import com.example.discmath.ui.quiz_fragment.set_quizzes.text_watchers.SetEditTextWatcher

class SimpleSetInputAdapter(dataset: Array<Char>): SetInputAdapter(dataset) {

    override fun appendTextWatcher(holder: ViewHolder) {
        holder.textField.addTextChangedListener(SetEditTextWatcher(holder.textField))
    }
}