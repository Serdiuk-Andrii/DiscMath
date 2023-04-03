package com.example.discmath.entity.quizzes

import com.google.firebase.firestore.DocumentSnapshot

const val TYPE_KEY = "type"

class BasicQuizFactory: QuizFactory {

    override fun getQuizFromDocumentSnapshot(documentSnapshot: DocumentSnapshot,
    ): Quiz {
        val quizType: QuizType = QuizType.getQuizTypeFromString(
            documentSnapshot.get(TYPE_KEY) as String)
        val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
        return when(quizType) {
            QuizType.MULTIPLE_CHOICE -> {
                val correctAnswerIndex: Int = documentSnapshot.getLong(ANSWER_KEY)!!.toInt()
                FourChoicesQuiz(problemUrl,
                    documentSnapshot.get(SOLUTIONS_KEY) as ArrayList<*>,
                    correctAnswerIndex
                )
            }
            QuizType.YES_NO -> {
                val correctAnswerIndex: Int = documentSnapshot.getLong(ANSWER_KEY)!!.toInt()
                YesNoQuiz(problemUrl, correctAnswerIndex)
            }
            QuizType.SET_EQUATION -> {
                SetEquationQuiz(problemUrl,
                    documentSnapshot.get(LEFT_EQUATION_KEY) as String,
                    documentSnapshot.get(RIGHT_EQUATION_KEY) as String
                    )
            }
        }
    }

}