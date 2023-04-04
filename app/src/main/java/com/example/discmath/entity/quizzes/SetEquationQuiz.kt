package com.example.discmath.entity.quizzes

const val LEFT_EQUATION_KEY = "left"
const val RIGHT_EQUATION_KEY = "right"

class SetEquationQuiz(override val problemUrl: String, val leftEquation: String,
                        val rightEquation: String):
    Quiz(problemUrl, QuizType.SET_EQUATION) {
}
