package com.example.discmath.ui.quiz_fragment.set_quizzes.text_watchers

import android.text.Editable
import android.widget.EditText

class UniversalSetAdditionalElementsTextWatcher(override val textField: EditText):
                                SetEditTextWatcher(textField) {


    private lateinit var observer: UniversalSetObserver

    fun setObserver(observer: UniversalSetObserver) {
        this.observer = observer
    }

    fun notifyNewElements(elements: Collection<Char>) {
        textField.removeTextChangedListener(this)
        val remainingChars = textField.text.toSet().toMutableSet()
        remainingChars.removeAll(elements.toSet())
        remainingChars.removeAll(setOf(',', ' '))
        previousText = remainingChars.joinToString()
        textField.setText(previousText)
        textField.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, before: Int, p2: Int, count: Int) {}

    override fun onTextChanged(text: CharSequence?, p1: Int, before: Int, count: Int) {
        if (observer.getElements().intersect(text!!.toSet()).isNotEmpty()) {
            vibrate()
            textField.removeTextChangedListener(this)
            textField.setText(previousText)
            textField.addTextChangedListener(this)
        } else {
            super.onTextChanged(text, p1, before, count)
        }
    }

    override fun afterTextChanged(field: Editable?) {}


}