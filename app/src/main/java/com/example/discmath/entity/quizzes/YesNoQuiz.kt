package com.example.discmath.entity.quizzes

import android.view.View

open class YesNoQuiz(override val problemUrl: String, val answer: String, override val clickListener:
                View.OnClickListener?): Quiz(problemUrl, clickListener) {
}