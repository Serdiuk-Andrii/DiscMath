package com.example.discmath.ui.quiz_fragment

class SimpleSetInputAdapter(dataset: Array<Char>): SetInputAdapter(dataset) {

    override fun appendTextWatcher(holder: ViewHolder) {
        holder.textField.addTextChangedListener(SetEditTextWatcher(holder.textField))
    }
}