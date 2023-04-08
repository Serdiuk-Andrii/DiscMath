package com.example.discmath.ui.quiz_fragment

import android.widget.TextView

class UniversalSetObserver(private val permanentElementsView: TextView,
                           private val additionalElements: UniversalSetAdditionalElementsTextWatcher) {

    private val permanentElements: MutableMap<Char, Int> = mutableMapOf()

    fun getElements(): Set<Char> = permanentElements.keys

    fun notifyNewElements(elements: Collection<Char>) {
        for (element in elements) {
            permanentElements[element] = permanentElements.getOrElse(element) { 0 } + 1
        }
        permanentElementsView.text = permanentElements.keys.joinToString()
        additionalElements.notifyNewElements(elements)
    }

    fun notifyElementsRemoved(elements: Collection<Char>) {
        for (element in elements) {
            permanentElements[element] = permanentElements[element]!! - 1
            if (permanentElements[element] == 0) {
                permanentElements.remove(element)
            }
        }
        permanentElementsView.text = permanentElements.keys.joinToString()
    }

}