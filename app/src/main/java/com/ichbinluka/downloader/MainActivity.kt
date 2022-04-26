package com.ichbinluka.downloader

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ichbinluka.downloader.data.supportedPlatforms
import com.ichbinluka.downloader.ui.theme.DownloaderTheme
import com.ichbinluka.downloader.ui.views.Warning
import com.ichbinluka.downloader.workers.DownloadWorker
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val NOTIFICATION_CHANNEL_ID = "downloader"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = mutableListOf<String>(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissions.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1)

        val url = intent.getStringExtra(Intent.EXTRA_TEXT)

        if (url != null) {
            YoutubeDL.getInstance().init(this)
            FFmpeg.getInstance().init(this)

            val data = Data.Builder()
                .putString(DownloadWorker.CHANNEL_ID_KEY, NOTIFICATION_CHANNEL_ID)
                .putString("url", url)
                .build()
            val request = OneTimeWorkRequestBuilder<DownloadWorker>().setInputData(data)
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

    private inline fun startDownload(request: OneTimeWorkRequest.Builder) {
        WorkManager.getInstance().enqueue(request.build())
        finish()
    }
}