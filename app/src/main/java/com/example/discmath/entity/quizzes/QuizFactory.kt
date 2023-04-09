package com.example.discmath.entity.quizzes

import com.google.firebase.firestore.DocumentSnapshot

interface QuizFactory {

    fun getQuizFromDocumentSnapshot(documentSnapshot: DocumentSnapshot,
                                    learningSectionName: String): Quiz

}