package com.example.discmath.entity.quizzes

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.FirebaseStorage


private var storage: FirebaseStorage = FirebaseStorage.getInstance()
fun loadImageIntoViewFrom(url: String, imageView: ImageView, loader: RequestManager) {
    loader.load(storage.getReferenceFromUrl(url)).into(imageView)
}

// This field points to the image saved in the Firebase Storage
const val QUIZ_PROBLEM_KEY = "url"
const val ANSWER_KEY = "correct"

abstract class Quiz(open val problemUrl: String, open val quizType: QuizType,
                    open val learningSectionName: String) {

    private fun loadProblemInto(imageView: ImageView, loader: RequestManager) {

        loadImageIntoViewFrom(problemUrl, imageView, loader)
    }

    fun loadProblemInto(imageView: ImageView, loader: RequestManager, makeVisible: Boolean) {

        loadProblemInto(imageView, loader)
        if (makeVisible) {
            imageView.visibility = View.VISIBLE
        }
    }

}

