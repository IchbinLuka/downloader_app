package com.ichbinluka.downloader.activities

import com.ichbinluka.downloader.workers.DownloadType

class MusicDownloaderActivity : DownloaderActivity() {
    override val downloadType: DownloadType = DownloadType.MUSIC

}