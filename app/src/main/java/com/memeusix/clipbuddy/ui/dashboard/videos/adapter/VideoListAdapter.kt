package com.memeusix.clipbuddy.ui.dashboard.videos.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.databinding.ItemVideoBinding
import com.memeusix.clipbuddy.utils.formatDuration
import com.memeusix.clipbuddy.utils.setSingleClickListener
import java.io.File

class VideoListAdapter(
    val menuClick : (VideoModel) -> Unit,
    val rootClick : (VideoModel) -> Unit
) : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoModel) {
            Glide.with(itemView.context)
                .asDrawable()
                .load(Uri.fromFile(item.path?.let { File(it) }))
                .error(R.drawable.ic_video)
                .placeholder(R.drawable.ic_video)
                .thumbnail(0.2f)
                .into(binding.imgThumbnail)

            binding.txtVideoTitle.text = item.name
            binding.txtDuration.text = item.duration?.formatDuration()
            binding.imgMenu.setSingleClickListener {
                menuClick(item)
            }

            binding.root.setSingleClickListener {
                rootClick(item)
            }

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoListAdapter.VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }


    val diff = object : DiffUtil.ItemCallback<VideoModel>() {
        override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val items = AsyncListDiffer(this, diff)

    override fun onBindViewHolder(holder: VideoListAdapter.VideoViewHolder, position: Int) {
        return holder.bind(items.currentList[position])
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}