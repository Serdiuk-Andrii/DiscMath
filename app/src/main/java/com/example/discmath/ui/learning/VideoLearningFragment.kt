package com.example.discmath.ui.learning

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.discmath.databinding.FragmentLearningVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoLearningFragment : Fragment() {

    private var _binding: FragmentLearningVideoBinding? = null

    private lateinit var name: String
    private lateinit var url: String


    // Views
    private lateinit var nameTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var videoView: YouTubePlayerView
    private lateinit var fullScreenContainer: ViewGroup

    // YouTube player
    private lateinit var youtubePlayer: YouTubePlayer

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isFullscreen: Boolean = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                youtubePlayer.toggleFullscreen()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(NAME_KEY)!!
            url = it.getString(URL_VIDEO_KEY)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningVideoBinding.inflate(inflater, container, false)
        initializeViews()

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //lifecycle.addObserver(videoView)
        return binding.root
    }



    private fun initializeViews() {
        nameTextView = binding.name
        urlTextView = binding.url
        nameTextView.text = name
        urlTextView.text = url
        videoView = binding.lectureVideo
        fullScreenContainer = binding.fullScreenContainer

        urlTextView.setOnClickListener {
            val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            try {
                startActivity(appIntent)
            } catch (ex: ActivityNotFoundException) {
                startActivity(webIntent)
            }
        }

        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {

            }


        }
        videoView.addFullscreenListener(object: FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                videoView.visibility = View.GONE
                urlTextView.visibility = View.GONE
                nameTextView.visibility = View.GONE
                fullScreenContainer.visibility = View.VISIBLE
                fullScreenContainer.addView(fullscreenView)
                /*
                val window = requireActivity().window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.hide(WindowInsets.Type.statusBars())
                } else {
                    @Suppress("DEPRECATION")
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                }
                */
                //TODO: fix this
                Thread.sleep(200)
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                videoView.visibility = View.VISIBLE
                urlTextView.visibility = View.VISIBLE
                nameTextView.visibility = View.VISIBLE
                /*
                val window = requireActivity().window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.show(WindowInsets.Type.statusBars())
                } else {
                    @Suppress("DEPRECATION")
                    window.clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                }
                */
                fullScreenContainer.visibility = View.GONE
                fullScreenContainer.removeAllViews()
            }

        })

        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(1)
            .rel(0).ccLoadPolicy(1).fullscreen(1).build()
        videoView.initialize(listener, options)

        val videoId: String? = Uri.parse(url).getQueryParameter("v")
        if (videoId == null) {
            Toast.makeText(context, "There is no video id in the url", Toast.LENGTH_SHORT).show()
        } else {
            videoView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 100F)
                    this@VideoLearningFragment.youtubePlayer = youTubePlayer
                }
            })
        }
        //webView = binding.lectureVideo
    }



    override fun onDestroy() {
        super.onDestroy()
        videoView.release()
    }

}