package com.example.discmath.entity.quizzes

const val TEXT_ANSWER_KEY = "answer"

open class ImageQuizTextAnswer(override val problemUrl: String, open val correctAnswer: String,
                          override val learningSectionName: String):
    Quiz(problemUrl, QuizType.IMAGE_PROBLEM_TEXT_ANSWER, learningSectionName)