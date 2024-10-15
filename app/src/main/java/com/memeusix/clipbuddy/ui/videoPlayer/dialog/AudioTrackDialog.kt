package com.memeusix.clipbuddy.ui.videoPlayer.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.databinding.DialogTracksBinding

class AudioTrackDialog(
    val context: Context,
    val selectedItem: String?,
    val list: List<Tracks.Group>,
    val title: String,
    val onItemSelected: (Tracks.Group?) -> Unit
) {

    private val dialog: Dialog = Dialog(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
    private val binding: DialogTracksBinding =
        DialogTracksBinding.inflate(LayoutInflater.from(context))

    private val adapter = object : RecyclerView.Adapter<TrackViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return TrackViewHolder(view.rootView)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            holder.bind(list[position])
        }

    }

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        binding.rvTrack.adapter = adapter
        binding.txtTitle.text = title

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        binding.btnDisable.setOnClickListener {
            onItemSelected(null)
            dismiss()
        }
    }

    fun show() {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    fun dismiss() {
        if (dialog.isShowing) dialog.dismiss()
    }

    inner class TrackViewHolder(private val bindingAdapter: View) :
        RecyclerView.ViewHolder(bindingAdapter) {
        @OptIn(UnstableApi::class)
        fun bind(item: Tracks.Group) {
            (bindingAdapter as TextView).apply {
                this.includeFontPadding = false

                this.text = item.mediaTrackGroup.getFormat(0).label

                if (item.mediaTrackGroup.id == selectedItem) {
                    this.setBackgroundColor(context.getColor(R.color.colorBackgroundSecondary))
                } else {
                    this.setBackgroundColor(context.getColor(android.R.color.transparent))
                }

                this.rootView.setOnClickListener {
                    onItemSelected(item)
                    dismiss()
                }
            }
        }
    }
}
