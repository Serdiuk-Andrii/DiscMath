package com.example.discmath.ui.learning

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningVideoBinding
import com.example.discmath.ui.learning.adapters.VideoTimestampAdapter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import okhttp3.*
import java.io.IOException

fun String.parseTimestampToTimeInSeconds(): Int {
    val timeSections = this.split(':').reversed()
    var result = 0
    var currentPower = 1
    for (timeSection in timeSections) {
        result += timeSection.toInt() * currentPower
        currentPower *= 60
    }
    return result
}

class VideoLearningFragment : Fragment() {

    // System
    private lateinit var window: Window

    // Network and YouTube
    private val httpClient: OkHttpClient = OkHttpClient()
    private lateinit var youtubeAPIKey: String
    private val timestampRegex: Regex = "\\d{1,2}(:\\d{2})+".toRegex()

    private val videoTimestampsData: ArrayList<Pair<Float, Int>> = arrayListOf()
    private var currentTimestampIndex: Int = 0

    // JSON parser
    private val parser: ObjectMapper = ObjectMapper()

    // Views
    private var _binding: FragmentLearningVideoBinding? = null

    private lateinit var name: String
    private lateinit var url: String

    // Views
    private val viewsToHide: ArrayList<View> = arrayListOf()
    private lateinit var videoView: YouTubePlayerView
    private lateinit var videoTimestamps: RecyclerView
    private lateinit var fullScreenContainer: ViewGroup
    private lateinit var timestampsErrorText: TextView
    private lateinit var videoTitle: TextView
    private lateinit var videoTitleContainer: FrameLayout

