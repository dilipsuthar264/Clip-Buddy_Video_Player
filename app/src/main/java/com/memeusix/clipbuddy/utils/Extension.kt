package com.memeusix.clipbuddy.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.memeusix.clipbuddy.ui.videoPlayer.viewModel.VideoZoom

private const val TAG = "Extension"

fun View.setSingleClickListener(onSingleClick: (View) -> Unit) {
    val clickListener = OneClickListener {
        onSingleClick(it)
    }
    setOnClickListener(clickListener)
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Long.formatDuration(): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60)) % 60
    val hours = (this / (1000 * 60 * 60)) % 24

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}


inline fun <reified T : Parcelable> Bundle.parcelize(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun VideoZoom.next(): VideoZoom {
    val values = VideoZoom.entries.toTypedArray()
    val nextIndex = (this.ordinal + 1) % values.size
    return values[nextIndex]
}

