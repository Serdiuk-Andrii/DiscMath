package com.example.discmath.entity.learning_item

import com.example.discmath.entity.learning_section.NAME_FIELD_KEY
import com.example.discmath.entity.learning_section.PDF_URL_FIELD_KEY
import com.example.discmath.entity.learning_section.TYPE_FIELD_KEY
import com.example.discmath.entity.learning_section.VIDEO_URL_FIELD_KEY
import com.google.firebase.firestore.DocumentSnapshot


data class LearningItem(val type: LearningItemType, val name: String,
                        val urlVideo: String, val urlPdf: String) {

    constructor(typeString: String, name: String,
                urlVideo: String, urlPdf: String) :
            this(LearningItemType.getLearningTypeFromString(typeString), name, urlVideo, urlPdf)

    constructor(documentSnapshot: DocumentSnapshot): this(
        documentSnapshot.get(TYPE_FIELD_KEY) as String,
        documentSnapshot.get(NAME_FIELD_KEY) as String,
        documentSnapshot.get(VIDEO_URL_FIELD_KEY) as String,
        documentSnapshot.get(PDF_URL_FIELD_KEY) as String
    )

}
