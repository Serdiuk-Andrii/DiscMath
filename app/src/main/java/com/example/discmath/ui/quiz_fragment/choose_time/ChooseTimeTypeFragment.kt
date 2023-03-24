package com.example.discmath.ui.quiz_fragment.choose_time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.databinding.FragmentChooseTimeTypeBinding
import com.example.discmath.ui.quiz_fragment.QuizzesViewModel

val timeOptions: Array<Int> = arrayOf(3, 5, 10)

class ChooseTimeTypeFragment : Fragment() {

    private var _binding: FragmentChooseTimeTypeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var quizzesViewModel: QuizzesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quizzesViewModel = ViewModelProvider(requireActivity())[QuizzesViewModel::class.java]
        _binding = FragmentChooseTimeTypeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Recycler view
        val timeOptionsRecyclerView: RecyclerView = binding.timeOptionsRecyclerView
        timeOptionsRecyclerView.adapter = TimeOptionAdapter(timeOptions, ::itemClicked)
        if (quizzesViewModel.sectionRestrictions.value == null) {
            Toast.makeText(context, "No sections!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "There are " +
                    "${quizzesViewModel.sectionRestrictions.value!!.size} sections!",
                Toast.LENGTH_SHORT).show()
        }

        // Inflate the layout for this fragment
        return root
    }

    private fun itemClicked(value: Int) {
        Toast.makeText(context, "Time chosen: ${value}", Toast.LENGTH_SHORT).show()
    }

}