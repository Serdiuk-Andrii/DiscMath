package com.example.discmath.entity.quizzes.quiz_preferences

import com.example.discmath.entity.learning_section.LearningSection

const val DEFAULT_QUIZ_DURATION = 5

//TODO: remove this class
class QuizPreference private constructor(val sectionRestrictions: Array<LearningSection>?,
                                         val timeInMinutes: Int) {
    class QuizPreferenceBuilder(
        var sectionRestrictions: Array<LearningSection>? = null,
        var timeInMinutes: Int = DEFAULT_QUIZ_DURATION
    ) {
        fun setSectionRestrictions(sectionRestrictions: Array<LearningSection>) =
            apply { this.sectionRestrictions = sectionRestrictions }

        fun setTimeInMinutes(timeInMinutes: Int) = apply { this.timeInMinutes = timeInMinutes }
        fun build() = QuizPreference(this@QuizPreferenceBuilder.sectionRestrictions,
                                    this@QuizPreferenceBuilder.timeInMinutes)
    }

}
