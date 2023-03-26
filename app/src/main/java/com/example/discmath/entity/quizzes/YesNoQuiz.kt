package com.example.discmath.entity.quizzes


open class YesNoQuiz(override val problemUrl: String,
                     override val correctAnswerIndex: Int):
    Quiz(problemUrl, correctAnswerIndex, QuizType.YES_NO)