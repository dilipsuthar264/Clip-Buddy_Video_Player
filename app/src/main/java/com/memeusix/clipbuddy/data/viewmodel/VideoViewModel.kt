package com.memeusix.clipbuddy.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.memeusix.clipbuddy.data.model.VideoModel
import com.memeusix.clipbuddy.data.repository.VideoRepository
import kotlinx.coroutines.launch

class VideoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = VideoRepository(application.applicationContext)

    private val _videosByFolder = MutableLiveData<Map<String, List<VideoModel>>>()
    val videosByFolder: LiveData<Map<String, List<VideoModel>>> get() = _videosByFolder

    fun loadVideos() {
        viewModelScope.launch {
            _videosByFolder.value = repository.getVideosByFolder()
        }
    }
}