package com.example.discmath.ui.quizzes.set_quizzes.adapter

import com.example.discmath.ui.quizzes.set_quizzes.text_watchers.SetEditTextWatcher

class SimpleSetInputAdapter(dataset: Array<Char>): SetInputAdapter(dataset) {

    override fun appendTextWatcher(holder: ViewHolder) {
        holder.textField.addTextChangedListener(SetEditTextWatcher(holder.textField))
    }
}