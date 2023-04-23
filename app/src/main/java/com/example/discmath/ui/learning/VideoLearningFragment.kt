package com.example.discmath.ui.learning

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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

class VideoLearningFragment : Fragment() {

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
    private lateinit var videoView: YouTubePlayerView
    private lateinit var videoTimestamps: RecyclerView
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
        _binding = FragmentLearningVideoBinding.inflate(inflater, container, false)
        youtubeAPIKey = requireActivity().resources.getString(R.string.google_api_key)
        initializeViews()
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        //lifecycle.addObserver(videoView)
        return binding.root
    }


    private fun initializeViews() {
        videoView = binding.lectureVideo
        videoTimestamps = binding.videoTimestamps
        fullScreenContainer = binding.fullScreenContainer

        videoView.addFullscreenListener(object : FullscreenListener {

            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                videoView.visibility = View.GONE
                videoTimestamps.visibility = View.GONE
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
                //TODO: fix this
                Thread.sleep(200)
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                videoView.visibility = View.VISIBLE
                videoTimestamps.visibility = View.VISIBLE

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
            }

        })

        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(1)
            .rel(0).ccLoadPolicy(1).fullscreen(1).build()
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {}
        }
        videoView.initialize(listener, options)

        val videoId: String? = Uri.parse(url).getQueryParameter("v")
        if (videoId == null) {
            Toast.makeText(context, "There is no video id in the url", Toast.LENGTH_SHORT).show()
        } else {
            videoView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0F)
                    this@VideoLearningFragment.youtubePlayer = youTubePlayer
                }
            })
            downloadTimestamps(videoId)
        }
    }

    private fun downloadTimestamps(videoId: String) {
        val requestUrl = getYoutubeRequestUrl(videoId)
        val request: Request = Request.Builder().url(requestUrl).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                notifyVideoInformationRequestHasFailed()
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
                        initializeTimestampsData(timestamps)
                        this@VideoLearningFragment.requireActivity().runOnUiThread {
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
                        }
                        )
                    } catch (e: NullPointerException) {
                        notifyVideoInformationRequestHasFailed()
                    }
                } else {
                    notifyVideoInformationRequestHasFailed()
                }
            }
        })
    }

    private fun updateTimestamp() {
        val currentHolder =  videoTimestamps.findViewHolderForLayoutPosition(currentTimestampIndex)
                as VideoTimestampAdapter.TimestampHolder
        (videoTimestamps.adapter as VideoTimestampAdapter).switchCurrentTimestamp(currentHolder)
    }

    private fun timestampClicked(timestamp: String, position: Int) {
        currentTimestampIndex = position
        youtubePlayer.seekTo(parseTimestampToTimeInSeconds(timestamp))
    }

    private fun initializeTimestampsData(data: Array<Pair<String, String>>) {
        for ((index, timestamp) in data.withIndex()) {
            videoTimestampsData.add(Pair(parseTimestampToTimeInSeconds(timestamp.first), index))
        }
    }

    private fun parseTimestampToTimeInSeconds(timestamp: String): Float {
        val timeSections = timestamp.split(':').reversed()
        var result = 0.0
        var currentPower = 1F
        for (timeSection in timeSections) {
            result += timeSection.toInt() * currentPower
            currentPower *= 60F
        }
        return result.toFloat()
    }

    private fun notifyVideoInformationRequestHasFailed() {

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

    private fun getYoutubeRequestUrl(videoId: String): String =
        "https://www.googleapis.com/youtube/" +
                "v3/videos?part=snippet&id=${videoId}&key=${youtubeAPIKey}"

    override fun onDestroy() {
        super.onDestroy()
        videoView.release()
    }

}
