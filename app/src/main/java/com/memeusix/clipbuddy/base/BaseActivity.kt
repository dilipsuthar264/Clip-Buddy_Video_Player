package com.memeusix.clipbuddy.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.memeusix.clipbuddy.utils.showRedCustomToast

open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun showErrorToast(message: String) {
        Toast(this).showRedCustomToast(message, this)
    }

    companion object {
        val TAG = BaseActivity::class.java.name
    }
}
