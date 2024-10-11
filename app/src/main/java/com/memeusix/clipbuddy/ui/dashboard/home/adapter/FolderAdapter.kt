package com.memeusix.clipbuddy.ui.dashboard.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.data.model.FolderModel
import com.memeusix.clipbuddy.databinding.ItemFolderBinding
import com.memeusix.clipbuddy.utils.setSingleClickListener

class FolderAdapter(
    val listener: (FolderModel) -> Unit,
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(private val binding: ItemFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("StringFormatMatches")
        fun bind(item: FolderModel) {
            binding.txtFolderName.text = item.folderName
            binding.txtVideoCount.text =
                itemView.context.getString(R.string.videos, item.videos.size)
            binding.root.setSingleClickListener {
                listener(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    private val diff = object : DiffUtil.ItemCallback<FolderModel>() {
        override fun areItemsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FolderModel, newItem: FolderModel): Boolean {
            return oldItem.folderName == newItem.folderName
        }

    }
    val items = AsyncListDiffer(this, diff)

    override fun getItemCount(): Int {
        return items.currentList.size
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        return holder.bind(items.currentList[position])
    }
}