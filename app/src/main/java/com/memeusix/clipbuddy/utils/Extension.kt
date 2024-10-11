package com.memeusix.clipbuddy.utils

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.media3.common.C
import androidx.media3.common.TrackGroup
import androidx.media3.common.util.UnstableApi
import com.memeusix.clipbuddy.R
import java.io.Serializable
import java.util.Locale

private const val TAG = "Extension"

fun View.setSingleClickListener(onSingleClick: (View) -> Unit) {
    val clickListener = OneClickListener {
        onSingleClick(it)
    }
    setOnClickListener(clickListener)
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Editable.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun View.gone() {
    this.visibility = View.GONE
}
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Toast.showRedCustomToast(message: String, activity: Activity) {
    val layout = activity.layoutInflater.inflate(
        R.layout.item_custom_red_toast,
        activity.findViewById(R.id.redToastContainer)
    )
    val textView = layout.findViewById<TextView>(R.id.txtOtpSentMassage)
    textView.text = message
    this.apply {
        setGravity(Gravity.TOP, 0, 120)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
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

inline fun <reified T : Serializable> Intent.parcelize(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
        key, T::class.java
    )

    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

@UnstableApi
fun TrackGroup.getName(trackType: @C.TrackType Int, index: Int): String {
    val format = this.getFormat(0)
    val language = format.language
    val label = format.label
    return buildString {
        if (label != null) {
            append(label)
        }
        if (isEmpty()) {
            if (trackType == C.TRACK_TYPE_TEXT) {
                append("Subtitle Track #${index + 1}")
            } else {
                append("Audio Track #${index + 1}")
            }
        }
        if (language != null && language != "und") {
            append(" - ")
            append(Locale(language).displayLanguage)
        }
    }
}
