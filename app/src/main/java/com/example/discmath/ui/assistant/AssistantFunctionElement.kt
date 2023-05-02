package com.example.discmath.ui.assistant

import android.graphics.drawable.Drawable

fun Array<String>.getFormattedText(): String {
    return this.map { "—$it\n" }.reduce{accumulator, string -> accumulator.plus(string)}
}

open class AssistantFunctionElement(
    override val title: String,
    override val destinationFragmentId: Int,
    val drawable: Drawable,
    open val functionsText: Array<String>
): NamedNavigationElement(title, destinationFragmentId) {


    fun getFunctionsPrettyText(): String {
        return functionsText.getFormattedText()
    }

}
