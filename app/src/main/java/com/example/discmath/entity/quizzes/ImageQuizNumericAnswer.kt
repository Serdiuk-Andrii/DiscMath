package com.example.discmath.entity.quizzes

const val TEXT_ANSWER_KEY = "answer"

open class ImageQuizNumericAnswer(override val problemUrl: String, open val correctAnswer: Int,
                                  override val learningSectionName: String):
    ImageProblemQuiz(problemUrl, QuizType.IMAGE_PROBLEM_TEXT_ANSWER, learningSectionName)