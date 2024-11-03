package com.memeusix.clipbuddy.ui.sharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.memeusix.clipbuddy.databinding.FragmentShareDashboardBinding
import com.memeusix.clipbuddy.utils.setSingleClickListener

class ShareDashboardFragment : Fragment() {
    private var _binding: FragmentShareDashboardBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSend.setSingleClickListener {
            val direction =
                ShareDashboardFragmentDirections.actionShareDashboardFragmentToSenderFragment()
            findNavController().navigate(direction)
        }
        binding.btnReceive.setSingleClickListener {
            val direction =
                ShareDashboardFragmentDirections.actionShareDashboardFragmentToReceiverFragment()
            findNavController().navigate(direction)
        }

    }

    companion object {
        val TAG = ShareDashboardFragment::class.java.name
    }
}