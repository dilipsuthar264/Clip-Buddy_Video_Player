package com.memeusix.clipbuddy.ui.videoPlayer.viewModel

import androidx.lifecycle.ViewModel
import androidx.media3.common.Tracks

class PlayerViewModel : ViewModel() {
    var playWhenReady = true
    var videoUri: String? = null
    var currentPlaybackPosition: Long = 0
    var selectedAudioTrack: Tracks.Group? = null
    var selectedSubTitle: Tracks.Group? = null
    var currentOrientation: Int? = null
    var isLandScape: Boolean = false
    var videoZoomMode: VideoZoom = VideoZoom.BEST_FIT


    fun reset() {
        playWhenReady = true
        videoUri = null
        currentPlaybackPosition = 0
        selectedAudioTrack = null
        selectedSubTitle = null
        currentOrientation = null
        isLandScape = false
    }
}

enum class VideoZoom {
    BEST_FIT,
    STRETCH,
    CROP,
}
