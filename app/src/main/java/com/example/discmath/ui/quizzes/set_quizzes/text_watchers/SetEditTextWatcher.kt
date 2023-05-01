package com.example.discmath.ui.quizzes.set_quizzes.text_watchers

import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


val REGEX_TO_REMOVE: Regex = Regex("[, ]")

fun CharSequence.hasDuplicates(): Boolean {
    return this.toSet().size != this.length
}

open class SetEditTextWatcher(open val textField: EditText): TextWatcher {

    protected var previousText: String = ""

    override fun beforeTextChanged(s: CharSequence?, before: Int, p2: Int, count: Int) {

    }

    override fun onTextChanged(text: CharSequence?, p1: Int, before: Int, count: Int) {
        // Changing the text only if something has changed
        if (before != count) {
            textField.removeTextChangedListener(this)
            val formattedText = text!!.replace(REGEX_TO_REMOVE, "")
            val newText = if (formattedText.hasDuplicates()) {
                vibrate()
                previousText
            } else {
                formattedText.toSet().joinToString()
            }
            previousText = newText
            textField.setText(newText)
            textField.setSelection(newText.length)
            textField.addTextChangedListener(this)
        }
    }

    override fun afterTextChanged(field: Editable?) {

    }

    protected fun vibrate() {
        val vibrator = textField.context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT > 25) {
            vibrator.vibrate(VibrationEffect.createOneShot(150,
                VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(150)
        }
    }
}
