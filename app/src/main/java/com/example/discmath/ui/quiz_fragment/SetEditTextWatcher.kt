package com.example.discmath.ui.quiz_fragment

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


fun String.modifyToSetRepresentation(): String {
    if (this.length <= 1) {
        return this
    }
    return if (!this.last().isLetter()) {
        this.removeSuffix(", ")
    } else {

        if (this.dropLast(1).contains(this.last())) {
            this.dropLast(1)
        } else {
            StringBuilder(this).insert(this.length - 1, ", ").toString()
        }
    }
}

class SetEditTextWatcher(private val textField: EditText) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, before: Int, p2: Int, count: Int) {

    }

    override fun onTextChanged(text: CharSequence?, p1: Int, before: Int, count: Int) {
        textField.removeTextChangedListener(this)
        textField.setText(text!!.toString().modifyToSetRepresentation())
        textField.setSelection(textField.text.length)
        textField.addTextChangedListener(this)
    }

    override fun afterTextChanged(field: Editable?) {

    }
}
