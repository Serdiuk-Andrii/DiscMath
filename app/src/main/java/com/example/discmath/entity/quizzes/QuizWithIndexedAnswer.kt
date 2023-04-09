package com.example.discmath.entity.quizzes

abstract class QuizWithIndexedAnswer(override val problemUrl: String,
                                     open val correctAnswerIndex: Int, override val quizType: QuizType,
                                     override val learningSectionName: String,
                                     ): Quiz(problemUrl, quizType, learningSectionName) {
}