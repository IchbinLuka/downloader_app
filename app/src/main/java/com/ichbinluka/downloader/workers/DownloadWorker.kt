package com.ichbinluka.downloader.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ichbinluka.downloader.R
import com.ichbinluka.downloader.activities.DownloaderActivity
import com.ichbinluka.downloader.util.NotificationId
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

abstract class DownloadWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(
    params = params, appContext = context
) {
    protected abstract val afterIntent: Intent

    protected open val ytDLArgs = listOf<Pair<String, String?>>()

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
        .setSmallIcon(R.drawable.ic_launcher_big)

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val notificationId = NotificationId.newId

    override suspend fun doWork(): Result {
        initNotificationChannel()
        notificationManager.notify(notificationId, notification.build())
        // Update youtube dl

        val dir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "downloader"
        )
        val url = inputData.getString("url")
        if (url != "") {
            val request = YoutubeDLRequest(url).apply {
                addOption("-o", "${dir.absolutePath}/%(title)s.%(ext)s")
                addOption("-S", "ext")
                addOption("--add-metadata")
                addOption("--no-mtime") // Use current time as last modified date

                for (arg in ytDLArgs) {
                    if (arg.second == null) {
                        addOption(arg.first)
                    } else {
                        addOption(arg.first, arg.second!!)
                    }
                }
            }
            withContext(Dispatchers.IO) {
                try {
                    val info = ytDL.getInfo(url)
                    notification.setContentTitle(applicationContext.getString(R.string.downloading, info.title))
                    updateNotification()
                    var currentProgress: Float = 0f
                    val response = ytDL.execute(request) { progress: Float, _, _ ->
                        if (currentProgress > progress + 0.3f) {
                            notification.setContentTitle(applicationContext.getString(R.string.converting))
                        }
                        currentProgress = progress
                        notification.setProgress(100, progress.toInt(), false)
                        updateNotification()
                    }
                    //Log.d(TAG, response.out)
                    val mergerIndex = response.out.lastIndexOf(MERGER_TERM)
                    val index = if (mergerIndex == -1) {
                        response.out.lastIndexOf(DESTINATION_TERM) + DESTINATION_TERM.length + 1
                    } else {
                        mergerIndex + MERGER_TERM.length + 1
                    }

                    val path = response.out.substring(startIndex = index).takeWhile { it != '\n' && it != '"' }
                    // Manually set the last modified date to current time since yt-dlp does not seem
                    // to do this very reliable
                    val file = File(path)
                    file.setLastModified(Calendar.getInstance().timeInMillis)
                    var i = 0
                    var endFile: File
                    do {
                        val index = file.path.lastIndexOf(".")
                        endFile = File(file.path.replaceRange(index, index, " ($i)"))
                        i++
                    } while (endFile.exists())

                    file.renameTo(endFile)

                    MediaScannerConnection.scanFile(applicationContext, arrayOf(endFile.path), null) { s, uri ->
                        afterIntent.data = uri
                        val flags = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            PendingIntent.FLAG_IMMUTABLE
                        } else {
                            0
                        }
                        notification.apply {
                            setProgress(0, 0, false)
                            setContentIntent(PendingIntent.getActivity(applicationContext, 0, afterIntent, flags, null))
                            setContentTitle("Finished")
                        }
                        updateNotification()
                    }

                } catch (e: Exception) {
                    Log.e(TAG, e.message ?: "")
                    notification.setContentTitle(applicationContext.getString(R.string.download_error))
                    notification.setProgress(0, 0, false)
                    updateNotification()
                    return@withContext Result.failure()
                }
            }

        }
        val status = ytDL.updateYoutubeDL(applicationContext)
        return Result.success()
    }

    private fun updateNotification() {
        notificationManager.notify(notificationId, notification.build())
    }

    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DownloaderActivity.NOTIFICATION_CHANNEL_ID,
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
        private const val MERGER_TERM = "[Merger] Merging formats into "
    }
}