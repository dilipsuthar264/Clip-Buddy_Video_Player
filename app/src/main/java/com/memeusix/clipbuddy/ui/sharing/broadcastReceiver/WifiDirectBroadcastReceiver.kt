package com.memeusix.clipbuddy.ui.sharing.broadcastReceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import com.memeusix.clipbuddy.ui.sharing.SharingActivity

class WifiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: SharingActivity
) : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive: $intent")
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    Log.e(TAG, "Wi-Fi P2P is enabled")
                } else {
                    Log.e(TAG, "Wi-Fi P2P is disabled")
                }
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                Log.e(TAG, "P2P peers changed")
                manager.requestPeers(channel) { peerList ->
                    Log.e(TAG, "Peers available: ${peerList.deviceList}")
                    activity.updateList(peerList.deviceList)
                }
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                val networkInfo: NetworkInfo? =
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)
                if (networkInfo?.isConnected == true) {
                    manager.requestConnectionInfo(channel) { channelInfo->
                        Log.e(TAG, "connection request : $channelInfo", )
                    }
                } else {

                }
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                Log.e(TAG, "This device's Wi-Fi state changed")
            }
        }
    }


    companion object {
        val TAG = WifiDirectBroadcastReceiver::class.java.name
    }
}


