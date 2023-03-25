package com.example.discmath.entity.learning_section

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

data class LearningSection(val name: String, val collectionPath: String) {

    fun getLearningElementsPath(): String = "${collectionPath}/${SECTION_ELEMENTS_STORAGE_PATH}"
    fun getQuizzesPath(): String = "${collectionPath}/${SECTION_QUIZZES_STORAGE_PATH}"

}