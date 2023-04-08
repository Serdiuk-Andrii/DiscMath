package com.example.discmath.entity.learning_section

import com.google.firebase.firestore.DocumentSnapshot

// Firebase Storage
const val SECTION_NAME_STORAGE_PATH = "name"
const val SECTIONS_COLLECTION_STORAGE_PATH = "sections"
const val SECTION_ELEMENTS_STORAGE_PATH = "items"
const val SECTION_QUIZZES_STORAGE_PATH = "quizzes"

// Field keys
const val TYPE_FIELD_KEY = "type"
const val NAME_FIELD_KEY = "name"
const val VIDEO_URL_FIELD_KEY = "url_video"
const val PDF_URL_FIELD_KEY = "url_pdf"
const val BACKGROUND_IMAGE_URL_KEY = "image"
const val DESCRIPTION_KEY = "description"
const val ORDER_KEY = "order"

data class LearningSection(val name: String, val description: String, val backgroundImageUrl: String?,
                           private val order: Long, val collectionPath: String):
            Comparable<LearningSection> {

    constructor(documentSnapshot: DocumentSnapshot) :
            this(name = documentSnapshot[SECTION_NAME_STORAGE_PATH] as String,
                description = documentSnapshot[DESCRIPTION_KEY] as String,
                backgroundImageUrl = documentSnapshot[BACKGROUND_IMAGE_URL_KEY] as String?,
                order = documentSnapshot[ORDER_KEY] as Long,
                collectionPath = documentSnapshot.reference.path) {

    }

    fun getLearningElementsPath(): String = "${collectionPath}/${SECTION_ELEMENTS_STORAGE_PATH}"
    fun getQuizzesPath(): String = "${collectionPath}/${SECTION_QUIZZES_STORAGE_PATH}"

    override fun compareTo(other: LearningSection): Int {
        return this.order.compareTo(other.order)
    }
}