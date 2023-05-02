package com.example.discmath.ui.learning

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.discmath.databinding.FragmentLearningPdfBinding
import com.example.discmath.util.Downloader
import com.example.discmath.util.FileDownloadManager
import com.github.barteksc.pdfviewer.PDFView
import java.io.FileNotFoundException


fun String.getUrlLastComponent(): String = this.substringAfterLast('/')

class PdfLearningFragment : Fragment() {

    private var _binding: FragmentLearningPdfBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var name: String
    private lateinit var url: String

    // Views
    private lateinit var pdfView: PDFView

    // State
    private var isDownloading: Boolean = false

    // Navigation
    private lateinit var navController: NavController
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!isDownloading) {
                this.isEnabled = false
                navController.popBackStack()
            } else {
                Toast.makeText(context, "Будь ласка, зачекайте", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME_KEY)!!
            url = it.getString(URL_PDF_KEY)!!
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningPdfBinding.inflate(inflater, container, false)
        navController = findNavController()
        pdfView = binding.pdfView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val result = openPdf(url.getUrlLastComponent())
            loadPdfIntoView(result)
        } catch (error: FileNotFoundException) {
            isDownloading = true
            val downloader: Downloader = FileDownloadManager(::savePdfAndLoad)
            downloader.downloadFile(url)
            isDownloading = false
        }
    }

    private fun openPdf(filename: String): ByteArray {
        return requireContext().openFileInput(filename).readBytes()
    }

    private fun savePdf(fileInBytes: ByteArray, filename: String) {
        context?.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it?.write(fileInBytes)
        }
    }

    private fun savePdfAndLoad(fileInBytes: ByteArray) {
        savePdf(fileInBytes, url.getUrlLastComponent())
        loadPdfIntoView(fileInBytes)
    }

    private fun loadPdfIntoView(fileInBytes: ByteArray) {
        pdfView.fromBytes(fileInBytes)
            .enableAntialiasing(true)
            .pageFling(true)
            .load()
    }

}