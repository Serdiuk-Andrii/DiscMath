package com.example.discmath.entity.quizzes

abstract class QuizWithIndexedAnswer(override val problemUrl: String,
                                     open val correctAnswerIndex: Int,
                               override val quizType: QuizType): Quiz(problemUrl, quizType) {
}