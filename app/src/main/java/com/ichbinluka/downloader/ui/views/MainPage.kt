package com.ichbinluka.downloader.ui.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ichbinluka.downloader.ui.components.RoundButton
import com.ichbinluka.downloader.ui.theme.DownloaderTheme

@Preview(
    //heightDp = 600,
    //widthDp = 300,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    //heightDp = 600,
    //widthDp = 300,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun Test() {
    DownloaderTheme {
        MainPage()
    }
}

@Composable
fun MainPage(
    onUpdate: () -> Unit = {},
    onQuit: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RoundButton(onClick = onUpdate, title = "Update yt-dlp executable")
            RoundButton(onClick = onQuit, title = "Quit")
        }
    }
}