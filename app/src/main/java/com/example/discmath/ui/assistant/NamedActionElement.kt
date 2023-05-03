package com.example.discmath.ui.assistant

import android.graphics.drawable.Drawable

open class NamedActionElement(open val title: String, open val actionImage: Drawable,
                              open val itemClickedCallback: (() -> Unit))