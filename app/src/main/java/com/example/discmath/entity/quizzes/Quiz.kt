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

abstract class Quiz(
    open val problemUrl: String, open val clickListener: OnClickListener?) {

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

