package com.example.discmath.entity.quizzes

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager

abstract class ImageProblemQuiz(open val problemUrl: String, override val quizType: QuizType,
                                 override val learningSectionName: String):
    Quiz(quizType, learningSectionName){


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