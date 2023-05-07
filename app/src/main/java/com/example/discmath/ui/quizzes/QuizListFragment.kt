package com.example.discmath.ui.quizzes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.FragmentQuizListBinding
import com.example.discmath.ui.util.navigation.FunctionElement
import com.example.discmath.ui.util.navigation.FunctionElementAdapter


class QuizListFragment : Fragment() {

    // Views
    private var _binding: FragmentQuizListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var quizViewPager: ViewPager2

    // Navigation
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizListBinding.inflate(inflater, container, false)
        navController = findNavController()
        initializeViews()
        return binding.root
    }

    private fun initializeViews() {
        quizViewPager = binding.quizOptionsViewPager
        val options: Array<FunctionElement> = arrayOf(
            FunctionElement(resources.getString(R.string.quiz_test_section_name),
                R.id.action_quizListFragment_to_chooseMultipleSectionsFragment2,
                ResourcesCompat.getDrawable(resources, R.drawable.quiz, null)!!,
                arrayOf("Проходження тестів за обраними темами")),
            FunctionElement(resources.getString(R.string.quiz_logic_game_section_name),
                R.id.action_quizListFragment_to_quizLogicFormulaFragment,
                ResourcesCompat.getDrawable(resources, R.drawable.logic, null)!!,
                arrayOf("Перевірка правильності таблиці істинності")
            )
        )
        quizViewPager.adapter = FunctionElementAdapter(options, ::navigateTo)
    }

    private fun navigateTo(destination: Int) {
        navController.navigate(destination)
    }

}