package com.example.discmath.ui.entity.quizzes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.StorageReference

const val FADED_ANSWER_ALPHA = 0.5F
const val SOLUTION_URL_KEY = "url"
const val SOLUTION_EXPLANATION_KEY = "explanation"

const val DEFAULT_EXPLANATION = "obvious"

open class FourChoicesQuiz(
    override val problemUrl: String, open val solutionsUrl: ArrayList<*>,
    open val correctAnswer: Int, override val clickListener: View.OnClickListener?):
    Quiz(problemUrl, clickListener) {

    private fun loadAnswerInto(answerIndex: Int, imageView: ImageView, loader: RequestManager,
                               storageReference: StorageReference
    ) {
        imageView.alpha = 1F
        val solutionData = solutionsUrl[answerIndex]
        if (solutionData !is Map<*, *>) {
            throw Exception("Incorrect quiz data format")
        }
        val url: String = solutionData[SOLUTION_URL_KEY] as String
        val explanation: String = (solutionData[SOLUTION_EXPLANATION_KEY] as String?)
            ?.dropLast(1) ?: DEFAULT_EXPLANATION

        val dialog = BottomSheetDialog(imageView.context)
        if (answerIndex == correctAnswer) {
            if (clickListener != null) {
                imageView.setOnClickListener(clickListener)
            }
        } else {
            imageView.setOnClickListener {
                dialog.setContentView(R.layout.incorrect_answer_dialog_layout)
                val explanationTextView = dialog.findViewById<TextView>(R.id.explanation_text)
                explanationTextView?.text = explanation
                dialog.show()
                imageView.isClickable = false
                imageView.alpha = FADED_ANSWER_ALPHA
            }
        }
        loadImageIntoViewFrom(url, imageView, loader, storageReference)
    }

    fun loadAnswerInto(answerIndex: Int, imageView: ImageView, loader: RequestManager,
                       storageReference: StorageReference, makeVisible: Boolean) {
        loadAnswerInto(answerIndex, imageView, loader, storageReference)
        if (makeVisible) {
            imageView.visibility = View.VISIBLE
        }
    }

}