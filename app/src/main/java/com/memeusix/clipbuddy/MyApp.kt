package com.memeusix.clipbuddy

import android.app.Application
import com.memeusix.clipbuddy.data.viewmodel.VideoViewModel

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        VideoViewModel(this)
    }
}