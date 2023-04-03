package com.example.discmath.entity.quizzes

enum class QuizType(val typeName: String) {

    MULTIPLE_CHOICE("multiple_choice"), YES_NO("true_false"),
    SET_EQUATION("set");

    companion object {
        fun getQuizTypeFromString(string: String): QuizType {
            return QuizType.values().first { it.typeName == string }
        }
    }
}