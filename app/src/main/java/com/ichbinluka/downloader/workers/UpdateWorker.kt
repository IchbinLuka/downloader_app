package com.ichbinluka.downloader.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ichbinluka.downloader.R
import com.ichbinluka.downloader.util.NotificationId
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException

class UpdateWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {

    private val notification: NotificationCompat.Builder = NotificationCompat.Builder(
        appContext,
        NOTIFICATION_CHANNEL_ID
    )
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setContentTitle("Updating YoutubeDL")
        .setOnlyAlertOnce(true)
        .setProgress(0, 0, true)
        .setAutoCancel(true)
        .setSmallIcon(com.ichbinluka.downloader.R.drawable.ic_launcher_big)

    private val notificationId = NotificationId.newId
    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
        notificationManager.notify(notificationId, notification.build())
        val ytDl = YoutubeDL.getInstance()
        ytDl.init(applicationContext)
        val status = try {
            ytDl.updateYoutubeDL(applicationContext)
        } catch (e: YoutubeDLException) {
            Log.e(TAG, "Failed to update YoutubeDL", e)
            notification.setContentTitle(applicationContext.getString(R.string.update_error))
            notification.setContentText(e.message)
            notificationManager.notify(notificationId, notification.build())
            return Result.failure()
        }
        when (status) {
            YoutubeDL.UpdateStatus.DONE -> {
                Log.i(TAG, "YoutubeDL updated successfully")
            }
            YoutubeDL.UpdateStatus.ALREADY_UP_TO_DATE -> {
                Log.i(TAG, "YoutubeDL is already up to date")
            }
            null -> {
                Log.e(TAG, "Update failed: null status")
                notification.setContentTitle(applicationContext.getString(R.string.update_error))
                notification.setContentText("null status")
                notificationManager.notify(notificationId, notification.build())
                return Result.failure()
            }
        }

        notificationManager.cancel(notificationId)

        return Result.success()
    }

    companion object {
        private const val TAG = "UpdateWorker"
        private const val NOTIFICATION_CHANNEL_ID = "update_worker_channel"
    }
}