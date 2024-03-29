package com.example.discmath.entity.quizzes

import com.example.set_theory.RPN.SetTheoryOperatorComparator.COMPLEMENT
import com.google.firebase.firestore.DocumentSnapshot

const val TYPE_KEY = "type"

class BasicQuizFactory : QuizFactory {

    override fun getQuizFromDocumentSnapshot(
        documentSnapshot: DocumentSnapshot,
        learningSectionName: String
    ): Quiz {
        val quizType: QuizType = QuizType.getQuizTypeFromString(
            documentSnapshot.get(TYPE_KEY) as String
        )
        //TODO: Consider creating an abstract method for initializing a quiz with a document
        return when (quizType) {
            QuizType.MULTIPLE_CHOICE -> {
                val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
                val correctAnswerIndex: Int = documentSnapshot.getLong(ANSWER_KEY)!!.toInt()
                FourChoicesQuiz(
                    problemUrl,
                    documentSnapshot.get(SOLUTIONS_KEY) as ArrayList<*>,
                    correctAnswerIndex, learningSectionName
                )
            }
            QuizType.YES_NO -> {
                val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
                val correctAnswerIndex: Int = documentSnapshot.getLong(ANSWER_KEY)!!.toInt()
                YesNoQuiz(problemUrl, correctAnswerIndex, learningSectionName)
            }
            QuizType.SET_EQUATION -> {
                val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
                val leftSide = documentSnapshot.get(LEFT_EQUATION_KEY) as String
                val rightSide = documentSnapshot.get(RIGHT_EQUATION_KEY) as String
                val universalSetRequired = leftSide.contains(COMPLEMENT) || rightSide.contains(
                    COMPLEMENT
                )
                SetEquationQuiz(
                    problemUrl, leftSide, rightSide, universalSetRequired,
                    learningSectionName
                )
            }
            QuizType.IMAGE_PROBLEM_TEXT_ANSWER -> {
                val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
                val correctAnswer = documentSnapshot.get(TEXT_ANSWER_KEY) as String
                ImageQuizNumericAnswer(problemUrl, correctAnswer.toInt(), learningSectionName)
            }
            QuizType.MULTIPLE_CHOICE_IMAGE -> {
                val optionsUrl = documentSnapshot.get(OPTIONS_KEY) as ArrayList<String>
                val correctOptions = documentSnapshot.get(CORRECT_OPTIONS_KEY) as ArrayList<Long>
                val title = documentSnapshot.get(TITLE_KEY) as String
                MultipleChoiceImageQuiz(optionsUrl.toTypedArray(), correctOptions.toTypedArray(),
                    title, QuizType.MULTIPLE_CHOICE_IMAGE, learningSectionName)
            }
        }
    }

}