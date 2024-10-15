package com.memeusix.clipbuddy.ui.dashboard.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.data.model.FolderModel
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.data.viewmodel.VideoViewModel
import com.memeusix.clipbuddy.databinding.FragmentHomeBinding
import com.memeusix.clipbuddy.ui.dashboard.home.adapter.FolderAdapter
import com.memeusix.clipbuddy.utils.gone
import com.memeusix.clipbuddy.utils.setSingleClickListener
import com.memeusix.clipbuddy.utils.visible

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val videoViewModel by viewModels<VideoViewModel>()
    private lateinit var folderAdapter: FolderAdapter
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted) {
                videoViewModel.loadVideos()
            } else {
                showPermissionRequiredView()
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
        videoViewModel.videosByFolder.observe(
            viewLifecycleOwner, this::observeFolders
        )
        setUpAdapter()
        setOnClickListener()
    }

    private fun setUpAdapter() {
        folderAdapter = FolderAdapter {
            val directions = HomeFragmentDirections.actionHomeFragmentToVideoFragment(
                label = it.folderName, folderDetails = it
            )
            findNavController().navigate(directions)
        }
        binding.rvFolder.adapter = folderAdapter
    }


    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                videoViewModel.loadVideos()
            } else {
                showPermissionRequiredView()
            }
        } else {
            if (checkPermissionBelowR()) {
                videoViewModel.loadVideos()
            } else {
                requestPermissionLauncher.launch(
                    storagePermissionBelowR
                )
            }
        }
    }

    private fun checkPermissionBelowR() = storagePermissionBelowR.all {
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showPermissionRequiredView() {
        binding.apply {
            txtEmptyMessage.text = getString(R.string.primission_not_granted)
            txtEmptyMessageDesc.text =
                getString(R.string.the_clip_buddy_app_required_permission_to_access_and_play_videos_nwithout_it_this_app_wont_works)
            lytEmpty.visible()
            loader.gone()
            rvFolder.gone()
        }
    }

    private fun askingForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(intent)
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(intent)
        }
    }


    private fun showEmptyMessageView() {
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


    private fun setOnClickListener() {
        binding.btnPermission.setSingleClickListener {
            askingForPermission()
        }
    }


    private fun observeFolders(result: Map<String, List<VideoModel>>) {
        if (result.isEmpty()) {
            showEmptyMessageView()
        } else {
            Log.e(TAG, "observeFolders: $result")
            binding.apply {
                loader.gone()
                txtEmptyMessage.gone()
                txtEmptyMessageDesc.gone()
                btnPermission.gone()
                rvFolder.visible()
            }
            val folderList: List<FolderModel> = result.map { FolderModel(it.key, it.value) }
            folderAdapter.items.submitList(folderList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = HomeFragment::class.java.name
        val storagePermissionBelowR = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }
}
