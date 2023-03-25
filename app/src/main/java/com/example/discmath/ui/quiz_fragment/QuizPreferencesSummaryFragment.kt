package com.example.discmath.ui.quiz_fragment


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizPreferencesSummaryBinding
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.quizzes.BasicQuizFactory
import com.example.discmath.entity.quizzes.QuizFactory
import com.example.discmath.ui.quiz_fragment.view_models.QuizPreferencesViewModel
import com.example.discmath.ui.quiz_fragment.view_models.QuizViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class QuizPreferencesSummaryFragment : Fragment() {

    // Firebase
    private var db = Firebase.firestore
    private lateinit var quizFactory: QuizFactory

    private var _binding: FragmentQuizPreferencesSummaryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // View-models
    private lateinit var quizPreferencesViewModel: QuizPreferencesViewModel
    private lateinit var quizzesViewModel: QuizViewModel

    // Views
    private lateinit var sectionsNumberTextView: TextView
    private lateinit var timeChosenTextView: TextView
    private lateinit var startQuizButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializeViewModels()
        _binding = FragmentQuizPreferencesSummaryBinding.inflate(inflater, container, false)
        initializeViews()
        val res: Resources = resources
        val text: String = String.format(
            res.getString(R.string.quiz_summary_sections_number_text),
            quizPreferencesViewModel.sectionRestrictions.value!!.size
        )
        sectionsNumberTextView.text = text
        timeChosenTextView.text = quizPreferencesViewModel.time.value!!.toString()
        quizFactory = BasicQuizFactory()
        return binding.root
    }

    private fun initializeViewModels() {
        val activity = requireActivity()
        quizPreferencesViewModel = ViewModelProvider(activity)[QuizPreferencesViewModel::class.java]
        quizzesViewModel = ViewModelProvider(activity)[QuizViewModel::class.java]
    }

    private fun initializeViews() {
        sectionsNumberTextView = binding.sectionsChosenNumber
        timeChosenTextView = binding.timeChosen
        startQuizButton = binding.startQuizButton
        startQuizButton.setOnClickListener {
            downloadQuizzes()
        }
    }

    private fun downloadQuizzes() {
        val sections: Array<LearningSection>? = quizPreferencesViewModel.sectionRestrictions.value
        if (sections == null) {
            // Download all the quizzes
        } else {
            // Download quizzes from the specified sections
            sections.forEach { section ->
                db.collection(section.getQuizzesPath()).get().addOnSuccessListener {
                    quizzesViewModel.addQuizzes(it.documents.map {
                            documentSnapshot -> quizFactory.getQuizFromDocumentSnapshot(documentSnapshot)
                    })
                }
            }
        }
    }

}