package de.pse.kit.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme as MaterialTheme3
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable

private val LightColorPalette3 = ColorScheme(
    primary = Grey200,
    onPrimary = Color.Black,
    primaryContainer = Green200,
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.Black,
    secondary = Green500,
    onSecondary = Color.Black,
    secondaryContainer = Green100,
    onSecondaryContainer = Green700,
    tertiary = Green700,
    onTertiary = Color.White,
    tertiaryContainer = Red500,
    onTertiaryContainer = Color.White,
    background = White200,
    onBackground = Color.Black,
    surface = White200,
    onSurface = Color.Black,
    surfaceVariant = White200,
    onSurfaceVariant = Color.Black,
    inverseSurface = White200,
    inverseOnSurface = Color.Black,
    error = Color.Red,
    onError = Color.Black,
    errorContainer = Color.Red,
    onErrorContainer = Color.Black,
    outline = Green700
)

private val LightColorPalette = Colors(
    primary = Green700,
    primaryVariant = Green200,
    onPrimary = Color.Black,
    secondary = Green500,
    secondaryVariant = Green700,
    onSecondary = Color.Black,
    background = White200,
    onBackground = Color.Black,
    surface = White200,
    onSurface = Color.Black,
    error = Color.Red,
    onError = Color.Black,
    isLight = true
)

@Composable
fun MyApplicationTheme3(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = LightColorPalette3

    MaterialTheme3(
        colorScheme = colors,
        typography = Typography3,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}