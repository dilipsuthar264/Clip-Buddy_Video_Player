package com.memeusix.clipbuddy.ui.dashboard.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.base.BaseFragment
import com.memeusix.clipbuddy.data.model.FolderModel
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.data.viewmodel.VideoViewModel
import com.memeusix.clipbuddy.databinding.FragmentHomeBinding
import com.memeusix.clipbuddy.ui.dashboard.home.adapter.FolderAdapter
import com.memeusix.clipbuddy.utils.FilePathUtils
import com.memeusix.clipbuddy.utils.gone
import com.memeusix.clipbuddy.utils.setSingleClickListener
import com.memeusix.clipbuddy.utils.visible

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // Use safe non-null getter after view is created
    private val videoViewModel by viewModels<VideoViewModel>()
    private lateinit var folderAdapter: FolderAdapter

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            when (isGranted) {
                true -> loadData()
                false -> showPermissionRequiredText()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        askingForPermission()
        setOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding reference to avoid memory leak
    }

    private fun setUpAdapter() {
        folderAdapter = FolderAdapter {
            val directions = HomeFragmentDirections.actionHomeFragmentToVideoFragment(
                label = it.folderName,
                folderDetails = it
            )
            findNavController().navigate(directions)
        }
        binding.rvFolder.adapter = folderAdapter
    }

    private fun setOnClickListener() {
        binding.btnPermission.setSingleClickListener {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(intent)
        }
    }

    private fun showPermissionRequiredText() {
        binding.apply {
            txtEmptyMessage.text = getString(R.string.primission_not_granted)
            txtEmptyMessageDesc.text =
                getString(R.string.the_clip_buddy_app_required_permission_to_access_and_play_videos_nwithout_it_this_app_wont_works)
            lytEmpty.visible()
            loader.gone()
            rvFolder.gone()
        }
    }

    private fun showEmptyMessage() {
        binding.apply {
            txtEmptyMessage.text = getString(R.string.ooops_no_videos_found)
            txtEmptyMessageDesc.text =
                getString(R.string.we_are_unable_to_find_any_videos_on_your_mobile_phone)
            lytEmpty.visible()
            loader.gone()
            rvFolder.gone()
            btnPermission.gone()
        }
    }

    private fun askingForPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(), FilePathUtils.storagePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(FilePathUtils.storagePermission)
        } else {
            loadData()
        }
    }

    private fun loadData() {
        videoViewModel.videosByFolder.observe(
            viewLifecycleOwner, this::observeFolders
        )
        videoViewModel.loadVideos()
        binding.apply {
            loader.visible()
            lytEmpty.gone()
        }
    }

    private fun observeFolders(result: Map<String, List<VideoModel>>) {
        if (result.isEmpty()) {
            showEmptyMessage()
        } else {
            Log.e(TAG, "observeFolders: $result")
            binding.apply {
                loader.gone()
                rvFolder.visible()
            }
            val folderList: List<FolderModel> = result.map { FolderModel(it.key, it.value) }
            folderAdapter.items.submitList(folderList)
        }
    }

    companion object {
        const val TAG: String = "HomeFragment" // Avoid using Java class name
    }
}
