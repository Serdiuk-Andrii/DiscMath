package com.example.discmath.ui.entity.learning_item


data class LearningItem(val type: LearningItemType, val name: String,
                        val urlVideo: String, val urlPdf: String) {

    constructor(typeString: String, name: String,
                urlVideo: String, urlPdf: String) :
            this(LearningItemType.getLearningTypeFromString(typeString), name, urlVideo, urlPdf)

}
