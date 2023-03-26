package com.example.discmath.entity.learning_item

enum class LearningItemType(val typeName: String) {

    LECTURE("lecture"), SUMMARY("summary"), QUIZ("quiz");

    companion object {
        fun getLearningTypeFromString(string: String): LearningItemType {
            return values().first { it.typeName == string }
        }
    }

}