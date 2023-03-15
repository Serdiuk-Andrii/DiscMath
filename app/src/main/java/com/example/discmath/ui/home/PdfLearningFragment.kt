package com.example.discmath.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.discmath.databinding.FragmentLearningPdfBinding

class PdfLearningFragment : Fragment() {

    private var _binding: FragmentLearningPdfBinding? = null

    private lateinit var name: String
    private lateinit var url: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME_KEY)!!
            url = it.getString(URL_PDF_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningPdfBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        return root
    }

}