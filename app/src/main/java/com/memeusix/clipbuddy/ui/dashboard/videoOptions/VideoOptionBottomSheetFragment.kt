package com.memeusix.clipbuddy.ui.dashboard.videoOptions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.memeusix.clipbuddy.databinding.FragmentVideoOptionBottomSheetBinding
import com.memeusix.clipbuddy.utils.DialogType
import com.memeusix.clipbuddy.utils.RequestKey
import com.memeusix.clipbuddy.utils.setSingleClickListener

class VideoOptionBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentVideoOptionBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args: VideoOptionBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoOptionBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtVideoName.text = args.videoDetails.name
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.txtRename.setSingleClickListener {
            RenameDialog(
                context = requireActivity(),
                type = DialogType.RENAME_DIALOG,
                video = args.videoDetails,
                listener = {
                    setFragmentResult(
                        RequestKey.RENAME_FILE, bundleOf(
                            RequestKey.RENAME_FILE to args.videoDetails.copy(
                                name = it?.name, path = it?.path
                            )
                        )
                    )
                    findNavController().popBackStack()
                }).show()
        }
        binding.txtShare.setSingleClickListener {
            shareFile(Uri.parse(args.videoDetails.path), args.videoDetails.name)
            findNavController().popBackStack()
        }
        binding.txtDetails.setSingleClickListener {
            DetailsDialog(
                video = args.videoDetails,
                context = requireActivity()
            ).show()
        }
        binding.txtDelete.setSingleClickListener {
            RenameDialog(
                context = requireActivity(),
                type = DialogType.DELETE_DIALOG,
                video = args.videoDetails,
                listener = {
                    setFragmentResult(
                        RequestKey.DELETE_FILE, bundleOf(
                            RequestKey.DELETE_FILE to args.videoDetails.id
                        )
                    )
                    findNavController().popBackStack()
                }).show()
        }
    }


    private fun shareFile(uri: Uri, fileName: String?) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TITLE, "Share file: $fileName")
            type = "*/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        val chooser = Intent.createChooser(intent, "Share File")
        startActivity(chooser)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val TAG: String = VideoOptionBottomSheetFragment::class.java.name
    }
}