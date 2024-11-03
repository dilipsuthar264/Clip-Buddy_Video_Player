package com.memeusix.clipbuddy.ui.sharing

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.memeusix.clipbuddy.R
import com.memeusix.clipbuddy.databinding.ActivitySharingBinding
import com.memeusix.clipbuddy.ui.sharing.broadcastReceiver.WifiDirectBroadcastReceiver

class SharingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySharingBinding

    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val intentFilter = IntentFilter()


    private var receiver: BroadcastReceiver? = null

    private lateinit var navController: NavController

    private lateinit var peeringOptions: PeeringOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.sharingNavHost)

        manager = getSystemService(WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

    }

    @SuppressLint("MissingPermission")
    fun connectToPeer(device: WifiP2pDevice) {
        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress

        manager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(
                    this@SharingActivity, "Connection Initialized", Toast.LENGTH_SHORT
                ).show()

            }

            override fun onFailure(code: Int) {
                Toast.makeText(
                    this@SharingActivity, "Connection Failed $code", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    @SuppressLint("MissingPermission")
    fun discoverPeers() {
        manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(
                    this@SharingActivity, "Discovery Initiated", Toast.LENGTH_SHORT
                ).show()
            }

            override fun onFailure(p0: Int) {
                Toast.makeText(
                    this@SharingActivity, "Discovery Failed : $p0" + "", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    fun updateList(newPeers: MutableCollection<WifiP2pDevice>) {
        peeringOptions.getPeering(newPeers)
    }


    override fun onResume() {
        super.onResume()
        if (::channel.isInitialized) {
            receiver = WifiDirectBroadcastReceiver(manager, channel, this)
        }
        registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    companion object {
        val TAG = SharingActivity::class.java.name
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES
        ) else arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }


    fun initPeeringOptions(peeringOptions: PeeringOptions) {
        this.peeringOptions = peeringOptions
    }

    interface PeeringOptions {
        fun getPeering(list: MutableCollection<WifiP2pDevice>)
    }
}