package com.ichbinluka.downloader.activities

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.ichbinluka.downloader.workers.VideoDownloadWorker

class VideoDownloaderActivity: DownloaderActivity() {
    override fun getDownloadRequestBuilder(): OneTimeWorkRequest.Builder = OneTimeWorkRequestBuilder<VideoDownloadWorker>()
}