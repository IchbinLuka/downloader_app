package com.ichbinluka.downloader.workers

import android.content.Context
import android.content.Intent
import androidx.work.WorkerParameters
import com.ichbinluka.downloader.activities.TagEditorActivity

class MusicDownloadWorker(
    context: Context,
    params: WorkerParameters
) : DownloadWorker(context, params) {
    override val afterIntent: Intent = Intent(applicationContext, TagEditorActivity::class.java)

    override val ytDLArgs = listOf(
        "-f" to "bestaudio",
        "--extract-audio" to null,
        "--audio-format" to "mp3",
        "--audio-quality" to "0"
    )
}