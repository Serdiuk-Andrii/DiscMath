package com.example.discmath.ui.quizzes


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizPreferencesSummaryBinding
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.quizzes.BasicQuizFactory
import com.example.discmath.entity.quizzes.QuizFactory
import com.example.discmath.ui.quizzes.view_models.QuizPreferencesViewModel
import com.example.discmath.ui.quizzes.view_models.QuizViewModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.QuerySnapshot
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
        val selectedTimeText: String = String.format(
            res.getString(R.string.quiz_summary_sections_time),
            quizPreferencesViewModel.time.value!!
        )
        sectionsNumberTextView.text = text
        timeChosenTextView.text = selectedTimeText
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
        val sections: Array<LearningSection> = quizPreferencesViewModel.sectionRestrictions.value!!
        quizzesViewModel.clearQuizzes()
        val navController = findNavController()
        val tasks = sections.map { section ->
            db.collection(section.getQuizzesPath()).get()
        }
        Tasks.whenAllSuccess<QuerySnapshot>(tasks).addOnSuccessListener { querySnapshots ->
            querySnapshots.forEach { snapshot ->
                quizzesViewModel.addQuizzes(snapshot.documents.map { documentSnapshot ->
                    val path = documentSnapshot.reference.path
                    val name = sections.first { path.contains(it.collectionPath) }.name
                    quizFactory.getQuizFromDocumentSnapshot(documentSnapshot, name)
                })
            }
            quizzesViewModel.shuffleQuizzes()
            navController.navigate(R.id.action_quizPreferencesSummaryFragment_to_quizFragment)
        }

    }

}