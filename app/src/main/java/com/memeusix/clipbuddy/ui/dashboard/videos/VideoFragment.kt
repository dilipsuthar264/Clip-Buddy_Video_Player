package com.memeusix.clipbuddy.ui.dashboard.videos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.navArgs
import com.memeusix.clipbuddy.base.BaseFragment
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.databinding.FragmentVideoBinding
import com.memeusix.clipbuddy.ui.dashboard.videos.adapter.VideoListAdapter
import com.memeusix.clipbuddy.ui.videoPlayer.VideoPlayerActivity

class VideoFragment : BaseFragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!! // Safe non-null getter
    private val args: VideoFragmentArgs by navArgs()
    private lateinit var adapter: VideoListAdapter

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
                // Handle menu click
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
        _binding = null // Set binding to null to avoid memory leak
    }

    companion object {
        const val TAG = "VideoFragment" // Use a simple TAG
    }
}
