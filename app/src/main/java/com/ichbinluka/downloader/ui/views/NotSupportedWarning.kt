package com.ichbinluka.downloader.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichbinluka.downloader.R
import com.ichbinluka.downloader.ui.theme.DownloaderTheme

@Preview(
    heightDp = 400,
    widthDp = 200,

)
@Composable
fun Preview() {
    DownloaderTheme {
        Warning(onApprove = { /*TODO*/ }) {

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
            .fillMaxWidth(0.6f)
    ) {
        Column {
            Text(stringResource(id = R.string.unsupported_platform_warning))
            Row {
                RoundButton(onClick = onCancel,
                    title = stringResource(id = R.string.warning_cancel),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                )
                RoundButton(onClick = onApprove,
                    title = stringResource(id = R.string.warning_approve),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)
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
        Text(text = title)
    }
}