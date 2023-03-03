package com.example.discmath.ui.home

enum class LearningItemType(val typeName: String) {

    LECTURE("lecture"), SUMMARY("summary"), QUIZ("quiz");

    companion object {
        fun getLearningTypeFromString(string: String): LearningItemType {
            return values().first { it.typeName == string }
        }
    }

}