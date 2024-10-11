package com.memeusix.clipbuddy.ui.videoPlayer.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.Tracks
import com.memeusix.clipbuddy.data.model.VideoModel
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    var currentPlaybackPosition: Long? = null
    var currentPlaybackSpeed: Float = 1f
    var currentAudioTrackIndex: Int? = null
    var currentSubtitleTrackIndex: Int? = null
    var currentVideoScale: Float = 1f
    var skipSilenceEnabled: Boolean = false


    fun saveState(
        uri: Uri,
        position: Long,
        duration: Long,
        audioTrackIndex: Int,
        subtitleTrackIndex: Int,
        playbackSpeed: Float,
        skipSilence: Boolean,
        videoScale: Float,
    ) {
        currentPlaybackPosition = position
        currentAudioTrackIndex = audioTrackIndex
        currentSubtitleTrackIndex = subtitleTrackIndex
        currentPlaybackSpeed = playbackSpeed
        currentVideoScale = videoScale
        skipSilenceEnabled = skipSilence
    }

}