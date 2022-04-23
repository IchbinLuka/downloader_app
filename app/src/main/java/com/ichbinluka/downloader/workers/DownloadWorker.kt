package com.ichbinluka.downloader.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ichbinluka.downloader.MainActivity
import com.ichbinluka.downloader.R
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class DownloadWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(
    params = params, appContext = context
) {
    private val ytDL: YoutubeDL by lazy {
        YoutubeDL.getInstance()
    }

    private val channelId: String = inputData.getString(CHANNEL_ID_KEY) ?: ""

    private val notification: NotificationCompat.Builder = NotificationCompat.Builder(
        context,
        channelId
    )
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentTitle("Downloading")
        .setOnlyAlertOnce(true)
        .setProgress(100, 0, false)
        .setAutoCancel(true)
        .setSmallIcon(R.drawable.ic_launcher_foreground)

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        initNotificationChannel()
        notificationManager.notify(0, notification.build())

        Log.d(TAG, inputData.toString())

        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "downloader")
        val url = inputData.getString("url")
        Log.d(TAG, url!!)
        if (url != "") {
            val request = YoutubeDLRequest(url).apply {
                addOption("-o", "${dir.absolutePath}/%(title)s.%(ext)s")
                addOption("-f", "\"bv*[ext=mp4]+ba[ext=m4a]/b[ext=mp4] / bv*+ba/b\"")
            }
            withContext(Dispatchers.IO) {
                try {
                    launch {
                        val info = ytDL.getInfo(url)
                        notification.setContentTitle("Downloading ${info.title}")
                        updateNotification()
                    }
                    val response = ytDL.execute(request) {
                        progress: Float, _, _ ->
                        notification.setProgress(100, progress.toInt(), false)
                        updateNotification()
                    }
                    val index = response.out.lastIndexOf(DESTINATION_TERM) + DESTINATION_TERM.length + 1
                    val path = response.out.substring(startIndex = index).takeWhile { it != '\n' }
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(path)).apply { type = "video/*" }
                    val flags = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        PendingIntent.FLAG_IMMUTABLE
                    } else {
                        0
                    }
                    notification.apply {
                        setProgress(1, 1, false)
                        setContentIntent(PendingIntent.getActivity(applicationContext, 0, intent, flags, null))
                        setContentTitle("Finished")
                    }
                    updateNotification()

                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "")
                }
            }

        }
        return Result.success()
    }

    private fun updateNotification() {
        notificationManager.notify(0, notification.build())
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID,
                "Downloader",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Downloading"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID_KEY = "channel_id"
        const val TAG = "Download Worker"
        private const val DESTINATION_TERM = "Destination:"
    }
}