package com.example.discmath.entity.quizzes


open class YesNoQuiz(override val problemUrl: String,
                     override val correctAnswerIndex: Int):
    QuizWithIndexedAnswer(problemUrl, correctAnswerIndex, QuizType.YES_NO)