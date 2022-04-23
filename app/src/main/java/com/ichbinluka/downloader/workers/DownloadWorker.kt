package com.ichbinluka.downloader.workers

import android.app.NotificationManager
import android.content.Context
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class DownloadWorker(
    context: Context,
    params: WorkerParameters,
    channelId: String
) : CoroutineWorker(
    params = params, appContext = context
) {
    private val ytDL: YoutubeDL by lazy {
        YoutubeDL.getInstance()
    }

    private val notification = NotificationCompat.Builder(
        context,
        channelId
    )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentTitle("Downloading")
        .setOnlyAlertOnce(true)
        .setProgress(100, 0, false)

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {

        notificationManager.notify(0, notification.build())

        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloader")
        val url = inputData.getString("url")
        if (url != "") {
            val request = YoutubeDLRequest(url).apply {
                addOption("-o", "${dir.absolutePath}/%(title)s.%(ext)s")
            }
            withContext(Dispatchers.IO) {
                try {
                    val response = ytDL.execute(request) {
                        progress: Float, _, _ ->
                        notification.setProgress(100, progress.toInt(), false)
                        notificationManager.notify(0, notification.build())
                    }
                } catch (e: Exception) {

                }
            }

        }
        return Result.success()
    }
}