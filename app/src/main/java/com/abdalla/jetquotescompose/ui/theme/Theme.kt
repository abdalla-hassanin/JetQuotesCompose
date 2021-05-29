package com.abdalla.jetquotescompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF00BCD4),
    onPrimary = Color.Black,
    background = Color(0xFF00BCD4),
    onBackground = Color.Black,
    surface = Color(0xFF795548),
    onSurface = Color.White,
    primaryVariant = Color(0xFF0097A7),

    )

private val LightColorPalette = lightColors(
    primary = Color(0xFF795548),
    onPrimary = Color.White,
    background = Color(0xFF795548),
    onBackground = Color.White,
    surface = Color(0xFF00BCD4),
    onSurface = Color.Black,
    primaryVariant = Color(0xFF5D4037),
)

@Composable
fun JetQuotesComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
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