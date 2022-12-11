package com.ichbinluka.downloader.workers

import android.content.Context
import android.content.Intent
import androidx.work.WorkerParameters

class VideoDownloadWorker(context: Context, params: WorkerParameters) : DownloadWorker(context,
    params
) {
    override val afterIntent: Intent = Intent(Intent.ACTION_VIEW)
}