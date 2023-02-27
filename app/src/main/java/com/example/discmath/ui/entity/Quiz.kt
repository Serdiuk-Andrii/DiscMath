package com.example.discmath.ui.entity

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.RequestManager
import com.example.discmath.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.storage.StorageReference

fun loadImageIntoViewFrom(url: String, imageView: ImageView, loader: RequestManager,
                          storageReference: StorageReference) {
    loader.load(storageReference.child(url)).into(imageView)
}

data class Quiz(val problemUrl: String, val solutionsUrl: ArrayList<*>,
           val correctAnswer: Int) {


    fun loadProblemInto(imageView: ImageView, loader: RequestManager,
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

    fun loadAnswerInto(answerIndex: Int, imageView: ImageView, loader: RequestManager,
                       storageReference: StorageReference ) {
        val solutionData = solutionsUrl[answerIndex]
        if (solutionData !is Map<*, *>) {
            throw Exception("Incorrect quiz data format")
        }
        val url: String = solutionData["url"] as String
        val explanation: String = (solutionData["explanation"] as String?)?.substring(1)
            ?.dropLast(1) ?: "obvious"

        val dialog = BottomSheetDialog(imageView.context)
        if (answerIndex == correctAnswer) {
            imageView.setOnClickListener {
                Toast.makeText(imageView.context, "Correct!\nExplanation: $explanation",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            imageView.setOnClickListener {
                dialog.setContentView(R.layout.incorrect_answer_dialog_layout)
                val explanationTextView = dialog.findViewById<TextView>(R.id.explanation_text)
                explanationTextView?.text = explanation
                dialog.show()
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

