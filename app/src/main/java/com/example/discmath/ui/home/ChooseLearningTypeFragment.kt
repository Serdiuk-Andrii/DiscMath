package com.example.discmath.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.R
import com.example.discmath.databinding.FragmentChooseLearningTypeBinding

class ChooseLearningTypeFragment : Fragment() {

    private lateinit var name: String
    private lateinit var urlVideo: String
    private lateinit var urlPdf: String

    private var _binding: FragmentChooseLearningTypeBinding? = null
    private lateinit var videoChoiceCard: CardView
    private lateinit var pdfChoiceCard: CardView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME_KEY)!!
            urlVideo = it.getString(URL_VIDEO_KEY)!!
            urlPdf = it.getString(URL_PDF_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLearningTypeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        videoChoiceCard = binding.optionVideo
        pdfChoiceCard = binding.optionPdf

        val navHostFragment =
            activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main)
        val navController = navHostFragment!!.findNavController()

        videoChoiceCard.setOnClickListener {
            navController.navigate(R.id.video_learning_fragment, Bundle().apply {
                putString(NAME_KEY, name)
                putString(URL_VIDEO_KEY, urlVideo)
            })
        }
        pdfChoiceCard.setOnClickListener {
            navController.navigate(R.id.pdfLearningFragment, Bundle().apply {
                putString(NAME_KEY, name)
                putString(URL_PDF_KEY, urlPdf)
            })
        }
        return root
    }

}