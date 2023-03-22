package com.example.discmath.ui.quiz_fragment.choose_time

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val quizzesViewModel =
            ViewModelProvider(this)[QuizzesViewModel::class.java]
        _binding = FragmentChooseTimeTypeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Recycler view
        val timeOptionsRecyclerView: RecyclerView = binding.timeOptionsRecyclerView
        timeOptionsRecyclerView.adapter = TimeOptionAdapter(timeOptions, ::itemClicked)

        // Inflate the layout for this fragment
        return root
    }

    private fun itemClicked(value: Int) {
        Toast.makeText(context, "Time chosen: ${value}", Toast.LENGTH_SHORT).show()
    }

}