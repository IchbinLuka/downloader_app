package com.ichbinluka.downloader.ui.views

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichbinluka.downloader.R
import com.ichbinluka.downloader.ui.theme.DownloaderTheme
import com.ichbinluka.downloader.ui.theme.surfaceVariant

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
fun Preview() {
    DownloaderTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Warning(onApprove = { /*TODO*/ }) {

            }
        }
    }
}

@Composable
fun Warning(
    onApprove: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier
            .sizeIn(maxWidth = 600.dp)
            .fillMaxWidth(fraction = 0.8f)
            .wrapContentHeight()
            .clip(RoundedCornerShape(30.dp))
    ) {
        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(10.dp)) {
                Text(stringResource(id = R.string.unsupported_platform_warning), textAlign = TextAlign.Center)
            }
            Row {
                RoundButton(onClick = onCancel,
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(id = R.string.warning_cancel),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surfaceVariant)
                )
                RoundButton(onClick = onApprove,
                    modifier = Modifier.weight(0.5f),
                    title = stringResource(id = R.string.warning_approve),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                )
            }
        }
    }
}

@Composable
fun RoundButton(
    onClick: () -> Unit,
    colors: ButtonColors,
    title: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = colors,
        modifier = modifier.padding(10.dp)
        ) {
        Text(text = title, softWrap = false, textAlign = TextAlign.Center)
    }
}