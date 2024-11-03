package com.memeusix.clipbuddy.ui.sharing.reciever.adapter

import android.net.wifi.p2p.WifiP2pDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.memeusix.clipbuddy.databinding.ItemPeerDeviceBinding
import com.memeusix.clipbuddy.utils.setSingleClickListener

class PeerAdapter(
    val listener: (WifiP2pDevice) -> Unit
) : RecyclerView.Adapter<PeerAdapter.PeerViewHolder>() {

    inner class PeerViewHolder(val binding: ItemPeerDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WifiP2pDevice) {
            binding.txtDeviceName.text = "Device Name : ${item.deviceName}"
            binding.txtDeviceIp.text = "Device Address : ${item.deviceAddress}"

            binding.btnSendConnection.setSingleClickListener {
                listener(item)
            }
        }
    }

    val diff = object : DiffUtil.ItemCallback<WifiP2pDevice>() {
        override fun areItemsTheSame(oldItem: WifiP2pDevice, newItem: WifiP2pDevice): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WifiP2pDevice, newItem: WifiP2pDevice): Boolean {
            return oldItem.deviceAddress == newItem.deviceAddress
        }
    }

    val items = AsyncListDiffer(this, diff)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeerViewHolder {
        val binding =
            ItemPeerDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }

    override fun onBindViewHolder(holder: PeerViewHolder, position: Int) {
        holder.bind(items.currentList[position])
    }
}