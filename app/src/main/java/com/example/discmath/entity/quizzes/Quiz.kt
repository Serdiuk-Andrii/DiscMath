package com.example.discmath.entity.quizzes

import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.StorageReference

fun loadImageIntoViewFrom(url: String, imageView: ImageView, loader: RequestManager,
                          storageReference: StorageReference) {
    loader.load(storageReference.child(url)).into(imageView)
}

const val QUIZ_PROBLEM_KEY = "url"
const val ANSWER_KEY = "correct"

abstract class Quiz(
    open val problemUrl: String, open val correctAnswerIndex: Number,
    open val clickListener: OnClickListener?) {

    private fun loadProblemInto(imageView: ImageView, loader: RequestManager,
                                storageReference: StorageReference) {
        loadImageIntoViewFrom(problemUrl, imageView, loader, storageReference)
    }

    fun loadProblemInto(imageView: ImageView, loader: RequestManager,
                        storageReference: StorageReference, makeVisible: Boolean) {
        loadProblemInto(imageView, loader, storageReference)
        if (makeVisible) {
            imageView.visibility = View.VISIBLE
        }
    }

}

