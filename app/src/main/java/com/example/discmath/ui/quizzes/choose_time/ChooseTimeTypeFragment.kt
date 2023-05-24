package com.example.discmath.ui.quizzes.choose_time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentChooseTimeTypeBinding
import com.example.discmath.ui.quizzes.view_models.QuizPreferencesViewModel

class ChooseTimeTypeFragment : Fragment() {

    private var _binding: FragmentChooseTimeTypeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var quizPreferencesViewModel: QuizPreferencesViewModel

    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizPreferencesViewModel = ViewModelProvider(requireActivity())[QuizPreferencesViewModel::class.java]
        _binding = FragmentChooseTimeTypeBinding.inflate(inflater, container, false)
        navController = findNavController()
        val root: View = binding.root
        // Recycler view
        val timeOptionsRecyclerView: RecyclerView = binding.timeOptionsRecyclerView
        val timeOptionsWithDrawables = arrayOf(
            Pair("03:00", ResourcesCompat.getDrawable(resources, R.drawable.thunder, null)!!),
            Pair("05:00", ResourcesCompat.getDrawable(resources, R.drawable.running, null)!!),
            Pair("10:00", ResourcesCompat.getDrawable(resources, R.drawable.snail, null)!!)
        )
        timeOptionsRecyclerView.adapter = TimeOptionAdapter(timeOptionsWithDrawables, ::itemClicked)
        // Inflate the layout for this fragment
        return root
    }

    private fun itemClicked(value: String) {
        quizPreferencesViewModel.setTime(value)
        navController.navigate(R.id.action_chooseTimeTypeFragment_to_quizPreferencesSummaryFragment)
    }

}