package com.memeusix.clipbuddy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoModel(
    val id: Long? = null,
    val name: String? = null,
    val folderName: String? = null,
    val path: String? = null,
    val duration: Long? = null,
    val size: Long? = null,
    val dimensions : String? = null,
    val time : Long? = null
) : Parcelable