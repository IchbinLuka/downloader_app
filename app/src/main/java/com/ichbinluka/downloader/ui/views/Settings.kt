package com.ichbinluka.downloader.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ichbinluka.downloader.MainActivity
import com.ichbinluka.downloader.R
import com.ichbinluka.downloader.ui.widgets.RoundButton
import com.ichbinluka.downloader.workers.DownloadWorker
import com.ichbinluka.downloader.workers.UpdateWorker
import com.yausername.youtubedl_android.YoutubeDL

@Composable
fun Settings() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {

        TopAppBar(contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)) {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.h6
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            RoundButton(
                onClick = {
                    val request = OneTimeWorkRequestBuilder<UpdateWorker>()
                    WorkManager.getInstance().enqueue(request.build())
                },
                title = stringResource(id = R.string.update_yt_dl)
            )
        }
    }
}