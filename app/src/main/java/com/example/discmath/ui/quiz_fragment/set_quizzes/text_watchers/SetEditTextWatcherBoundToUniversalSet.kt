package com.example.discmath.ui.quiz_fragment.set_quizzes.text_watchers

import android.widget.EditText

fun String.getDifferenceCharacters(other: String): Collection<Char> {
    val list = this.toMutableList()
    list.removeAll(other.toList())
    return list
}

class SetEditTextWatcherBoundToUniversalSet(override val textField: EditText, private val universalSetWatcher:
UniversalSetObserver
): SetEditTextWatcher(textField) {


    override fun onTextChanged(text: CharSequence?, p1: Int, before: Int, count: Int) {
        val beforeString = super.previousText.replace(REGEX_TO_REMOVE, "")
        super.onTextChanged(text, p1, before, count)
        val afterString = super.previousText.replace(REGEX_TO_REMOVE, "")
        if (beforeString.length > afterString.length) {
            // Character has been removed
            universalSetWatcher.notifyElementsRemoved(
                beforeString.getDifferenceCharacters(afterString)
            )
        } else if (beforeString.length < afterString.length) {
            // Character has been added
            universalSetWatcher.notifyNewElements(afterString.getDifferenceCharacters(beforeString))
        }
    }
}