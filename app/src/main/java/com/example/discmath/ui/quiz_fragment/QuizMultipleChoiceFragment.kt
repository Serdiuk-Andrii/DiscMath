package com.example.discmath.ui.quiz_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.discmath.databinding.FragmentQuizMultipleChoiceBinding
import com.example.discmath.entity.quizzes.FourChoicesQuiz
import com.example.discmath.entity.quizzes.QUIZ_PROBLEM_KEY
import com.example.discmath.entity.quizzes.SOLUTIONS_KEY
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class QuizMultipleChoiceFragment : Fragment() {

    private var _binding: FragmentQuizMultipleChoiceBinding? = null

    private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private var db = Firebase.firestore

    private var problemsImagesData: MutableList<FourChoicesQuiz> = mutableListOf()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuizMultipleChoiceBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val clickListener = OnClickListener {
            loadNextProblem()
            Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
        }
        db.collection("images").get(Source.DEFAULT).addOnSuccessListener { querySnapshot ->
            problemsImagesData.addAll(querySnapshot.documents.map
            { documentSnapshot ->  FourChoicesQuiz(documentSnapshot.get(QUIZ_PROBLEM_KEY) as String,
                                    documentSnapshot.get(SOLUTIONS_KEY) as ArrayList<*>,
                (documentSnapshot.get("correct") as Long).toInt(), clickListener)
            })
            loadNextProblem()
        }
        binding.skipButton.setOnClickListener { loadNextProblem() }
        return root
    }

    private fun loadNextProblem() {
        if (problemsImagesData.isNotEmpty()) {
            val quiz: FourChoicesQuiz = problemsImagesData.removeFirst()
            val loader: RequestManager = Glide.with(this)
            quiz.loadProblemInto(binding.problem, loader, storageRef, true)
            quiz.loadAnswerInto(0, binding.answer1, loader, storageRef, true)
            quiz.loadAnswerInto(1, binding.answer2, loader, storageRef, true)
            quiz.loadAnswerInto(2, binding.answer3, loader, storageRef, true)
            quiz.loadAnswerInto(3, binding.answer4, loader, storageRef, true)
        } else {
            Toast.makeText(context, "There are no images left", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}