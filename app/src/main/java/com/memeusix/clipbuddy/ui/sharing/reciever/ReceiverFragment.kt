package com.memeusix.clipbuddy.ui.sharing.reciever

import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.memeusix.clipbuddy.databinding.FragmentRecieverBinding
import com.memeusix.clipbuddy.ui.sharing.SharingActivity
import com.memeusix.clipbuddy.ui.sharing.reciever.adapter.PeerAdapter

class ReceiverFragment : Fragment() {
    private var _binding: FragmentRecieverBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var adapter: PeerAdapter

    private val activity by lazy {
        requireActivity() as SharingActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecieverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity.initPeeringOptions(peeringOptions)
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.values.all { it }
            if (granted) {
                activity.discoverPeers()
            } else {

            }
        }
    }

    private val peeringOptions = object : SharingActivity.PeeringOptions {
        override fun getPeering(list: MutableCollection<WifiP2pDevice>) {
            Log.e(TAG, "getPeering: inside fragment $list")
            if (::adapter.isInitialized) {
                adapter.items.submitList(list.toMutableList())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionCheck()
        setUpAdapter()
    }

    private fun setUpAdapter() {
        if (!::adapter.isInitialized) {
            adapter = PeerAdapter() {
                activity.connectToPeer(it)
            }
        }
        binding.rvPeers.adapter = adapter
    }


    private fun permissionCheck() {
        if (SharingActivity.permissions.all {
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            requestPermissionLauncher.launch(SharingActivity.permissions)
        } else {
            activity.discoverPeers()
        }
    }

    companion object {
        val TAG = ReceiverFragment::class.java.name

    }

}