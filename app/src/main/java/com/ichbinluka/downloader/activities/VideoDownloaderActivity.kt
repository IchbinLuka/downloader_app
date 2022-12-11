package com.ichbinluka.downloader.activities

import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.ichbinluka.downloader.workers.VideoDownloadWorker

class VideoDownloaderActivity: DownloaderActivity() {
    override val requestBuilder: OneTimeWorkRequest.Builder
        get() = OneTimeWorkRequestBuilder<VideoDownloadWorker>()
}