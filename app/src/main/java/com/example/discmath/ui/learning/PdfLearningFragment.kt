package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningPdfBinding
import com.example.discmath.util.Downloader
import com.example.discmath.util.FileDownloadManager
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.bottomnavigation.BottomNavigationView

class PdfLearningFragment : Fragment() {

    private var _binding: FragmentLearningPdfBinding? = null

    private lateinit var name: String
    private lateinit var url: String
    private lateinit var pdfView: PDFView

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
        pdfView = binding.pdfView
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.GONE
        val downloader: Downloader = FileDownloadManager(::loadPdfIntoView)
        downloader.downloadFile(url)
        return root
    }

    private fun loadPdfIntoView(fileInBytes: ByteArray) {
        pdfView.fromBytes(fileInBytes)
            .enableAntialiasing(true)
            .pageFling(true)
            .load()
    }

}