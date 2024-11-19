package com.ichbinluka.downloader.activities

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import com.ichbinluka.downloader.workers.MusicDownloadWorker

class MusicDownloaderActivity : DownloaderActivity() {
    override val requestBuilder: OneTimeWorkRequest.Builder
        get() = OneTimeWorkRequestBuilder<MusicDownloadWorker>()
}