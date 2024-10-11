package com.memeusix.clipbuddy.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.memeusix.clipbuddy.utils.showRedCustomToast


open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    fun showErrorToast(message: String) {
        Toast(requireContext()).showRedCustomToast(message, requireActivity())
    }


    companion object {
        private val TAG = BaseFragment::class.java.name
    }
}
