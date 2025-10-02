package com.ichbinluka.downloader.activities

import com.ichbinluka.downloader.workers.DownloadType

class VideoDownloaderActivity: DownloaderActivity() {
    override val downloadType = DownloadType.VIDEO
}