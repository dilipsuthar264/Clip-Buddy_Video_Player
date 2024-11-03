package com.memeusix.clipbuddy.utils

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import java.text.DecimalFormat
import java.text.SimpleDateFormat


// Method to hide the system UI (status bar and navigation bar)
fun hideSystemUI(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.let { controller ->
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }
}

// Method to show the system UI (status bar and navigation bar)
fun showSystemUI(window: Window) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

fun formatDat(date: Long): String {
    val timestampInMillis = date * 1000L
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
    return dateFormat.format(timestampInMillis)
}

fun formatFileSize(sizeInBytes: Long): String {
    val df = DecimalFormat("#.##")

    return when {
        sizeInBytes >= 1024 * 1024 * 1024 -> {
            df.format(sizeInBytes / (1024.0 * 1024.0 * 1024.0)) + "GB"
        }

        sizeInBytes >= 1024 * 1024 -> {
            df.format(sizeInBytes / (1024.0 * 1024.0)) + "MB"
        }

        sizeInBytes >= 1024 -> {
            df.format(sizeInBytes / 1024.0) + "KB"
        }

        else -> {
            "$sizeInBytes Bytes"
        }
    }
}
