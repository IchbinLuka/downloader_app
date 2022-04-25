package com.ichbinluka.downloader.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xff03a9f4),
    primaryVariant = Color(0xff29b6f6),
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Color(0xff03a9f4),
    primaryVariant = Color(0xff29b6f6),
    secondary = Teal200,

)

val Colors.surfaceVariant: Color
    get() = if (isLight) Color(0xffeceff1) else surface

@Composable
fun DownloaderTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}