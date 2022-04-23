package com.ichbinluka.downloader

import androidx.lifecycle.ViewModel
import com.yausername.youtubedl_android.YoutubeDL

class MainViewModel : ViewModel() {

    public var downloading: Boolean = false

    private val ytDL: YoutubeDL by lazy {
        YoutubeDL.getInstance()
    }

    fun download(url: String) {
        ytDL.
    }

}