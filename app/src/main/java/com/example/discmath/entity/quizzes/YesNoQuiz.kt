package com.example.discmath.entity.quizzes

import android.view.View



open class YesNoQuiz(override val problemUrl: String,
                     override val correctAnswerIndex: Number,
                     override val clickListener: View.OnClickListener?):
    Quiz(problemUrl, correctAnswerIndex, clickListener)