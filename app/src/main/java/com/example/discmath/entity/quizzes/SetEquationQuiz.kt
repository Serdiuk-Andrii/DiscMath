package com.example.discmath.entity.quizzes

const val LEFT_EQUATION_KEY = "left"
const val RIGHT_EQUATION_KEY = "right"

class SetEquationQuiz(override val problemUrl: String, val leftEquation: String,
                        val rightEquation: String, val isUniversalSetRequired: Boolean,
                      override val learningSectionName: String):
    ImageProblemQuiz(problemUrl, QuizType.SET_EQUATION, learningSectionName) {
}
