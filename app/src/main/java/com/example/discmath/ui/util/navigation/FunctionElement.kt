package com.example.discmath.ui.util.navigation

import android.graphics.drawable.Drawable
import com.example.discmath.ui.assistant.NamedNavigationElement

fun Array<String>.getFormattedText(): String {
    return this.map { "—$it\n" }.reduce{accumulator, string -> accumulator.plus(string)}
}

open class FunctionElement(
    override val title: String,
    override val destinationFragmentId: Int,
    val drawable: Drawable,
    open val functionsText: Array<String>
): NamedNavigationElement(title, destinationFragmentId) {


    fun getFunctionsPrettyText(): String {
        return functionsText.getFormattedText()
    }

}
