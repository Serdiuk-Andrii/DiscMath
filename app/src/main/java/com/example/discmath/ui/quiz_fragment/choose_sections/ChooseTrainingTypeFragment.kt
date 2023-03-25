package com.example.discmath.ui.quiz_fragment.choose_sections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentChooseTrainingTypeBinding


class ChooseTrainingTypeFragment : Fragment() {

    private var _binding: FragmentChooseTrainingTypeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var randomTypeCardView: CardView
    private lateinit var chooseSectionsCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTrainingTypeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        randomTypeCardView = binding.trainingRandomTypeOption
        val navController = findNavController()
        randomTypeCardView.setOnClickListener {
            randomTypeTrainingSelected()
            navController.navigate(R.id.chooseTimeTypeFragment)
        }
        chooseSectionsCardView = binding.trainingChooseSectionsOptions
        chooseSectionsCardView.setOnClickListener {
            specificSectionsTrainingSelected()
            navController.navigate(R.id.move_from_training_type_to_section_selection)
        }
        // Inflate the layout for this fragment
        return root
    }

    private fun randomTypeTrainingSelected() {
        Toast.makeText(context, "Random selected", Toast.LENGTH_SHORT).show()

    }

    private fun specificSectionsTrainingSelected() {
        Toast.makeText(context, "Specific sections selected", Toast.LENGTH_SHORT).show()
    }

}