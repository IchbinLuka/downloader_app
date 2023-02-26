package com.ichbinluka.downloader.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UpdateWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {
    override suspend fun doWork(): Result {
        //val ytDl = YoutubeDL.getInstance()
        //ytDl.update
        // TODO: Update youtube dl
        return Result.success()
    }
}