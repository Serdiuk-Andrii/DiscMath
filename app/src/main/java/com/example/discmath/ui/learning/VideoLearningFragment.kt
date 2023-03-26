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
import androidx.fragment.app.Fragment
import com.example.discmath.databinding.FragmentLearningVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class VideoLearningFragment : Fragment() {

    private var _binding: FragmentLearningVideoBinding? = null

    private lateinit var name: String
    private lateinit var url: String

    private lateinit var nameTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var videoView: YouTubePlayerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        nameTextView = binding.name
        urlTextView = binding.url
        //webView = binding.lectureVideo
        val root: View = binding.root
        nameTextView.text = name
        urlTextView.text = url
        videoView = binding.lectureVideo


        // Sets custom view
        /*
        val listener: YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val defaultPlayerUiController =
                    DefaultPlayerUiController(videoView, youTubePlayer)
                videoView.setCustomPlayerUi(defaultPlayerUiController.rootView)
            }
        }
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        videoView.initialize(listener, options)
        */
        val videoId: String? = Uri.parse(url).getQueryParameter("v")
        if (videoId == null) {
            Toast.makeText(context, "There is no video id in the url", Toast.LENGTH_SHORT).show()
        } else {
            videoView.getYouTubePlayerWhenReady(object: YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 100F)
                }
            })
            /*
            val fullScreenHelper: FullScreenHelper = FullScreenHelper(
                requireActivity(), arrayOf(urlTextView, nameTextView)
            )
            videoView.addFullScreenListener(object: YouTubePlayerFullScreenListener {
                override fun onYouTubePlayerEnterFullScreen() {
                    val attrs = activity!!.window!!.attributes
                    attrs.flags.and(WindowManager.LayoutParams.FLAG_FULLSCREEN)
                    activity!!.window.attributes = attrs

                    fullScreenHelper.enterFullScreen()
                }

                override fun onYouTubePlayerExitFullScreen() {
                    fullScreenHelper.exitFullScreen()
                }
            })
            */
        }
        /*val videoId: String? = Uri.parse(url).getQueryParameter("v")
        if (videoId == null) {
            Toast.makeText(context, "There is no video id in the url", Toast.LENGTH_SHORT).show()
        } else {
            val embeddedUrl: String = "http://www.youtube.com/embed/$videoId/?autoplay=1&vq=small"
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            //webView.webViewClient = WebViewClient()
            // webView.settings.javaScriptEnabled = true

            val html = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "  <body>\n" +
                    "    <!-- 1. The <iframe> (and video player) will replace this <div> tag. -->\n" +
                    "    <div id=\"player\"></div>\n" +
                    "\n" +
                    "    <script>\n" +
                    "      // 2. This code loads the IFrame Player API code asynchronously.\n" +
                    "      var tag = document.createElement('script');\n" +
                    "\n" +
                    "      tag.src = \"https://www.youtube.com/iframe_api\";\n" +
                    "      var firstScriptTag = document.getElementsByTagName('script')[0];\n" +
                    "      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);\n" +
                    "\n" +
                    "      // 3. This function creates an <iframe> (and YouTube player)\n" +
                    "      //    after the API code downloads.\n" +
                    "      var player;\n" +
                    "      function onYouTubeIframeAPIReady() {\n" +
                    "        player = new YT.Player('player', {\n" +
                    "          height: '590',\n" +
                    "          width: '940',\n" +
                    "          videoId: '$videoId',\n" +
                    "          playerVars: {\n" +
                    "            'playsinline': 0,\n" +
                    "            'start': 10\n" +
                    "          },\n" +
                    "          events: {\n" +
                    "            'onReady': onPlayerReady,\n" +
                    "            'onStateChange': onPlayerStateChange\n" +
                    "          }\n" +
                    "        });\n" +
                    "      }\n" +
                    "\n" +
                    "      // 4. The API will call this function when the video player is ready.\n" +
                    "      function onPlayerReady(event) {\n" +
                    "        event.target.playVideo();\n" +
                    "      }\n" +
                    "\n" +
                    "      // 5. The API calls this function when the player's state changes.\n" +
                    "      //    The function indicates that when playing a video (state=1),\n" +
                    "      //    the player should play for six seconds and then stop.\n" +
                    "      var done = false;\n" +
                    "      function onPlayerStateChange(event) {\n" +
                    "        if (event.data == YT.PlayerState.PLAYING && !done) {\n" +
                    "          setTimeout(stopVideo, 60000);\n" +
                    "          done = true;\n" +
                    "        }\n" +
                    "      }\n" +
                    "      function stopVideo() {\n" +
                    "        player.stopVideo();\n" +
                    "      }\n" +
                    "    </script>\n" +
                    "  </body>\n" +
                    "</html>"
            val encodedHtml = Base64.encodeToString(html.toByteArray(), Base64.NO_PADDING)
            webView.loadData(encodedHtml, "text/html", "base64")
        }*/

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

        return root
    }

}