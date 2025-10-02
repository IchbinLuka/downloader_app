package com.ichbinluka.downloader.services

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ichbinluka.downloader.activities.DownloaderActivity
import com.ichbinluka.downloader.workers.DownloadWorker
import com.ichbinluka.downloader.workers.UpdateWorker


class RetryReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val url = intent?.getStringExtra(URL_KEY) ?: throw IllegalArgumentException("No URL provided")
        val notificationId = intent.getIntExtra(NOTIFICATION_ID_KEY, -1)
        if (notificationId == -1) throw IllegalArgumentException("No notification ID provided")
        val type = intent.getStringExtra(DOWNLOAD_TYPE_KEY) ?: throw IllegalArgumentException("No download type provided")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

        val workRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(UpdateWorker.CONSTRAINTS)
            .setInputData(DownloaderActivity.createInputData(url, type))
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }

    companion object {
        const val URL_KEY = "url"
        const val NOTIFICATION_ID_KEY = "notification_id"
        const val DOWNLOAD_TYPE_KEY = "type"
    }
}