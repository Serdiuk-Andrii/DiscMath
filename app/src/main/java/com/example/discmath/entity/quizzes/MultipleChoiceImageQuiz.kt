package com.example.discmath.entity.quizzes

const val TITLE_KEY = "title"
const val OPTIONS_KEY = "options"
const val CORRECT_OPTIONS_KEY = "correct_options"

class MultipleChoiceImageQuiz
    (val optionsUrls: Array<String>, val correctAnswersIndices: Array<Long>, val title: String,
     override val quizType: QuizType, override val learningSectionName: String):
    Quiz(quizType, learningSectionName)