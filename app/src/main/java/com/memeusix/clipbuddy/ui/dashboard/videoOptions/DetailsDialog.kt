package com.memeusix.clipbuddy.ui.dashboard.videoOptions

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.databinding.DialogDetailsBinding
import com.memeusix.clipbuddy.utils.formatDat
import com.memeusix.clipbuddy.utils.formatDuration
import com.memeusix.clipbuddy.utils.formatFileSize

class DetailsDialog(
    val video: VideoModel,
    val context: Context
) {
    private val dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    val binding = DialogDetailsBinding.inflate(LayoutInflater.from(context))

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        setData()
    }

    private fun setData() {
        binding.lytName.apply {
            title.text = context.getString(R.string.name)
            desc.text = video.name
        }
        binding.lytTime.apply {
            title.text = context.getString(R.string.time)
            desc.text = video.time?.let { formatDat(it) } ?: ""
        }
        binding.lytDuration.apply {
            title.text = context.getString(R.string.duration)
            desc.text = video.duration?.formatDuration()
        }
        binding.lytDimensions.apply {
            title.text = context.getString(R.string.dimensions)
            desc.text = context.getString(R.string.pixels, video.dimensions)
        }
        binding.lytSize.apply {
            title.text = context.getString(R.string.size)
            desc.text = video.size?.let { formatFileSize(it) } ?: ""
        }
        binding.lytPath.apply {
            title.text = context.getString(R.string.path)
            desc.text = video.path
        }
    }

    fun show() {
        if (!dialog.isShowing) dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }

    companion object {
        val TAG: String? = DetailsDialog::class.java.name
    }
}