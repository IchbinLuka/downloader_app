package com.ichbinluka.downloader.workers

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

abstract class NotificationWorker (
    context: Context,
    params: WorkerParameters,
    protected val id: Int
) : CoroutineWorker(
    params = params, appContext = context
) {
    protected val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    protected abstract val notification: NotificationCompat.Builder

    protected fun updateNotification() {
        notificationManager.notify(id, notification.build())
    }
}