package com.example.discmath.entity.learning_item

import com.example.discmath.entity.learning_section.*
import com.google.firebase.firestore.DocumentSnapshot

const val ORDER_FIELD_KEY = "order"

data class LearningItem(val type: LearningItemType, val name: String, val description: Array<*>?,
                        val urlVideo: String, val urlPdf: String, val order: Long) {

    constructor(typeString: String, name: String, description: Array<*>?,
                urlVideo: String, urlPdf: String, order: Long) :
            this(LearningItemType.getLearningTypeFromString(typeString), name,
                description, urlVideo, urlPdf, order)

    constructor(documentSnapshot: DocumentSnapshot): this(
        documentSnapshot.get(TYPE_FIELD_KEY) as String,
        documentSnapshot.get(NAME_FIELD_KEY) as String,
        (documentSnapshot.get(DESCRIPTION_FIELD_KEY) as ArrayList<*>?)?.toArray(),
        documentSnapshot.get(VIDEO_URL_FIELD_KEY) as String,
        documentSnapshot.get(PDF_URL_FIELD_KEY) as String,
        (documentSnapshot.get(ORDER_FIELD_KEY) ?: 0L) as Long
    )

}
