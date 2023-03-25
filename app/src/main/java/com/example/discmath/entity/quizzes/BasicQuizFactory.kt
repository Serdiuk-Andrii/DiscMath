package com.example.discmath.entity.quizzes

import com.google.firebase.firestore.DocumentSnapshot

const val TYPE_KEY = "type"

class BasicQuizFactory: QuizFactory {

    override fun getQuizFromDocumentSnapshot(documentSnapshot: DocumentSnapshot,
    ): Quiz {
        val quizType: QuizType = QuizType.getQuizTypeFromString(
            documentSnapshot.get(TYPE_KEY) as String)
        val problemUrl: String = documentSnapshot.get(QUIZ_PROBLEM_KEY) as String
        val correctAnswerIndex: Number = documentSnapshot.get(ANSWER_KEY) as Number
        return when(quizType) {
            QuizType.MULTIPLE_CHOICE -> {
                FourChoicesQuiz(problemUrl,
                    documentSnapshot.get(SOLUTIONS_KEY) as ArrayList<*>,
                    correctAnswerIndex
                ) {}
            }
            QuizType.YES_NO -> {
                YesNoQuiz(problemUrl, correctAnswerIndex) {}
            }
        }
    }

}