    // View data
    private lateinit var timestampsAbsentString: String
    private lateinit var networkErrorString: String

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
                this.isEnabled = false
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
        window = requireActivity().window
        _binding = FragmentLearningVideoBinding.inflate(inflater, container, false)
        youtubeAPIKey = requireActivity().resources.getString(R.string.google_api_key)
        initializeViewData()
        initializeViews()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        lifecycle.addObserver(videoView)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoId: String? = Uri.parse(url).getQueryParameter("v")
        if (videoId == null) {
            Toast.makeText(context, "There is no video id in the url", Toast.LENGTH_SHORT).show()
        } else {
            videoView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0F)
                    this@VideoLearningFragment.youtubePlayer = youTubePlayer
                    downloadTimestamps(videoId)
                }
            })
        }
    }

    private fun initializeViewData() {
        timestampsAbsentString = requireActivity().resources.getString(R.string.timestamps_absent_text)
        networkErrorString = requireActivity().resources.getString(R.string.timestamps_network_error)
    }

    private fun initializeViews() {
        videoView = binding.lectureVideo
        viewsToHide.add(videoView)
        videoTimestamps = binding.videoTimestamps
        fullScreenContainer = binding.fullScreenContainer
        timestampsErrorText = binding.timestampsErrorTextView
        videoTitleContainer = binding.lectureTitleContainer
        viewsToHide.add(videoTitleContainer)
        videoTitle = binding.lectureTitle
        videoTitle.text = name
        viewsToHide.add(videoTitleContainer)

        videoView.addFullscreenListener(object : FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                isFullscreen = true
                toggleViewsVisibility(View.GONE)
                fullScreenContainer.visibility = View.VISIBLE
                fullScreenContainer.addView(fullscreenView)

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
                hideSystemUI()
                //TODO: fix this
                Thread.sleep(200)
            }

            @SuppressLint("SourceLockedOrientationActivity")
            override fun onExitFullscreen() {
                requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                isFullscreen = false
                toggleViewsVisibility(View.VISIBLE)

                val window = requireActivity().window
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.show(WindowInsets.Type.statusBars())
                } else {
                    @Suppress("DEPRECATION")
                    window.clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                }
                fullScreenContainer.visibility = View.GONE
                fullScreenContainer.removeAllViews()
                showSystemUI()
            }

        })

        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(1)
            .rel(0).ccLoadPolicy(1).fullscreen(1).build()
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {}
        }
        videoView.initialize(listener, options)
    }

    private fun downloadTimestamps(videoId: String) {
        val requestUrl = getYoutubeRequestUrl(videoId)
        val request: Request = Request.Builder().url(requestUrl).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                notifyVideoInformationRequestHasFailed(networkErrorString)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val bodyString = response.body!!.string()
                    response.body!!.close()
                    val node = parser.readValue(bodyString, ObjectNode::class.java)
                    try {
                        val description =
                            node["items"]!![0]["snippet"]["description"].toString()
                        val timestamps = getTimestampsFromVideoDescription(description)
                        if (timestamps.isEmpty()) {
                            this@VideoLearningFragment.requireActivity().runOnUiThread {
                                notifyVideoInformationRequestHasFailed(timestampsAbsentString)
                            }
                        } else {
                            viewsToHide.add(videoTimestamps)
                            initializeTimestampsData(timestamps)
                            this@VideoLearningFragment.activity?.runOnUiThread {
                                videoTimestamps.adapter = VideoTimestampAdapter(
                                    timestamps,
                                    ::timestampClicked
                                )
                            }
                            videoView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                                    youTubePlayer.addListener(object :
                                        AbstractYouTubePlayerListener() {
                                        override fun onCurrentSecond(
                                            youTubePlayer: YouTubePlayer,
                                            second: Float
                                        ) {
                                            super.onCurrentSecond(youTubePlayer, second)
                                            val timestampIndex =
                                                (videoTimestampsData.firstOrNull { it.first >= second }?.second
                                                    ?: videoTimestampsData.size) - 1
                                            if (currentTimestampIndex != timestampIndex) {
                                                currentTimestampIndex = timestampIndex
                                                updateTimestamp()
                                            }
                                        }
                                    })
                                }

                            })
                        }
                    } catch (e: NullPointerException) {
                        notifyVideoInformationRequestHasFailed(networkErrorString)
                    }
                } else {
                    notifyVideoInformationRequestHasFailed(networkErrorString)
                }
            }
        })
    }

    private fun updateTimestamp() {
        val currentHolder = videoTimestamps.findViewHolderForLayoutPosition(currentTimestampIndex)
                as VideoTimestampAdapter.TimestampHolder
        (videoTimestamps.adapter as VideoTimestampAdapter).switchCurrentTimestamp(currentHolder)
    }

    private fun timestampClicked(time: Float, position: Int) {
        currentTimestampIndex = position
        youtubePlayer.seekTo(time)
    }

    private fun initializeTimestampsData(data: Array<Pair<String, String>>) {
        for ((index, timestamp) in data.withIndex()) {
            videoTimestampsData.add(Pair(timestamp.first.parseTimestampToTimeInSeconds()
                .toFloat(), index))
        }
    }

    private fun notifyVideoInformationRequestHasFailed(text: String) {
        timestampsErrorText.visibility = View.VISIBLE
        timestampsErrorText.text = text
        videoTimestamps.visibility = View.GONE
        viewsToHide.add(timestampsErrorText)
    }

    private fun getTimestampsFromVideoDescription(description: String): Array<Pair<String, String>> {
        val timestampsStartIndex = description.indexOf("0:00")
        if (timestampsStartIndex == -1) {
            return arrayOf()
        }
        val shortDescription = description.substring(timestampsStartIndex)
        val lines = shortDescription.split("\\n")
        val timestamps: ArrayList<Pair<String, String>> = arrayListOf()
        for (line in lines) {
            val timestampEndIndex = line.indexOf(' ')
            if (timestampEndIndex == -1) {
                break
            }
            val timestamp = line.substring(0, timestampEndIndex)
            if (!timestampRegex.matches(timestamp)) {
                break
            }
            timestamps.add(Pair(timestamp, line.substring(timestampEndIndex)))
        }
        return timestamps.toTypedArray()
    }



    private fun toggleViewsVisibility(visibility: Int) {
        viewsToHide.forEach { it.visibility = visibility }
    }

    private fun getYoutubeRequestUrl(videoId: String): String =
        "https://www.googleapis.com/youtube/" +
                "v3/videos?part=snippet&id=${videoId}&key=${youtubeAPIKey}"


    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.release()
    }

}
