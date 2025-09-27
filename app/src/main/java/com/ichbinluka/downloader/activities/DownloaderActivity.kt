package com.ichbinluka.downloader.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ichbinluka.downloader.data.supportedPlatforms
import com.ichbinluka.downloader.ui.theme.DownloaderTheme
import com.ichbinluka.downloader.ui.views.Warning
import com.ichbinluka.downloader.workers.DownloadWorker
import com.ichbinluka.downloader.workers.UpdateWorker
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import java.util.concurrent.TimeUnit

abstract class DownloaderActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val NOTIFICATION_CHANNEL_ID = "downloader"
    }

    protected abstract val requestBuilder: OneTimeWorkRequest.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = mutableListOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)

        initUpdateWorker()

        val url = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (url != null) {
            YoutubeDL.getInstance().init(this)
            FFmpeg.getInstance().init(this)

            val data = Data.Builder()
                .putString(DownloadWorker.CHANNEL_ID_KEY, NOTIFICATION_CHANNEL_ID)
                .putAll(mapOf(
                    Pair("url", url)
                ))
                .build()
            val request = requestBuilder.setInputData(data)
            if (!supportedPlatforms.any { url.matches(it) }) {
                setContent {
                    DownloaderTheme {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Warning(
                                onApprove = {
                                    startDownload(request)
                                },
                                onCancel = {
                                    finish()
                                }
                            )
                        }
                    }
                }
            } else {
                startDownload(request)
            }

        } else finish()

    }

    private fun startDownload(request: OneTimeWorkRequest.Builder) {
        WorkManager.getInstance(this).enqueue(request.build())
        finish()
    }

    private fun initUpdateWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val work = PeriodicWorkRequestBuilder<UpdateWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            "updater",
            ExistingPeriodicWorkPolicy.UPDATE,
            work,
        )
    }
}