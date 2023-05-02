package com.example.discmath.entity.learning_item

import com.example.discmath.entity.learning_section.*
import com.google.firebase.firestore.DocumentSnapshot


data class LearningItem(val type: LearningItemType, val name: String, val description: Array<*>?,
                        val urlVideo: String, val urlPdf: String) {

    constructor(typeString: String, name: String, description: Array<*>?,
                urlVideo: String, urlPdf: String) :
            this(LearningItemType.getLearningTypeFromString(typeString), name,
                description, urlVideo, urlPdf)

    constructor(documentSnapshot: DocumentSnapshot): this(
        documentSnapshot.get(TYPE_FIELD_KEY) as String,
        documentSnapshot.get(NAME_FIELD_KEY) as String,
        (documentSnapshot.get(DESCRIPTION_FIELD_KEY) as ArrayList<*>?)?.toArray(),
        documentSnapshot.get(VIDEO_URL_FIELD_KEY) as String,
        documentSnapshot.get(PDF_URL_FIELD_KEY) as String
    )

}
