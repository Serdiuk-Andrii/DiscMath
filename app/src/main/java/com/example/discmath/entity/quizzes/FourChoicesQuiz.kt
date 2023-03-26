package com.example.discmath.entity.quizzes

const val FADED_ANSWER_ALPHA = 0.5F
const val SOLUTION_URL_KEY = "url"
const val SOLUTION_EXPLANATION_KEY = "explanation"
const val SOLUTIONS_KEY = "answers"

const val DEFAULT_EXPLANATION = "obvious"

open class FourChoicesQuiz(
    override val problemUrl: String, open val solutionsUrl: ArrayList<*>,
    override val correctAnswerIndex: Int):
    Quiz(problemUrl, correctAnswerIndex, QuizType.MULTIPLE_CHOICE) {
}
