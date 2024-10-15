package com.memeusix.clipbuddy.ui.dashboard.videos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.databinding.FragmentVideoBinding
import com.memeusix.clipbuddy.ui.dashboard.videos.adapter.VideoListAdapter
import com.memeusix.clipbuddy.ui.videoPlayer.VideoPlayerActivity
import com.memeusix.clipbuddy.utils.RequestKey
import com.memeusix.clipbuddy.utils.parcelize

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!
    private val args: VideoFragmentArgs by navArgs()
    private lateinit var adapter: VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.RENAME_FILE) { _, bundle ->
            bundle.parcelize<VideoModel>(RequestKey.RENAME_FILE).apply {
                if (::adapter.isInitialized) {
                    val videoList = adapter.items.currentList.map {
                        if (it.id == this?.id) this else it
                    }
                    adapter.items.submitList(videoList)
                    adapter.notifyItemChanged(videoList.indexOf(this))
                }
            }
            clearFragmentResultListener(RequestKey.RENAME_FILE)
        }
        setFragmentResultListener(RequestKey.DELETE_FILE) { _, bundle ->
            bundle.getLong(RequestKey.DELETE_FILE).let { videoId ->
                val videoList = adapter.items.currentList.toMutableList()
                videoList.removeIf { it.id == videoId }
                adapter.items.submitList(videoList)
            }
            clearFragmentResultListener(RequestKey.RENAME_FILE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        Log.e(TAG, "onViewCreated: ${args.folderDetails.videos.firstOrNull()}")
    }

    private fun setUpAdapter() {
        adapter = VideoListAdapter(
            menuClick = {
                val directions =
                    VideoFragmentDirections.actionVideoFragmentToVideoOptionBottomSheetFragment(
                        it
                    )
                findNavController().navigate(directions)
            },
            rootClick = { video ->
                goToVideoPlayer(video)
            }
        )
        binding.rvVideos.adapter = adapter
        adapter.items.submitList(args.folderDetails.videos)
    }

    @OptIn(UnstableApi::class)
    private fun goToVideoPlayer(video: VideoModel) {
        val intent = Intent(requireActivity(), VideoPlayerActivity::class.java)
        val bundle = Bundle().apply {
            putParcelable("VIDEO_DETAILS", video)
        }
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvVideos.adapter = null
        _binding = null
    }

    companion object {
        const val TAG = "VideoFragment" // Use a simple TAG
    }
}
