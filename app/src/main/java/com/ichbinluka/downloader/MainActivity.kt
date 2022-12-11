package com.ichbinluka.downloader

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
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ichbinluka.downloader.data.supportedPlatforms
import com.ichbinluka.downloader.ui.theme.DownloaderTheme
import com.ichbinluka.downloader.ui.views.Settings
import com.ichbinluka.downloader.ui.views.Warning
import com.ichbinluka.downloader.workers.DownloadWorker
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
        const val NOTIFICATION_DOWNLOAD_CHANNEL_ID = "downloader"
        const val NOTIFICATION_UPDATE_CHANNEL_ID = "updater"
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
        YoutubeDL.getInstance().init(this)
        FFmpeg.getInstance().init(this)
        if (url != null) {
            val data = Data.Builder()
                .putString(DownloadWorker.CHANNEL_ID_KEY, NOTIFICATION_DOWNLOAD_CHANNEL_ID)
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

        } else {
            setContent {
                DownloaderTheme {
                    Settings()
                }
            }
        }

    }

    private inline fun startDownload(request: OneTimeWorkRequest.Builder) {
        WorkManager.getInstance().enqueue(request.build())
        finish()
    }
}