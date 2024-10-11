package com.memeusix.clipbuddy.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderModel(
    val folderName: String,
    val videos: List<VideoModel>
) : Parcelable