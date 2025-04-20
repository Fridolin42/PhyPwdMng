package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object Theme {
    private val darkTheme = darkColors(
        primary = Color(0xFFBB86FC),
        secondary = Color(0xFF03DAC5),
        background = Color.Black
    )
    private val lightTheme = lightColors(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC5),
        background = Color.White
    )

    @Composable
    @Preview
    fun AutoTheme(content: @Composable () -> Unit) {
        val colors = if (isSystemInDarkTheme()) darkTheme else lightTheme
        MaterialTheme(colors = colors, content = content)
    }
}