package com.example.discmath.entity.quizzes


open class YesNoQuiz(override val problemUrl: String,
                     override val correctAnswerIndex: Int,
                     override val learningSectionName: String):
    QuizWithIndexedAnswer(problemUrl, correctAnswerIndex, QuizType.YES_NO, learningSectionName)