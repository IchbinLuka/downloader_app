package com.ichbinluka.downloader.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RoundButton(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
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