package com.example.discmath.entity.quizzes

enum class QuizType(val typeName: String) {

    MULTIPLE_CHOICE("multiple_choice"), YES_NO("true_false"),
    SET_EQUATION("set"), IMAGE_PROBLEM_TEXT_ANSWER("text"),
    MULTIPLE_CHOICE_IMAGE("image_multiple_choice");

    companion object {
        fun getQuizTypeFromString(string: String): QuizType {
            return QuizType.values().first { it.typeName == string }
        }
    }
}