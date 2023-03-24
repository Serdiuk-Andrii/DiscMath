package com.example.discmath.ui.quiz_fragment


import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizPreferencesSummaryBinding


class QuizPreferencesSummaryFragment : Fragment() {

    private var _binding: FragmentQuizPreferencesSummaryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var quizzesViewModel: QuizzesViewModel

    private lateinit var sectionsNumberTextView: TextView
    private lateinit var timeChosenTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizzesViewModel::class.java]
        _binding = FragmentQuizPreferencesSummaryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        sectionsNumberTextView = binding.sectionsChosenNumber
        timeChosenTextView = binding.timeChosen

        val res: Resources = resources
        val text: String = String.format(res.getString(R.string.quiz_summary_sections_number_text),
                quizzesViewModel.sectionRestrictions.value!!.size)
        sectionsNumberTextView.text = text
        timeChosenTextView.text = quizzesViewModel.time.value!!.toString()
        return root
    }


}