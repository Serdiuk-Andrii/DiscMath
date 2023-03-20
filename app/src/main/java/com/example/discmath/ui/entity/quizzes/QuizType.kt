package com.example.discmath.ui.entity.quizzes

enum class QuizType(val typeName: String) {

    SELECT_CORRECT_CHOICE("select_choice");

    companion object {
        fun getQuizTypeFromString(string: String): QuizType {
            return QuizType.values().first { it.typeName == string }
        }
    }
}