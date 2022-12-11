package com.ichbinluka.downloader.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ichbinluka.downloader.MainActivity
import com.ichbinluka.downloader.R
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException

class UpdateWorker(
    private val context: Context,
    params: WorkerParameters,
) : NotificationWorker(
    params = params, context = context, id = 1
) {

    override val notification: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        channelId
    )
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setContentTitle(applicationContext.getString(R.string.updating))
        .setOnlyAlertOnce(true)
        .setProgress(100, 0, true)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_launcher_big)

    override suspend fun doWork(): Result {
        initNotificationChannel()
        updateNotification()
        return try {
            YoutubeDL.getInstance().updateYoutubeDL(context)
            Result.success()
        } catch (e: YoutubeDLException) {
            Result.failure()
        } finally {
            notification.setProgress(0, 0, true)
            updateNotification()
        }
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.NOTIFICATION_UPDATE_CHANNEL_ID,
                "Updater",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Updating"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val channelId = "yt-dl Updater"
    }

}