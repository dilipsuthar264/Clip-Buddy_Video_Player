package com.memeusix.clipbuddy.ui.sharing.sender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.memeusix.clipbuddy.databinding.FragmentSenderBinding

class SenderFragment : Fragment() {
    private var _binding: FragmentSenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        val TAG = SenderFragment::class.java.name
    }
}