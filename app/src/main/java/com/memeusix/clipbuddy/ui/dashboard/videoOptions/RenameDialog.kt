package com.memeusix.clipbuddy.ui.dashboard.videoOptions

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.databinding.DialogRenameBinding
import com.memeusix.clipbuddy.utils.DialogType
import com.memeusix.clipbuddy.utils.gone
import com.memeusix.clipbuddy.utils.visible
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(DelicateCoroutinesApi::class)
class RenameDialog(
    val context: Context, val type: String, val video: VideoModel, val listener: (File?) -> Unit
) {
    private val dialog: Dialog

    init {
        val binding = DialogRenameBinding.inflate(LayoutInflater.from(context))
        dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)

        when (type) {
            DialogType.DELETE_DIALOG -> {
                binding.edtRename.gone()
                binding.txtSubtitle.visible()
                binding.txtTitle.text = context.getString(R.string.delete_video)
                binding.txtSubtitle.text = video.name

                binding.btnSave.text = context.getString(R.string.delete)

                binding.btnSave.setOnClickListener {
                    video.path?.let {
                        val filePath = File(it)
                        GlobalScope.launch(Dispatchers.IO) {
                            if (filePath.exists()) {
                                val result = filePath.delete()
                                withContext(Dispatchers.Main) {
                                    if (result) {
                                        Toast.makeText(
                                            context,
                                            "Video Deleted successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        listener(null)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Video Delete Failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    dialog.dismiss()
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context, "File not found", Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }

            DialogType.RENAME_DIALOG -> {
                binding.txtSubtitle.gone()
                binding.edtRename.visible()

                binding.txtTitle.text = context.getString(R.string.rename_video)
                binding.edtRename.setText(video.name)

                binding.btnSave.text = context.getString(R.string.save)

                binding.btnSave.setOnClickListener {
                    val newName = binding.edtRename.text.toString()
                    if (newName.isNotEmpty()) {
                        val (success, newFile) = renameVideo(newName)
                        if (success) {
                            listener(newFile)
                            Toast.makeText(context, "File renamed to $newName", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Rename failed", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    dialog.dismiss()
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    private fun renameVideo(newName: String): Pair<Boolean, File?> {
        val videoPath = video.path ?: return Pair(false, null)
        val videoFile = File(videoPath)

        if (!videoFile.exists()) return Pair(false, null)

        val newNameWithoutExtension = newName.removeSuffix("." + videoFile.extension)

        val newFile = File(videoFile.parentFile, "$newNameWithoutExtension.${videoFile.extension}")
        val result = videoFile.renameTo(newFile)
        return Pair(result, newFile)
    }


    companion object {
        val TAG: String = RenameDialog::class.java.name
    }

}
